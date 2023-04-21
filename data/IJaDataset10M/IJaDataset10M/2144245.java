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
@Table(name = "recursos_hidricos")
@NamedQueries({ @NamedQuery(name = "RecursosHidricos.findAll", query = "SELECT r FROM RecursosHidricos r"), @NamedQuery(name = "RecursosHidricos.findByIdRecursosHidricos", query = "SELECT r FROM RecursosHidricos r WHERE r.idRecursosHidricos = :idRecursosHidricos"), @NamedQuery(name = "RecursosHidricos.findByDescricao", query = "SELECT r FROM RecursosHidricos r WHERE r.descricao = :descricao") })
public class RecursosHidricos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_recursos_hidricos")
    private Integer idRecursosHidricos;

    @Column(name = "descricao")
    private String descricao;

    @JoinColumn(name = "id_relatorio_incendio_FK", referencedColumnName = "id_relatorio_incendio")
    @ManyToOne(optional = false)
    private Relatorioincendio idrelatorioincendioFK;

    public RecursosHidricos() {
    }

    public RecursosHidricos(Integer idRecursosHidricos) {
        this.idRecursosHidricos = idRecursosHidricos;
    }

    public Integer getIdRecursosHidricos() {
        return idRecursosHidricos;
    }

    public void setIdRecursosHidricos(Integer idRecursosHidricos) {
        this.idRecursosHidricos = idRecursosHidricos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Relatorioincendio getIdrelatorioincendioFK() {
        return idrelatorioincendioFK;
    }

    public void setIdrelatorioincendioFK(Relatorioincendio idrelatorioincendioFK) {
        this.idrelatorioincendioFK = idrelatorioincendioFK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRecursosHidricos != null ? idRecursosHidricos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RecursosHidricos)) {
            return false;
        }
        RecursosHidricos other = (RecursosHidricos) object;
        if ((this.idRecursosHidricos == null && other.idRecursosHidricos != null) || (this.idRecursosHidricos != null && !this.idRecursosHidricos.equals(other.idRecursosHidricos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.RecursosHidricos[idRecursosHidricos=" + idRecursosHidricos + "]";
    }
}
