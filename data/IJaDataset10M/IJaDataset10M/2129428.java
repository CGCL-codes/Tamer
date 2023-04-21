package cu.edu.cujae.biowh.parser.drugbank.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the DrugBankSecondAccessionNumbers entity
 * @author rvera
 * @version 1.0
 * @since Oct 6, 2011
 */
@Entity
@Table(name = "DrugBankSecondAccessionNumbers")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DrugBankSecondAccessionNumbers.findAll", query = "SELECT d FROM DrugBankSecondAccessionNumbers d"), @NamedQuery(name = "DrugBankSecondAccessionNumbers.findByWid", query = "SELECT d FROM DrugBankSecondAccessionNumbers d WHERE d.wid = :wid"), @NamedQuery(name = "DrugBankSecondAccessionNumbers.findByDrugBankWID", query = "SELECT d FROM DrugBankSecondAccessionNumbers d WHERE d.drugBankWID = :drugBankWID"), @NamedQuery(name = "DrugBankSecondAccessionNumbers.findByAccessionNumber", query = "SELECT d FROM DrugBankSecondAccessionNumbers d WHERE d.accessionNumber = :accessionNumber") })
public class DrugBankSecondAccessionNumbers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "DrugBank_WID")
    private long drugBankWID;

    @Column(name = "AccessionNumber")
    private String accessionNumber;

    @ManyToOne
    @JoinColumn(name = "DrugBank_WID", insertable = false, unique = false, nullable = true, updatable = false)
    private DrugBank drugBank;

    public DrugBankSecondAccessionNumbers() {
    }

    public DrugBankSecondAccessionNumbers(Long wid) {
        this.wid = wid;
    }

    public DrugBankSecondAccessionNumbers(Long wid, long drugBankWID) {
        this.wid = wid;
        this.drugBankWID = drugBankWID;
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

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wid != null ? wid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DrugBankSecondAccessionNumbers)) {
            return false;
        }
        DrugBankSecondAccessionNumbers other = (DrugBankSecondAccessionNumbers) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "DrugBankSecondAccessionNumbers{" + "wid=" + wid + ", drugBankWID=" + drugBankWID + ", accessionNumber=" + accessionNumber + '}';
    }
}
