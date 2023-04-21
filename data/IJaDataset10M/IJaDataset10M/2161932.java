package com.akcess.vo;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.io.Serializable;

/**
*
* This classes is the Data Transfer Object(Value Object) defination for the entity modulo_tiene_operacion
*
*/
public class Modulo_tiene_operacion implements Serializable {

    private short id_modulo;

    private short id_permiso;

    public Modulo_tiene_operacion() {
    }

    public Modulo_tiene_operacion(short id_modulo, short id_permiso) {
        this.id_modulo = id_modulo;
        this.id_permiso = id_permiso;
    }

    public void setId_modulo(short id_modulo) {
        this.id_modulo = id_modulo;
    }

    public short getId_modulo() {
        return (id_modulo);
    }

    public void setId_permiso(short id_permiso) {
        this.id_permiso = id_permiso;
    }

    public short getId_permiso() {
        return (id_permiso);
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.akcess.vo.Modulo_tiene_operacion :");
        ret.append("id_modulo='" + id_modulo + "'");
        ret.append(", id_permiso='" + id_permiso + "'");
        return ret.toString();
    }
}
