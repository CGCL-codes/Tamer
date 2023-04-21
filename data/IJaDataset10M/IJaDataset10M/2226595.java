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
 * Classe com os elementos do/a QuadroDePessoa
 * 
 * @author Grupo EDAWIR
 * @since 14/10/2011
 */
@Entity
@Table(name = "quadroDePessoa")
public class QuadroDePessoa extends ModelEntity {

    private static final long serialVersionUID = -5852668273534638067L;

    private Projeto projeto;

    private List<ItemQuadroDePessoa> listaQuadroDePessoa;

    /**
     * @return id o identificador da entidade
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_quadroDePessoa")
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
	 * @return listaQuadroDePessoa o/a listaQuadroDePessoa
	 */
    @OneToMany(targetEntity = br.com.edawir.integracao.model.ItemQuadroDePessoa.class, fetch = FetchType.EAGER)
    @IndexColumn(name = "INDEX_COL", nullable = false, base = 1)
    @JoinColumn(name = "cod_quadroDePessoa", nullable = false)
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
    public List<ItemQuadroDePessoa> getListaQuadroDePessoa() {
        return listaQuadroDePessoa;
    }

    /**
	 * @param listaQuadroDePessoa o/a listaQuadroDePessoa � atualizado/a
	 */
    public void setListaQuadroDePessoa(List<ItemQuadroDePessoa> listaQuadroDePessoa) {
        this.listaQuadroDePessoa = listaQuadroDePessoa;
    }

    /**
	 * M�todo retorna todos os atributos da entidade
	 * @return String toString()
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "QuadroDePessoa [" + super.toString() + ", projeto=" + projeto + ", listaQuadroDePessoa=" + listaQuadroDePessoa + "]";
    }
}
