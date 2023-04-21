package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author petr
 */
@Entity
@Table(name = "CROOMADMINTYPE")
@NamedQueries({ @NamedQuery(name = "Croomadmintype.findAll", query = "SELECT c FROM Croomadmintype c"), @NamedQuery(name = "Croomadmintype.findByRatId", query = "SELECT c FROM Croomadmintype c WHERE c.ratId = :ratId"), @NamedQuery(name = "Croomadmintype.findByRatDesc", query = "SELECT c FROM Croomadmintype c WHERE c.ratDesc = :ratDesc"), @NamedQuery(name = "Croomadmintype.findByCreator", query = "SELECT c FROM Croomadmintype c WHERE c.creator = :creator"), @NamedQuery(name = "Croomadmintype.findByCreated", query = "SELECT c FROM Croomadmintype c WHERE c.created = :created") })
public class Croomadmintype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "RAT_ID")
    private BigDecimal ratId;

    @Basic(optional = false)
    @Column(name = "RAT_DESC")
    private String ratDesc;

    @Basic(optional = false)
    @Column(name = "CREATOR")
    private BigInteger creator;

    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ratId")
    private Collection<Troomadmin> troomadminCollection;

    public Croomadmintype() {
    }

    public Croomadmintype(BigDecimal ratId) {
        this.ratId = ratId;
    }

    public Croomadmintype(BigDecimal ratId, String ratDesc, BigInteger creator, Date created) {
        this.ratId = ratId;
        this.ratDesc = ratDesc;
        this.creator = creator;
        this.created = created;
    }

    public BigDecimal getRatId() {
        return ratId;
    }

    public void setRatId(BigDecimal ratId) {
        this.ratId = ratId;
    }

    public String getRatDesc() {
        return ratDesc;
    }

    public void setRatDesc(String ratDesc) {
        this.ratDesc = ratDesc;
    }

    public BigInteger getCreator() {
        return creator;
    }

    public void setCreator(BigInteger creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Collection<Troomadmin> getTroomadminCollection() {
        return troomadminCollection;
    }

    public void setTroomadminCollection(Collection<Troomadmin> troomadminCollection) {
        this.troomadminCollection = troomadminCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ratId != null ? ratId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Croomadmintype)) {
            return false;
        }
        Croomadmintype other = (Croomadmintype) object;
        if ((this.ratId == null && other.ratId != null) || (this.ratId != null && !this.ratId.equals(other.ratId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Croomadmintype[ratId=" + ratId + "]";
    }
}
