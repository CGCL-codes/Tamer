package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * The persistent class for the tbmovimento database table.
 * 
 */
@Entity
@Table(name = "tbmovimento")
public class Tbmovimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    private double acrescimo;

    @Column(name = "aliq_icms")
    private double aliqIcms;

    @Column(name = "aliq_ipi")
    private double aliqIpi;

    @Column(name = "aliq_irrf")
    private double aliqIrrf;

    @Column(name = "aliq_iss")
    private double aliqIss;

    @Column(columnDefinition = "enum('Y','N')")
    private String aprovada;

    @Column(columnDefinition = "enum('Y','N')")
    private String cancelado;

    @Column(length = 10)
    private String cfop;

    @Column(name = "cod_vendedor")
    private double codVendedor;

    @Column(name = "codigo_cliente")
    private int codigoCliente;

    private int codigoproduto;

    @Column(name = "codplano_avista")
    private int codplanoAvista;

    @Column(name = "codplano_cartao")
    private int codplanoCartao;

    @Column(name = "codplano_cheque")
    private int codplanoCheque;

    @Column(name = "codplano_chequeprazo")
    private int codplanoChequeprazo;

    @Column(name = "codplano_convenio")
    private int codplanoConvenio;

    @Column(name = "codplano_prazo")
    private int codplanoPrazo;

    @Column(name = "comissao_gerada")
    private double comissaoGerada;

    @Column(columnDefinition = "enum('Y','N')")
    private String conferido;

    @Column(length = 30)
    private String contrato;

    @Column(name = "custo_total_epoca")
    private double custoTotalEpoca;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_documento", nullable = false)
    private Date dataDocumento;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_lancamento")
    private Date dataLancamento;

    private int deposito;

    private double desconto;

    @Column(name = "desconto_item")
    private double descontoItem;

    private int despesas;

    @Column(name = "doc_origem")
    private int docOrigem;

    private int documento;

    private int ecf;

    @Column(nullable = false)
    private int empresa;

    @Column(columnDefinition = "enum('Y','N')")
    private String endossado;

    @Column(columnDefinition = "enum('Y','N')")
    private String excluido;

    private int ficha;

    @Column(name = "forma_pagto")
    private int formaPagto;

    private int frete;

    @Column(name = "hora_lancamento")
    private Time horaLancamento;

    @Column(length = 200)
    private String identificador;

    @Column(name = "markup_liquido")
    private double markupLiquido;

    @Lob()
    private String obs;

    @Column(nullable = false, length = 5)
    private String operacao;

    @Column(name = "perc_desc_acresc_item")
    private double percDescAcrescItem;

    private double plano;

    @Column(name = "pre_venda", columnDefinition = "enum('Y','N')")
    private String preVenda;

    @Column(name = "preco_liquido_item")
    private double precoLiquidoItem;

    @Column(name = "preco_tabela_epoca")
    private double precoTabelaEpoca;

    @Column(name = "preco_unitario")
    private double precoUnitario;

    @Column(name = "pv_desconto")
    private double pvDesconto;

    @Column(name = "pv_totcartao")
    private double pvTotcartao;

    @Column(name = "pv_totchqprazo")
    private double pvTotchqprazo;

    @Column(name = "pv_totprazo")
    private double pvTotprazo;

    @Column(name = "pv_troco")
    private double pvTroco;

    private double qtdeproduto;

    @Column(name = "qtdeproduto_conferido")
    private double qtdeprodutoConferido;

    private int seguro;

    @Column(length = 100)
    private String serie;

    @Column(name = "tipo_preco", length = 10)
    private String tipoPreco;

    @Column(name = "tipo_transacao", length = 5)
    private String tipoTransacao;

    @Column(name = "total_avista")
    private double totalAvista;

    @Column(name = "total_cartao")
    private double totalCartao;

    @Column(name = "total_cheque")
    private double totalCheque;

    @Column(name = "total_cheque_prazo")
    private int totalChequePrazo;

    @Column(name = "total_convenio")
    private int totalConvenio;

    @Column(name = "total_prazo")
    private double totalPrazo;

    @Column(nullable = false)
    private int transacao;

    @Column(name = "transacao_compra")
    private int transacaoCompra;

    private int usuario;

    @Column(name = "valor_desc_acresc_item")
    private double valorDescAcrescItem;

    @Column(name = "valor_ipi")
    private double valorIpi;

    @Column(name = "valor_irrf")
    private double valorIrrf;

    @Column(name = "valor_pis_cofins")
    private double valorPisCofins;

    public Tbmovimento() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public double getAcrescimo() {
        return this.acrescimo;
    }

    public void setAcrescimo(double acrescimo) {
        this.acrescimo = acrescimo;
    }

    public double getAliqIcms() {
        return this.aliqIcms;
    }

    public void setAliqIcms(double aliqIcms) {
        this.aliqIcms = aliqIcms;
    }

    public double getAliqIpi() {
        return this.aliqIpi;
    }

    public void setAliqIpi(double aliqIpi) {
        this.aliqIpi = aliqIpi;
    }

    public double getAliqIrrf() {
        return this.aliqIrrf;
    }

    public void setAliqIrrf(double aliqIrrf) {
        this.aliqIrrf = aliqIrrf;
    }

    public double getAliqIss() {
        return this.aliqIss;
    }

    public void setAliqIss(double aliqIss) {
        this.aliqIss = aliqIss;
    }

    public String getAprovada() {
        return this.aprovada;
    }

    public void setAprovada(String aprovada) {
        this.aprovada = aprovada;
    }

    public String getCancelado() {
        return this.cancelado;
    }

    public void setCancelado(String cancelado) {
        this.cancelado = cancelado;
    }

    public String getCfop() {
        return this.cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public double getCodVendedor() {
        return this.codVendedor;
    }

    public void setCodVendedor(double codVendedor) {
        this.codVendedor = codVendedor;
    }

    public int getCodigoCliente() {
        return this.codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoproduto() {
        return this.codigoproduto;
    }

    public void setCodigoproduto(int codigoproduto) {
        this.codigoproduto = codigoproduto;
    }

    public int getCodplanoAvista() {
        return this.codplanoAvista;
    }

    public void setCodplanoAvista(int codplanoAvista) {
        this.codplanoAvista = codplanoAvista;
    }

    public int getCodplanoCartao() {
        return this.codplanoCartao;
    }

    public void setCodplanoCartao(int codplanoCartao) {
        this.codplanoCartao = codplanoCartao;
    }

    public int getCodplanoCheque() {
        return this.codplanoCheque;
    }

    public void setCodplanoCheque(int codplanoCheque) {
        this.codplanoCheque = codplanoCheque;
    }

    public int getCodplanoChequeprazo() {
        return this.codplanoChequeprazo;
    }

    public void setCodplanoChequeprazo(int codplanoChequeprazo) {
        this.codplanoChequeprazo = codplanoChequeprazo;
    }

    public int getCodplanoConvenio() {
        return this.codplanoConvenio;
    }

    public void setCodplanoConvenio(int codplanoConvenio) {
        this.codplanoConvenio = codplanoConvenio;
    }

    public int getCodplanoPrazo() {
        return this.codplanoPrazo;
    }

    public void setCodplanoPrazo(int codplanoPrazo) {
        this.codplanoPrazo = codplanoPrazo;
    }

    public double getComissaoGerada() {
        return this.comissaoGerada;
    }

    public void setComissaoGerada(double comissaoGerada) {
        this.comissaoGerada = comissaoGerada;
    }

    public String getConferido() {
        return this.conferido;
    }

    public void setConferido(String conferido) {
        this.conferido = conferido;
    }

    public String getContrato() {
        return this.contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public double getCustoTotalEpoca() {
        return this.custoTotalEpoca;
    }

    public void setCustoTotalEpoca(double custoTotalEpoca) {
        this.custoTotalEpoca = custoTotalEpoca;
    }

    public Date getDataDocumento() {
        return this.dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public Date getDataLancamento() {
        return this.dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public int getDeposito() {
        return this.deposito;
    }

    public void setDeposito(int deposito) {
        this.deposito = deposito;
    }

    public double getDesconto() {
        return this.desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getDescontoItem() {
        return this.descontoItem;
    }

    public void setDescontoItem(double descontoItem) {
        this.descontoItem = descontoItem;
    }

    public int getDespesas() {
        return this.despesas;
    }

    public void setDespesas(int despesas) {
        this.despesas = despesas;
    }

    public int getDocOrigem() {
        return this.docOrigem;
    }

    public void setDocOrigem(int docOrigem) {
        this.docOrigem = docOrigem;
    }

    public int getDocumento() {
        return this.documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public int getEcf() {
        return this.ecf;
    }

    public void setEcf(int ecf) {
        this.ecf = ecf;
    }

    public int getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public String getEndossado() {
        return this.endossado;
    }

    public void setEndossado(String endossado) {
        this.endossado = endossado;
    }

    public String getExcluido() {
        return this.excluido;
    }

    public void setExcluido(String excluido) {
        this.excluido = excluido;
    }

    public int getFicha() {
        return this.ficha;
    }

    public void setFicha(int ficha) {
        this.ficha = ficha;
    }

    public int getFormaPagto() {
        return this.formaPagto;
    }

    public void setFormaPagto(int formaPagto) {
        this.formaPagto = formaPagto;
    }

    public int getFrete() {
        return this.frete;
    }

    public void setFrete(int frete) {
        this.frete = frete;
    }

    public Time getHoraLancamento() {
        return this.horaLancamento;
    }

    public void setHoraLancamento(Time horaLancamento) {
        this.horaLancamento = horaLancamento;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public double getMarkupLiquido() {
        return this.markupLiquido;
    }

    public void setMarkupLiquido(double markupLiquido) {
        this.markupLiquido = markupLiquido;
    }

    public String getObs() {
        return this.obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getOperacao() {
        return this.operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public double getPercDescAcrescItem() {
        return this.percDescAcrescItem;
    }

    public void setPercDescAcrescItem(double percDescAcrescItem) {
        this.percDescAcrescItem = percDescAcrescItem;
    }

    public double getPlano() {
        return this.plano;
    }

    public void setPlano(double plano) {
        this.plano = plano;
    }

    public String getPreVenda() {
        return this.preVenda;
    }

    public void setPreVenda(String preVenda) {
        this.preVenda = preVenda;
    }

    public double getPrecoLiquidoItem() {
        return this.precoLiquidoItem;
    }

    public void setPrecoLiquidoItem(double precoLiquidoItem) {
        this.precoLiquidoItem = precoLiquidoItem;
    }

    public double getPrecoTabelaEpoca() {
        return this.precoTabelaEpoca;
    }

    public void setPrecoTabelaEpoca(double precoTabelaEpoca) {
        this.precoTabelaEpoca = precoTabelaEpoca;
    }

    public double getPrecoUnitario() {
        return this.precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getPvDesconto() {
        return this.pvDesconto;
    }

    public void setPvDesconto(double pvDesconto) {
        this.pvDesconto = pvDesconto;
    }

    public double getPvTotcartao() {
        return this.pvTotcartao;
    }

    public void setPvTotcartao(double pvTotcartao) {
        this.pvTotcartao = pvTotcartao;
    }

    public double getPvTotchqprazo() {
        return this.pvTotchqprazo;
    }

    public void setPvTotchqprazo(double pvTotchqprazo) {
        this.pvTotchqprazo = pvTotchqprazo;
    }

    public double getPvTotprazo() {
        return this.pvTotprazo;
    }

    public void setPvTotprazo(double pvTotprazo) {
        this.pvTotprazo = pvTotprazo;
    }

    public double getPvTroco() {
        return this.pvTroco;
    }

    public void setPvTroco(double pvTroco) {
        this.pvTroco = pvTroco;
    }

    public double getQtdeproduto() {
        return this.qtdeproduto;
    }

    public void setQtdeproduto(double qtdeproduto) {
        this.qtdeproduto = qtdeproduto;
    }

    public double getQtdeprodutoConferido() {
        return this.qtdeprodutoConferido;
    }

    public void setQtdeprodutoConferido(double qtdeprodutoConferido) {
        this.qtdeprodutoConferido = qtdeprodutoConferido;
    }

    public int getSeguro() {
        return this.seguro;
    }

    public void setSeguro(int seguro) {
        this.seguro = seguro;
    }

    public String getSerie() {
        return this.serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTipoPreco() {
        return this.tipoPreco;
    }

    public void setTipoPreco(String tipoPreco) {
        this.tipoPreco = tipoPreco;
    }

    public String getTipoTransacao() {
        return this.tipoTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public double getTotalAvista() {
        return this.totalAvista;
    }

    public void setTotalAvista(double totalAvista) {
        this.totalAvista = totalAvista;
    }

    public double getTotalCartao() {
        return this.totalCartao;
    }

    public void setTotalCartao(double totalCartao) {
        this.totalCartao = totalCartao;
    }

    public double getTotalCheque() {
        return this.totalCheque;
    }

    public void setTotalCheque(double totalCheque) {
        this.totalCheque = totalCheque;
    }

    public int getTotalChequePrazo() {
        return this.totalChequePrazo;
    }

    public void setTotalChequePrazo(int totalChequePrazo) {
        this.totalChequePrazo = totalChequePrazo;
    }

    public int getTotalConvenio() {
        return this.totalConvenio;
    }

    public void setTotalConvenio(int totalConvenio) {
        this.totalConvenio = totalConvenio;
    }

    public double getTotalPrazo() {
        return this.totalPrazo;
    }

    public void setTotalPrazo(double totalPrazo) {
        this.totalPrazo = totalPrazo;
    }

    public int getTransacao() {
        return this.transacao;
    }

    public void setTransacao(int transacao) {
        this.transacao = transacao;
    }

    public int getTransacaoCompra() {
        return this.transacaoCompra;
    }

    public void setTransacaoCompra(int transacaoCompra) {
        this.transacaoCompra = transacaoCompra;
    }

    public int getUsuario() {
        return this.usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public double getValorDescAcrescItem() {
        return this.valorDescAcrescItem;
    }

    public void setValorDescAcrescItem(double valorDescAcrescItem) {
        this.valorDescAcrescItem = valorDescAcrescItem;
    }

    public double getValorIpi() {
        return this.valorIpi;
    }

    public void setValorIpi(double valorIpi) {
        this.valorIpi = valorIpi;
    }

    public double getValorIrrf() {
        return this.valorIrrf;
    }

    public void setValorIrrf(double valorIrrf) {
        this.valorIrrf = valorIrrf;
    }

    public double getValorPisCofins() {
        return this.valorPisCofins;
    }

    public void setValorPisCofins(double valorPisCofins) {
        this.valorPisCofins = valorPisCofins;
    }
}
