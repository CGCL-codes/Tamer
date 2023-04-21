package br.com.brasilsemchamas.entidade.relatorio;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author lellis
 */
@Entity
@Table(name = "novidades_p&d")
@NamedQueries({ @NamedQuery(name = "NovidadesPD.findAll", query = "SELECT n FROM NovidadesPD n"), @NamedQuery(name = "NovidadesPD.findByPkId", query = "SELECT n FROM NovidadesPD n WHERE n.pkId = :pkId"), @NamedQuery(name = "NovidadesPD.findByUrl", query = "SELECT n FROM NovidadesPD n WHERE n.url = :url") })
public class NovidadesPD implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "pk_id")
    private Integer pkId;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "url")
    private String url;

    @JoinColumn(name = "FK_sistema", referencedColumnName = "pk_id")
    @ManyToOne(optional = false)
    private Sistema fKsistema;

    public NovidadesPD() {
    }

    public NovidadesPD(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Sistema getFKsistema() {
        return fKsistema;
    }

    public void setFKsistema(Sistema fKsistema) {
        this.fKsistema = fKsistema;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkId != null ? pkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NovidadesPD)) {
            return false;
        }
        NovidadesPD other = (NovidadesPD) object;
        if ((this.pkId == null && other.pkId != null) || (this.pkId != null && !this.pkId.equals(other.pkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.NovidadesPD[pkId=" + pkId + "]";
    }
}
