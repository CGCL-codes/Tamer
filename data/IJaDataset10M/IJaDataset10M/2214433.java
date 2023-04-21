package br.com.edawir.integracao.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe com os elementos do/a Livro
 * 
 * @author Grupo EDAWIR
 * @since 13/10/2011
 */
@Entity
@Table(name = "livro")
public class Livro extends ModelEntity {

    private static final long serialVersionUID = 7074066726370071227L;

    private String titulo;

    private String autor;

    private String editora;

    private String anoDeEdicao;

    private String numeroDeEdicao;

    private String isbn;

    /**
     * @return id o identificador da entidade
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_livro")
    @ManyToOne
    public Long getId() {
        return super.getId();
    }

    /**
	 * @return titulo o/a titulo
	 */
    @Column(name = "desc_titulo")
    public String getTitulo() {
        return titulo;
    }

    /**
	 * @param titulo o/a titulo � atualizado/a
	 */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
	 * @return autor o/a autor
	 */
    @Column(name = "nm_autor")
    public String getAutor() {
        return autor;
    }

    /**
	 * @param autor o/a autor � atualizado/a
	 */
    public void setAutor(String autor) {
        this.autor = autor;
    }

    /**
	 * @return editora o/a editora
	 */
    @Column(name = "desc_editora")
    public String getEditora() {
        return editora;
    }

    /**
	 * @param editora o/a editora � atualizado/a
	 */
    public void setEditora(String editora) {
        this.editora = editora;
    }

    /**
	 * @return anoDeEdicao o/a anoDeEdicao
	 */
    @Column(name = "nr_anoDeEdicao")
    public String getAnoDeEdicao() {
        return anoDeEdicao;
    }

    /**
	 * @param anoDeEdicao o/a anoDeEdicao � atualizado/a
	 */
    public void setAnoDeEdicao(String anoDeEdicao) {
        this.anoDeEdicao = anoDeEdicao;
    }

    /**
	 * @return numeroDeEdicao o/a numeroDeEdicao
	 */
    @Column(name = "nr_edicao")
    public String getNumeroDeEdicao() {
        return numeroDeEdicao;
    }

    /**
	 * @param numeroDeEdicao o/a numeroDeEdicao � atualizado/a
	 */
    public void setNumeroDeEdicao(String numeroDeEdicao) {
        this.numeroDeEdicao = numeroDeEdicao;
    }

    /**
	 * @return isbn o/a isbn
	 * O ISBN - International Standard Book Number - � um sistema internacional
	 * padronizado que identifica numericamente os livros segundo o t�tulo,
	 * o autor, o pa�s, a editora, individualizando-os inclusive por edi��o.
	 */
    @Column(name = "nr_isbn")
    public String getIsbn() {
        return isbn;
    }

    /**
	 * @param isbn o/a isbn � atualizado/a
	 * O ISBN - International Standard Book Number - � um sistema internacional
	 * padronizado que identifica numericamente os livros segundo o t�tulo,
	 * o autor, o pa�s, a editora, individualizando-os inclusive por edi��o.
	 */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
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
        return "Livro [" + super.toString() + ", titulo=" + titulo + ", autor=" + autor + ", editora=" + editora + ", anoDeEdicao=" + anoDeEdicao + ", numeroDeEdicao=" + numeroDeEdicao + ", isbn=" + isbn + "]";
    }
}
