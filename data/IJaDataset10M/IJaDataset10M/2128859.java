package br.com.hsj.financeiro.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Classe responsável por armazenar as informações de Movimentacao
 * 
 * @author Hamilton dos Santos Junior
 * @date 08/10/2011
 *
 */
@Entity
@Table(name = "movimentacoes")
@SequenceGenerator(name = "seq_movimentacao", sequenceName = "movimentacao_id_seq", initialValue = 1)
public class Movimentacao implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4917737168776144905L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movimentacao")
    @Column(name = "movimentacao_id")
    private Long id;

    @NotNull
    @Column(name = "valor_pagamento", nullable = false)
    private BigDecimal valorPagamento;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_pagamento", nullable = false)
    private Date dataPagamento;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "descricao", nullable = true, length = 100)
    private String descricao;

    @ManyToOne(optional = true)
    @JoinColumn(name = "subcategoria_id")
    private SubCategoria subcategoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public SubCategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubCategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Movimentacao other = (Movimentacao) obj;
        if (getId() == null) {
            if (other.getId() != null) return false;
        } else if (!getId().equals(other.getId())) return false;
        return true;
    }
}
