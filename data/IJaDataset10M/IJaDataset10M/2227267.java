package com.guias.utilidades;

import com.guias.data.Ejecutar;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Joshua
 */
public class HtmlUtils {

    public static String getComboDepartamentos(String codi_departamento, String id, String opcion) throws SQLException {
        String s_retorno = "";
        String sql_select = "select codi_departamento, nomb_departamento from tb_departamentos";
        Ejecutar exec = new Ejecutar();
        ArrayList ly_datos = new ArrayList();
        ly_datos = exec.ejecutarSQLselect(sql_select, false);
        if (ly_datos != null) {
            s_retorno = "<select id='" + id + "' " + opcion + ">";
            for (Iterator it1 = ly_datos.iterator(); it1.hasNext(); ) {
                ArrayList ly_campos = new ArrayList();
                ly_campos = (ArrayList) it1.next();
                if (ly_campos != null) {
                    String codigo = (String) ly_campos.get(0);
                    String nombre = (String) ly_campos.get(1);
                    if (codigo.equals(codi_departamento)) {
                        s_retorno += "<option selected id='" + codigo + "'>" + nombre + "</option>";
                    } else {
                        s_retorno += "<option id='" + codigo + "'>" + nombre + "</option>";
                    }
                }
            }
            s_retorno += "</select>";
        }
        return s_retorno;
    }

    public static String getComboMunicipios(String codi_departamento, String codi_municipio, String id, String opcion) throws SQLException {
        String s_retorno = "";
        String sql_select = "select codi_municipio, nomb_municipio from tb_municipios where codi_departamento = '" + codi_departamento + "'";
        Ejecutar exec = new Ejecutar();
        ArrayList ly_datos = new ArrayList();
        ly_datos = exec.ejecutarSQLselect(sql_select, false);
        if (ly_datos != null) {
            s_retorno = "<select id='" + id + "' " + opcion + ">";
            for (Iterator it1 = ly_datos.iterator(); it1.hasNext(); ) {
                ArrayList ly_campos = new ArrayList();
                ly_campos = (ArrayList) it1.next();
                if (ly_campos != null) {
                    String codigo = (String) ly_campos.get(0);
                    String nombre = (String) ly_campos.get(1);
                    if (codigo.equals(codi_municipio)) {
                        s_retorno += "<option selected id='" + codigo + "'>" + nombre + "</option>";
                    } else {
                        s_retorno += "<option id='" + codigo + "'>" + nombre + "</option>";
                    }
                }
            }
            s_retorno += "</select>";
        }
        return s_retorno;
    }
}
