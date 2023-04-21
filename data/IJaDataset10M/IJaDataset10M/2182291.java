package cu.edu.cujae.biowh.parser.mif25.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This Class is the MIFEntrySource entity
 * @author rvera
 * @version 1.0
 * @since Aug 18, 2011
 */
@Entity
@Table(name = "MIFEntrySource")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "MIFEntrySource.findAll", query = "SELECT m FROM MIFEntrySource m"), @NamedQuery(name = "MIFEntrySource.findByWid", query = "SELECT m FROM MIFEntrySource m WHERE m.wid = :wid"), @NamedQuery(name = "MIFEntrySource.findByMIFEntrySetEntryWID", query = "SELECT m FROM MIFEntrySource m WHERE m.mIFEntrySetEntryWID = :mIFEntrySetEntryWID"), @NamedQuery(name = "MIFEntrySource.findByReleaseValue", query = "SELECT m FROM MIFEntrySource m WHERE m.releaseValue = :releaseValue"), @NamedQuery(name = "MIFEntrySource.findByReleaseDate", query = "SELECT m FROM MIFEntrySource m WHERE m.releaseDate = :releaseDate"), @NamedQuery(name = "MIFEntrySource.findByShortLabel", query = "SELECT m FROM MIFEntrySource m WHERE m.shortLabel = :shortLabel"), @NamedQuery(name = "MIFEntrySource.findByFullName", query = "SELECT m FROM MIFEntrySource m WHERE m.fullName = :fullName") })
public class MIFEntrySource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "MIFEntrySetEntry_WID")
    private long mIFEntrySetEntryWID;

    @Column(name = "ReleaseValue")
    private String releaseValue;

    @Column(name = "ReleaseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    @Column(name = "ShortLabel")
    private String shortLabel;

    @Column(name = "FullName")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "MIFEntrySetEntry_WID", insertable = false, unique = false, nullable = true, updatable = false)
    private MIFEntrySetEntry mIFEntrySetEntry;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mifEntrySource")
    private Set<MIFOtherAlias> mIFOtherAlias;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mifEntrySource")
    private Set<MIFOtherBibref> mIFOtherBibRef;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mifEntrySource")
    private Set<MIFOtherXRef> mIFOtherXRef;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mifEntrySource")
    private Set<MIFOtherAttribute> mIFOtherAttribute;

    public MIFEntrySource() {
    }

    public MIFEntrySource(Long wid) {
        this.wid = wid;
    }

    public MIFEntrySource(Long wid, long mIFEntrySetEntryWID) {
        this.wid = wid;
        this.mIFEntrySetEntryWID = mIFEntrySetEntryWID;
    }

    public Set<MIFOtherAlias> getmIFOtherAlias() {
        return mIFOtherAlias;
    }

    public void setmIFOtherAlias(Set<MIFOtherAlias> mIFOtherAlias) {
        this.mIFOtherAlias = mIFOtherAlias;
    }

    public Set<MIFOtherAttribute> getmIFOtherAttribute() {
        return mIFOtherAttribute;
    }

    public void setmIFOtherAttribute(Set<MIFOtherAttribute> mIFOtherAttribute) {
        this.mIFOtherAttribute = mIFOtherAttribute;
    }

    public Set<MIFOtherBibref> getmIFOtherBibRef() {
        return mIFOtherBibRef;
    }

    public void setmIFOtherBibRef(Set<MIFOtherBibref> mIFOtherBibRef) {
        this.mIFOtherBibRef = mIFOtherBibRef;
    }

    public Set<MIFOtherXRef> getmIFOtherXRef() {
        return mIFOtherXRef;
    }

    public void setmIFOtherXRef(Set<MIFOtherXRef> mIFOtherXRef) {
        this.mIFOtherXRef = mIFOtherXRef;
    }

    public MIFEntrySetEntry getmIFEntrySetEntry() {
        return mIFEntrySetEntry;
    }

    public void setmIFEntrySetEntry(MIFEntrySetEntry mIFEntrySetEntry) {
        this.mIFEntrySetEntry = mIFEntrySetEntry;
    }

    public long getmIFEntrySetEntryWID() {
        return mIFEntrySetEntryWID;
    }

    public void setmIFEntrySetEntryWID(long mIFEntrySetEntryWID) {
        this.mIFEntrySetEntryWID = mIFEntrySetEntryWID;
    }

    public Long getWid() {
        return wid;
    }

    public void setWid(Long wid) {
        this.wid = wid;
    }

    public long getMIFEntrySetEntryWID() {
        return mIFEntrySetEntryWID;
    }

    public void setMIFEntrySetEntryWID(long mIFEntrySetEntryWID) {
        this.mIFEntrySetEntryWID = mIFEntrySetEntryWID;
    }

    public String getReleaseValue() {
        return releaseValue;
    }

    public void setReleaseValue(String releaseValue) {
        this.releaseValue = releaseValue;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wid != null ? wid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MIFEntrySource)) {
            return false;
        }
        MIFEntrySource other = (MIFEntrySource) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MIFEntrySource{" + "wid=" + wid + ", mIFEntrySetEntryWID=" + mIFEntrySetEntryWID + ", releaseValue=" + releaseValue + ", releaseDate=" + releaseDate + ", shortLabel=" + shortLabel + ", fullName=" + fullName + '}';
    }
}
