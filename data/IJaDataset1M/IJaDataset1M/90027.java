package beans;

import java.util.*;
import java.sql.*;
import dbUtil.*;
import utils.*;
import iteradores.IteradorModulo;

/**
 * Clase que representa el bean de la tabla Modulo de la BD
 * @author José Giménez, Gustavo Planás, Esteban Benítez
 */
public class Modulo extends PgAbm implements Abm {

    ResultSet rs_modulo;

    private Hashtable th_modulo = new Hashtable();

    /**
     * Constructor de la clase Modulo
     */
    public Modulo() {
        super.setTableName("modulo");
        ArrayList array_campos = new ArrayList();
        array_campos.add("idmodulo");
        array_campos.add("descripcion");
        array_campos.add("idtipomodulo");
        array_campos.add("pathservlet");
        array_campos.add("usuario_ing");
        array_campos.add("fecha_ing");
        ArrayList array_claves = new ArrayList();
        array_claves.add("idmodulo");
        super.initMapping();
        super.setCampos(array_campos);
        super.setClaves(array_claves);
        super.setSequence("idmodulo", "idmodulo_idmodulo_seq");
    }

    /**
     * Obtiene el ID del módulo
     * @return ID del módulo
     */
    public String getIdModulo() {
        return (String) getMapValue("idmodulo");
    }

    /**
     * Sete el ID del módulo 
     * @param idmodulo Nuevo ID del módulo 
     */
    public void setIdModulo(String idmodulo) {
        setMapValue("idmodulo", idmodulo);
    }

    /**
     * Obtiene la descripción del módulo
     * @return Descripción del módulo
     */
    public String getDescripcion() {
        return (String) getMapValue("descripcion");
    }

    /**
     * Setea la descripción del módulo
     * @param descripcion Nueva descripción del módulo
     */
    public void setDescripcion(String descripcion) {
        setMapValue("descripcion", descripcion);
    }

    /**
     * Obtiene el ID del tipo del módulo
     * @return ID del tipo del módulo
     */
    public String getIdTipoModulo() {
        return (String) getMapValue("idtipomodulo");
    }

    /**
     * Setea el ID del tipo del módulo
     * @param idtipomodulo Nuevo ID del tipo del módulo
     */
    public void setIdTipoModulo(String idtipomodulo) {
        setMapValue("idtipomodulo", idtipomodulo);
    }

    /**
     * Obtiene el path del servlet asociado al módulo
     * @return Path del servlet que maneja el módulo
     */
    public String getPath() {
        return (String) getMapValue("pathservlet");
    }

    /**
     * Setea el path del servlet que maneja el módulo
     * @param pathservlet Nuevo path del servlet que maneja el módulo
     */
    public void setPath(String pathservlet) {
        setMapValue("pathservlet", pathservlet);
    }

    /**
     * Obtiene el iterador de la clase Modulo
     * @return Iterador de la clase Modulo
     * @throws java.lang.Exception
     */
    public Iterador getIterator() throws Exception {
        ResultSet rs_datos = super.getResultSet();
        IteradorModulo iter = new IteradorModulo(rs_datos);
        return iter;
    }

    public int getModulos() throws Exception {
        String sql_query = "";
        int cant_filas;
        Hashtable th_aux = new Hashtable();
        sql_query = "select * from modulo order by 2; ";
        sql_query += getLimitOffset();
        Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        System.out.println("SQL: " + sql_query);
        rs_modulo = stm.executeQuery(sql_query);
        cant_filas = 0;
        if (rs_modulo.last()) {
            cant_filas = rs_modulo.getRow();
            rs_modulo.beforeFirst();
        }
        if (rs_modulo != null) {
            while (rs_modulo.next()) {
                th_aux.put(rs_modulo.getString(1), rs_modulo.getString(2));
            }
            th_modulo = th_aux;
        }
        System.out.println("Cantidad Filas: " + cant_filas);
        return cant_filas;
    }

    public String getModuloByID(String ID) throws Exception {
        String result = "";
        if (!th_modulo.isEmpty()) {
            result = (String) th_modulo.get(ID);
        }
        return result;
    }

    public Iterador getIteratorAll() throws Exception {
        IteradorModulo iter = new IteradorModulo(rs_modulo);
        return iter;
    }
}
