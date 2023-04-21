package br.com.fabrica_ti.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import br.com.fabrica_ti.model.Requerente;
import br.com.fabrica_ti.model.Usuario;

public class RequerenteDAO {

    private ConnectionFactory connectionFactory = ConnectionFactory.getInstance();

    public Requerente getRequerente(Requerente requerente) throws SQLException {
        Connection conn = null;
        Requerente requerenteEncontrado;
        try {
            conn = connectionFactory.getConnection(true);
            Statement stmt = conn.createStatement();
            String sqlSelect = "SELECT * FROM Requerente where idrequerente = " + requerente.getIdRequerente();
            requerente = null;
            ResultSet rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                requerenteEncontrado = new Requerente();
                requerenteEncontrado.setIdRequerente(rs.getInt("idrequerente"));
                requerenteEncontrado.setIdUsuario(rs.getInt("usuario_idusuario"));
                requerenteEncontrado.setCnpj(rs.getString("cnpj"));
                return requerenteEncontrado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return null;
    }

    public Requerente getRequerenteUsuario(Requerente requerente) throws SQLException {
        Connection conn = null;
        Requerente requerenteEncontrado;
        try {
            conn = connectionFactory.getConnection(true);
            Statement stmt = conn.createStatement();
            String sqlSelect = "SELECT * FROM Requerente where idrequerente = " + requerente.getIdRequerente();
            requerente = null;
            ResultSet rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                requerenteEncontrado = new Requerente();
                requerenteEncontrado.setIdRequerente(rs.getInt("idrequerente"));
                requerenteEncontrado.setIdUsuario(rs.getInt("usuario_idusuario"));
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(requerenteEncontrado.getIdUsuario());
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                usuario = usuarioDAO.getUsuario(usuario);
                requerenteEncontrado.setNome(usuario.getNome());
                usuarioDAO = null;
                requerenteEncontrado.setCnpj(rs.getString("cnpj"));
                return requerenteEncontrado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return null;
    }

    public Requerente insertRequerente(Requerente requerente, Connection conn) throws SQLException {
        String insert = "insert into Requerente (idrequerente, usuario_idusuario, cnpj) " + "values " + "(nextval('seq_requerente'), " + requerente.getIdUsuario() + ", '" + requerente.getCnpj() + "')";
        try {
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            if (result == 1) {
                String sqlSelect = "SELECT * FROM Requerente where usuario_idusuario = " + requerente.getIdUsuario();
                ResultSet rs = stmt.executeQuery(sqlSelect);
                while (rs.next()) {
                    requerente.setIdRequerente(rs.getInt("idRequerente"));
                    requerente.setIdUsuario(rs.getInt("usuario_idusuario"));
                    requerente.setCnpj(rs.getString("cnpj"));
                    return requerente;
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void updateRequerente(Requerente requerenteNovo) throws SQLException {
        Connection conn = null;
        String update = "update Requerente set cnpj='" + requerenteNovo.getCnpj() + "' " + "where " + "idrequerente=" + requerenteNovo.getIdRequerente();
        try {
            conn = connectionFactory.getConnection(true);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    public List<Requerente> getRequerentes() throws SQLException {
        List<Requerente> requerentes = new ArrayList<Requerente>();
        Connection conn = null;
        try {
            conn = connectionFactory.getConnection(true);
            Statement stmt = conn.createStatement();
            String sqlSelect = "SELECT * FROM Requerente";
            ResultSet rs = stmt.executeQuery(sqlSelect);
            Requerente requerentesList = null;
            while (rs.next()) {
                requerentesList = new Requerente();
                requerentesList.setIdRequerente(rs.getInt("idrequerente"));
                requerentesList.setIdUsuario(rs.getInt("usuario_idusuario"));
                requerentesList.setCnpj(rs.getString("cnpj"));
                requerentes.add(requerentesList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        return requerentes;
    }

    public void deleteRequerente(Requerente usuario) throws SQLException {
        Connection conn = null;
        String update = "delete from Requerente " + "where " + "idrequerente=" + usuario.getIdRequerente();
        try {
            conn = connectionFactory.getConnection(true);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }
}
