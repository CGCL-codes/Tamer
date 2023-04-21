package cu.edu.cujae.biowh.parser.drugbank.entities;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the DrugBankGeneralRef entity
 * @author rvera
 * @version 1.0
 * @since Oct 6, 2011
 */
@Entity
@Table(name = "DrugBankGeneralRef")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DrugBankGeneralRef.findAll", query = "SELECT d FROM DrugBankGeneralRef d"), @NamedQuery(name = "DrugBankGeneralRef.findByWid", query = "SELECT d FROM DrugBankGeneralRef d WHERE d.wid = :wid"), @NamedQuery(name = "DrugBankGeneralRef.findByDrugBankWID", query = "SELECT d FROM DrugBankGeneralRef d WHERE d.drugBankWID = :drugBankWID") })
public class DrugBankGeneralRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "DrugBank_WID")
    private long drugBankWID;

    @Lob
    @Column(name = "Cite")
    private String cite;

    @Lob
    @Column(name = "Link")
    private String link;

    @ManyToOne
    @JoinColumn(name = "DrugBank_WID", insertable = false, unique = false, nullable = true, updatable = false)
    private DrugBank drugBank;

    public DrugBankGeneralRef() {
    }

    public DrugBankGeneralRef(Long wid) {
        this.wid = wid;
    }

    public DrugBankGeneralRef(Long wid, long drugBankWID) {
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

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wid != null ? wid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DrugBankGeneralRef)) {
            return false;
        }
        DrugBankGeneralRef other = (DrugBankGeneralRef) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "DrugBankGeneralRef{" + "wid=" + wid + ", drugBankWID=" + drugBankWID + ", cite=" + cite + ", link=" + link + '}';
    }
}
