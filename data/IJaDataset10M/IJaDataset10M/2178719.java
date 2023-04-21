package com.akcess.vo;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.io.Serializable;

/**
*
* This class represents the primary key of the tipo_tiene_atributos table
*
*/
public class Tipo_tiene_atributosPK implements Serializable {

    private int id_tipo_recurso;

    private int id_atributos;

    public Tipo_tiene_atributosPK() {
    }

    public Tipo_tiene_atributosPK(int id_tipo_recurso, int id_atributos) {
        this.id_tipo_recurso = id_tipo_recurso;
        this.id_atributos = id_atributos;
    }

    public void setId_tipo_recurso(int id_tipo_recurso) {
        this.id_tipo_recurso = id_tipo_recurso;
    }

    public int getId_tipo_recurso() {
        return (id_tipo_recurso);
    }

    public void setId_atributos(int id_atributos) {
        this.id_atributos = id_atributos;
    }

    public int getId_atributos() {
        return (id_atributos);
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.akcess.vo.Tipo_tiene_atributosPK :");
        ret.append("id_tipo_recurso='" + id_tipo_recurso + "'");
        ret.append(", id_atributos='" + id_atributos + "'");
        return ret.toString();
    }
}
