package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the tbcontas_movimento database table.
 * 
 */
@Entity
@Table(name = "tbcontas_movimento")
public class TbcontasMovimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Integer idcod;

    @Column(columnDefinition = "enum('Y','N')")
    private String alterado;

    @Column(columnDefinition = "enum('Y','N')")
    private String confirmado;

    @Temporal(TemporalType.DATE)
    private Date data;

    @Column(length = 80)
    private String descricao;

    @Column(length = 20)
    private String documento;

    private Integer empresa;

    @Column(length = 30)
    private String flag;

    private Integer historico;

    private Time hora;

    private Integer idcaixa;

    private Integer idcompra;

    private Integer idconta;

    private Integer idcontapagar;

    private Integer idcontareceber;

    private Integer iddevolucao;

    @Column(length = 100)
    private String identificador;

    private Integer idforma;

    private Integer idmovto;

    private Integer idos;

    @Column(name = "idos_itens")
    private Integer idosItens;

    private Integer idpagaritens;

    private Integer idreceberitens;

    private Integer idtipo;

    private Integer idvenda;

    private Integer indexador;

    private Integer lanc;

    private Integer referencial;

    private Double saldo;

    @Column(columnDefinition = "enum('F','C','X')")
    @Enumerated(EnumType.STRING)
    private String tipo;

    @Column(name = "tipo_lcto")
    private Integer tipoLcto;

    private Integer usuario;

    private Double valor;

    public TbcontasMovimento() {
    }

    public Integer getIdcod() {
        return this.idcod;
    }

    public void setIdcod(Integer idcod) {
        this.idcod = idcod;
    }

    public String getAlterado() {
        return this.alterado;
    }

    public void setAlterado(String alterado) {
        this.alterado = alterado;
    }

    public String getConfirmado() {
        return this.confirmado;
    }

    public void setConfirmado(String confirmado) {
        this.confirmado = confirmado;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDocumento() {
        return this.documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getHistorico() {
        return this.historico;
    }

    public void setHistorico(Integer historico) {
        this.historico = historico;
    }

    public Time getHora() {
        return this.hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public Integer getIdcaixa() {
        return this.idcaixa;
    }

    public void setIdcaixa(Integer idcaixa) {
        this.idcaixa = idcaixa;
    }

    public Integer getIdcompra() {
        return this.idcompra;
    }

    public void setIdcompra(Integer idcompra) {
        this.idcompra = idcompra;
    }

    public Integer getIdconta() {
        return this.idconta;
    }

    public void setIdconta(Integer idconta) {
        this.idconta = idconta;
    }

    public Integer getIdcontapagar() {
        return this.idcontapagar;
    }

    public void setIdcontapagar(Integer idcontapagar) {
        this.idcontapagar = idcontapagar;
    }

    public Integer getIdcontareceber() {
        return this.idcontareceber;
    }

    public void setIdcontareceber(Integer idcontareceber) {
        this.idcontareceber = idcontareceber;
    }

    public Integer getIddevolucao() {
        return this.iddevolucao;
    }

    public void setIddevolucao(Integer iddevolucao) {
        this.iddevolucao = iddevolucao;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getIdforma() {
        return this.idforma;
    }

    public void setIdforma(Integer idforma) {
        this.idforma = idforma;
    }

    public Integer getIdmovto() {
        return this.idmovto;
    }

    public void setIdmovto(Integer idmovto) {
        this.idmovto = idmovto;
    }

    public Integer getIdos() {
        return this.idos;
    }

    public void setIdos(Integer idos) {
        this.idos = idos;
    }

    public Integer getIdosItens() {
        return this.idosItens;
    }

    public void setIdosItens(Integer idosItens) {
        this.idosItens = idosItens;
    }

    public Integer getIdpagaritens() {
        return this.idpagaritens;
    }

    public void setIdpagaritens(Integer idpagaritens) {
        this.idpagaritens = idpagaritens;
    }

    public Integer getIdreceberitens() {
        return this.idreceberitens;
    }

    public void setIdreceberitens(Integer idreceberitens) {
        this.idreceberitens = idreceberitens;
    }

    public Integer getIdtipo() {
        return this.idtipo;
    }

    public void setIdtipo(Integer idtipo) {
        this.idtipo = idtipo;
    }

    public Integer getIdvenda() {
        return this.idvenda;
    }

    public void setIdvenda(Integer idvenda) {
        this.idvenda = idvenda;
    }

    public Integer getIndexador() {
        return this.indexador;
    }

    public void setIndexador(Integer indexador) {
        this.indexador = indexador;
    }

    public Integer getLanc() {
        return this.lanc;
    }

    public void setLanc(Integer lanc) {
        this.lanc = lanc;
    }

    public Integer getReferencial() {
        return this.referencial;
    }

    public void setReferencial(Integer referencial) {
        this.referencial = referencial;
    }

    public Double getSaldo() {
        return this.saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoLcto() {
        return this.tipoLcto;
    }

    public void setTipoLcto(Integer tipoLcto) {
        this.tipoLcto = tipoLcto;
    }

    public Integer getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Double getValor() {
        return this.valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
