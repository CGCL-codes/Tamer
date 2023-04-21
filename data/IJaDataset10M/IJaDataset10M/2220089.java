package invsys.Controladores;

import invsys.Modelo.DetalleCompra;
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
public class ControlDetalleCompra {

    public static DetalleCompra load(ResultSet rs) throws SQLException {
        DetalleCompra dc = new DetalleCompra();
        dc.setProducto(ControlProducto.BuscarProducto(rs.getString(1)));
        dc.setCompra(ControlCompra.BuscarCompra(rs.getString(2)));
        dc.setCantidad(rs.getDouble(3));
        dc.setSubtotal(rs.getDouble(4));
        dc.setCantidadActual(rs.getDouble(5));
        dc.setCantidadFinal(rs.getDouble(6));
        return dc;
    }

    private static List detallescompras;

    public static Connection con;

    public static void RegistrarDetalleCompra(DetalleCompra dc) throws SQLException, IOException, Exception {
        ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        con = mbd.getConexion();
        if (con == null) {
            throw new SQLException(" no hay conexion ");
        }
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("Insert Into detallecompras values(?,?,?,?,?,?)");
            pst.setString(1, dc.getProducto().getCodigo());
            pst.setString(2, dc.getCompra().getNumero());
            pst.setDouble(3, dc.getCantidad());
            pst.setDouble(4, dc.getSubtotal());
            pst.setDouble(5, dc.getCantidadActual());
            pst.setDouble(6, dc.getCantidadFinal());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static DetalleCompra BuscarCompra(String producto, String compra) throws SQLException {
        if (producto == null) {
            throw new SQLException("No hay elemento clave de la clase usuario");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        DetalleCompra detallecompra = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from detallecompras where producto= ? and compra=?");
            pst.setString(1, producto.trim());
            pst.setString(2, compra);
            rs = pst.executeQuery();
            while (rs.next()) {
                detallecompra = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return detallecompra;
        }
    }

    public static void ModificarCompra(DetalleCompra dc, String producto, String compra) throws SQLException, Exception {
        if (BuscarCompra(producto, compra) == null) {
            throw new SQLException("usuario no registrado ");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("UPDATE detallecompra SET cantidad = ? ," + " where producto= ? and compra=?");
            pst.setDouble(1, dc.getCantidad());
            pst.setString(2, producto);
            pst.setString(3, compra);
            ;
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarCompra() throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        detallescompras = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from compras ");
            rs = pst.executeQuery();
            while (rs.next()) {
                detallescompras.add(load(rs));
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

    public static boolean ExisteCompra(int ide) {
        boolean existe = false;
        return existe;
    }

    public static void listarContacto1(String sql) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        detallescompras = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                detallescompras.add(load(rs));
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

    public static void setContacto(List aDetalleCompra) {
        detallescompras = aDetalleCompra;
    }

    public static List getContacto() {
        return detallescompras;
    }
}
