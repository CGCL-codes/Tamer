package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the tbvenda_itens database table.
 * 
 */
@Entity
@Table(name = "tbvenda_itens")
public class TbvendaIten implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idcod;

    @Column(length = 100)
    private String agrupador;

    private Double creditotroca;

    private Double custo;

    @Column(name = "dacp_tot", nullable = false)
    private Double dacpTot;

    @Column(name = "dacp_unt", nullable = false)
    private Double dacpUnt;

    @Column(name = "dacr_tot", nullable = false)
    private Double dacrTot;

    @Column(name = "dacr_unt", nullable = false)
    private Double dacrUnt;

    private Double descmaxporc;

    private Double descmaxreais;

    @Column(length = 100)
    private String descricao;

    @Temporal(TemporalType.DATE)
    private Date entregue;

    @Column(columnDefinition = "enum('Y','N')")
    private String fabrica;

    private Double icms;

    private Integer idconcli;

    private Integer idinstalador;

    private Integer idos;

    @Column(nullable = false)
    private Integer idproduto;

    private Integer idproduto2;

    @Column(nullable = false)
    private Integer idvenda;

    @Column(columnDefinition = "enum('Y','N')")
    private String imprimenf;

    private Integer item;

    private Double liquido;

    private Double liquidox;

    @Column(length = 50)
    private String pedidofabrica;

    @Column(nullable = false)
    private Double pedidopendente;

    @Column(name = "preco_cadastro")
    private Double precoCadastro;

    @Column(name = "preco_venda_tot")
    private Double precoVendaTot;

    @Column(name = "preco_venda_unt")
    private Double precoVendaUnt;

    @Temporal(TemporalType.DATE)
    private Date previsao;

    @Column(nullable = false)
    private Double qtde;

    @Column(name = "rds_fim")
    private Double rdsFim;

    @Column(name = "rds_ini")
    private Double rdsIni;

    @Column(length = 100)
    private String referencia;

    @Column(length = 10)
    private String servico;

    private Double sub;

    private Integer tipo;

    @Column(nullable = false)
    private Double total;

    @Column(length = 2)
    private String unidade;

    private Double venda;

    public TbvendaIten() {
    }

    public Integer getIdcod() {
        return this.idcod;
    }

    public void setIdcod(Integer idcod) {
        this.idcod = idcod;
    }

    public String getAgrupador() {
        return this.agrupador;
    }

    public void setAgrupador(String agrupador) {
        this.agrupador = agrupador;
    }

    public Double getCreditotroca() {
        return this.creditotroca;
    }

    public void setCreditotroca(Double creditotroca) {
        this.creditotroca = creditotroca;
    }

    public Double getCusto() {
        return this.custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public Double getDacpTot() {
        return this.dacpTot;
    }

    public void setDacpTot(Double dacpTot) {
        this.dacpTot = dacpTot;
    }

    public Double getDacpUnt() {
        return this.dacpUnt;
    }

    public void setDacpUnt(Double dacpUnt) {
        this.dacpUnt = dacpUnt;
    }

    public Double getDacrTot() {
        return this.dacrTot;
    }

    public void setDacrTot(Double dacrTot) {
        this.dacrTot = dacrTot;
    }

    public Double getDacrUnt() {
        return this.dacrUnt;
    }

    public void setDacrUnt(Double dacrUnt) {
        this.dacrUnt = dacrUnt;
    }

    public Double getDescmaxporc() {
        return this.descmaxporc;
    }

    public void setDescmaxporc(Double descmaxporc) {
        this.descmaxporc = descmaxporc;
    }

    public Double getDescmaxreais() {
        return this.descmaxreais;
    }

    public void setDescmaxreais(Double descmaxreais) {
        this.descmaxreais = descmaxreais;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getEntregue() {
        return this.entregue;
    }

    public void setEntregue(Date entregue) {
        this.entregue = entregue;
    }

    public String getFabrica() {
        return this.fabrica;
    }

    public void setFabrica(String fabrica) {
        this.fabrica = fabrica;
    }

    public Double getIcms() {
        return this.icms;
    }

    public void setIcms(Double icms) {
        this.icms = icms;
    }

    public Integer getIdconcli() {
        return this.idconcli;
    }

    public void setIdconcli(Integer idconcli) {
        this.idconcli = idconcli;
    }

    public Integer getIdinstalador() {
        return this.idinstalador;
    }

    public void setIdinstalador(Integer idinstalador) {
        this.idinstalador = idinstalador;
    }

    public Integer getIdos() {
        return this.idos;
    }

    public void setIdos(Integer idos) {
        this.idos = idos;
    }

    public Integer getIdproduto() {
        return this.idproduto;
    }

    public void setIdproduto(Integer idproduto) {
        this.idproduto = idproduto;
    }

    public Integer getIdproduto2() {
        return this.idproduto2;
    }

    public void setIdproduto2(Integer idproduto2) {
        this.idproduto2 = idproduto2;
    }

    public Integer getIdvenda() {
        return this.idvenda;
    }

    public void setIdvenda(Integer idvenda) {
        this.idvenda = idvenda;
    }

    public String getImprimenf() {
        return this.imprimenf;
    }

    public void setImprimenf(String imprimenf) {
        this.imprimenf = imprimenf;
    }

    public Integer getItem() {
        return this.item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Double getLiquido() {
        return this.liquido;
    }

    public void setLiquido(Double liquido) {
        this.liquido = liquido;
    }

    public Double getLiquidox() {
        return this.liquidox;
    }

    public void setLiquidox(Double liquidox) {
        this.liquidox = liquidox;
    }

    public String getPedidofabrica() {
        return this.pedidofabrica;
    }

    public void setPedidofabrica(String pedidofabrica) {
        this.pedidofabrica = pedidofabrica;
    }

    public Double getPedidopendente() {
        return this.pedidopendente;
    }

    public void setPedidopendente(Double pedidopendente) {
        this.pedidopendente = pedidopendente;
    }

    public Double getPrecoCadastro() {
        return this.precoCadastro;
    }

    public void setPrecoCadastro(Double precoCadastro) {
        this.precoCadastro = precoCadastro;
    }

    public Double getPrecoVendaTot() {
        return this.precoVendaTot;
    }

    public void setPrecoVendaTot(Double precoVendaTot) {
        this.precoVendaTot = precoVendaTot;
    }

    public Double getPrecoVendaUnt() {
        return this.precoVendaUnt;
    }

    public void setPrecoVendaUnt(Double precoVendaUnt) {
        this.precoVendaUnt = precoVendaUnt;
    }

    public Date getPrevisao() {
        return this.previsao;
    }

    public void setPrevisao(Date previsao) {
        this.previsao = previsao;
    }

    public Double getQtde() {
        return this.qtde;
    }

    public void setQtde(Double qtde) {
        this.qtde = qtde;
    }

    public Double getRdsFim() {
        return this.rdsFim;
    }

    public void setRdsFim(Double rdsFim) {
        this.rdsFim = rdsFim;
    }

    public Double getRdsIni() {
        return this.rdsIni;
    }

    public void setRdsIni(Double rdsIni) {
        this.rdsIni = rdsIni;
    }

    public String getReferencia() {
        return this.referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getServico() {
        return this.servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public Double getSub() {
        return this.sub;
    }

    public void setSub(Double sub) {
        this.sub = sub;
    }

    public Integer getTipo() {
        return this.tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Double getTotal() {
        return this.total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getUnidade() {
        return this.unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Double getVenda() {
        return this.venda;
    }

    public void setVenda(Double venda) {
        this.venda = venda;
    }
}
