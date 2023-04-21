package br.faimg.pomar.modelo.dao;

import br.faimg.pomar.modelo.interfaces.CRUDInterface;
import br.faimg.pomar.modelo.pojo.TipodeEstacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipodeEstacaoDAO implements CRUDInterface<TipodeEstacao> {

    public void create(TipodeEstacao pojo) {
        Connection con = null;
        PreparedStatement stm = null;
        String sql = "insert into TIPO_ESTACAO values(?,?);";
        try {
            con = ConnectionManager.getInstance().getConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, pojo.getCodigo());
            stm.setString(2, pojo.getDescricao());
            stm.execute();
        } catch (SQLException ex) {
            Logger.getLogger(TipodeEstacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TipodeEstacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TipodeEstacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void update(TipodeEstacao pojo) {
        throw new UnsupportedOperationException("Este cadastro não pode ser alterado!");
    }

    public List<TipodeEstacao> readByExample(TipodeEstacao pojo) {
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select * from TIPO_ESTACAO where tpe_descricao ilike ?";
        List<TipodeEstacao> tipodeestacao = new ArrayList<TipodeEstacao>();
        if (pojo == null) {
            pojo = new TipodeEstacao();
        }
        if (pojo.getDescricao() == null) {
            pojo.setDescricao("");
        }
        con = ConnectionManager.getInstance().getConnection();
        try {
            stm = con.prepareStatement(sql);
            stm.setString(1, "%" + pojo.getDescricao() + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                TipodeEstacao temp = new TipodeEstacao();
                temp.setCodigo(rs.getString("tpe_codigo"));
                temp.setDescricao(rs.getString("tpe_descricao"));
                tipodeestacao.add(temp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipodeEstacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tipodeestacao;
    }

    public TipodeEstacao readById(Character codigo) {
        Connection conn = null;
        PreparedStatement stm = null;
        TipodeEstacao tipoEstacao = null;
        ResultSet rs = null;
        String sql = "Select * from TIPO_ESTACAO where TPE_CODIGO = ?";
        try {
            conn = ConnectionManager.getInstance().getConnection();
            stm = conn.prepareStatement(sql);
            stm.setString(1, codigo.toString());
            rs = stm.executeQuery();
            if (rs.next()) {
                tipoEstacao = new TipodeEstacao();
                tipoEstacao.setCodigo("TPE_CODIGO");
                tipoEstacao.setDescricao("TPE_DESCRICAO");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                stm.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return tipoEstacao;
    }

    public void delete(Character codigo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TipodeEstacao readById(String codigo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(String codigo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TipodeEstacao readById(Integer codigo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(Integer codigo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
