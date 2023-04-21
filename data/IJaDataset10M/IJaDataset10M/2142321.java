package accesoDatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.JOptionPane;

public class DAOReportes {

    private String sqlInstruction;

    private FachadaBD fachada;

    ResultSet objresult1, objresult2, r;

    public DAOReportes() {
        fachada = new FachadaBD();
    }

    public Object[][] reporte(String sql) {
        try {
            Connection con = fachada.conectar();
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery(sql);
            Object[][] arr = ResultSetToArray(rs);
            con.close();
            rs.close();
            con.close();
            return arr;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Object[][] ResultSetToArray(ResultSet rs) {
        Object obj[][] = null;
        try {
            rs.last();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            int numFils = rs.getRow();
            obj = new Object[numFils][numCols];
            rs.beforeFirst();
            int j = 0;
            while (rs.next()) {
                for (int i = 0; i < numCols; i++) {
                    obj[j][i] = rs.getObject(i + 1);
                }
                j++;
            }
        } catch (Exception e) {
        }
        return obj;
    }

    public Object[][] getDatos1() {
        Object fila[][] = reporte("SELECT DISTINCT material.titulo_principal_material, COUNT(material.titulo_principal_material) FROM material,descargar_usuario_material WHERE material.codigo_material=descargar_usuario_material.codigo_material GROUP BY(material.titulo_principal_material) ORDER BY COUNT(material.titulo_principal_material) DESC");
        return fila;
    }

    public Object[][] getCount1() {
        Object fila[][] = reporte("SELECT COUNT(*) FROM(SELECT DISTINCT material.titulo_principal_material, COUNT(material.titulo_principal_material) FROM material,descargar_usuario_material WHERE material.codigo_material=descargar_usuario_material.codigo_material GROUP BY(titulo_principal_material) ORDER BY COUNT(material.titulo_principal_material) DESC) AS M");
        return fila;
    }

    public Object[][] getDatos2(String dia, String mes, String ano) {
        Object fila[][] = reporte("SELECT DISTINCT material.titulo_principal_material, COUNT(material.titulo_principal_material) FROM material,descargar_usuario_material WHERE material.codigo_material= descargar_usuario_material.codigo_material AND descargar_usuario_material.fecha_descarga>='" + dia + "-" + mes + "-" + ano + "' AND descargar_usuario_material.fecha_descarga<='28-" + mes + "-" + ano + "' GROUP BY(material.titulo_principal_material) ORDER BY(material.titulo_principal_material) DESC");
        return fila;
    }

    public Object[][] getCount2(String dia, String mes, String ano) {
        Object fila[][] = reporte("SELECT COUNT(*) FROM(SELECT DISTINCT material.titulo_principal_material, COUNT(material.titulo_principal_material) FROM material,descargar_usuario_material WHERE material.codigo_material= descargar_usuario_material.codigo_material AND descargar_usuario_material.fecha_descarga>='" + dia + "-" + mes + "-" + ano + "' AND descargar_usuario_material.fecha_descarga<='28-" + mes + "-" + ano + "' GROUP BY(material.titulo_principal_material) ORDER BY(material.titulo_principal_material) DESC) AS M");
        return fila;
    }

    public Object[][] getDatos3() {
        Object fila[][] = reporte("SELECT DISTINCT area_conocimiento.nombre_area,COUNT(area_conocimiento.nombre_area) FROM area_conocimiento,area_usuario WHERE area_usuario.codigo_area=area_conocimiento.codigo_area GROUP BY(area_conocimiento.nombre_area) ORDER BY COUNT(area_conocimiento.nombre_area) DESC");
        return fila;
    }

    public Object[][] getCount3() {
        Object fila[][] = reporte("SELECT COUNT(*) FROM(SELECT DISTINCT area_conocimiento.nombre_area,COUNT(area_conocimiento.nombre_area) FROM area_conocimiento,area_usuario WHERE area_usuario.codigo_area=area_conocimiento.codigo_area GROUP BY(area_conocimiento.nombre_area) ORDER BY COUNT(area_conocimiento.nombre_area) DESC) AS M");
        return fila;
    }

    public Object[][] getDatos4(String dia, String mes, String ano) {
        Object fila[][] = reporte("SELECT nombres_usuario,apellidos_usuario,fecha_registro_usuario FROM usuario WHERE fecha_registro_usuario>='01-" + mes + "-" + ano + "' AND fecha_registro_usuario<='" + dia + "-" + mes + "-" + ano + "'");
        return fila;
    }

    public Object[][] getCount4(String dia, String mes, String ano) {
        Object fila[][] = reporte("SELECT COUNT(*)FROM(SELECT nombres_usuario,apellidos_usuario,fecha_registro_usuario FROM usuario WHERE fecha_registro_usuario>='01-" + mes + "-" + ano + "' AND fecha_registro_usuario<='" + dia + "-" + mes + "-" + ano + "') AS M");
        return fila;
    }

    public Object[][] getDatos5(String area, String dia, String mes, String ano) {
        Object fila[][] = reporte("SELECT material.titulo_principal_material,material.fecha_catalogacion_material FROM material,area_material WHERE material.codigo_material=area_material.codigo_material AND material.fecha_catalogacion_material>='01-" + mes + "-" + ano + "' AND material.fecha_catalogacion_material<='" + dia + "-" + mes + "-" + ano + "' AND area_material.codigo_area='" + area + "' ORDER BY(material.fecha_catalogacion_material)");
        return fila;
    }

    public Object[][] getCount5(String area, String dia, String mes, String ano) {
        Object fila[][] = reporte("SELECT COUNT(*)FROM(SELECT material.titulo_principal_material,material.fecha_catalogacion_material FROM material,area_material WHERE material.codigo_material=area_material.codigo_material AND material.fecha_catalogacion_material>='01-" + mes + "-" + ano + "' AND material.fecha_catalogacion_material<='" + dia + "-" + mes + "-" + ano + "' AND area_material.codigo_area='" + area + "' ORDER BY(material.fecha_catalogacion_material)) AS M");
        return fila;
    }

    public Object[][] getDatos6_1() {
        Object fila[][] = reporte("SELECT DISTINCT hora_consulta,COUNT(hora_consulta) FROM consulta_usuario_material GROUP BY(hora_consulta) ORDER BY COUNT(hora_consulta) DESC");
        return fila;
    }

    public Object[][] getCount6_1() {
        Object fila[][] = reporte("SELECT COUNT(*)FROM(SELECT DISTINCT hora_consulta,COUNT(hora_consulta) FROM consulta_usuario_material GROUP BY(hora_consulta) ORDER BY COUNT(hora_consulta) DESC) AS M");
        return fila;
    }

    public Object[][] getDatos6_2() {
        Object fila[][] = reporte("SELECT DISTINCT hora_descarga,COUNT(hora_descarga) FROM descargar_usuario_material GROUP BY(hora_descarga) ORDER BY COUNT(hora_descarga) DESC");
        return fila;
    }

    public Object[][] getCount6_2() {
        Object fila[][] = reporte("SELECT COUNT(*)FROM(SELECT DISTINCT hora_descarga,COUNT(hora_descarga) FROM descargar_usuario_material GROUP BY(hora_descarga) ORDER BY COUNT(hora_descarga) DESC) AS M");
        return fila;
    }

    public Object[][] getDatos7_1() {
        Object fila[][] = reporte("SELECT DISTINCT usuario.nombres_usuario,usuario.apellidos_usuario,material.titulo_principal_material,descargar_usuario_material.fecha_descarga,descargar_usuario_material.hora_descarga FROM descargar_usuario_material,material,usuario WHERE material.codigo_material=descargar_usuario_material.codigo_material AND usuario.codigo_usuario=descargar_usuario_material.codigo_usuario ORDER BY(material.titulo_principal_material)");
        return fila;
    }

    public Object[][] getCount7_1() {
        Object fila[][] = reporte("SELECT COUNT(*)FROM(SELECT DISTINCT usuario.nombres_usuario,usuario.apellidos_usuario,material.titulo_principal_material,descargar_usuario_material.fecha_descarga,descargar_usuario_material.hora_descarga FROM descargar_usuario_material,material,usuario WHERE material.codigo_material=descargar_usuario_material.codigo_material AND usuario.codigo_usuario=descargar_usuario_material.codigo_usuario ORDER BY(material.titulo_principal_material)) AS M");
        return fila;
    }

    public Object[][] getDatos7_2(String codigo) {
        Object fila[][] = reporte("SELECT DISTINCT material.titulo_principal_material,descargar_usuario_material.fecha_descarga,descargar_usuario_material.hora_descarga FROM descargar_usuario_material,material WHERE material.codigo_material=descargar_usuario_material.codigo_material AND descargar_usuario_material.codigo_usuario='" + codigo + "'");
        return fila;
    }

    public Object[][] getCount7_2(String codigo) {
        Object fila[][] = reporte("SELECT COUNT(*)FROM(SELECT DISTINCT material.titulo_principal_material,descargar_usuario_material.fecha_descarga,descargar_usuario_material.hora_descarga FROM descargar_usuario_material,material WHERE material.codigo_material=descargar_usuario_material.codigo_material AND descargar_usuario_material.codigo_usuario='" + codigo + "') AS M");
        return fila;
    }

    public String[] returnNombre(String codigo) {
        String[] nombre = new String[2];
        try {
            Connection con = fachada.conectar();
            Statement state = con.createStatement();
            ResultSet objresult = state.executeQuery("SELECT * FROM usuario");
            while (objresult.next()) {
                if (objresult.getString("codigo_usuario").equals(codigo)) {
                    nombre[0] = (String) objresult.getObject("nombres_usuario");
                    nombre[1] = (String) objresult.getObject("apellidos_usuario");
                }
            }
            objresult.close();
            state.close();
            con.close();
        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Hay error para ingresar datos en la base de datos : " + error.toString());
        }
        return nombre;
    }

    public Vector<StringBuffer> documentosDescargadosPorTipoDeUsuario(int opcion) {
        Vector<StringBuffer> retorno = new Vector();
        if (opcion == 0) {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_usuario , nombres_usuario, apellidos_usuario,tipo_usuario FROM usuario ORDER BY tipo_usuario;";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (opcion == 1) {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_usuario , nombres_usuario, apellidos_usuario,tipo_usuario FROM usuario  WHERE tipo_usuario='1' ;";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (opcion == 2) {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_usuario , nombres_usuario, apellidos_usuario,tipo_usuario FROM usuario  WHERE tipo_usuario='2' ;";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (opcion == 3) {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_usuario , nombres_usuario, apellidos_usuario,tipo_usuario FROM usuario  WHERE tipo_usuario='3' ;";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (opcion == 4) {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_usuario , nombres_usuario, apellidos_usuario,tipo_usuario FROM usuario  WHERE tipo_usuario='4' ;";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
    }

    public Vector<StringBuffer> documentosExistentesPorTipoDeFormato(String opcion) {
        Vector<StringBuffer> retorno = new Vector();
        if (opcion.equals("todos los formatos")) {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_material,  titulo_principal_material, formato_material  FROM material ORDER BY formato_material;";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            retorno = new Vector();
            sqlInstruction = "SELECT codigo_material,titulo_principal_material, formato_material  FROM material WHERE formato_material='" + opcion + "';";
            try {
                Connection conn = fachada.conectar();
                Statement sentencia = conn.createStatement();
                ResultSet tabla = sentencia.executeQuery(sqlInstruction);
                ResultSetMetaData metaDatos = tabla.getMetaData();
                int numeroDeColumnas = metaDatos.getColumnCount();
                StringBuffer fila = new StringBuffer();
                fila.append("codigo\t   usuario registrado");
                retorno.add(fila);
                while (tabla.next()) {
                    fila = new StringBuffer();
                    for (int i = 1; i < numeroDeColumnas; i++) fila.append(tabla.getObject(i) + "\t");
                    retorno.add(fila);
                }
                for (int i = 0; i < retorno.size(); i++) System.out.println(retorno.get(i));
            } catch (SQLException ex) {
                Logger.getLogger(DAOReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
    }

    public static void main(String args[]) {
        DAOReportes apli = new DAOReportes();
        apli.documentosDescargadosPorTipoDeUsuario(0);
    }
}
