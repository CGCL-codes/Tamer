package edu.estacio.siscope.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import edu.estacio.siscope.bean.Cargo;

public class CargoDAO {

    public static boolean insert(final Cargo cargo) {
        int result = 0;
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "insert into cargo (nome) values (?)";
            pst = c.prepareStatement(sql);
            pst.setString(1, cargo.getNome());
            result = pst.executeUpdate();
            c.commit();
        } catch (final SQLException e) {
            try {
                c.rollback();
            } catch (final SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("[CargoDAO.insert] Erro ao inserir -> " + e.getMessage());
        } finally {
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean update(Cargo cargo) {
        int result = 0;
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            String sql = "update cargo set nome = (?) where id_cargo= ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, cargo.getNome());
            pst.setInt(2, cargo.getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("[CargoDAO.update] Erro ao atualizar -> " + e.getMessage());
        } finally {
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Cargo selectByID(String id_cargo) {
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Cargo objCargo = null;
        if (c == null) {
            return null;
        }
        try {
            String sql = "Select id_cargo, nome from cargo where id_cargo = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, id_cargo);
            rs = pst.executeQuery();
            if (rs.next()) {
                objCargo = new Cargo();
                objCargo.setCodigo(rs.getInt("id_cargo"));
                objCargo.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("[CargoDAO.selectByID] Erro ao atualizar -> " + e.getMessage());
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        return objCargo;
    }

    public static Collection selectAll() {
        Connection c = DBConnection.getConnection();
        if (c == null) {
            return null;
        }
        String sql = "select id_cargo, nome from cargo order by nome";
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Cargo> al = new ArrayList<Cargo>();
        Cargo objCargo = null;
        try {
            st = c.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objCargo = new Cargo();
                objCargo.setCodigo(rs.getInt("id_cargo"));
                objCargo.setNome(rs.getString("nome"));
                al.add(objCargo);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closeStatement(st);
            DBConnection.closeConnection(c);
        }
        return al;
    }

    public static boolean delete(String codigo) {
        int result = 0;
        Connection c = DBConnection.getConnection();
        Statement st = null;
        if (c == null) {
            return false;
        }
        try {
            st = c.createStatement();
            String sql = "delete from cargo where id_cargo = " + codigo;
            result = st.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("[CargoDAO.delete] Erro ao deletar -> " + e.getMessage());
        } finally {
            DBConnection.closeStatement(st);
            DBConnection.closeConnection(c);
        }
        if (result > 0) return true; else return false;
    }
}
