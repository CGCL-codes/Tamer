package invsys.Controladores;

import invsys.Modelo.Promocion;
import invsys.Utilidades.ManejadorBaseDatos;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author luigie
 */
public class ControlPromocion {

    public static Promocion load(ResultSet rs) throws SQLException {
        Promocion p = new Promocion();
        p.setId(rs.getString(1));
        p.setProducto(ControlProducto.BuscarProducto(rs.getString(2)));
        p.setDescripcion(rs.getString(3));
        p.setFechinicial(rs.getString(4));
        p.setFechinicial(rs.getString(5));
        p.setPorcentaje(rs.getDouble(6));
        return p;
    }

    private static List promociones;

    public static Connection con;

    public static void RegistrarPromocion(Promocion p) throws SQLException, IOException, Exception {
        ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        con = mbd.getConexion();
        if (con == null) {
            throw new SQLException(" no hay conexion ");
        }
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("Insert Into promociones values(?,?,?,?,?,?)");
            pst.setString(1, p.getId());
            pst.setDouble(2, p.getProducto().getCantidad());
            pst.setString(3, p.getDescripcion());
            pst.setString(4, p.getFechinicial());
            pst.setString(5, p.getFechinicial());
            pst.setDouble(6, p.getPorcentaje());
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static Promocion BuscarClienteid(String ide) throws SQLException {
        if (ide == null) {
            throw new SQLException("No hay elemento clave de la clase usuario");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        Promocion promocion = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from promociones where id= ?");
            pst.setString(1, ide.trim());
            rs = pst.executeQuery();
            while (rs.next()) {
                promocion = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return promocion;
        }
    }

    public static Promocion BuscarClientenombre(String nom) throws SQLException {
        if (nom == null) {
            throw new SQLException("No hay elemento clave de la clase Clientes");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        Promocion promocion = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from promociones where porcentaje= ?");
            pst.setString(1, nom.trim());
            rs = pst.executeQuery();
            while (rs.next()) {
                promocion = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return promocion;
        }
    }

    public static void ModificarCliente(Promocion p, String ide) throws SQLException, Exception {
        if (BuscarClienteid(ide) == null) {
            throw new SQLException("usuario no registrado ");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("UPDATE porcentaje SET id = ? ," + " nombre = ?,apellido = ?, direccion = ?, telefono = ? ," + " celular = ? WHERE id = ?");
            pst.setString(1, p.getId());
            pst.setString(2, p.getProducto().getCodigo());
            pst.setString(3, p.getDescripcion());
            pst.setString(4, p.getFechinicial());
            pst.setString(5, p.getFechfinal());
            pst.setDouble(6, p.getPorcentaje());
            pst.setString(7, ide);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarCliente() throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        promociones = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from promociones ");
            rs = pst.executeQuery();
            while (rs.next()) {
                promociones.add(load(rs));
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

    public static boolean ExisteCliente(int ide) {
        boolean existe = false;
        return existe;
    }

    public static void EliminarCliente(String ide) throws SQLException, Exception {
        if (BuscarClienteid(ide) == null) {
            throw new SQLException("No hay elemento clave de la clase Clientes");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("delete from promociones where id = ?");
            pst.setString(1, ide.trim());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarCliente1(String sql) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        promociones = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                promociones.add(load(rs));
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

    public static void setCliente(List aEmpleado) {
        promociones = aEmpleado;
    }

    public static List getCliente() {
        return promociones;
    }
}
