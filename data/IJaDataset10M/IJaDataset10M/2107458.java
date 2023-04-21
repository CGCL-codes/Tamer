package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * The persistent class for the tbreceber_itens database table.
 * 
 */
@Entity
@Table(name = "tbreceber_itens")
public class TbreceberIten implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    @Column(length = 20)
    private String agencia;

    @Column(length = 20)
    private String cc;

    @Column(length = 10)
    private String cheque;

    @Column(length = 14)
    private String cpf;

    @Temporal(TemporalType.DATE)
    private Date datarecebimento;

    @Temporal(TemporalType.DATE)
    private Date dpg;

    @Column(length = 50)
    private String flag;

    @Column(length = 50)
    private String flag2;

    private Time hora;

    @Column(length = 20)
    private String idbanco;

    private int idcaixa;

    @Column(name = "idconta_destino")
    private int idcontaDestino;

    @Column(name = "idconta_receber")
    private int idcontaReceber;

    private int idforma;

    private int idvenda;

    private double juros;

    private double multa;

    @Column(length = 80)
    private String nome;

    @Column(length = 80)
    private String observacao;

    private float saldo;

    private double subtotal;

    @Column(length = 80)
    private String titular;

    @Column(columnDefinition = "enum('Y','N')")
    private String utilizada;

    private double valor;

    public TbreceberIten() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public String getAgencia() {
        return this.agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCheque() {
        return this.cheque;
    }

    public void setCheque(String cheque) {
        this.cheque = cheque;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDatarecebimento() {
        return this.datarecebimento;
    }

    public void setDatarecebimento(Date datarecebimento) {
        this.datarecebimento = datarecebimento;
    }

    public Date getDpg() {
        return this.dpg;
    }

    public void setDpg(Date dpg) {
        this.dpg = dpg;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag2() {
        return this.flag2;
    }

    public void setFlag2(String flag2) {
        this.flag2 = flag2;
    }

    public Time getHora() {
        return this.hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getIdbanco() {
        return this.idbanco;
    }

    public void setIdbanco(String idbanco) {
        this.idbanco = idbanco;
    }

    public int getIdcaixa() {
        return this.idcaixa;
    }

    public void setIdcaixa(int idcaixa) {
        this.idcaixa = idcaixa;
    }

    public int getIdcontaDestino() {
        return this.idcontaDestino;
    }

    public void setIdcontaDestino(int idcontaDestino) {
        this.idcontaDestino = idcontaDestino;
    }

    public int getIdcontaReceber() {
        return this.idcontaReceber;
    }

    public void setIdcontaReceber(int idcontaReceber) {
        this.idcontaReceber = idcontaReceber;
    }

    public int getIdforma() {
        return this.idforma;
    }

    public void setIdforma(int idforma) {
        this.idforma = idforma;
    }

    public int getIdvenda() {
        return this.idvenda;
    }

    public void setIdvenda(int idvenda) {
        this.idvenda = idvenda;
    }

    public double getJuros() {
        return this.juros;
    }

    public void setJuros(double juros) {
        this.juros = juros;
    }

    public double getMulta() {
        return this.multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public float getSaldo() {
        return this.saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public double getSubtotal() {
        return this.subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getTitular() {
        return this.titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getUtilizada() {
        return this.utilizada;
    }

    public void setUtilizada(String utilizada) {
        this.utilizada = utilizada;
    }

    public double getValor() {
        return this.valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
