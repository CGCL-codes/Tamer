package br.com.vendaweb.integracao.entidade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Perfil implements Entidade {

    /**
	 * @uml.property  name="id"
	 */
    @Id
    @SequenceGenerator(name = "sq_perfil", sequenceName = "sq_perfil", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_perfil")
    private Long id;

    /** 
	 * @uml.property name="permissao"
	 * @uml.associationEnd multiplicity="(1 -1)" inverse="perfil:br.com.vendaweb.integracao.entidade.Permissao"
	 */
    @OneToMany
    private Collection<Permissao> permissao;

    /**
	 * @uml.property  name="nome"
	 */
    @Column
    private String nome;

    /**
	 * @uml.property  name="indSuperUsuario"
	 */
    @Column
    private boolean indSuperUsuario;

    /**
	 * Getter of the property <tt>id</tt>
	 * @return   Returns the id.
	 * @uml.property  name="id"
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Getter of the property <tt>nome</tt>
	 * @return   Returns the nome.
	 * @uml.property  name="nome"
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * Getter of the property <tt>indSuperUsuario</tt>
	 * @return   Returns the indSuperUsuario.
	 * @uml.property  name="indSuperUsuario"
	 */
    public boolean isIndSuperUsuario() {
        return indSuperUsuario;
    }

    /**
	 * Setter of the property <tt>id</tt>
	 * @param id   The id to set.
	 * @uml.property  name="id"
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Setter of the property <tt>indSuperUsuario</tt>
	 * @param indSuperUsuario   The indSuperUsuario to set.
	 * @uml.property  name="indSuperUsuario"
	 */
    public void setIndSuperUsuario(boolean indSuperUsuario) {
        this.indSuperUsuario = indSuperUsuario;
    }

    /**
	 * Setter of the property <tt>nome</tt>
	 * @param nome   The nome to set.
	 * @uml.property  name="nome"
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Collection<Permissao> getPermissao() {
        return permissao;
    }

    public void setPermissao(Collection<Permissao> permissao) {
        this.permissao = permissao;
    }
}
