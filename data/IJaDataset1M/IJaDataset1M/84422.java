package cu.edu.cujae.biowh.parser.kegg.entities;

import cu.edu.cujae.biowh.parser.kegg.KEGGTables;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the KEGG Compound entity
 * @author rvera
 * @version 1.0
 * @since Nov 17, 2011
 */
@Entity
@Table(name = "KEGGCompound")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "KEGGCompound.findAll", query = "SELECT k FROM KEGGCompound k"), @NamedQuery(name = "KEGGCompound.findByWid", query = "SELECT k FROM KEGGCompound k WHERE k.wid = :wid"), @NamedQuery(name = "KEGGCompound.findByEntry", query = "SELECT k FROM KEGGCompound k WHERE k.entry = :entry"), @NamedQuery(name = "KEGGCompound.findByMass", query = "SELECT k FROM KEGGCompound k WHERE k.mass = :mass"), @NamedQuery(name = "KEGGCompound.findByRemark", query = "SELECT k FROM KEGGCompound k WHERE k.remark = :remark"), @NamedQuery(name = "KEGGCompound.findByFormula", query = "SELECT k FROM KEGGCompound k WHERE k.formula = :formula"), @NamedQuery(name = "KEGGCompound.findByDataSetWID", query = "SELECT k FROM KEGGCompound k WHERE k.dataSetWID = :dataSetWID") })
public class KEGGCompound implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "WID")
    private Long wid;

    @Basic(optional = false)
    @Column(name = "Entry")
    private String entry;

    @Lob
    @Column(name = "Comment")
    private String comment;

    @Column(name = "Mass")
    private Float mass;

    @Column(name = "Remark")
    private String remark;

    @Column(name = "Formula")
    private String formula;

    @Basic(optional = false)
    @Column(name = "DataSet_WID")
    private long dataSetWID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kEGGCompound")
    @MapKey(name = "keggcompounddblinkPK")
    private Map<KEGGCompoundDBLinkPK, KEGGCompoundDBLink> kEGGCompoundDBLink;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kEGGCompound")
    private Set<KEGGCompoundName> kEGGCompoundName;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGREACTION_HAS_KEGGCOMPOUND_AS_PRODUCT, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGReaction_WID", referencedColumnName = "WID"))
    private Set<KEGGReaction> kEGGReactionAsProduct;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGREACTION_HAS_KEGGCOMPOUND_AS_SUBSTRATE, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGReaction_WID", referencedColumnName = "WID"))
    private Set<KEGGReaction> kEGGReactionAsSubstrate;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGENZYME_HAS_KEGGCOMPOUND_AS_COFACTOR, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGEnzyme_WID", referencedColumnName = "WID"))
    private Set<KEGGEnzyme> kEGGEnzymeAsCofactor;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGENZYME_HAS_KEGGCOMPOUND_AS_EFFECTOR, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGEnzyme_WID", referencedColumnName = "WID"))
    private Set<KEGGEnzyme> kEGGEnzymeAsEffector;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGENZYME_HAS_KEGGCOMPOUND_AS_INHIBITOR, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGEnzyme_WID", referencedColumnName = "WID"))
    private Set<KEGGEnzyme> kEGGEnzymeAsInhibitor;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGRPAIR_HAS_KEGGCOMPOUND, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGRPair_WID", referencedColumnName = "WID"))
    private Set<KEGGRPair> kEGGRPair;

    @ManyToMany
    @JoinTable(name = KEGGTables.KEGGCOMPOUND_HAS_KEGGPATHWAY, joinColumns = @JoinColumn(name = "KEGGCompound_WID", referencedColumnName = "WID"), inverseJoinColumns = @JoinColumn(name = "KEGGPathway_WID", referencedColumnName = "WID"))
    private Set<KEGGPathway> kEGGPathways;

    public KEGGCompound() {
    }

    public KEGGCompound(Long wid) {
        this.wid = wid;
    }

    public KEGGCompound(Long wid, String entry, long dataSetWID) {
        this.wid = wid;
        this.entry = entry;
        this.dataSetWID = dataSetWID;
    }

    public Set<KEGGPathway> getkEGGPathways() {
        return kEGGPathways;
    }

    public void setkEGGPathways(Set<KEGGPathway> kEGGPathways) {
        this.kEGGPathways = kEGGPathways;
    }

    public Set<KEGGRPair> getkEGGRPair() {
        return kEGGRPair;
    }

    public void setkEGGRPair(Set<KEGGRPair> kEGGRPair) {
        this.kEGGRPair = kEGGRPair;
    }

    public Set<KEGGEnzyme> getkEGGEnzymeAsCofactor() {
        return kEGGEnzymeAsCofactor;
    }

    public void setkEGGEnzymeAsCofactor(Set<KEGGEnzyme> kEGGEnzymeAsCofactor) {
        this.kEGGEnzymeAsCofactor = kEGGEnzymeAsCofactor;
    }

    public Set<KEGGEnzyme> getkEGGEnzymeAsEffector() {
        return kEGGEnzymeAsEffector;
    }

    public void setkEGGEnzymeAsEffector(Set<KEGGEnzyme> kEGGEnzymeAsEffector) {
        this.kEGGEnzymeAsEffector = kEGGEnzymeAsEffector;
    }

    public Set<KEGGEnzyme> getkEGGEnzymeAsInhibitor() {
        return kEGGEnzymeAsInhibitor;
    }

    public void setkEGGEnzymeAsInhibitor(Set<KEGGEnzyme> kEGGEnzymeAsInhibitor) {
        this.kEGGEnzymeAsInhibitor = kEGGEnzymeAsInhibitor;
    }

    public Set<KEGGReaction> getkEGGReactionAsProduct() {
        return kEGGReactionAsProduct;
    }

    public void setkEGGReactionAsProduct(Set<KEGGReaction> kEGGReactionAsProduct) {
        this.kEGGReactionAsProduct = kEGGReactionAsProduct;
    }

    public Set<KEGGReaction> getkEGGReactionAsSubstrate() {
        return kEGGReactionAsSubstrate;
    }

    public void setkEGGReactionAsSubstrate(Set<KEGGReaction> kEGGReactionAsSubstrate) {
        this.kEGGReactionAsSubstrate = kEGGReactionAsSubstrate;
    }

    public Map<KEGGCompoundDBLinkPK, KEGGCompoundDBLink> getkEGGCompoundDBLink() {
        return kEGGCompoundDBLink;
    }

    public void setkEGGCompoundDBLink(Map<KEGGCompoundDBLinkPK, KEGGCompoundDBLink> kEGGCompoundDBLink) {
        this.kEGGCompoundDBLink = kEGGCompoundDBLink;
    }

    public Set<KEGGCompoundName> getkEGGCompoundName() {
        return kEGGCompoundName;
    }

    public void setkEGGCompoundName(Set<KEGGCompoundName> kEGGCompoundName) {
        this.kEGGCompoundName = kEGGCompoundName;
    }

    public Long getWid() {
        return wid;
    }

    public void setWid(Long wid) {
        this.wid = wid;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getMass() {
        return mass;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
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
        if (!(object instanceof KEGGCompound)) {
            return false;
        }
        KEGGCompound other = (KEGGCompound) object;
        if ((this.wid == null && other.wid != null) || (this.wid != null && !this.wid.equals(other.wid))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "KEGGCompound{" + "wid=" + wid + ", entry=" + entry + ", comment=" + comment + ", mass=" + mass + ", remark=" + remark + ", formula=" + formula + ", dataSetWID=" + dataSetWID + '}';
    }
}
