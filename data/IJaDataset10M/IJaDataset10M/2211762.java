package com.dashboard.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "negocio")
public class Negocio implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6795050884647091893L;

    @Id
    @Column(name = "id_negocio")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_organizacao")
    private Organizacao organizacao;

    @OneToMany(mappedBy = "negocio", cascade = { CascadeType.ALL })
    private List<Dre> dres;

    /**
	 * sf
	 */
    public Negocio() {
    }

    /**
	 * dfsd.
	 * 
	 * @param name fds
	 * @param description sdf
	 */
    public Negocio(String name, String description) {
        this.nome = name;
        this.descricao = description;
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the nome
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * @param nome the nome to set
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * @return the descricao
	 */
    public String getDescricao() {
        return descricao;
    }

    /**
	 * @param descricao the descricao to set
	 */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
	 * @return the organizacao
	 */
    public Organizacao getOrganizacao() {
        return organizacao;
    }

    /**
	 * @param organizacao the organizacao to set
	 */
    public void setOrganizacao(Organizacao organizacao) {
        this.organizacao = organizacao;
    }

    /**
	 * @return the dres
	 */
    public List<Dre> getDres() {
        return dres;
    }

    /**
	 * @param dres the dres to set
	 */
    public void setDres(List<Dre> dres) {
        this.dres = dres;
    }

    /**
	 * {@inheritDoc}
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((descricao == null) ? 0 : descricao.hashCode());
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        result = PRIME * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Negocio other = (Negocio) obj;
        if (descricao == null) {
            if (other.descricao != null) {
                return false;
            }
        } else if (!descricao.equals(other.descricao)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (nome == null) {
            if (other.nome != null) {
                return false;
            }
        } else if (!nome.equals(other.nome)) {
            return false;
        }
        return true;
    }
}
