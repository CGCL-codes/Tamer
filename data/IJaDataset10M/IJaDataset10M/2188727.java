package invsys.Controladores;

import invsys.Modelo.Proveedor;
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
public class ControlProveedor {

    public static Proveedor load(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setNit(rs.getString(1));
        p.setNombre(rs.getString(2));
        p.setDireccion(rs.getString(3));
        p.setTelefono(rs.getString(4));
        return p;
    }

    private static List proveedores;

    public static Connection con;

    public static void RegistrarProveedor(Proveedor p) throws SQLException, IOException, Exception {
        ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        con = mbd.getConexion();
        if (con == null) {
            throw new SQLException(" no hay conexion ");
        }
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("Insert Into proveedores values(?,?,?,?)");
            pst.setString(1, p.getNit());
            pst.setString(2, p.getNombre());
            pst.setString(3, p.getDireccion());
            pst.setString(4, p.getTelefono());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static Proveedor BuscarProveedor(String ide) throws SQLException {
        if (ide == null) {
            throw new SQLException("No hay elemento clave de la clase usuario");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        Proveedor proveedor = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from proveedores where nit=?");
            pst.setString(1, ide.trim());
            rs = pst.executeQuery();
            while (rs.next()) {
                proveedor = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return proveedor;
        }
    }

    public static Proveedor BuscarProveedornombre(String nom) throws SQLException {
        if (nom == null) {
            throw new SQLException("No hay elemento clave de la clase usuarios");
        }
        ResultSet rs = null;
        PreparedStatement pst = null;
        Proveedor proveedor = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from proveedores where nombre= ?");
            pst.setString(1, nom.trim());
            rs = pst.executeQuery();
            while (rs.next()) {
                proveedor = load(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            return proveedor;
        }
    }

    public static void ModificarProveedor(Proveedor p, String ide) throws SQLException, Exception {
        if (BuscarProveedor(ide) == null) {
            throw new SQLException("usuario no registrado ");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("UPDATE proveedores SET nit =? ," + " nombre =?,direccion=?,telefono =?" + " WHERE nit =?");
            pst.setString(1, p.getNit());
            pst.setString(2, p.getNombre());
            pst.setString(3, p.getDireccion());
            pst.setString(4, p.getTelefono());
            pst.setString(5, ide);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarProveedor() throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        proveedores = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("select * from proveedores ");
            rs = pst.executeQuery();
            while (rs.next()) {
                proveedores.add(load(rs));
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

    public static boolean ExisteUsuario(int ide) {
        boolean existe = false;
        return existe;
    }

    public static void EliminarUsuario(String ide) throws SQLException, Exception {
        if (BuscarProveedor(ide) == null) {
            throw new SQLException("No hay elemento clave de la clase Usuario");
        }
        PreparedStatement pst = null;
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement("delete from proveedores where nit = ?");
            pst.setString(1, ide.trim());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public static void listarProveedor1(String sql) throws SQLException, Exception {
        ResultSet rs = null;
        PreparedStatement pst = null;
        proveedores = new LinkedList();
        try {
            ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
            mbd.conectar();
            con = mbd.getConexion();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                proveedores.add(load(rs));
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

    public static void setProveedor(List aProveedores) {
        proveedores = aProveedores;
    }

    public static List getProveedor() {
        return proveedores;
    }
}
