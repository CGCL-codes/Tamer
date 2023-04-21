package cu.edu.cujae.biowh.parser.drugbank.entities;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the DrugBankTransporters entity
 * @author rvera
 * @version 1.0
 * @since Oct 6, 2011
 */
@Entity
@Table(name = "DrugBankTransporters")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DrugBankTransporters.findAll", query = "SELECT d FROM DrugBankTransporters d"), @NamedQuery(name = "DrugBankTransporters.findByWid", query = "SELECT d FROM DrugBankTransporters d WHERE d.wid = :wid"), @NamedQuery(name = "DrugBankTransporters.findByDrugBankWID", query = "SELECT d FROM DrugBankTransporters d WHERE d.drugBankWID = :drugBankWID"), @NamedQuery(name = "DrugBankTransporters.findByPartner", query = "SELECT d FROM DrugBankTransporters d WHERE d.partner = :partner"), @NamedQuery(name = "DrugBankTransporters.findByPosition", query = "SELECT d FROM DrugBankTransporters d WHERE d.position = :position") })
public class DrugBankTransporters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "DrugBank_WID")
    private long drugBankWID;

    @Basic(optional = false)
    @Column(name = "Partner")
    private int partner;

    @Column(name = "Position")
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "DrugBank_WID", insertable = false, unique = false, nullable = true, updatable = false)
    private DrugBank drugBank;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "drugBankTransporters")
    private Set<DrugBankTransportersRef> drugBankTransportersRef;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "drugBankTransporters")
    private Set<DrugBankTransportersActions> drugBankTransportersActions;

    public DrugBankTransporters() {
    }

    public DrugBankTransporters(Long wid) {
        this.wid = wid;
    }

    public DrugBankTransporters(Long wid, long drugBankWID, int partner) {
        this.wid = wid;
        this.drugBankWID = drugBankWID;
        this.partner = partner;
    }

    public Set<DrugBankTransportersActions> getDrugBankTransportersActions() {
        return drugBankTransportersActions;
    }

    public void setDrugBankTransportersActions(Set<DrugBankTransportersActions> drugBankTransportersActions) {
        this.drugBankTransportersActions = drugBankTransportersActions;
    }

    public Set<DrugBankTransportersRef> getDrugBankTransportersRef() {
        return drugBankTransportersRef;
    }

    public void setDrugBankTransportersRef(Set<DrugBankTransportersRef> drugBankTransportersRef) {
        this.drugBankTransportersRef = drugBankTransportersRef;
    }

    public DrugBank getDrugBank() {
        return drugBank;
    }

    public void setDrugBank(DrugBank drugBank) {
        this.drugBank = drugBank;
    }

    public Long getWid() {
        return wid;
    }

    public void setWid(Long wid) {
        this.wid = wid;
    }

    public long getDrugBankWID() {
        return drugBankWID;
    }

    public void setDrugBankWID(long drugBankWID) {
        this.drugBankWID = drugBankWID;
    }

    public int getPartner() {
        return partner;
    }

    public void setPartner(int partner) {
        this.partner = partner;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wid != null ? wid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DrugBankTransporters)) {
            return false;
        }
        DrugBankTransporters other = (DrugBankTransporters) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    public String toString() {
        Iterator it;
        StringBuilder pData = new StringBuilder();
        if (!getDrugBankTransportersRef().isEmpty()) {
            it = getDrugBankTransportersRef().iterator();
            while (it.hasNext()) {
                pData.append("\t").append(it.next()).append("\n");
            }
        }
        if (!getDrugBankTransportersActions().isEmpty()) {
            it = getDrugBankTransportersActions().iterator();
            while (it.hasNext()) {
                pData.append("\t").append(it.next()).append("\n");
            }
        }
        return "DrugBankTransporters{" + "wid=" + wid + ", drugBankWID=" + drugBankWID + ", partner=" + partner + ", position=" + position + "}\n" + pData;
    }
}
