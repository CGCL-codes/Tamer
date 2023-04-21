package org.ras.bin.checklister.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author GZSVM
 */
@Entity
@Table(name = "tax_species_name_list")
public class SpeciesName implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_SIGNATURE = "TSN";

    @Id
    @Column(name = "species_name_id")
    private String speciesNameId;

    @JoinColumn(name = "genus_name_id", referencedColumnName = "genus_name_id")
    @ManyToOne
    private GenusName genusNameId;

    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    @ManyToOne
    private Publication publication;

    @JoinColumn(name = "species_name_uplink", referencedColumnName = "species_name_id")
    @ManyToOne
    private SpeciesName speciesNameUplink;

    @JoinColumn(name = "basionym_name_id", referencedColumnName = "species_name_id")
    @ManyToOne
    private SpeciesName basionymNameId;

    @Column(name = "species_name_rank")
    private String speciesNameRank;

    @Column(name = "species_name_hybrid_sign")
    private String speciesNameHybridSign;

    @Column(name = "species_name_name")
    private String speciesNameName;

    @Column(name = "species_name_authority")
    private String speciesNameAuthority;

    @Column(name = "species_name_authority_stack")
    private String speciesNameAuthorityStack;

    @Column(name = "species_name_year")
    private String speciesNameYear;

    @Column(name = "species_name_citation")
    private String speciesNameCitation;

    @Column(name = "species_name_coords")
    private String speciesNameCoords;

    @Column(name = "species_name_note")
    private String speciesNameNote;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "speciesName")
    private Collection<VernacularName> vernacularNameCollection;

    public String getSpeciesNameId() {
        return speciesNameId;
    }

    public void setSpeciesNameId(String _SpeciesNameId) {
        speciesNameId = _SpeciesNameId;
    }

    public GenusName getGenusNameId() {
        return genusNameId;
    }

    public void setGenusNameId(GenusName _GenusNameId) {
        genusNameId = _GenusNameId;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication _Publication) {
        publication = _Publication;
    }

    public SpeciesName getSpeciesNameUplink() {
        return speciesNameUplink;
    }

    public void setSpeciesNameUplink(SpeciesName _SpeciesNameUplink) {
        speciesNameUplink = _SpeciesNameUplink;
    }

    public SpeciesName getBasionymNameId() {
        return basionymNameId;
    }

    public void setBasionymNameId(SpeciesName _BasionymNameId) {
        basionymNameId = _BasionymNameId;
    }

    public String getSpeciesNameRank() {
        return speciesNameRank;
    }

    public void setSpeciesNameRank(String _SpeciesNameRank) {
        speciesNameRank = _SpeciesNameRank;
    }

    public String getSpeciesNameHybridSign() {
        return speciesNameHybridSign;
    }

    public void setSpeciesNameHybridSign(String _SpeciesNameHybridSign) {
        speciesNameHybridSign = _SpeciesNameHybridSign;
    }

    public String getSpeciesNameName() {
        return speciesNameName;
    }

    public void setSpeciesNameName(String _SpeciesNameName) {
        speciesNameName = _SpeciesNameName;
    }

    public String getSpeciesNameAuthority() {
        return speciesNameAuthority;
    }

    public void setSpeciesNameAuthority(String _SpeciesNameAuthority) {
        speciesNameAuthority = _SpeciesNameAuthority;
    }

    public String getSpeciesNameAuthorityStack() {
        return speciesNameAuthorityStack;
    }

    public void setSpeciesNameAuthorityStack(String speciesNameAuthorityStack) {
        this.speciesNameAuthorityStack = speciesNameAuthorityStack;
    }

    public String getSpeciesNameYear() {
        return speciesNameYear;
    }

    public void setSpeciesNameYear(String _SpeciesNameYear) {
        speciesNameYear = _SpeciesNameYear;
    }

    public String getSpeciesNameCitation() {
        return speciesNameCitation;
    }

    public void setSpeciesNameCitation(String _SpeciesNameCitation) {
        speciesNameCitation = _SpeciesNameCitation;
    }

    public String getSpeciesNameCoords() {
        return speciesNameCoords;
    }

    public void setSpeciesNameCoords(String _SpeciesNameCoords) {
        speciesNameCoords = _SpeciesNameCoords;
    }

    public String getSpeciesNameNote() {
        return speciesNameNote;
    }

    public void setSpeciesNameNote(String _SpeciesNameNote) {
        speciesNameNote = _SpeciesNameNote;
    }

    public Collection<VernacularName> getVernacularNameCollection() {
        return vernacularNameCollection;
    }

    public void setVernacularNameCollection(Collection<VernacularName> vernacularNameCollection) {
        this.vernacularNameCollection = vernacularNameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (speciesNameId != null ? speciesNameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SpeciesName)) {
            return false;
        }
        SpeciesName other = (SpeciesName) object;
        if ((this.speciesNameId == null && other.speciesNameId != null) || (this.speciesNameId != null && !this.speciesNameId.equalsIgnoreCase(other.speciesNameId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test_dbdesktopapplication.tax.SpeciesName[id=" + speciesNameId + "] (" + speciesNameName + ")";
    }

    public String getSpeciesNamePresentation() {
        String st = "<html>" + "<font color=\"gray\">" + "[" + genusNameId.getFamilyId().getFamilySignature() + "] <b>" + genusNameId.getFamilyId().getFamilyName() + "</b> " + genusNameId.getFamilyId().getFamilyAuthority() + "</font>" + "<br>" + getNameHtmlPresentation("b");
        return st;
    }

    protected String getHybridSignPresentation() {
        if ((this.speciesNameHybridSign == null) || (this.speciesNameHybridSign.equals(""))) {
            return "";
        } else {
            return "x ";
        }
    }

    public String getAcceptedNameHtmlPresentation() {
        return getNameHtmlPresentation("b");
    }

    public String getSynonymNameHtmlPresentation() {
        return getNameHtmlPresentation("i");
    }

    public String getNameHtmlPresentation(String _Mark) {
        String st = "<" + _Mark + ">" + genusNameId.getHybridSignPresentation() + genusNameId.getGenusNameName() + "</" + _Mark + "> " + "<" + _Mark + ">" + getHybridSignPresentation() + "</" + _Mark + "> " + "";
        if (this.equals(speciesNameUplink)) {
            st = st + "<" + _Mark + ">" + speciesNameName + "</" + _Mark + ">" + " ";
        } else {
            st = st + "<" + _Mark + ">" + this.speciesNameUplink.speciesNameName + "</" + _Mark + ">" + " " + speciesNameRank + " " + "<" + _Mark + ">" + speciesNameName + "</" + _Mark + ">" + " ";
        }
        if ((basionymNameId != null) && (!basionymNameId.speciesNameId.equals("[none]"))) {
            st = st + "(" + basionymNameId.speciesNameAuthority + ")" + " ";
        }
        st = st + speciesNameAuthority + "";
        return st;
    }
}
