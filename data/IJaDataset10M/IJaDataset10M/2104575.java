package br.com.dip.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipobeneficio")
public class TipoBeneficio implements EntidadePadrao {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tpb_iddoobjeto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tpb_desc", nullable = false, length = 70)
    private String descricao;

    @Column(name = "tpb_sigla", nullable = false, length = 4)
    private String sigla;

    @Column(name = "tpb_calctipo", nullable = false)
    private Integer tipoCalc;

    @Column(name = "tpb_valor", nullable = false)
    private Float valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Integer getTipoCalc() {
        return tipoCalc;
    }

    public void setTipoCalc(Integer tipoCalc) {
        this.tipoCalc = tipoCalc;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }
}
