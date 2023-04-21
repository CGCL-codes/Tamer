package br.com.edawir.integracao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Classe com os elementos do/a ItemQuadroDePessoa
 * 
 * @author Grupo EDAWIR
 * @since 03/12/2011
 */
@Entity
@Table(name = "item_do_quadro_de_pessoa")
public class ItemQuadroDePessoa extends ModelEntity {

    private static final long serialVersionUID = -5074601639821052049L;

    private Funcionario funcionario;

    private Integer totalHoraExtra;

    private Double valorDaHoraAula;

    private Integer cargaHoraria;

    private Double total;

    private Double somaTotal;

    private String observacao;

    /**
     * @return id o identificador da entidade
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_item_quadroDePessoa")
    @Override
    public Long getId() {
        return super.getId();
    }

    /**
	 * @return funcionario o/a funcionario
	 */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cod_funcionario")
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
	 * @param funcionario o/a funcionario � atualizado/a
	 */
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    /**
	 * @return totalHoraExtra o/a totalHoraExtra
	 * Total m�ximo de hora extra que o
	 * funcion�rio pode fazer por per�odo
	 */
    @Column(name = "totalHoraExtra_quadroDePessoa")
    public Integer getTotalHoraExtra() {
        return totalHoraExtra;
    }

    /**
	 * @param totalHoraExtra o/a totalHoraExtra � atualizado/a
	 */
    public void setTotalHoraExtra(Integer totalHoraExtra) {
        this.totalHoraExtra = totalHoraExtra;
    }

    /**
	 * @return valorDaHoraAula o/a valorDaHoraAula
	 * � definido de acordo com o grau de
	 * escolaridade: Doutor, Mestre,
	 * Especialista
	 */
    @Column(name = "valorDaHoraAula_quadroDePessoa")
    public Double getValorDaHoraAula() {
        return valorDaHoraAula;
    }

    /**
	 * @param valorDaHoraAula o/a valorDaHoraAula � atualizado/a
	 */
    public void setValorDaHoraAula(Double valorDaHoraAula) {
        this.valorDaHoraAula = valorDaHoraAula;
    }

    /**
	 * @return cargaHoraria o/a cargaHoraria
	 */
    @Column(name = "cargaHoraria_quadroDePessoa")
    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    /**
	 * @param cargaHoraria o/a cargaHoraria � atualizado/a
	 */
    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    /**
	 * @return total o/a total (valorDaHoraAula x cargaHoraria)
	 */
    @Column(name = "total_quadroDePessoa")
    public Double getTotal() {
        if (this.cargaHoraria == null || this.valorDaHoraAula == null) {
            this.total = 0.0;
        } else {
            this.total = getCargaHoraria() * getValorDaHoraAula();
        }
        double p = Math.pow(10, 2);
        total = Math.floor(((total + 0.00000001) * p)) / p;
        return total;
    }

    /**
	 * @param total o/a total (valorDaHoraAula x cargaHoraria) � atualizado/a
	 */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
	 * @return somaTotal o/a somaTotal
	 */
    @Column(name = "somaTotal_quadroDePessoa")
    public Double getSomaTotal() {
        return somaTotal;
    }

    /**
	 * @param somaTotal o/a somaTotal � atualizado/a
	 */
    public void setSomaTotal(Double somaTotal) {
        this.somaTotal = somaTotal;
    }

    /**
	 * @return observacao o/a observacao
	 */
    @Column(name = "observacao_quadroDePessoa")
    public String getObservacao() {
        return observacao;
    }

    /**
	 * @param observacao o/a observacao � atualizado/a
	 */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
	 * M�todo que realiza compara��o entre objetos
	 * 
	 * @param obj o objeto
	 * @return boolean verdadeiro ou falso
	 */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * M�todo retorna todos os atributos da entidade
	 * @return String toString()
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "ItemQuadroDePessoa [" + super.toString() + ", funcionario=" + funcionario + ", totalHoraExtra=" + totalHoraExtra + ", valorDaHoraAula=" + valorDaHoraAula + ", cargaHoraria=" + cargaHoraria + ", total=" + total + ", somaTotal=" + somaTotal + ", observacao=" + observacao + "]";
    }
}
