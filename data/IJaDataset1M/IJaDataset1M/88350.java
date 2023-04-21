package br.com.fiapbank.dominio.entidade;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Representa uma renda simples para o cliente 
 * 
 * @author robson.oliveira
 *
 * @see {@link Cliente}
 *
 */
@Entity
@Table(name = "RENDA")
public class Renda {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "RENDA_MENSAL", nullable = false)
    private BigDecimal rendaMensal;

    @Column(name = "VALOR_TOTAL_PATRIMONIO", nullable = false)
    private BigDecimal valorTotalPatrimonio;

    @Column(name = "GASTO_MENSAL", nullable = false)
    private BigDecimal gastoMensal;

    @OneToOne(mappedBy = "renda")
    private Cliente cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public BigDecimal getValorTotalPatrimonio() {
        return valorTotalPatrimonio;
    }

    public void setValorTotalPatrimonio(BigDecimal valorTotalPatrimonio) {
        this.valorTotalPatrimonio = valorTotalPatrimonio;
    }

    public BigDecimal getGastoMensal() {
        return gastoMensal;
    }

    public void setGastoMensal(BigDecimal gastoMensal) {
        this.gastoMensal = gastoMensal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
