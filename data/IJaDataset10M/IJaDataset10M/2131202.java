package br.com.brasilsemchamas.entidade.relatorio;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author lellis
 */
@Entity
@Table(name = "livro")
@NamedQueries({ @NamedQuery(name = "Livro.findAll", query = "SELECT l FROM Livro l"), @NamedQuery(name = "Livro.findById", query = "SELECT l FROM Livro l WHERE l.id = :id"), @NamedQuery(name = "Livro.findByNumVolume", query = "SELECT l FROM Livro l WHERE l.numVolume = :numVolume"), @NamedQuery(name = "Livro.findByNumEdicao", query = "SELECT l FROM Livro l WHERE l.numEdicao = :numEdicao"), @NamedQuery(name = "Livro.findByIsbn", query = "SELECT l FROM Livro l WHERE l.isbn = :isbn"), @NamedQuery(name = "Livro.findByIssn", query = "SELECT l FROM Livro l WHERE l.issn = :issn"), @NamedQuery(name = "Livro.findByNumPaginas", query = "SELECT l FROM Livro l WHERE l.numPaginas = :numPaginas") })
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numVolume")
    private Integer numVolume;

    @Column(name = "numEdicao")
    private Integer numEdicao;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "issn")
    private String issn;

    @Column(name = "numPaginas")
    private Integer numPaginas;

    @JoinColumn(name = "id_documento_FK", referencedColumnName = "id_documento")
    @ManyToOne(optional = false)
    private Documento iddocumentoFK;

    public Livro() {
    }

    public Livro(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumVolume() {
        return numVolume;
    }

    public void setNumVolume(Integer numVolume) {
        this.numVolume = numVolume;
    }

    public Integer getNumEdicao() {
        return numEdicao;
    }

    public void setNumEdicao(Integer numEdicao) {
        this.numEdicao = numEdicao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public Integer getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(Integer numPaginas) {
        this.numPaginas = numPaginas;
    }

    public Documento getIddocumentoFK() {
        return iddocumentoFK;
    }

    public void setIddocumentoFK(Documento iddocumentoFK) {
        this.iddocumentoFK = iddocumentoFK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Livro)) {
            return false;
        }
        Livro other = (Livro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.Livro[id=" + id + "]";
    }
}
