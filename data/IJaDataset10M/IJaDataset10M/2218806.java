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
@Table(name = "CSERVERACCOUNTTYPE")
@NamedQueries({ @NamedQuery(name = "Cserveraccounttype.findAll", query = "SELECT c FROM Cserveraccounttype c"), @NamedQuery(name = "Cserveraccounttype.findBySstId", query = "SELECT c FROM Cserveraccounttype c WHERE c.sstId = :sstId"), @NamedQuery(name = "Cserveraccounttype.findBySstName", query = "SELECT c FROM Cserveraccounttype c WHERE c.sstName = :sstName"), @NamedQuery(name = "Cserveraccounttype.findBySstSystem", query = "SELECT c FROM Cserveraccounttype c WHERE c.sstSystem = :sstSystem"), @NamedQuery(name = "Cserveraccounttype.findByCreator", query = "SELECT c FROM Cserveraccounttype c WHERE c.creator = :creator"), @NamedQuery(name = "Cserveraccounttype.findByCreated", query = "SELECT c FROM Cserveraccounttype c WHERE c.created = :created") })
public class Cserveraccounttype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "SST_ID")
    private BigDecimal sstId;

    @Basic(optional = false)
    @Column(name = "SST_NAME")
    private String sstName;

    @Column(name = "SST_SYSTEM")
    private BigInteger sstSystem;

    @Basic(optional = false)
    @Column(name = "CREATOR")
    private BigInteger creator;

    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sstId")
    private Collection<Tserviceserveraccount> tserviceserveraccountCollection;

    public Cserveraccounttype() {
    }

    public Cserveraccounttype(BigDecimal sstId) {
        this.sstId = sstId;
    }

    public Cserveraccounttype(BigDecimal sstId, String sstName, BigInteger creator, Date created) {
        this.sstId = sstId;
        this.sstName = sstName;
        this.creator = creator;
        this.created = created;
    }

    public BigDecimal getSstId() {
        return sstId;
    }

    public void setSstId(BigDecimal sstId) {
        this.sstId = sstId;
    }

    public String getSstName() {
        return sstName;
    }

    public void setSstName(String sstName) {
        this.sstName = sstName;
    }

    public BigInteger getSstSystem() {
        return sstSystem;
    }

    public void setSstSystem(BigInteger sstSystem) {
        this.sstSystem = sstSystem;
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

    public Collection<Tserviceserveraccount> getTserviceserveraccountCollection() {
        return tserviceserveraccountCollection;
    }

    public void setTserviceserveraccountCollection(Collection<Tserviceserveraccount> tserviceserveraccountCollection) {
        this.tserviceserveraccountCollection = tserviceserveraccountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sstId != null ? sstId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cserveraccounttype)) {
            return false;
        }
        Cserveraccounttype other = (Cserveraccounttype) object;
        if ((this.sstId == null && other.sstId != null) || (this.sstId != null && !this.sstId.equals(other.sstId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Cserveraccounttype[sstId=" + sstId + "]";
    }
}
