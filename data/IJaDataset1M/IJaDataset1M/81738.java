package br.com.brasilsemchamas.entidade.relatorio;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author lellis
 */
@Entity
@Table(name = "ambiente_afetado")
@NamedQueries({ @NamedQuery(name = "AmbienteAfetado.findAll", query = "SELECT a FROM AmbienteAfetado a"), @NamedQuery(name = "AmbienteAfetado.findByPkId", query = "SELECT a FROM AmbienteAfetado a WHERE a.pkId = :pkId"), @NamedQuery(name = "AmbienteAfetado.findByAgua", query = "SELECT a FROM AmbienteAfetado a WHERE a.agua = :agua"), @NamedQuery(name = "AmbienteAfetado.findByAr", query = "SELECT a FROM AmbienteAfetado a WHERE a.ar = :ar"), @NamedQuery(name = "AmbienteAfetado.findBySolo", query = "SELECT a FROM AmbienteAfetado a WHERE a.solo = :solo"), @NamedQuery(name = "AmbienteAfetado.findByInformacoesAdicionais", query = "SELECT a FROM AmbienteAfetado a WHERE a.informacoesAdicionais = :informacoesAdicionais") })
public class AmbienteAfetado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_id")
    private Integer pkId;

    @Column(name = "agua")
    private Boolean agua;

    @Column(name = "ar")
    private Boolean ar;

    @Column(name = "solo")
    private Boolean solo;

    @Column(name = "informacoes_adicionais")
    private String informacoesAdicionais;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fKambienteafetado")
    private Collection<ProdutoPerigoso> produtoPerigosoCollection;

    public AmbienteAfetado() {
    }

    public AmbienteAfetado(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public Boolean getAgua() {
        return agua;
    }

    public void setAgua(Boolean agua) {
        this.agua = agua;
    }

    public Boolean getAr() {
        return ar;
    }

    public void setAr(Boolean ar) {
        this.ar = ar;
    }

    public Boolean getSolo() {
        return solo;
    }

    public void setSolo(Boolean solo) {
        this.solo = solo;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public Collection<ProdutoPerigoso> getProdutoPerigosoCollection() {
        return produtoPerigosoCollection;
    }

    public void setProdutoPerigosoCollection(Collection<ProdutoPerigoso> produtoPerigosoCollection) {
        this.produtoPerigosoCollection = produtoPerigosoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkId != null ? pkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AmbienteAfetado)) {
            return false;
        }
        AmbienteAfetado other = (AmbienteAfetado) object;
        if ((this.pkId == null && other.pkId != null) || (this.pkId != null && !this.pkId.equals(other.pkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.AmbienteAfetado[pkId=" + pkId + "]";
    }
}
