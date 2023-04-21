package br.com.progepe.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.faces.context.FacesContext;
import br.com.progepe.constantes.Constantes;
import br.com.progepe.dao.DAO;
import br.com.progepe.dao.ServidorDAO;
import br.com.progepe.entity.Autenticacao;
import br.com.progepe.entity.RegimeTrabalho;
import br.com.progepe.entity.Servidor;
import br.com.progepe.entity.SolicitacaoAlimentacao;
import br.com.progepe.entity.StatusSolicitacao;
import br.com.progepe.entity.TipoSolicitacao;
import br.com.progepe.jsfUtil.EnviarEmail;

public class SolicitacaoAlimentacaoController {

    private static final long serialVersionUID = 1L;

    private SolicitacaoAlimentacao solicitacaoAlimentacao;

    private Boolean indCancelarAlimentacao = false;

    private Boolean indIncluirAlimentacao = true;

    private Boolean desabilitaBotao = true;

    public SolicitacaoAlimentacao getSolicitacaoAlimentacao() {
        return solicitacaoAlimentacao;
    }

    public void setSolicitacaoAlimentacao(SolicitacaoAlimentacao solicitacaoAlimentacao) {
        this.solicitacaoAlimentacao = solicitacaoAlimentacao;
    }

    public Boolean getIndCancelarAlimentacao() {
        return indCancelarAlimentacao;
    }

    public void setIndCancelarAlimentacao(Boolean indCancelarAlimentacao) {
        this.indCancelarAlimentacao = indCancelarAlimentacao;
    }

    public Boolean getIndIncluirAlimentacao() {
        return indIncluirAlimentacao;
    }

    public void setIndIncluirAlimentacao(Boolean indIncluirAlimentacao) {
        this.indIncluirAlimentacao = indIncluirAlimentacao;
    }

    public Boolean getDesabilitaBotao() {
        return desabilitaBotao;
    }

    public void setDesabilitaBotao(Boolean desabilitaBotao) {
        this.desabilitaBotao = desabilitaBotao;
    }

    public void abrirSolicitacaoAlimentacao() throws ParseException {
        try {
            solicitacaoAlimentacao = new SolicitacaoAlimentacao();
            desabilitaBotao = false;
            buscarServidorLogado();
            FacesContext.getCurrentInstance().getExternalContext().redirect("solicitacaoAlimentacao.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarServidorLogado() throws IOException, ParseException {
        solicitacaoAlimentacao.setSolicitante(new Servidor());
        solicitacaoAlimentacao.getSolicitante().setRegimeTrabalho(new RegimeTrabalho());
        Autenticacao siapeAutenticado = (Autenticacao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        solicitacaoAlimentacao.getSolicitante().setSiape(siapeAutenticado.getSiape());
        solicitacaoAlimentacao.setSolicitante(ServidorDAO.getInstance().refreshBySiape(solicitacaoAlimentacao.getSolicitante()));
    }

    public void isAlimentacao() {
        if (indCancelarAlimentacao) {
            indIncluirAlimentacao = true;
            indCancelarAlimentacao = false;
            solicitacaoAlimentacao.setIndAlimentacao(false);
        } else {
            indIncluirAlimentacao = false;
            indCancelarAlimentacao = true;
            solicitacaoAlimentacao.setIndAlimentacao(true);
        }
    }

    public void salvar() throws IOException, ParseException {
        solicitacaoAlimentacao.setDataAbertura(new Date());
        solicitacaoAlimentacao.setDataAtendimento(null);
        solicitacaoAlimentacao.setTipoSolicitacao(new TipoSolicitacao());
        solicitacaoAlimentacao.getTipoSolicitacao().setCodigo(Constantes.TIPO_SOLICITACAO_AUXILIO_ALIMENTACAO);
        solicitacaoAlimentacao.setStatusSolicitacao(new StatusSolicitacao());
        solicitacaoAlimentacao.getStatusSolicitacao().setCodigo(Constantes.STATUS_SOLICITACAO_ENCAMINHADO);
        DAO.getInstance().saveOrUpdate(solicitacaoAlimentacao);
        desabilitaBotao = true;
        EnviarEmail enviarEmail = new EnviarEmail();
        enviarEmail.enviarEmailSolicitacao(solicitacaoAlimentacao);
        solicitacaoAlimentacao = new SolicitacaoAlimentacao();
        buscarServidorLogado();
    }
}
