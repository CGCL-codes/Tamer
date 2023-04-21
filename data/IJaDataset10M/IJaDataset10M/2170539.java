package br.unic.wargen.gerador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import br.unic.wargen.gerador.bean.ArquivoBean;
import br.unic.wargen.gerador.manager.DBManager;

public class ArquivoDAO {

    public static void excluirArquivoPorId(int id, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement(DBManager.getInstance().getQueryFile().getProperty("excluirArquivoPorId"));
            stmt.setInt(1, id);
            stmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean verificarArquivoExisteBanco(ArquivoBean arquivo, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement(new DBManager().getQueryFile().getProperty("verificarArquivoExiste"));
            stmt.setString(1, arquivo.getNome());
            stmt.setString(2, arquivo.getExtensao());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            throw e;
        }
    }

    public static void excluirArquivoPorNomeExtensao(String nome, String extensao, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement(new DBManager().getQueryFile().getProperty("excluirPorNomeExtensao"));
            stmt.setString(1, nome);
            stmt.setString(2, extensao);
            stmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void inserirArquivo(ArquivoBean arquivo, Connection conn) throws Exception {
        try {
            PreparedStatement stmt = conn.prepareStatement(new DBManager().getQueryFile().getProperty("inserirArquivo"));
            stmt.setInt(1, arquivo.getUsuario().getId());
            stmt.setString(2, arquivo.getNome());
            stmt.setString(3, arquivo.getExtensao());
            stmt.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    public static ArquivoBean carregarArquivoPorId(int id, Connection conn) throws Exception {
        ArquivoBean arquivo = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(new DBManager().getQueryFile().getProperty("carregarArquivoPorId"));
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                arquivo = new ArquivoBean();
                arquivo.setId(id);
                arquivo.setUsuario(UsuarioDAO.carregarUsuarioPorId(rs.getInt("usuario"), conn));
                arquivo.setNome(rs.getString("nome"));
                arquivo.setExtensao(rs.getString("extensao"));
                arquivo.setCaminho("/arquivos/" + arquivo.getFullName());
            }
            return arquivo;
        } catch (Exception e) {
            throw e;
        }
    }

    public static ArquivoBean carregarArquivoPorNomeExtensao(String nome, String extensao, Connection conn) throws Exception {
        ArquivoBean arquivo = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(new DBManager().getQueryFile().getProperty("carregarArquivoPorNomeExtensao"));
            stmt.setString(1, nome);
            stmt.setString(2, extensao);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                arquivo = new ArquivoBean();
                arquivo.setId(rs.getInt("id"));
                arquivo.setNome(nome);
                arquivo.setExtensao(extensao);
            }
            return arquivo;
        } catch (Exception e) {
            throw e;
        }
    }

    public static ArrayList<ArquivoBean> listarArquivosPorUsuario(int usuarioId, Connection conn) throws Exception {
        ArrayList<ArquivoBean> listaArquivos = null;
        ArquivoBean arquivo = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(new DBManager().getQueryFile().getProperty("listarArquivosPorUsuario"));
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            listaArquivos = new ArrayList<ArquivoBean>();
            while (rs.next()) {
                arquivo = new ArquivoBean();
                arquivo.setId(rs.getInt("id"));
                arquivo.setUsuario(UsuarioDAO.carregarUsuarioPorId(rs.getInt("usuario"), conn));
                arquivo.setNome(rs.getString("nome"));
                arquivo.setExtensao(rs.getString("extensao"));
                arquivo.setCaminho(arquivo.getUsuario().getLogin() + "/arquivos/" + arquivo.getFullName());
                listaArquivos.add(arquivo);
            }
            return listaArquivos;
        } catch (Exception e) {
            throw e;
        }
    }
}
