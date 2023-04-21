package pe.edu.cibertec.ventas.dao.mysql;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import pe.edu.cibertec.ventas.bean.Producto;
import pe.edu.cibertec.ventas.dao.factory.DAOFactory;
import pe.edu.cibertec.ventas.dao.interfaces.ProductoDAO;
import pe.edu.cibertec.ventas.dao.util.UtilDAO;

/**
 *
 * @author Instructor
 */
public class MySQLProductoDAO implements ProductoDAO {

    public ArrayList queryAll() {
        ArrayList lista = new ArrayList();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        try {
            conn = UtilDAO.getConnection(DAOFactory.MYSQL);
            String sql = "{call usp_queryAllProducts()}";
            cstmt = conn.prepareCall(sql);
            rs = cstmt.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setCodigo(rs.getInt("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStock(rs.getInt("stock"));
                Blob fotito = rs.getBlob("foto");
                if (fotito != null) p.setFoto(fotito.getBinaryStream());
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cstmt != null) try {
                cstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    public Producto queryById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void add(Producto p) {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = UtilDAO.getConnection(DAOFactory.MYSQL);
            String sql = "{call usp_addProducto(?,?,?,?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setString(1, p.getDescripcion());
            cstmt.setDouble(2, p.getPrecio());
            cstmt.setInt(3, p.getStock());
            cstmt.setBinaryStream(4, (InputStream) p.getFoto());
            cstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cstmt != null) try {
                cstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Producto p) {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = UtilDAO.getConnection(DAOFactory.MYSQL);
            String sql = "{call usp_updProducto(?,?,?,?,?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, p.getCodigo());
            cstmt.setString(2, p.getDescripcion());
            cstmt.setDouble(3, p.getPrecio());
            cstmt.setInt(4, p.getStock());
            cstmt.setBinaryStream(5, (InputStream) p.getFoto());
            cstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cstmt != null) try {
                cstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = UtilDAO.getConnection(DAOFactory.MYSQL);
            String sql = "{call usp_delProducto(?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, id);
            cstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cstmt != null) try {
                cstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
