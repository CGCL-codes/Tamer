package cu.edu.cujae.biowh.parser.mif25.entities;

import java.io.Serializable;
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
 * This Class is the MIFOtherBibref entity
 * @author rvera
 * @version 1.0
 * @since Aug 18, 2011
 */
@Entity
@Table(name = "MIFOtherBibref")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "MIFOtherBibref.findAll", query = "SELECT m FROM MIFOtherBibref m"), @NamedQuery(name = "MIFOtherBibref.findByWid", query = "SELECT m FROM MIFOtherBibref m WHERE m.wid = :wid"), @NamedQuery(name = "MIFOtherBibref.findByMIFOtherWID", query = "SELECT m FROM MIFOtherBibref m WHERE m.mIFOtherWID = :mIFOtherWID"), @NamedQuery(name = "MIFOtherBibref.findByDataSetWID", query = "SELECT m FROM MIFOtherBibref m WHERE m.dataSetWID = :dataSetWID") })
public class MIFOtherBibref implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "MIFOtherWID")
    private long mIFOtherWID;

    @Basic(optional = false)
    @Column(name = "DataSetWID")
    private long dataSetWID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mifOtherBibref")
    private Set<MIFOtherXRefPubMed> mifOtherXRefPubMed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mifOtherBibref")
    private Set<MIFOtherAttribute> mifOtherAttribute;

    @ManyToOne
    @JoinColumn(name = "MIFOtherWID", insertable = false, unique = false, nullable = true, updatable = false)
    private MIFEntrySource mifEntrySource;

    @ManyToOne
    @JoinColumn(name = "MIFOtherWID", insertable = false, unique = false, nullable = true, updatable = false)
    private MIFEntryExperiment mifEntryExperiment;

    public MIFOtherBibref() {
    }

    public MIFOtherBibref(Long wid) {
        this.wid = wid;
    }

    public MIFOtherBibref(Long wid, long mIFOtherWID, long dataSetWID) {
        this.wid = wid;
        this.mIFOtherWID = mIFOtherWID;
        this.dataSetWID = dataSetWID;
    }

    public Set<MIFOtherAttribute> getMifOtherAttribute() {
        return mifOtherAttribute;
    }

    public void setMifOtherAttribute(Set<MIFOtherAttribute> mifOtherAttribute) {
        this.mifOtherAttribute = mifOtherAttribute;
    }

    public Set<MIFOtherXRefPubMed> getMifOtherXRefPubMed() {
        return mifOtherXRefPubMed;
    }

    public void setMifOtherXRefPubMed(Set<MIFOtherXRefPubMed> mifOtherXRefPubMed) {
        this.mifOtherXRefPubMed = mifOtherXRefPubMed;
    }

    public MIFEntryExperiment getMifEntryExperiment() {
        return mifEntryExperiment;
    }

    public void setMifEntryExperiment(MIFEntryExperiment mifEntryExperiment) {
        this.mifEntryExperiment = mifEntryExperiment;
    }

    public MIFEntrySource getMifEntrySource() {
        return mifEntrySource;
    }

    public void setMifEntrySource(MIFEntrySource mifEntrySource) {
        this.mifEntrySource = mifEntrySource;
    }

    public long getmIFOtherWID() {
        return mIFOtherWID;
    }

    public void setmIFOtherWID(long mIFOtherWID) {
        this.mIFOtherWID = mIFOtherWID;
    }

    public Long getWid() {
        return wid;
    }

    public void setWid(Long wid) {
        this.wid = wid;
    }

    public long getMIFOtherWID() {
        return mIFOtherWID;
    }

    public void setMIFOtherWID(long mIFOtherWID) {
        this.mIFOtherWID = mIFOtherWID;
    }

    public long getDataSetWID() {
        return dataSetWID;
    }

    public void setDataSetWID(long dataSetWID) {
        this.dataSetWID = dataSetWID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wid != null ? wid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MIFOtherBibref)) {
            return false;
        }
        MIFOtherBibref other = (MIFOtherBibref) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MIFOtherBibref{" + "wid=" + wid + ", mIFOtherWID=" + mIFOtherWID + ", dataSetWID=" + dataSetWID + '}';
    }
}
