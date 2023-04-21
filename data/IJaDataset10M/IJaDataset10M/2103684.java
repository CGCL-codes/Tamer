package fondefitco.Controlador;

import fondefitco.Modelo.Surtido;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MasterFire
 */
public class ControlSurtido {

    public static Surtido load(ResultSet rs) throws SQLException {
        Surtido s = new Surtido();
        s.setCodigo(rs.getInt(1));
        s.setFecha(rs.getDate(2));
        s.setResponsable(rs.getString(3));
        return s;
    }

    private static List surtidos;

    public static Connection con;

    public static void RegistrarSurtido(Surtido s) throws SQLException, IOException, Exception {
        ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        con = mbd.getConexion();
        if (con == null) {
            throw new SQLException(" no hay conexion ");
        }
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("Insert Into surtido values(?,?,?)");
            pst.setInt(1, s.getCodigo());
            java.sql.Date fechas = new java.sql.Date(s.getFecha().getTime());
            pst.setDate(2, fechas);
            pst.setString(3, s.getResponsable());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static Surtido BuscarSurtido(int ide) throws SQLException {
        if (ide == 0) {
            throw new SQLException("No hay elemento clave de la clase solicitud");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        Surtido solicitud = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from surtido where codigo = ?");
            pst.setInt(1, ide);
            rs = pst.executeQuery();
            while (rs.next()) {
                solicitud = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return solicitud;
        }
    }

    public static void ModificarSurtido(Surtido s, int ide) throws SQLException, Exception {
        if (BuscarSurtido(ide) == null) {
            throw new SQLException("solicitud no registrada");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("Update surtido set codigo = ?, fecha = ?," + "responsable = ?  where codigo = ?");
            pst.setInt(1, s.getCodigo());
            java.sql.Date fechas = new java.sql.Date(s.getFecha().getTime());
            pst.setDate(2, fechas);
            pst.setString(3, s.getResponsable());
            pst.setInt(4, ide);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static int proximo() {
        try {
            String sql = "SELECT MAX(Codigo)+1 as Proximo FROM surtido";
            ManejadorBaseDatos basedatos = ManejadorBaseDatos.getInstancia();
            basedatos.conectar();
            ResultSet res = basedatos.consultar(sql);
            int prox = 0;
            if (res.next()) {
                prox = res.getInt("Proximo");
                return prox;
            } else {
                return prox;
            }
        } catch (Exception er) {
            System.out.println("Error al consultar la proxima Solicitud de Compra");
            return 0;
        }
    }

    public static void listarSurtido() throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        surtidos = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from surtido");
            rs = pst.executeQuery();
            while (rs.next()) {
                surtidos.add(load(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void setSurtido(List listasurtidos) {
        surtidos = listasurtidos;
    }

    public static List getSurtido() {
        return surtidos;
    }
}
