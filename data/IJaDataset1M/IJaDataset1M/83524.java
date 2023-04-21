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
@Table(name = "CMAILDOMAIN")
@NamedQueries({ @NamedQuery(name = "Cmaildomain.findAll", query = "SELECT c FROM Cmaildomain c"), @NamedQuery(name = "Cmaildomain.findByMadId", query = "SELECT c FROM Cmaildomain c WHERE c.madId = :madId"), @NamedQuery(name = "Cmaildomain.findByMadName", query = "SELECT c FROM Cmaildomain c WHERE c.madName = :madName"), @NamedQuery(name = "Cmaildomain.findByCreator", query = "SELECT c FROM Cmaildomain c WHERE c.creator = :creator"), @NamedQuery(name = "Cmaildomain.findByCreated", query = "SELECT c FROM Cmaildomain c WHERE c.created = :created") })
public class Cmaildomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "MAD_ID")
    private BigDecimal madId;

    @Basic(optional = false)
    @Column(name = "MAD_NAME")
    private String madName;

    @Basic(optional = false)
    @Column(name = "CREATOR")
    private BigInteger creator;

    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "madId")
    private Collection<Smailstate> smailstateCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "madId")
    private Collection<Tmailalias> tmailaliasCollection;

    public Cmaildomain() {
    }

    public Cmaildomain(BigDecimal madId) {
        this.madId = madId;
    }

    public Cmaildomain(BigDecimal madId, String madName, BigInteger creator, Date created) {
        this.madId = madId;
        this.madName = madName;
        this.creator = creator;
        this.created = created;
    }

    public BigDecimal getMadId() {
        return madId;
    }

    public void setMadId(BigDecimal madId) {
        this.madId = madId;
    }

    public String getMadName() {
        return madName;
    }

    public void setMadName(String madName) {
        this.madName = madName;
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

    public Collection<Smailstate> getSmailstateCollection() {
        return smailstateCollection;
    }

    public void setSmailstateCollection(Collection<Smailstate> smailstateCollection) {
        this.smailstateCollection = smailstateCollection;
    }

    public Collection<Tmailalias> getTmailaliasCollection() {
        return tmailaliasCollection;
    }

    public void setTmailaliasCollection(Collection<Tmailalias> tmailaliasCollection) {
        this.tmailaliasCollection = tmailaliasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (madId != null ? madId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cmaildomain)) {
            return false;
        }
        Cmaildomain other = (Cmaildomain) object;
        if ((this.madId == null && other.madId != null) || (this.madId != null && !this.madId.equals(other.madId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Cmaildomain[madId=" + madId + "]";
    }
}
