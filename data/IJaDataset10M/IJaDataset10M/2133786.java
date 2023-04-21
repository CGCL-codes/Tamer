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
@Table(name = "fotografia")
@NamedQueries({ @NamedQuery(name = "Fotografia.findAll", query = "SELECT f FROM Fotografia f"), @NamedQuery(name = "Fotografia.findByIdFotografia", query = "SELECT f FROM Fotografia f WHERE f.idFotografia = :idFotografia"), @NamedQuery(name = "Fotografia.findByDimensoes", query = "SELECT f FROM Fotografia f WHERE f.dimensoes = :dimensoes"), @NamedQuery(name = "Fotografia.findBySituacao", query = "SELECT f FROM Fotografia f WHERE f.situacao = :situacao"), @NamedQuery(name = "Fotografia.findBySuporte", query = "SELECT f FROM Fotografia f WHERE f.suporte = :suporte") })
public class Fotografia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_fotografia")
    private Integer idFotografia;

    @Column(name = "dimensoes")
    private String dimensoes;

    @Column(name = "situacao")
    private String situacao;

    @Column(name = "suporte")
    private String suporte;

    @JoinColumn(name = "id_documento_FK", referencedColumnName = "id_documento")
    @ManyToOne(optional = false)
    private Documento iddocumentoFK;

    public Fotografia() {
    }

    public Fotografia(Integer idFotografia) {
        this.idFotografia = idFotografia;
    }

    public Integer getIdFotografia() {
        return idFotografia;
    }

    public void setIdFotografia(Integer idFotografia) {
        this.idFotografia = idFotografia;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSuporte() {
        return suporte;
    }

    public void setSuporte(String suporte) {
        this.suporte = suporte;
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
        hash += (idFotografia != null ? idFotografia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Fotografia)) {
            return false;
        }
        Fotografia other = (Fotografia) object;
        if ((this.idFotografia == null && other.idFotografia != null) || (this.idFotografia != null && !this.idFotografia.equals(other.idFotografia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.Fotografia[idFotografia=" + idFotografia + "]";
    }
}
