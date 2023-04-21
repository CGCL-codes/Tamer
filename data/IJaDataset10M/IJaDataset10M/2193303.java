package accesoDatos;

import java.sql.*;
import logica.*;
import java.util.*;

/**
 *
 * @author Gamboa Family
 */
public class DaoPalabraClave {

    FachadaBD fachada;

    public DaoPalabraClave() {
        fachada = new FachadaBD();
    }

    public void guardarPalabra(PalabraClave pal) {
        String sql_guardar;
        sql_guardar = "INSERT INTO palabra_clave(codigo_palabra, nombre_palabra," + "descripcion_palabra, estado_palabra) " + "VALUES ('" + pal.getCodigo() + "', '" + pal.getNombre() + "', '" + pal.getDescripcion() + "', '" + pal.getEstado() + "')";
        ejecutarUpdate(sql_guardar);
    }

    public int ejecutarUpdate(String insert) {
        try {
            Connection conn = fachada.conectar();
            Statement sentencia = conn.createStatement();
            int numFilas = sentencia.executeUpdate(insert);
            conn.close();
            return numFilas;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public Vector consultarPalabraPorNombre(String nombre) {
        Vector v = new Vector();
        String sql_select;
        sql_select = "SELECT codigo_palabra, nombre_palabra, descripcion_palabra " + "FROM palabra_clave WHERE nombre_palabra like '%" + nombre + "%'";
        try {
            Connection conn = fachada.conectar();
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                Vector v2 = new Vector();
                v2.add(tabla.getString(1));
                v2.add(tabla.getString(2));
                v2.add(tabla.getString(3));
                v.add(v2);
            }
            conn.close();
            System.out.println("Conexion cerrada");
            return v;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Vector consultarPalabraPorCodigo(String codigo) {
        Vector v = new Vector();
        String sql_select;
        sql_select = "SELECT codigo_palabra, nombre_palabra, descripcion_palabra " + "FROM palabra_clave WHERE codigo_palabra = '" + codigo + "'";
        try {
            Connection conn = fachada.conectar();
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                Vector v2 = new Vector();
                v2.add(tabla.getString(1));
                v2.add(tabla.getString(2));
                v2.add(tabla.getString(3));
                v.add(v2);
            }
            conn.close();
            System.out.println("Conexion cerrada");
            return v;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Vector consultarTodasLasPalabras() {
        Vector v = new Vector();
        String sql_select;
        sql_select = "SELECT codigo_palabra, nombre_palabra, descripcion_palabra " + "FROM palabra_clave";
        try {
            Connection conn = fachada.conectar();
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                Vector v2 = new Vector();
                v2.add(tabla.getString(1));
                v2.add(tabla.getString(2));
                v2.add(tabla.getString(3));
                v.add(v2);
            }
            conn.close();
            System.out.println("Conexion cerrada");
            return v;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public PalabraClave retornarPalabraPorCodigo(String codigo) {
        PalabraClave p = new PalabraClave();
        String sql_select;
        sql_select = "SELECT codigo_palabra, nombre_palabra, descripcion_palabra " + "FROM palabra_clave WHERE codigo_palabra = '" + codigo + "'";
        try {
            Connection conn = fachada.conectar();
            System.out.println("consultando en la bd");
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                p.setCodigo(tabla.getString(1));
                p.setNombre(tabla.getString(2));
                p.setDescripcion(tabla.getString(3));
                System.out.println("Palabra Ok");
            }
            conn.close();
            return p;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void modificarPalabraClave(int caso, String cod_palabra, String valor) {
        String actualizacion;
        switch(caso) {
            case 1:
                actualizacion = "UPDATE palabra_clave SET nombre_palabra = \'" + valor + "\' " + "WHERE codigo_palabra = \'" + cod_palabra + "\'";
                ejecutarUpdate(actualizacion);
                break;
            case 2:
                actualizacion = "UPDATE palabra_clave SET descripcion_palabra = \'" + valor + "\' " + "WHERE codigo_palabra = \'" + cod_palabra + "\'";
                ejecutarUpdate(actualizacion);
                break;
        }
    }

    public boolean existeCodigo(String codigo) {
        String sql_select, aux = "";
        sql_select = "SELECT codigo_palabra FROM palabra_clave WHERE " + "codigo_palabra =\'" + codigo + "\'";
        try {
            Connection conn = fachada.conectar();
            System.out.println("consultando en la bd");
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                aux = tabla.getString(1);
                System.out.println(aux);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (aux.equals(codigo)) return true; else return false;
    }

    public void desactivarPalabra(String codigo) {
        String actualizacion;
        actualizacion = "UPDATE palabra_clave SET estado_palabra = '1' " + "WHERE codigo_palabra = \'" + codigo + "\'";
        ejecutarUpdate(actualizacion);
        System.out.println(actualizacion);
    }

    public void activarPalabra(String codigo) {
        String actualizacion;
        actualizacion = "UPDATE palabra_clave SET estado_palabra = '0' " + "WHERE codigo_palabra = \'" + codigo + "\'";
        ejecutarUpdate(actualizacion);
        System.out.println(actualizacion);
    }

    public Vector listarPalabras() {
        Vector v = new Vector();
        PalabraClave p = new PalabraClave();
        String sql_select;
        sql_select = "SELECT codigo_palabra FROM palabra_clave ";
        try {
            Connection conn = fachada.conectar();
            System.out.println("consultando en la bd");
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                p = retornarPalabraPorCodigo(tabla.getString(1));
                v.add(p);
            }
            conn.close();
            System.out.println("Listado Palabras Ok");
            return v;
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean esActivada(String cod_palabra) {
        String sql_select, estado = "";
        boolean respuesta = true;
        sql_select = "SELECT estado_palabra FROM palabra " + "WHERE codigo_palabra = '" + cod_palabra + "'";
        try {
            Connection conn = fachada.conectar();
            System.out.println("consultando en la bd");
            Statement sentencia = conn.createStatement();
            ResultSet tabla = sentencia.executeQuery(sql_select);
            while (tabla.next()) {
                estado = tabla.getString(1);
            }
            if (estado.equals("1")) respuesta = false;
            conn.close();
            System.out.println("Consulta de si esta Activado Palabra Ok");
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return respuesta;
    }
}
