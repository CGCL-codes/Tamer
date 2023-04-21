package com.akcess.vo;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.io.Serializable;

/**
*
* This class represents the primary key of the solicitud_registro table
*
*/
public class Solicitud_registroPK implements Serializable {

    private int id_solic_registro;

    public Solicitud_registroPK() {
    }

    public Solicitud_registroPK(int id_solic_registro) {
        this.id_solic_registro = id_solic_registro;
    }

    public void setId_solic_registro(int id_solic_registro) {
        this.id_solic_registro = id_solic_registro;
    }

    public int getId_solic_registro() {
        return (id_solic_registro);
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("com.akcess.vo.Solicitud_registroPK :");
        ret.append("id_solic_registro='" + id_solic_registro + "'");
        return ret.toString();
    }
}
