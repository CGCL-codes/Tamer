package br.com.zonavibe.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.zonavibe.connect.Conexao;
import br.com.zonavibe.dao.EstadoDAO;
import br.com.zonavibe.model.Estado;

public class EstadoDAOImpl implements EstadoDAO {

    private Conexao conexao = new Conexao();

    private PreparedStatement ps = null;

    public Estado buscarEstado(int idEstado) throws SQLException {
        Estado estado = null;
        try {
            String sql = "select * from estado where id_estado = ?;";
            ps = conexao.getConnection().prepareStatement(sql);
            ps.setInt(1, idEstado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                estado = new Estado();
                estado.setIdEstado(rs.getInt("id_estado"));
                estado.setNome(rs.getString("nm_estado"));
                estado.setSigla(rs.getString("sigla"));
            } else {
                rs.close();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        } finally {
            conexao.closeConnection();
        }
        return estado;
    }

    public List<Estado> listarEstados() throws SQLException {
        List<Estado> lista = new ArrayList<Estado>();
        try {
            String sql = "select * from estado order by nm_estado;";
            ps = conexao.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Estado estado = null;
            while (rs.next()) {
                estado = new Estado();
                estado.setIdEstado(rs.getInt("id_estado"));
                estado.setNome(rs.getString("nm_estado"));
                estado.setSigla(rs.getString("sigla"));
                lista.add(estado);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        } finally {
            conexao.closeConnection();
        }
        return lista;
    }
}
