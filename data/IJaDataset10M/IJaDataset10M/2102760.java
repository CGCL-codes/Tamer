package edu.estacio.siscope.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import edu.estacio.siscope.bean.Funcionario;

public class FuncionarioDAO {

    public static boolean insert(final Funcionario objFuncionario) {
        int result = 0;
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "insert into funcionario " + "(nome, cpf, telefone, email, senha, login, id_cargo)" + " values (?, ?, ?, ?, ?, ?, ?)";
            pst = c.prepareStatement(sql);
            pst.setString(1, objFuncionario.getNome());
            pst.setString(2, objFuncionario.getCpf());
            pst.setString(3, objFuncionario.getTelefone());
            pst.setString(4, objFuncionario.getEmail());
            pst.setString(5, objFuncionario.getSenha());
            pst.setString(6, objFuncionario.getLogin());
            pst.setLong(7, (objFuncionario.getCargo()).getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (final SQLException e) {
            try {
                c.rollback();
            } catch (final SQLException e1) {
                System.out.println("[FuncionarioDAO.insert] Erro ao inserir -> " + e1.getMessage());
            }
            System.out.println("[FuncionarioDAO.insert] Erro ao inserir -> " + e.getMessage());
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

    public static boolean update(Funcionario objFuncionario) {
        int result = 0;
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "update funcionario " + " set nome = ? , cpf = ? , telefone = ? , email = ?, senha = ?, login = ?, id_cargo = ?" + " where id_funcionario = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, objFuncionario.getNome());
            pst.setString(2, objFuncionario.getCpf());
            pst.setString(3, objFuncionario.getTelefone());
            pst.setString(4, objFuncionario.getEmail());
            pst.setString(5, objFuncionario.getSenha());
            pst.setString(6, objFuncionario.getLogin());
            pst.setLong(7, (objFuncionario.getCargo()).getCodigo());
            pst.setLong(8, objFuncionario.getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                System.out.println("[FuncionarioDAO.update] Erro ao atualizar -> " + e1.getMessage());
            }
            System.out.println("[FuncionarioDAO.update] Erro ao atualizar -> " + e.getMessage());
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

    public static Funcionario selectByID(String id_funcionario) {
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Funcionario objFuncionario = null;
        if (c == null) {
            return null;
        }
        try {
            String sql = "Select * from funcionario where id_funcionario = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, id_funcionario);
            rs = pst.executeQuery();
            if (rs.next()) {
                objFuncionario = new Funcionario();
                objFuncionario.setCodigo(rs.getInt("id_funcionario"));
                objFuncionario.setNome(rs.getString("nome"));
                objFuncionario.setCpf(rs.getString("cpf"));
                objFuncionario.setEmail(rs.getString("email"));
                objFuncionario.setLogin(rs.getString("login"));
                objFuncionario.setSenha(rs.getString("senha"));
                objFuncionario.setTelefone(rs.getString("telefone"));
                objFuncionario.setCargo(CargoDAO.selectByID(rs.getString("id_cargo")));
                objFuncionario.setDepartamento(DepartamentoDAO.selectByFuncionario(rs.getInt("id_funcionario")));
            }
        } catch (SQLException e) {
            System.out.println("[FuncionarioDAO.selectByID] Erro ao atualizar -> " + e.getMessage());
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        return objFuncionario;
    }

    public static Funcionario selectByLogin(String login) {
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Funcionario objFuncionario = null;
        if (c == null) {
            return null;
        }
        try {
            String sql = "Select * from funcionario where login = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, login);
            rs = pst.executeQuery();
            if (rs.next()) {
                objFuncionario = new Funcionario();
                objFuncionario.setCodigo(rs.getInt("id_funcionario"));
                objFuncionario.setNome(rs.getString("nome"));
                objFuncionario.setCpf(rs.getString("cpf"));
                objFuncionario.setEmail(rs.getString("email"));
                objFuncionario.setLogin(rs.getString("login"));
                objFuncionario.setSenha(rs.getString("senha"));
                objFuncionario.setTelefone(rs.getString("telefone"));
                objFuncionario.setCargo(CargoDAO.selectByID(rs.getString("id_cargo")));
            }
        } catch (SQLException e) {
            System.out.println("[FuncionarioDAO.selectByLogin] Erro ao atualizar -> " + e.getMessage());
        } finally {
            DBConnection.closeResultSet(rs);
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        return objFuncionario;
    }

    public static Collection selectAll() {
        Connection c = DBConnection.getConnection();
        if (c == null) {
            return null;
        }
        String sql = "SELECT * FROM funcionario order by nome, login";
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Funcionario> al = new ArrayList<Funcionario>();
        Funcionario objFuncionario = null;
        try {
            st = c.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                objFuncionario = new Funcionario();
                objFuncionario.setCodigo(rs.getInt("id_funcionario"));
                objFuncionario.setNome(rs.getString("nome"));
                objFuncionario.setCpf(rs.getString("cpf"));
                objFuncionario.setEmail(rs.getString("email"));
                objFuncionario.setLogin(rs.getString("login"));
                objFuncionario.setSenha(rs.getString("senha"));
                objFuncionario.setTelefone(rs.getString("telefone"));
                objFuncionario.setCargo(CargoDAO.selectByID(rs.getString("id_cargo")));
                al.add(objFuncionario);
            }
        } catch (SQLException e) {
            System.out.println("[FuncionarioDAO.selectAll] Erro ao selecionar -> " + e.getMessage());
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
            String sql = "delete from funcionario where id_funcionario = " + codigo;
            result = st.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("[FuncionarioDAO.delete] Erro ao deletar -> " + e.getMessage());
        } finally {
            DBConnection.closeStatement(st);
            DBConnection.closeConnection(c);
        }
        if (result > 0) return true; else return false;
    }
}
