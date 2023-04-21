package fondefitco.Controlador;

import fondefitco.Modelo.Centrodecosto;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dell
 */
public class ControlCentrodeCosto {

    public static Connection con;

    private static List centrocosto;

    public static Centrodecosto load(ResultSet rs) throws SQLException {
        Centrodecosto a = new Centrodecosto();
        a.setcodigo(rs.getString(1));
        a.setnombre(rs.getString(2));
        return a;
    }

    public static void registrarCentrodecosto(Centrodecosto a) throws SQLException, IOException, Exception {
        ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        con = mbd.getConexion();
        if (con == null) {
            throw new SQLException(" no hay conexion ");
        }
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("Insert Into arp values(?,?)");
            pst.setString(1, a.getcodigo());
            pst.setString(2, a.getnombre());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static Centrodecosto BuscarCentrodecosto(String ide) throws SQLException {
        if (ide == null) {
            throw new SQLException("No hay elemento clave de la clase empleado");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        Centrodecosto cc = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from centro_costo where codigo = ?");
            pst.setString(1, ide.trim());
            rs = pst.executeQuery();
            while (rs.next()) {
                cc = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return cc;
        }
    }

    public static void ModificarCentrodecosto(Centrodecosto a, String ide) throws SQLException, Exception {
        if (BuscarCentrodecosto(ide) == null) {
            throw new SQLException("Empleado no registrado ");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("Update centro_costo set codigo_arp = ?, nombre = ? where codigo_arp = ?");
            pst.setString(1, a.getcodigo());
            pst.setString(2, a.getnombre());
            pst.setString(5, ide);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarCentrodeCosto() throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        centrocosto = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from centro_costo ");
            rs = pst.executeQuery();
            while (rs.next()) {
                centrocosto.add(load(rs));
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

    public static boolean ExisteCentrodeCosto(int ide) {
        boolean existe = false;
        return existe;
    }

    public static void EliminarCentrodeCosto(String ide) throws SQLException, Exception {
        if (BuscarCentrodecosto(ide) == null) {
            throw new SQLException("No hay elemento clave de la clase Empleado");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("delete from arp where codigo_arp = ?");
            pst.setString(1, ide.trim());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarCentrodeCosto1(String sql) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        centrocosto = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                centrocosto.add(load(rs));
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

    public static void setCentrodeCosto(List acentrocosto) {
        centrocosto = acentrocosto;
    }

    public static List getCentrodeCosto() {
        return centrocosto;
    }
}
