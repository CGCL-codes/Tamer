package br.ufpr.biblioteca.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import br.ufpr.biblioteca.bean.StatusBean;
import br.ufpr.biblioteca.util.Conexao;

public class StatusDAO {

    public static void incluir(StatusBean bean) throws Exception {
        String sql = "INSERT INTO STATUS " + "(nome)" + " VALUES ('" + bean.getNome() + "')";
        Conexao.atualizar(sql);
    }

    public static void excluir(StatusBean bean) throws Exception {
        String sql = "DELETE FROM STATUS WHERE ID=" + bean.getId();
        Conexao.atualizar(sql);
    }

    public static void alterar(StatusBean bean) throws Exception {
        String sql = "UPDATE STATUS SET " + "nome='" + bean.getNome() + "' WHERE ID=" + bean.getId();
        Conexao.atualizar(sql);
    }

    public static StatusBean pesquisar(int idStatus) throws Exception {
        String sql = "SELECT * FROM STATUS WHERE ID=" + idStatus;
        ResultSet rs = null;
        rs = Conexao.selecionar(sql);
        if (rs.next() == false) throw new Exception("Status n�o encontrado!");
        StatusBean fb = new StatusBean();
        fb.setId(rs.getInt("Id"));
        fb.setNome(rs.getString("Nome"));
        return fb;
    }

    public static ArrayList<StatusBean> pesquisar(String nomeStatus) throws Exception {
        String sql = "SELECT * FROM STATUS WHERE NOME LIKE '%" + nomeStatus + "%'";
        ResultSet rs = null;
        rs = Conexao.selecionar(sql);
        ArrayList<StatusBean> status = new ArrayList<StatusBean>();
        while (rs.next()) {
            StatusBean fb = new StatusBean();
            fb.setId(rs.getInt("Id"));
            fb.setNome(rs.getString("Nome"));
            status.add(fb);
        }
        if (status.size() == 0) throw new Exception("Status n�o encontrado!");
        return status;
    }

    public static ArrayList<StatusBean> listar() throws Exception {
        String sql = "SELECT * FROM STATUS ORDER BY NOME";
        ResultSet rs = null;
        rs = Conexao.selecionar(sql);
        if (rs == null) throw new Exception("N�o h� registro de status!");
        ArrayList<StatusBean> status = new ArrayList<StatusBean>();
        while (rs.next()) {
            StatusBean fb = new StatusBean();
            fb.setId(rs.getInt("Id"));
            fb.setNome(rs.getString("Nome"));
            status.add(fb);
        }
        return status;
    }
}
