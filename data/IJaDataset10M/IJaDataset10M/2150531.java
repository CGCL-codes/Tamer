package zonasoft.Manejadores;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import zonasoft.Conceptos.Especiales.Barrio;
import zonasoft.Conceptos.Especiales.Localidad;
import zonasoft.Conceptos.Especiales.UnidadComunera;

/**
 *
 * @author Usuario
 */
public class ManejadorLugar {

    public static Connection con;

    public static Connection conm;

    private static List listaBarrios;

    public static List getBarrios() {
        return listaBarrios;
    }

    public static List listarBarrios() throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        listaBarrios = new LinkedList();
        try {
            pst = (PreparedStatement) con.prepareStatement(" SELECT * FROM `barrio` ORDER BY `barrio`.`nombre` ASC");
            rs = pst.executeQuery();
            while (rs.next()) {
                listaBarrios.add(Barrio.load(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return listaBarrios;
        }
    }

    public static List listarBarriosByUnidadComu(String idUnidad) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        listaBarrios = new LinkedList();
        try {
            pst = (PreparedStatement) con.prepareStatement(" SELECT barrio.idBarrio,barrio.iducdg,barrio.nombre,barrio.descripcion" + " FROM zonificacion.barrio " + " INNER JOIN zonificacion.udegobierno " + " ON (barrio.iducdg = udegobierno.codigo) " + " WHERE udegobierno.codigo = ?");
            pst.setString(1, idUnidad);
            rs = pst.executeQuery();
            while (rs.next()) {
                listaBarrios.add(Barrio.load(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return listaBarrios;
        }
    }

    private static List listaLocalidad;

    private static List unidadesDeGob;

    public static List getLocalidad() {
        return listaLocalidad;
    }

    public static List listarLocalidad(String idBarrio) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        listaLocalidad = new LinkedList();
        try {
            pst = (PreparedStatement) con.prepareStatement(" select u.nombre as comuna, l.nombre as localidad from barrio b, udegobierno u, localidad l where b.idBarrio = ? && u.codigo = b.iducdg && l.codigo = u.idlocalidad");
            pst.setString(1, idBarrio);
            rs = pst.executeQuery();
            while (rs.next()) {
                listaLocalidad.add(Localidad.load2(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return listaLocalidad;
        }
    }

    public static List listarLocalidadByMunicipio(String idMunicipio) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        listaLocalidad = new LinkedList();
        try {
            pst = (PreparedStatement) con.prepareStatement("SELECT localidad.codigo,localidad.nombre,localidad.idmunicipio " + "FROM zonificacion.localidad " + " INNER JOIN zonificacion.municipios " + " ON (localidad.idmunicipio = municipios.codigo) " + " WHERE municipios.codigo = ?");
            pst.setString(1, idMunicipio);
            rs = pst.executeQuery();
            while (rs.next()) {
                listaLocalidad.add(Localidad.load(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return listaLocalidad;
        }
    }

    public static List listarUnidadComuneraByLocalidad(String idLocalidad) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        unidadesDeGob = new LinkedList();
        try {
            pst = (PreparedStatement) con.prepareStatement("SELECT udegobierno.codigo,udegobierno.nombre,udegobierno.idlocalidad " + "FROM zonificacion.udegobierno " + " INNER JOIN zonificacion.localidad " + " ON (udegobierno.idlocalidad = localidad.codigo) " + " WHERE localidad.codigo = ?");
            pst.setString(1, idLocalidad);
            rs = pst.executeQuery();
            while (rs.next()) {
                unidadesDeGob.add(UnidadComunera.load(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return unidadesDeGob;
        }
    }

    public static String[] getDatosLugar(String idBarrio) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        String[] datos = new String[2];
        try {
            pst = (PreparedStatement) con.prepareStatement(" select u.nombre as comuna, l.nombre as localidad from barrio b, udegobierno u, localidad l where b.idBarrio = ? && u.codigo = b.iducdg && l.codigo = u.idlocalidad");
            pst.setString(1, idBarrio);
            rs = pst.executeQuery();
            while (rs.next()) {
                datos[0] = rs.getString(2);
                datos[1] = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return datos;
        }
    }
}
