package br.com.progepe.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import br.com.progepe.constantes.Constantes;
import br.com.progepe.dao.DAO;
import br.com.progepe.dao.ServidorDAO;
import br.com.progepe.dao.ServidorTitulacaoDAO;
import br.com.progepe.entity.Autenticacao;
import br.com.progepe.entity.Servidor;
import br.com.progepe.entity.ServidorTitulacao;
import br.com.progepe.entity.SolicitacaoIncentivoQualificacao;
import br.com.progepe.entity.StatusSolicitacao;
import br.com.progepe.entity.TipoSolicitacao;
import br.com.progepe.jsfUtil.EnviarEmail;

public class SolicitacaoIncentivoQualificacaoController {

    private Boolean desabilitaBotao;

    private SolicitacaoIncentivoQualificacao solicitacaoIncentivoQualificacao;

    private List<ServidorTitulacao> listaTitulacoes = new ArrayList<ServidorTitulacao>();

    public SolicitacaoIncentivoQualificacao getSolicitacaoIncentivoQualificacao() {
        return solicitacaoIncentivoQualificacao;
    }

    public void setSolicitacaoIncentivoQualificacao(SolicitacaoIncentivoQualificacao solicitacaoIncentivoQualificacao) {
        this.solicitacaoIncentivoQualificacao = solicitacaoIncentivoQualificacao;
    }

    public List<ServidorTitulacao> getListaTitulacoes() {
        return listaTitulacoes;
    }

    public void setListaTitulacoes(List<ServidorTitulacao> listaTitulacoes) {
        this.listaTitulacoes = listaTitulacoes;
    }

    public Boolean getDesabilitaBotao() {
        return desabilitaBotao;
    }

    public void setDesabilitaBotao(Boolean desabilitaBotao) {
        this.desabilitaBotao = desabilitaBotao;
    }

    public void abrirSolicitacaoIncentivoQualificacao() throws ParseException {
        try {
            desabilitaBotao = false;
            solicitacaoIncentivoQualificacao = new SolicitacaoIncentivoQualificacao();
            solicitacaoIncentivoQualificacao.setServidorTitulacao(new ServidorTitulacao());
            buscarServidorLogado();
            listaTitulacoes.clear();
            solicitacaoIncentivoQualificacao.getServidorTitulacao().setServidor(solicitacaoIncentivoQualificacao.getSolicitante());
            listaTitulacoes = ServidorTitulacaoDAO.getInstance().listByIncentivo(solicitacaoIncentivoQualificacao.getServidorTitulacao());
            FacesContext.getCurrentInstance().getExternalContext().redirect("solicitacaoIncentivoQualificacao.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarServidorLogado() throws IOException, ParseException {
        solicitacaoIncentivoQualificacao.setSolicitante(new Servidor());
        Autenticacao siapeAutenticado = (Autenticacao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        solicitacaoIncentivoQualificacao.getSolicitante().setSiape(siapeAutenticado.getSiape());
        solicitacaoIncentivoQualificacao.setSolicitante(ServidorDAO.getInstance().refreshBySiape(solicitacaoIncentivoQualificacao.getSolicitante()));
    }

    public void salvar() throws IOException, ParseException {
        solicitacaoIncentivoQualificacao.setDataAbertura(new Date());
        solicitacaoIncentivoQualificacao.setDataAtendimento(null);
        solicitacaoIncentivoQualificacao.setTipoSolicitacao(new TipoSolicitacao());
        solicitacaoIncentivoQualificacao.getTipoSolicitacao().setCodigo(Constantes.TIPO_SOLICITACAO_INCENTIVO_QUALIFICACAO);
        solicitacaoIncentivoQualificacao.setStatusSolicitacao(new StatusSolicitacao());
        solicitacaoIncentivoQualificacao.getStatusSolicitacao().setCodigo(Constantes.STATUS_SOLICITACAO_ENCAMINHADO);
        DAO.getInstance().saveOrUpdate(solicitacaoIncentivoQualificacao);
        desabilitaBotao = true;
        EnviarEmail enviarEmail = new EnviarEmail();
        enviarEmail.enviarEmailSolicitacao(solicitacaoIncentivoQualificacao);
        solicitacaoIncentivoQualificacao = new SolicitacaoIncentivoQualificacao();
        solicitacaoIncentivoQualificacao.setServidorTitulacao(new ServidorTitulacao());
        buscarServidorLogado();
    }

    public void selectionChanged(ActionEvent event) {
        UIComponent comp = event.getComponent();
        Object obj = comp.getParent();
        org.richfaces.component.html.HtmlDataTable table = (org.richfaces.component.html.HtmlDataTable) obj;
        Object rowData = table.getRowData();
        if (rowData instanceof ServidorTitulacao) {
            ServidorTitulacao selObj = (ServidorTitulacao) rowData;
            solicitacaoIncentivoQualificacao.setServidorTitulacao(selObj);
        }
    }
}
