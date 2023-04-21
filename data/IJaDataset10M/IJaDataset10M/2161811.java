package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import bd.ConnectionFactory;
import bean.Contato;

public class ContatoDAO {

    public void adicionarContato(Contato contato) throws SQLException {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement("insert into contato (nome,telefone,endereco,email) values (?,?,?,?);");
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEndereco());
            stmt.setString(4, contato.getEmail());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (conexao != null) conexao.close();
        }
    }

    public void deleteContato(Contato contato) throws SQLException {
        Connection conexao = null;
        PreparedStatement stmt = null;
        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement("delete from contato where nome=? and telefone=? and endereco=? and email=?;");
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEndereco());
            stmt.setString(4, contato.getEmail());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (conexao != null) conexao.close();
        }
    }

    public List<Contato> listarContatos() throws SQLException {
        List<Contato> listaDeContatos = new ArrayList<Contato>();
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement("select * from contato;");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Contato contato = new Contato();
                contato.setNome(rs.getString("nome"));
                contato.setTelefone(rs.getString("telefone"));
                contato.setEndereco(rs.getString("endereco"));
                contato.setEmail(rs.getString("email"));
                listaDeContatos.add(contato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (conexao != null) conexao.close();
        }
        return listaDeContatos;
    }
}
