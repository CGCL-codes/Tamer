package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author petr
 */
@Entity
@Table(name = "SACCOUNTSTATE")
@NamedQueries({ @NamedQuery(name = "Saccountstate.findAll", query = "SELECT s FROM Saccountstate s"), @NamedQuery(name = "Saccountstate.findBySasId", query = "SELECT s FROM Saccountstate s WHERE s.sasId = :sasId"), @NamedQuery(name = "Saccountstate.findBySrvId", query = "SELECT s FROM Saccountstate s WHERE s.srvId = :srvId"), @NamedQuery(name = "Saccountstate.findByAccId", query = "SELECT s FROM Saccountstate s WHERE s.accId = :accId"), @NamedQuery(name = "Saccountstate.findByAccName", query = "SELECT s FROM Saccountstate s WHERE s.accName = :accName"), @NamedQuery(name = "Saccountstate.findByActId", query = "SELECT s FROM Saccountstate s WHERE s.actId = :actId"), @NamedQuery(name = "Saccountstate.findBySasTodo", query = "SELECT s FROM Saccountstate s WHERE s.sasTodo = :sasTodo"), @NamedQuery(name = "Saccountstate.findBySasActive", query = "SELECT s FROM Saccountstate s WHERE s.sasActive = :sasActive"), @NamedQuery(name = "Saccountstate.findByCreator", query = "SELECT s FROM Saccountstate s WHERE s.creator = :creator"), @NamedQuery(name = "Saccountstate.findByCreated", query = "SELECT s FROM Saccountstate s WHERE s.created = :created") })
public class Saccountstate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "SAS_ID")
    private BigDecimal sasId;

    @Basic(optional = false)
    @Column(name = "SRV_ID")
    private BigInteger srvId;

    @Basic(optional = false)
    @Column(name = "ACC_ID")
    private BigInteger accId;

    @Basic(optional = false)
    @Column(name = "ACC_NAME")
    private String accName;

    @Basic(optional = false)
    @Column(name = "ACT_ID")
    private BigInteger actId;

    @Basic(optional = false)
    @Column(name = "SAS_TODO")
    private BigInteger sasTodo;

    @Basic(optional = false)
    @Column(name = "SAS_ACTIVE")
    private BigInteger sasActive;

    @Basic(optional = false)
    @Column(name = "CREATOR")
    private BigInteger creator;

    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    public Saccountstate() {
    }

    public Saccountstate(BigDecimal sasId) {
        this.sasId = sasId;
    }

    public Saccountstate(BigDecimal sasId, BigInteger srvId, BigInteger accId, String accName, BigInteger actId, BigInteger sasTodo, BigInteger sasActive, BigInteger creator, Date created) {
        this.sasId = sasId;
        this.srvId = srvId;
        this.accId = accId;
        this.accName = accName;
        this.actId = actId;
        this.sasTodo = sasTodo;
        this.sasActive = sasActive;
        this.creator = creator;
        this.created = created;
    }

    public BigDecimal getSasId() {
        return sasId;
    }

    public void setSasId(BigDecimal sasId) {
        this.sasId = sasId;
    }

    public BigInteger getSrvId() {
        return srvId;
    }

    public void setSrvId(BigInteger srvId) {
        this.srvId = srvId;
    }

    public BigInteger getAccId() {
        return accId;
    }

    public void setAccId(BigInteger accId) {
        this.accId = accId;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public BigInteger getActId() {
        return actId;
    }

    public void setActId(BigInteger actId) {
        this.actId = actId;
    }

    public BigInteger getSasTodo() {
        return sasTodo;
    }

    public void setSasTodo(BigInteger sasTodo) {
        this.sasTodo = sasTodo;
    }

    public BigInteger getSasActive() {
        return sasActive;
    }

    public void setSasActive(BigInteger sasActive) {
        this.sasActive = sasActive;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sasId != null ? sasId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Saccountstate)) {
            return false;
        }
        Saccountstate other = (Saccountstate) object;
        if ((this.sasId == null && other.sasId != null) || (this.sasId != null && !this.sasId.equals(other.sasId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Saccountstate[sasId=" + sasId + "]";
    }
}
