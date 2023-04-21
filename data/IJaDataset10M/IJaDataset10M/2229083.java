package br.ufpr.secretaria.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import br.ufpr.secretaria.bean.TipoSolicitacaoBean;
import br.ufpr.secretaria.util.Conexao;

public class TipoSolicitacaoDAO {

    public static TipoSolicitacaoBean obterTipoSolicitacao(Integer codigo) throws Exception {
        String sql = "SELECT * FROM tb_tipo_solicitacao WHERE tso_codigo=" + codigo.toString();
        ResultSet rs = null;
        rs = Conexao.selecionar(sql);
        if (!rs.first()) throw new Exception("Nenhum tipo encontrado!");
        TipoSolicitacaoBean tipo = new TipoSolicitacaoBean();
        tipo.setCodigo(rs.getInt("tso_codigo"));
        tipo.setDescricao(rs.getString("tso_descricao"));
        tipo.setNome(rs.getString("tso_nome"));
        return tipo;
    }

    public static Collection<TipoSolicitacaoBean> listarTiposSolicitoes() throws Exception {
        String sql = "SELECT * FROM tb_tipo_solicitacao ORDER BY tso_nome";
        ResultSet rs = null;
        rs = Conexao.selecionar(sql);
        ArrayList<TipoSolicitacaoBean> listaTipos = new ArrayList<TipoSolicitacaoBean>();
        while (rs.next()) {
            TipoSolicitacaoBean tipo = new TipoSolicitacaoBean();
            tipo.setCodigo(rs.getInt("tso_codigo"));
            tipo.setDescricao(rs.getString("tso_descricao"));
            tipo.setNome(rs.getString("tso_nome"));
            listaTipos.add(tipo);
        }
        if (listaTipos.size() == 0) throw new Exception("Nenhum tipo encontrado!");
        return listaTipos;
    }
}
