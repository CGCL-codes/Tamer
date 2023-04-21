package com.akcess.vo;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.io.Serializable;

/**
*
* This class represents the primary key of the atributos_recursos table
*
*/
public class Atributos_recursosPK implements Serializable {

    private int id_atributos;

    public Atributos_recursosPK() {
    }

    public Atributos_recursosPK(int id_atributos) {
        this.id_atributos = id_atributos;
    }

    public void setId_atributos(int id_atributos) {
        this.id_atributos = id_atributos;
    }

    public int getId_atributos() {
        return (id_atributos);
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.akcess.vo.Atributos_recursosPK :");
        ret.append("id_atributos='" + id_atributos + "'");
        return ret.toString();
    }
}
