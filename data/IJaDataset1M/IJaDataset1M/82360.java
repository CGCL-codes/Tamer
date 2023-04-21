package modelo.persistencia.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.entidades.Compracomida;
import modelo.entidades.CompracomidaImpl;
import modelo.persistencia.CompracomidaDAO;

/**
 *
 * @author Inmaculada Casanova Ruiz
 */
public class CompracomidaDAOJDBC implements CompracomidaDAO {

    public List<Compracomida> listByCompracomida(String Codigosilo) {
        List<Compracomida> Compracomida = new ArrayList<Compracomida>();
        String Codigoalbaran = null;
        try {
            Statement stmt = Persistencia.createConnection().createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM compracomida where codigoalbaran=" + Codigoalbaran);
            String codigosilo, codigoproveedor, cantidad, precio;
            while (res.next()) {
                Codigoalbaran = res.getString("codigoalbaran");
                codigosilo = res.getString("codigosilo");
                codigoproveedor = res.getString("codigoproveedor");
                cantidad = res.getString("cantidad");
                precio = res.getString("precio");
                Compracomida.add(new CompracomidaImpl(Codigoalbaran, codigosilo, codigoproveedor, cantidad, precio));
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Persistencia.closeConnection();
        }
        return Compracomida;
    }

    public void create(Compracomida entidad) {
        String sql = "insert into compracomida(codigoalbaran,codigosilo,codigoproveedor,cantidad,precio) values (?,?,?,?,?)";
        try {
            PreparedStatement stm = Persistencia.createConnection().prepareStatement(sql);
            stm.setString(1, entidad.getCodigoalbaran());
            stm.setString(2, entidad.getCodigosilo());
            stm.setString(3, entidad.getCodigoproveedor());
            stm.setString(4, entidad.getCantidad());
            stm.setString(5, entidad.getPrecio());
            stm.execute();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Persistencia.closeConnection();
        }
    }

    public Compracomida read(String pk) {
        Compracomida f = null;
        try {
            Statement stmt = Persistencia.createConnection().createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM compracomida where codigoalbaran=" + pk);
            String codigoalbaran, codigosilo, codigoproveedor;
            String cantidad, precio;
            if (res.next()) {
                codigoalbaran = res.getString("codigoalbaran");
                codigosilo = res.getString("codigosilo");
                codigoproveedor = res.getString("codigoproveedor");
                cantidad = res.getString("cantidad");
                precio = res.getString("precio");
                f = new CompracomidaImpl(codigoalbaran, codigosilo, codigoproveedor, cantidad, precio);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Persistencia.closeConnection();
        }
        return f;
    }

    public void update(Compracomida entidad) {
        String sql = "update compracomidas set codigoalbaran=?, codigosilo=?,codigoproveedor=?,cantidad=?,precio=? where identificador like ?";
        try {
            PreparedStatement stm = Persistencia.createConnection().prepareStatement(sql);
            stm.setString(1, entidad.getCodigoalbaran());
            stm.setString(2, entidad.getCodigosilo());
            stm.setString(3, entidad.getCodigoproveedor());
            stm.setString(4, entidad.getCantidad());
            stm.setString(5, entidad.getPrecio());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Persistencia.closeConnection();
        }
    }

    public void delete(Compracomida entidad) {
        try {
            Statement stmt = Persistencia.createConnection().createStatement();
            stmt.executeUpdate("DELETE FROM compracomida where codigoalbaran=" + entidad.getCodigoalbaran());
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Persistencia.closeConnection();
        }
    }

    public List<Compracomida> list() {
        List<Compracomida> Compracomida = new ArrayList<Compracomida>();
        try {
            Statement stmt = Persistencia.createConnection().createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Compracomida");
            String codigosilo, codigoalbaran, codigoproveedor, cantidad, precio;
            while (res.next()) {
                codigoalbaran = res.getString("codigoalbaran");
                codigosilo = res.getString("codigosilo");
                codigoproveedor = res.getString("codigoproveedor");
                cantidad = res.getString("cantidad");
                precio = res.getString("precio");
                Compracomida.add(new CompracomidaImpl(codigoalbaran, codigosilo, codigoproveedor, cantidad, precio));
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            Persistencia.closeConnection();
        }
        return Compracomida;
    }
}
