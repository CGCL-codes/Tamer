package com.odontosis.view.paciente.recibo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.odontosis.entidade.FormaPagamento;
import com.odontosis.entidade.Mes;
import com.odontosis.entidade.Paciente;
import com.odontosis.entidade.Pagamento;
import com.odontosis.entidade.TipoRecibo;
import com.odontosis.ioc.ContainerIoc;
import com.odontosis.service.PagamentoService;
import com.odontosis.util.StringUtilsOdontosis;
import com.odontosis.view.OdontosisForm;

public class FormRecibo extends OdontosisForm {

    private String pagamentoId;

    private String valorPago;

    private Pagamento pagamento;

    private String tipoRecibo;

    private String formaPagamento;

    private String numeroCheque;

    private String nomeEmitente;

    private String banco;

    private String nomePaciente;

    private String dataVencimento;

    private String descricaoServico;

    private String totalParcelas;

    private String valorPagamento;

    private String forcarPagamento;

    public String getForcarPagamento() {
        return forcarPagamento;
    }

    public void setForcarPagamento(String forcarPagamento) {
        this.forcarPagamento = forcarPagamento;
    }

    public String getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(String pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public String getValorPago() {
        return valorPago;
    }

    public void setValorPago(String valorPago) {
        this.valorPago = valorPago;
    }

    public String getValorPagamentoFormatado() {
        double valor = Double.parseDouble(getValorPagamento());
        return StringUtilsOdontosis.formatarDouble(valor);
    }

    public String getTotalParcelas() throws Exception {
        PagamentoService pagamentoService = new PagamentoService();
        return pagamentoService.numeroParcelas(pagamento).toString();
    }

    public String getTipoRecibo() {
        return tipoRecibo;
    }

    public void setTipoRecibo(String tipoRecibo) {
        this.tipoRecibo = tipoRecibo;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formRecibo) {
        this.formaPagamento = formRecibo;
    }

    public String getNomeEmitente() {
        return nomeEmitente;
    }

    public String getBanco() {
        return banco;
    }

    public void setNomeEmitente(String nomeEmitente) {
        this.nomeEmitente = nomeEmitente;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Collection<TipoRecibo> getTiposRecibo() {
        return Arrays.asList(TipoRecibo.values());
    }

    public Collection<FormaPagamento> getFormasPagamento() {
        return Arrays.asList(FormaPagamento.values());
    }

    public String getNumeroParcela() {
        PagamentoService pagamentoService = null;
        try {
            pagamentoService = new PagamentoService();
        } catch (Exception e) {
        }
        pagamento = pagamentoService.load(Long.parseLong(getPagamentoId()));
        Integer mes = pagamento.getServico().getMesVencimentoPrimeiraParcela() - 1;
        Integer mesParcela = pagamento.getMesVencimento();
        Integer diferenca = mesParcela - mes;
        if (diferenca < 0) {
            diferenca += 12;
        }
        return diferenca.toString();
    }

    public String getTotalDivida() {
        double valor = ContainerIoc.getServicoService().obterSaldoDevedor(ContainerIoc.getPagamentoService().load(Long.valueOf(getPagamentoId())));
        return new Double(valor).toString();
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        if (StringUtilsOdontosis.isVazia(getValorPago())) {
            messages.add("nomePaciente", new ActionMessage("O campo Valor é obrigatório"));
        } else {
            try {
                double valorPago = Double.parseDouble(getValorPago());
                double obterSaldoDevedor = ContainerIoc.getServicoService().obterSaldoDevedor(ContainerIoc.getPagamentoService().load(Long.valueOf(getPagamentoId())));
                if (valorPago > obterSaldoDevedor) {
                    messages.add("nomePaciente", new ActionMessage("O Valor informado deve ser menor ou igual a " + StringUtilsOdontosis.formatarDouble(obterSaldoDevedor)));
                }
                if (valorPago <= 0) {
                    messages.add("nomePaciente", new ActionMessage("O Valor informado deve ser maior ou igual a \"0.00\". "));
                }
            } catch (NumberFormatException e) {
                messages.add("nomePaciente", new ActionMessage("O campo Valor é inválido"));
            }
        }
        if (StringUtilsOdontosis.isInteger(getFormaPagamento())) {
            if (Integer.parseInt(getFormaPagamento()) == 1) {
                if (StringUtilsOdontosis.isVazia(getNomeEmitente())) {
                    messages.add("nomePaciente", new ActionMessage("O campo Nome do Emitente deve ser preenchido."));
                }
                if (StringUtilsOdontosis.isVazia(numeroCheque)) {
                    messages.add("nomePaciente", new ActionMessage("O campo Número do cheque deve ser preenchido."));
                }
                if (StringUtilsOdontosis.isVazia(getBanco())) {
                    messages.add("nomePaciente", new ActionMessage("O campo Banco deve ser preenchido."));
                }
            }
        } else {
            messages.add("nomePaciente", new ActionMessage("O campo Forma de Pagamento é obrigatório."));
        }
        if (messages.size() > 0 && getMetodo() != null) {
            errors.add(messages);
            Collection<String> erros = new ArrayList<String>();
            for (Iterator iterator = messages.get(); iterator.hasNext(); ) {
                ActionMessage string = (ActionMessage) iterator.next();
                erros.add(string.getKey());
            }
            request.setAttribute("mensagens", erros);
            return errors;
        } else {
            request.setAttribute("mensagens", null);
            return super.validate(mapping, request);
        }
    }

    public String getCancelar() {
        Paciente paciente = pagamento.getServico().getPacienteServico();
        return "pagamento.do?nomePaciente=" + paciente.getDescricaoAutoComplete() + "?&pacienteId=" + paciente.getId() + "&metodo=pesquisar";
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public String getValorPagamento() {
        return valorPagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public void setTotalParcelas(String totalParcelas) {
        this.totalParcelas = totalParcelas;
    }

    public void setValorPagamento(String valorPagamento) {
        this.valorPagamento = valorPagamento;
    }
}
