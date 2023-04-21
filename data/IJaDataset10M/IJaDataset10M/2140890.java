package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;

/**
 * The persistent class for the tbprodutos_tabela database table.
 * 
 */
@Entity
@Table(name = "tbprodutos_tabela")
public class TbprodutosTabela implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int idcod;

    @Column(length = 50)
    private String flag;

    @Column(nullable = false)
    private long idproduto;

    private int idtabela;

    private double margem;

    @Lob()
    @Column(name = "ult_atu", nullable = false)
    private String ultAtu;

    @Column(nullable = false)
    private double venda;

    public TbprodutosTabela() {
    }

    public int getIdcod() {
        return this.idcod;
    }

    public void setIdcod(int idcod) {
        this.idcod = idcod;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public long getIdproduto() {
        return this.idproduto;
    }

    public void setIdproduto(long idproduto) {
        this.idproduto = idproduto;
    }

    public int getIdtabela() {
        return this.idtabela;
    }

    public void setIdtabela(int idtabela) {
        this.idtabela = idtabela;
    }

    public double getMargem() {
        return this.margem;
    }

    public void setMargem(double margem) {
        this.margem = margem;
    }

    public String getUltAtu() {
        return this.ultAtu;
    }

    public void setUltAtu(String ultAtu) {
        this.ultAtu = ultAtu;
    }

    public double getVenda() {
        return this.venda;
    }

    public void setVenda(double venda) {
        this.venda = venda;
    }
}
