package br.unic.ra.gerador.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import br.unic.ra.gerador.bean.MarcadorBean;
import br.unic.ra.gerador.core.dao.UsuarioDAO;
import br.unic.ra.gerador.core.enums.TipoBanco;
import br.unic.ra.gerador.manager.DBManager;

public class MarcadorDAO {

    private Connection conn;

    private DBManager dbManager;

    private Properties queryFile;

    private PreparedStatement stmt;

    public MarcadorDAO() throws IOException {
        dbManager = DBManager.getInstance();
    }

    public void insert(MarcadorBean marcador) throws Exception {
        try {
            conn = dbManager.getConnection(TipoBanco.MySQL);
            stmt = conn.prepareStatement(queryFile.getProperty("marcadorInsert"));
            ArquivoDAO arquivoDAO = new ArquivoDAO();
            if (marcador.getMarcador() != (null)) {
                new ArquivoDAO().insert(marcador.getMarcador());
                marcador.setMarcador(arquivoDAO.loadByNomeExtensao(marcador.getMarcador().getNome(), marcador.getMarcador().getExtensao()));
            }
            stmt.setInt(1, marcador.getUsuario().getId());
            stmt.setInt(2, marcador.getMarcador().getId());
            stmt.setObject(3, null);
            stmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public MarcadorBean loadById(int id) throws Exception {
        try {
            conn = dbManager.getConnection(TipoBanco.MySQL);
            stmt = conn.prepareStatement(queryFile.getProperty("loadMarcadorById"));
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!(rs.next())) {
                throw new Exception("Marcador n�o encontrado");
            }
            ArquivoDAO arquivoDAO = new ArquivoDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            MarcadorBean marcador = new MarcadorBean();
            marcador.setId(id);
            marcador.setUsuario(usuarioDAO.loadById(rs.getInt(1)));
            marcador.setMarcador(arquivoDAO.loadById(rs.getInt(2)));
            marcador.setArquivoImprimir(arquivoDAO.loadById(rs.getInt(3)));
            return marcador;
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public ArrayList<MarcadorBean> loadByUsuario(int usuario) throws Exception {
        try {
            stmt = conn.prepareStatement(queryFile.getProperty("loadMarcadorByUsuario"));
            stmt.setInt(1, usuario);
            ResultSet rs = stmt.executeQuery();
            ArrayList<MarcadorBean> marcadores = new ArrayList<MarcadorBean>();
            while (rs.next()) {
                marcadores.add(loadById(rs.getInt(1)));
            }
            return marcadores;
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
