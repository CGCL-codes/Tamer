package dao;

import com.framework.dao.DataAccessException;
import com.framework.dao.DataAccessObject;
import com.framework.dao.ObjectNotFoundException;
import com.framework.vo.PersistentObject;
import vo.Muelle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MuelleDAO extends DataAccessObject {

    private String url = System.getProperty("url");

    private String user = System.getProperty("user");

    private String pass = System.getProperty("pass");

    @Override
    public void delete(PersistentObject p) {
        Connection con = null;
        try {
            con = getConnection(url, user, pass);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        String sent = "DELETE FORM muelles WHERE muelles_id = ?";
        PreparedStatement pst = null;
        try {
            Muelle mue = (Muelle) p;
            pst = con.prepareStatement(sent);
            pst.setInt(1, mue.getNumero());
            pst.executeQuery();
        } catch (SQLException sqlex) {
            try {
                throw new DataAccessException(sqlex.getMessage());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException sq) {
            }
        }
    }

    public MuelleDAO() {
        super();
    }

    public void insert(PersistentObject p) throws SQLException {
        Connection con = null;
        try {
            con = getConnection(url, user, pass);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        String sent = "INSERT INTO muelles(muelles_id, muelles_descripcion, muelles_operador, muelles_totalKilos, muelles_totalDescargas, muelles_totalTiempoDescargas) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = null;
        try {
            Muelle mue = (Muelle) p;
            pst = con.prepareStatement(sent);
            pst.setInt(1, mue.getNumero());
            pst.setString(2, mue.getDescripcion());
            pst.setString(3, mue.getOperador());
            pst.setDouble(4, mue.getTotalKilos());
            pst.setInt(5, mue.getTotalDescargas());
            pst.setTime(6, mue.getTiempoAcumulado());
            pst.setObject(7, mue.getCola());
            pst.executeQuery();
        } catch (SQLException sqlex) {
            throw new SQLException(sqlex.getMessage());
        } finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException sq) {
            }
        }
    }

    @Override
    public List select(PersistentObject p) {
        Connection con = null;
        try {
            con = getConnection(url, user, pass);
        } catch (DataAccessException e1) {
            e1.printStackTrace();
        }
        String sql = "SELECT * FROM Muelles WHERE muelles_id = ?";
        PreparedStatement pst = null;
        ResultSet rs;
        try {
            List Muelles = new ArrayList();
            Muelle mue = (Muelle) p;
            pst = con.prepareStatement(sql);
            pst.setInt(1, mue.getNumero());
            rs = pst.executeQuery();
            if (rs.next()) {
                mue.setNumero(rs.getInt(rs.getString("muelles_id")));
                mue.setDescripcion(rs.getString("muelles_descripcion"));
                mue.setOperador(rs.getString("muelles_operador"));
                mue.setTotalKilos(rs.getDouble("muelles_totalKilos"));
                mue.setTotalDescargas(rs.getInt("muelles_totalDescargas"));
                mue.setTiempoAcumulado(rs.getTime("muelles_tiempoAcumulado"));
            } else try {
                throw new ObjectNotFoundException("No se encuentra el Muelle " + mue.getNumero());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
            Muelles.add(mue);
            return Muelles;
        } catch (SQLException sqlex) {
            try {
                throw new DataAccessException(sqlex.getMessage());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException sq) {
            }
        }
        return null;
    }

    @Override
    public List selectAll() {
        Connection con = null;
        try {
            con = getConnection(url, user, pass);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        String sql = "SELECT * FROM Muelles";
        PreparedStatement pst = null;
        ResultSet rs;
        try {
            List Muelles = new ArrayList();
            Muelle mue = null;
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                mue.setNumero(rs.getInt("muelles_id"));
                mue.setDescripcion(rs.getString("muelles_descripcion"));
                mue.setOperador(rs.getString("muelles_operador"));
                mue.setTotalKilos(rs.getDouble("muelles_totalKilos"));
                mue.setTotalDescargas(rs.getInt("muelles_totalDescargas"));
                mue.setTiempoAcumulado(rs.getTime("muelles_tiempoAcumulado"));
            } else try {
                throw new ObjectNotFoundException("No se encuentra el Muelle " + mue.getNumero());
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
            Muelles.add(mue);
            return Muelles;
        } catch (SQLException sqlex) {
            try {
                throw new DataAccessException(sqlex.getMessage());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException sq) {
            }
        }
        return null;
    }

    @Override
    public void update(PersistentObject p) {
        Connection con = null;
        try {
            con = getConnection(url, user, pass);
        } catch (DataAccessException e1) {
            e1.printStackTrace();
        }
        String sql = "UPDATE muelles SET muelles_id = ?, muelles_descripcion = ?, muelles_operador = ?, muelles_totalKilos = ?, muelles_totalDescargas = ?, muelles_tiempoAcumulado = ?";
        PreparedStatement pst = null;
        ResultSet rs;
        try {
            Muelle mue = null;
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                mue.setNumero(rs.getInt("muelles_id"));
                mue.setDescripcion(rs.getString("muelles_descripcion"));
                mue.setOperador(rs.getString("muelles_operador"));
                mue.setTotalKilos(rs.getDouble("muelles_totalKilos"));
                mue.setTotalDescargas(rs.getInt("muelles_totalDescargas"));
                mue.setTiempoAcumulado(rs.getTime("muelles_tiempoAcumulado"));
                pst.executeQuery();
            }
        } catch (SQLException sqlex) {
            try {
                throw new DataAccessException(sqlex.getMessage());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException sq) {
            }
        }
    }

    @Override
    public void save(PersistentObject arg0) throws DataAccessException {
    }
}
