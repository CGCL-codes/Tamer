package br.com.hsj.importador.entidades.tagcomercio;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the mv_vendas_movimento database table.
 * 
 */
@Entity
@Table(name = "mv_vendas_movimento")
public class MvVendasMovimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "cod_lancamento", nullable = false)
    private int codLancamento;

    @Column(nullable = false, length = 14)
    private String controle;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_venda", nullable = false)
    private Date dataVenda;

    @Column(name = "id_cliente", nullable = false)
    private int idCliente;

    @Column(name = "id_cliente_convenio", nullable = false)
    private int idClienteConvenio;

    @Column(name = "id_grade", nullable = false)
    private int idGrade;

    @Column(name = "id_login")
    private int idLogin;

    @Column(name = "id_produto", nullable = false)
    private int idProduto;

    @Column(name = "modo_lancamento", nullable = false)
    private int modoLancamento;

    @Column(name = "modo_venda", nullable = false)
    private int modoVenda;

    @Column(nullable = false)
    private double quant;

    @Column(nullable = false, length = 2)
    private char terminal;

    @Column(nullable = false, length = 1)
    private char turno;

    @Column(nullable = false)
    private double valor;

    @Column(name = "vr_cotacao", nullable = false)
    private double vrCotacao;

    @Column(name = "vr_total", nullable = false)
    private double vrTotal;

    public MvVendasMovimento() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodLancamento() {
        return this.codLancamento;
    }

    public void setCodLancamento(int codLancamento) {
        this.codLancamento = codLancamento;
    }

    public String getControle() {
        return this.controle;
    }

    public void setControle(String controle) {
        this.controle = controle;
    }

    public Date getDataVenda() {
        return this.dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public int getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdClienteConvenio() {
        return this.idClienteConvenio;
    }

    public void setIdClienteConvenio(int idClienteConvenio) {
        this.idClienteConvenio = idClienteConvenio;
    }

    public int getIdGrade() {
        return this.idGrade;
    }

    public void setIdGrade(int idGrade) {
        this.idGrade = idGrade;
    }

    public int getIdLogin() {
        return this.idLogin;
    }

    public void setIdLogin(int idLogin) {
        this.idLogin = idLogin;
    }

    public int getIdProduto() {
        return this.idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getModoLancamento() {
        return this.modoLancamento;
    }

    public void setModoLancamento(int modoLancamento) {
        this.modoLancamento = modoLancamento;
    }

    public int getModoVenda() {
        return this.modoVenda;
    }

    public void setModoVenda(int modoVenda) {
        this.modoVenda = modoVenda;
    }

    public double getQuant() {
        return this.quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }

    public char getTerminal() {
        return this.terminal;
    }

    public void setTerminal(char terminal) {
        this.terminal = terminal;
    }

    public char getTurno() {
        return this.turno;
    }

    public void setTurno(char turno) {
        this.turno = turno;
    }

    public double getValor() {
        return this.valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getVrCotacao() {
        return this.vrCotacao;
    }

    public void setVrCotacao(double vrCotacao) {
        this.vrCotacao = vrCotacao;
    }

    public double getVrTotal() {
        return this.vrTotal;
    }

    public void setVrTotal(double vrTotal) {
        this.vrTotal = vrTotal;
    }
}
