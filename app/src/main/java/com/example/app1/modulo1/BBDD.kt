package com.example.app1.modulo1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

var bbdd="UsuarioDB";
//val usr:UsuarioDB= UsuarioDB()

class BBDD(contexto: Context): SQLiteOpenHelper(contexto, bbdd, null,2) {
    override fun onCreate(db: SQLiteDatabase?) {
        //db?.execSQL("drop table if exists UsuarioDB")
        var crearTablaUsr ="create table UsuarioDB(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(16), password VARCHAR(16), nombreApellido VARCHAR(30), dni VARCHAR(9), email VARCHAR(30), asociado BOOLEAN)"
        db?.execSQL(crearTablaUsr)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //db?.execSQL("alter table UsuarioDB add column nombreApellido varchar(30)")
    }

    fun insertar(usr: UsuarioDB):String{
        val db = this.writableDatabase
        var contenedorValores = ContentValues()

        contenedorValores.put("username", usr.username)
        contenedorValores.put("password", usr.password)
        contenedorValores.put("nombreApellido", usr.nombreApellido)
        contenedorValores.put("dni", usr.dni)
        contenedorValores.put("email", usr.email)
        contenedorValores.put("asociado", usr.asociado)

        var resultado = db.insert("UsuarioDB", null, contenedorValores)
        if (resultado==-1.toLong()) {
            Log.i("modulo1", "Falló: ${usr.username}, ${usr.password}, ${usr.nombreApellido}. ${usr.dni}, ${usr.email}, ${usr.asociado}")
            return "Falló la inserción, user: ${usr.toString()}"
        } else {
            return "Inserción OK. user: ${usr.toString()}"
        }
    }

    fun leer():MutableList<UsuarioDB>{
        var lista:MutableList<UsuarioDB> = ArrayList()
        var db =  this.readableDatabase
        val sql = "select * from UsuarioDB"
        var resultado = db.rawQuery(sql,null)

        if(resultado.moveToFirst()) {
            do{
                var usr = UsuarioDB()
                val a:Int = resultado.getColumnIndex("id")
                val b:Int = resultado.getColumnIndex("username")
                val c:Int = resultado.getColumnIndex("password")
                usr.id = resultado.getInt(a)
                usr.username = resultado.getString(b)
                usr.password = resultado.getString(c)
                usr.nombreApellido = resultado.getString(3)
                usr.dni = resultado.getString(4)
                usr.email = resultado.getString(5)
                usr.asociado = resultado.getString(6) == "true"
                lista.add(usr)
            }while(resultado.moveToNext())
            resultado.close()
            db.close()
            return lista
        }
        return lista
    }

    fun leerUno(usNm:String):UsuarioDB{
        var usRes:UsuarioDB = UsuarioDB()
        val db =  this.readableDatabase
        val sql = "select * from UsuarioDB where username == '${usNm}'"
        var resultado = db.rawQuery(sql,null)
        if (resultado.moveToFirst()) {
            usRes.id = resultado.getInt(0)
            usRes.username =  resultado.getString(1)
            usRes.password = resultado.getString(2)
            Log.i("modulo1","LeerUno => id: ${usRes.id} username: ${usRes.username} pass: ${usRes.password} nomAp: ${usRes.nombreApellido}. dni: ${usRes.dni}, email: ${usRes.email}, asoc: ${usRes.asociado}")
            resultado.close()
            return usRes
        }
        Log.i("modulo1","NO encontro nada")
        return usRes
    }

    fun actualizar(id:String,username:String,password:String):String{
        val db = this.writableDatabase
        var contenedorValores = ContentValues()
        contenedorValores.put("username", username)
        contenedorValores.put("password", password)
        var resultado = db.update("UsuarioDB", contenedorValores, "id=?", arrayOf(id))

        if (resultado>0) {
            return "Actualizacion realizada"
        } else {
            return "No se realizó la actualización"
        }
    }

    fun borrar( id:String){
        val db = this.writableDatabase
        if (id.length>0) {
            db.delete("UsuarioDB","id=?", arrayOf(id))
        }
    }


}