package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TCASH")
@NamedQueries({ @NamedQuery(name = "Tcash.findAll", query = "SELECT t FROM Tcash t"), @NamedQuery(name = "Tcash.findByCasId", query = "SELECT t FROM Tcash t WHERE t.casId = :casId"), @NamedQuery(name = "Tcash.findByCasAmount", query = "SELECT t FROM Tcash t WHERE t.casAmount = :casAmount"), @NamedQuery(name = "Tcash.findByCreator", query = "SELECT t FROM Tcash t WHERE t.creator = :creator"), @NamedQuery(name = "Tcash.findByCreated", query = "SELECT t FROM Tcash t WHERE t.created = :created") })
public class Tcash implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "CAS_ID")
    private BigDecimal casId;

    @Basic(optional = false)
    @Column(name = "CAS_AMOUNT")
    private double casAmount;

    @Basic(optional = false)
    @Column(name = "CREATOR")
    private BigInteger creator;

    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @JoinColumn(name = "CAI_ID", referencedColumnName = "CAI_ID")
    @ManyToOne(optional = false)
    private Tcashitem caiId;

    public Tcash() {
    }

    public Tcash(BigDecimal casId) {
        this.casId = casId;
    }

    public Tcash(BigDecimal casId, double casAmount, BigInteger creator, Date created) {
        this.casId = casId;
        this.casAmount = casAmount;
        this.creator = creator;
        this.created = created;
    }

    public BigDecimal getCasId() {
        return casId;
    }

    public void setCasId(BigDecimal casId) {
        this.casId = casId;
    }

    public double getCasAmount() {
        return casAmount;
    }

    public void setCasAmount(double casAmount) {
        this.casAmount = casAmount;
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

    public Tcashitem getCaiId() {
        return caiId;
    }

    public void setCaiId(Tcashitem caiId) {
        this.caiId = caiId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (casId != null ? casId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tcash)) {
            return false;
        }
        Tcash other = (Tcash) object;
        if ((this.casId == null && other.casId != null) || (this.casId != null && !this.casId.equals(other.casId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Tcash[casId=" + casId + "]";
    }
}
