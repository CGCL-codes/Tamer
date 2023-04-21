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
 * This class is the DrugBankExternalLinks entity
 * @author rvera
 * @version 1.0
 * @since Oct 6, 2011
 */
@Entity
@Table(name = "DrugBankExternalLinks")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DrugBankExternalLinks.findAll", query = "SELECT d FROM DrugBankExternalLinks d"), @NamedQuery(name = "DrugBankExternalLinks.findByWid", query = "SELECT d FROM DrugBankExternalLinks d WHERE d.wid = :wid"), @NamedQuery(name = "DrugBankExternalLinks.findByDrugBankWID", query = "SELECT d FROM DrugBankExternalLinks d WHERE d.drugBankWID = :drugBankWID"), @NamedQuery(name = "DrugBankExternalLinks.findByResource", query = "SELECT d FROM DrugBankExternalLinks d WHERE d.resource = :resource") })
public class DrugBankExternalLinks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "DrugBank_WID")
    private long drugBankWID;

    @Column(name = "Resource")
    private String resource;

    @Lob
    @Column(name = "URL")
    private String url;

    @ManyToOne
    @JoinColumn(name = "DrugBank_WID", insertable = false, unique = false, nullable = true, updatable = false)
    private DrugBank drugBank;

    public DrugBankExternalLinks() {
    }

    public DrugBankExternalLinks(Long wid) {
        this.wid = wid;
    }

    public DrugBankExternalLinks(Long wid, long drugBankWID) {
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wid != null ? wid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DrugBankExternalLinks)) {
            return false;
        }
        DrugBankExternalLinks other = (DrugBankExternalLinks) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "DrugBankExternalLinks{" + "wid=" + wid + ", drugBankWID=" + drugBankWID + ", resource=" + resource + ", url=" + url + '}';
    }
}
