package br.com.edawir.integracao.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;

/**
 * Classe com os elementos do/a EstruturaCurricular
 * 
 * @author Grupo EDAWIR
 * @since 17/10/2011
 */
@Entity
@Table(name = "estruturaCurricular")
public class EstruturaCurricular extends ModelEntity {

    private static final long serialVersionUID = 119569084614174877L;

    private Projeto projeto;

    private List<ItemDaEstruturaCurricular> itensDaEstruturaCurricular;

    /**
     * @return id o identificador da entidade
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_estruturaCurricular")
    @Override
    public Long getId() {
        return super.getId();
    }

    /**
	 * @return projeto o/a projeto
	 */
    @ManyToOne
    @JoinColumn(name = "cod_projeto")
    public Projeto getProjeto() {
        return projeto;
    }

    /**
	 * @param projeto o/a projeto � atualizado/a
	 */
    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    /**
	 * @return itensDaEstruturaCurricular o/a itensDaEstruturaCurricular
	 */
    @OneToMany(fetch = FetchType.EAGER)
    @IndexColumn(name = "INDEX_COL")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    @JoinColumn(name = "cod_estruturaCurricular", nullable = false)
    public List<ItemDaEstruturaCurricular> getItensDaEstruturaCurricular() {
        return itensDaEstruturaCurricular;
    }

    /**
	 * @param itensDaEstruturaCurricular o/a itensDaEstruturaCurricular � atualizado/a
	 */
    public void setItensDaEstruturaCurricular(List<ItemDaEstruturaCurricular> itensDaEstruturaCurricular) {
        this.itensDaEstruturaCurricular = itensDaEstruturaCurricular;
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
        return "EstruturaCurricular [" + super.toString() + ", projeto=" + projeto + ", itensDaEstruturaCurricular=" + itensDaEstruturaCurricular + "]";
    }
}
