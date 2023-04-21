package ch.supsi.ist.geoshield.data;

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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Milan Antonovic, Massimiliano Cannata
 */
@Entity
@Table(name = "spr_req", schema = "public", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_req_fk", "id_spr_fk" }) })
@NamedQueries({ @NamedQuery(name = "SprReq.findAll", query = "SELECT s FROM SprReq s"), @NamedQuery(name = "SprReq.findByIdSre", query = "SELECT s FROM SprReq s WHERE s.idSre = :idSre"), @NamedQuery(name = "SprReq.findByIdReqIdSpr", query = "SELECT s FROM SprReq s " + "WHERE s.idReqFk = :idReq " + "AND s.idSprFk = :idSpr") })
public class SprReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sre", nullable = false)
    private Integer idSre;

    @JoinColumn(name = "id_req_fk", referencedColumnName = "id_req", nullable = false)
    @ManyToOne(optional = false)
    private Requests idReqFk;

    @JoinColumn(name = "id_spr_fk", referencedColumnName = "id_spr", nullable = false)
    @ManyToOne(optional = false)
    private ServicesPermissions idSprFk;

    public SprReq() {
    }

    public SprReq(Integer idSre) {
        this.idSre = idSre;
    }

    public Integer getIdSre() {
        return idSre;
    }

    public void setIdSre(Integer idSre) {
        this.idSre = idSre;
    }

    public Requests getIdReqFk() {
        return idReqFk;
    }

    public void setIdReqFk(Requests idReqFk) {
        this.idReqFk = idReqFk;
    }

    public ServicesPermissions getIdSprFk() {
        return idSprFk;
    }

    public void setIdSprFk(ServicesPermissions idSprFk) {
        this.idSprFk = idSprFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSre != null ? idSre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SprReq)) {
            return false;
        }
        SprReq other = (SprReq) object;
        if ((this.idSre == null && other.idSre != null) || (this.idSre != null && !this.idSre.equals(other.idSre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.supsi.ist.geoshield.data.SprReq[idSre=" + idSre + "]";
    }
}
