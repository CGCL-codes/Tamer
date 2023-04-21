package com.market.negocio;

import java.util.*;
import javax.servlet.http.*;
import com.market.datos.cDatos;

/**
 * Esta clase contiene los atributos y métodos para la administración de los 
 * usuarios compradores del proyecto MarketFarm
 * @author Ricardo
 */
public class cUsuarios {

    private cDatos db;

    private String sql;

    private Vector vr;

    /**
     * Constructor que inicializa los atributos de la clase
     */
    public cUsuarios() {
        this.db = new cDatos();
        this.db.openConnection();
    }

    /**
     * Regresa todos los registros de Usuarios
     * @return Registros de los Usuarios
     */
    public Vector getRegistros() {
        sql = "SELECT comp_codigo, comp_nombre, comp_direccion, comp_ruc, comp_email, comp_telefono, usu_codigo ";
        sql += "FROM comprador ORDER BY comp_nombre";
        System.out.println(sql);
        this.vr = this.db.query(sql);
        return this.vr;
    }

    /**
     * Retorna el registro completo del usuario
     * @param xcod DNI del usuario
     * @return Registro del usuario
     */
    public Vector getRegistro(String xcod) {
        sql = "SELECT comp_codigo, comp_nombre, comp_direccion, comp_ruc, comp_email, comp_telefono, usu_codigo ";
        sql += "FROM comprador WHERE comp_codigo='" + xcod + "'";
        System.out.println(sql);
        this.vr = this.db.query(sql);
        return this.vr;
    }

    /**
     * Almacena los cambios hechos en el registro del usuario actual
     * @param request Parámetros del formulario modificar registro del usuario
     */
    public void grabarModificarRegistro(HttpServletRequest request) {
        String xcod = request.getParameter("xcod");
        String xnom = request.getParameter("xnom");
        String xdir = request.getParameter("xdir");
        String xruc = request.getParameter("xruc");
        String xmail = request.getParameter("xmail");
        String xtel = request.getParameter("xtel");
        sql = "UPDATE comprador SET comp_nombre='" + xnom + "', comp_direccion='" + xdir + "', comp_ruc='" + xruc + "', comp_email='" + xmail + "', comp_telefono='" + xtel + "' ";
        sql += "WHERE comp_codigo='" + xcod + "'";
        System.out.println(sql);
        this.db.update(sql);
    }

    /**
     * Elimina el registro de un usuario
     * @param aData Datos del registro del usuario
     */
    public void eliminarRegistros(String aData[]) {
        boolean inicio;
        if (aData.length <= 0) {
            return;
        }
        sql = "DELETE FROM usuario WHERE usu_codigo in ( ";
        inicio = true;
        for (int xc = 0; xc < aData.length; xc++) {
            if (inicio) {
                sql += "'" + aData[xc] + "'";
            } else {
                sql += ",'" + aData[xc] + "'";
            }
            inicio = false;
        }
        sql += ")";
        System.out.println(sql);
        this.db.delete(sql);
    }
}
