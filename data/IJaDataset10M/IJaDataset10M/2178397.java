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
@Table(name = "CPORTTYPE")
@NamedQueries({ @NamedQuery(name = "Cporttype.findAll", query = "SELECT c FROM Cporttype c"), @NamedQuery(name = "Cporttype.findByPotId", query = "SELECT c FROM Cporttype c WHERE c.potId = :potId"), @NamedQuery(name = "Cporttype.findByPotDesc", query = "SELECT c FROM Cporttype c WHERE c.potDesc = :potDesc"), @NamedQuery(name = "Cporttype.findByCreator", query = "SELECT c FROM Cporttype c WHERE c.creator = :creator"), @NamedQuery(name = "Cporttype.findByCreated", query = "SELECT c FROM Cporttype c WHERE c.created = :created") })
public class Cporttype implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "POT_ID")
    private BigDecimal potId;

    @Basic(optional = false)
    @Column(name = "POT_DESC")
    private String potDesc;

    @Basic(optional = false)
    @Column(name = "CREATOR")
    private BigInteger creator;

    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "potId")
    private Collection<Tport> tportCollection;

    public Cporttype() {
    }

    public Cporttype(BigDecimal potId) {
        this.potId = potId;
    }

    public Cporttype(BigDecimal potId, String potDesc, BigInteger creator, Date created) {
        this.potId = potId;
        this.potDesc = potDesc;
        this.creator = creator;
        this.created = created;
    }

    public BigDecimal getPotId() {
        return potId;
    }

    public void setPotId(BigDecimal potId) {
        this.potId = potId;
    }

    public String getPotDesc() {
        return potDesc;
    }

    public void setPotDesc(String potDesc) {
        this.potDesc = potDesc;
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

    public Collection<Tport> getTportCollection() {
        return tportCollection;
    }

    public void setTportCollection(Collection<Tport> tportCollection) {
        this.tportCollection = tportCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (potId != null ? potId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cporttype)) {
            return false;
        }
        Cporttype other = (Cporttype) object;
        if ((this.potId == null && other.potId != null) || (this.potId != null && !this.potId.equals(other.potId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Cporttype[potId=" + potId + "]";
    }
}
