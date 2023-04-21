package br.com.progepe.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.faces.context.FacesContext;
import br.com.progepe.constantes.Constantes;
import br.com.progepe.dao.DAO;
import br.com.progepe.dao.ServidorDAO;
import br.com.progepe.entity.Autenticacao;
import br.com.progepe.entity.Servidor;
import br.com.progepe.entity.SolicitacaoAdicionalInsalubridade;
import br.com.progepe.entity.StatusSolicitacao;
import br.com.progepe.entity.TipoSolicitacao;
import br.com.progepe.jsfUtil.EnviarEmail;

public class SolicitacaoAdicionalInsalubridadeController {

    private SolicitacaoAdicionalInsalubridade solicitacaoAdicionalInsalubridade;

    private Boolean desabilitaBotao = true;

    public SolicitacaoAdicionalInsalubridade getSolicitacaoAdicionalInsalubridade() {
        return solicitacaoAdicionalInsalubridade;
    }

    public void setSolicitacaoAdicionalInsalubridade(SolicitacaoAdicionalInsalubridade solicitacaoAdicionalInsalubridade) {
        this.solicitacaoAdicionalInsalubridade = solicitacaoAdicionalInsalubridade;
    }

    public Boolean getDesabilitaBotao() {
        return desabilitaBotao;
    }

    public void setDesabilitaBotao(Boolean desabilitaBotao) {
        this.desabilitaBotao = desabilitaBotao;
    }

    public void isRiscoFisico() {
        if (solicitacaoAdicionalInsalubridade.getIndRiscosFisicos()) {
            solicitacaoAdicionalInsalubridade.setJustRiscosFisicos(new String());
        } else {
            solicitacaoAdicionalInsalubridade.setJustRiscosFisicos(null);
        }
    }

    public void abrirSolicitacaoAdicionalInsalubridade() throws ParseException {
        try {
            solicitacaoAdicionalInsalubridade = new SolicitacaoAdicionalInsalubridade();
            buscarServidorLogado();
            desabilitaBotao = false;
            FacesContext.getCurrentInstance().getExternalContext().redirect("solicitacaoAdicionalInsalubridade.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarServidorLogado() throws IOException, ParseException {
        solicitacaoAdicionalInsalubridade.setSolicitante(new Servidor());
        Autenticacao siapeAutenticado = (Autenticacao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        solicitacaoAdicionalInsalubridade.getSolicitante().setSiape(siapeAutenticado.getSiape());
        solicitacaoAdicionalInsalubridade.setSolicitante(ServidorDAO.getInstance().refreshBySiape(solicitacaoAdicionalInsalubridade.getSolicitante()));
    }

    public void salvar() throws IOException, ParseException {
        solicitacaoAdicionalInsalubridade.setDataAbertura(new Date());
        solicitacaoAdicionalInsalubridade.setDataAtendimento(null);
        solicitacaoAdicionalInsalubridade.setTipoSolicitacao(new TipoSolicitacao());
        solicitacaoAdicionalInsalubridade.getTipoSolicitacao().setCodigo(Constantes.TIPO_SOLICITACAO_AUXILIO_INSALUBRIDADE);
        solicitacaoAdicionalInsalubridade.setStatusSolicitacao(new StatusSolicitacao());
        solicitacaoAdicionalInsalubridade.getStatusSolicitacao().setCodigo(Constantes.STATUS_SOLICITACAO_ENCAMINHADO);
        DAO.getInstance().saveOrUpdate(solicitacaoAdicionalInsalubridade);
        EnviarEmail enviarEmail = new EnviarEmail();
        enviarEmail.enviarEmailSolicitacao(solicitacaoAdicionalInsalubridade);
        solicitacaoAdicionalInsalubridade = new SolicitacaoAdicionalInsalubridade();
        buscarServidorLogado();
        desabilitaBotao = true;
    }
}
