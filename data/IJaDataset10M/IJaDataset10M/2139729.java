package br.com.edawir.integracao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import br.com.edawir.enumerator.TipoTelefone;

/**
 * Classe com os elementos do/a UnidadeOrgaoTelefone
 * 
 * @author Grupo EDAWIR
 * @since 27/10/2011
 */
@Entity
@Table(name = "unidadeOrgao_telefone")
public class UnidadeOrgaoTelefone extends ModelEntity {

    private static final long serialVersionUID = 1L;

    private Telefone telefone;

    private TipoTelefone tipoTelefone;

    private UnidadeOrgao unidadeOrgao;

    private String contato;

    /**
     * @return id o identificador da entidade
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_unidadeTelefone")
    @Override
    public Long getId() {
        return super.getId();
    }

    /**
	 * @return telefone o/a telefone
	 */
    @ManyToOne
    @JoinColumn(name = "cod_telefone")
    public Telefone getTelefone() {
        return telefone;
    }

    /**
	 * @param telefone o/a telefone � atualizado/a
	 */
    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    /**
	 * @return tipoTelefone o/a tipoTelefone
	 */
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "tp_telefone")
    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    /**
	 * @param tipoTelefone o/a tipoTelefone � atualizado/a
	 */
    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    /**
	 * @return unidadeOrgao o/a unidadeOrgao
	 */
    @ManyToOne
    @JoinColumn(name = "cod_unidadeOrgao")
    public UnidadeOrgao getUnidadeOrgao() {
        return unidadeOrgao;
    }

    /**
	 * @param unidadeOrgao o/a unidadeOrgao � atualizado/a
	 */
    public void setUnidadeOrgao(UnidadeOrgao unidadeOrgao) {
        this.unidadeOrgao = unidadeOrgao;
    }

    /**
	 * @return contato o/a contato
	 */
    @Column(name = "desc_contato")
    public String getContato() {
        return contato;
    }

    /**
	 * @param contato o/a contato � atualizado/a
	 */
    public void setContato(String contato) {
        this.contato = contato;
    }

    /**
	 * M�todo retorna todos os atributos da entidade
	 * @return String toString()
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "UnidadeOrgaoTelefone [" + super.toString() + ", telefone=" + telefone + ", tipoTelefone=" + tipoTelefone + ", unidadeOrgao=" + unidadeOrgao + ", contato=" + contato + "]";
    }
}
