package br.com.edawir.integracao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe com os elementos do/a ClienteAlvo
 * 
 * @author Grupo EDAWIR
 * @since 13/10/2011
 */
@Entity
@Table(name = "clienteAlvo")
public class ClienteAlvo extends ModelEntity implements BaseEntity {

    private static final long serialVersionUID = 4048590607983583103L;

    private String nome;

    /**
     * @return id o identificador da entidade
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_clienteAlvo")
    @Override
    public Long getId() {
        return super.getId();
    }

    /**
	 * @return nome o/a nome
	 */
    @Column(name = "nm_clienteAlvo")
    public String getNome() {
        return nome;
    }

    /**
	 * @param nome o/a nome � atualizado/a
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * M�todo retorna todos os atributos da entidade
	 * @return String toString()
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "ClienteAlvo [" + super.toString() + ", nome=" + nome + "]";
    }
}
