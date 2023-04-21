package org.ras.bin.checklister.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "tax_genus_name_list")
public class GenusName implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_SIGNATURE = "TGN";

    @Id
    @Column(name = "genus_name_id")
    private String genusNameId;

    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    @ManyToOne
    private Publication publication;

    @Column(name = "genus_name_hybrid_sign")
    private String genusNameHybridSign;

    @Column(name = "genus_name_name")
    private String genusNameName;

    @Column(name = "genus_name_authority")
    private String genusNameAuthority;

    @Column(name = "genus_name_authority_stack")
    private String genusNameAuthorityStack;

    @Column(name = "genus_name_year")
    private String genusNameYear;

    @Column(name = "genus_name_citation")
    private String genusNameCitation;

    @Column(name = "genus_name_coords")
    private String genusNameCoords;

    @Column(name = "genus_name_note")
    private String genusNameNote;

    @JoinColumn(name = "family_id", referencedColumnName = "family_id")
    @ManyToOne
    private FamilyName familyId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genusNameId")
    private Collection<SpeciesName> speciesNameCollection;

    public String getGenusNameId() {
        return genusNameId;
    }

    public void setGenusNameId(String _GenusNameId) {
        genusNameId = _GenusNameId;
    }

    public FamilyName getFamilyId() {
        return familyId;
    }

    public void setFamilyId(FamilyName _FamilyId) {
        familyId = _FamilyId;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication _Publication) {
        publication = _Publication;
    }

    public String getGenusNameHybridSign() {
        return genusNameHybridSign;
    }

    public void setGenusNameHybridSign(String _GenusNameHybridSign) {
        genusNameHybridSign = _GenusNameHybridSign;
    }

    public String getGenusNameName() {
        return genusNameName;
    }

    public void setGenusNameName(String _GenusNameName) {
        genusNameName = _GenusNameName;
    }

    public String getGenusNameAuthority() {
        return genusNameAuthority;
    }

    public void setGenusNameAuthority(String _GenusNameAuthority) {
        genusNameAuthority = _GenusNameAuthority;
    }

    public String getGenusNameAuthorityStack() {
        return genusNameAuthorityStack;
    }

    public void setGenusNameAuthorityStack(String genusNameAuthorityStack) {
        this.genusNameAuthorityStack = genusNameAuthorityStack;
    }

    public String getGenusNameYear() {
        return genusNameYear;
    }

    public void setGenusNameYear(String _GenusNameYear) {
        genusNameYear = _GenusNameYear;
    }

    public String getGenusNameCitation() {
        return genusNameCitation;
    }

    public void setGenusNameCitation(String _GenusNameCitation) {
        genusNameCitation = _GenusNameCitation;
    }

    public String getGenusNameCoords() {
        return genusNameCoords;
    }

    public void setGenusNameCoords(String _GenusNameCoords) {
        genusNameCoords = _GenusNameCoords;
    }

    public String getGenusNameNote() {
        return genusNameNote;
    }

    public void setGenusNameNote(String _GenusNameNote) {
        genusNameNote = _GenusNameNote;
    }

    public Collection<SpeciesName> getSpeciesNameCollection() {
        return speciesNameCollection;
    }

    public void setSpeciesNameCollection(Collection<SpeciesName> _SpeciesNameCollection) {
        speciesNameCollection = _SpeciesNameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genusNameId != null ? genusNameId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GenusName)) {
            return false;
        }
        GenusName other = (GenusName) object;
        if ((this.genusNameId == null && other.genusNameId != null) || (this.genusNameId != null && !this.genusNameId.equalsIgnoreCase(other.genusNameId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test_dbdesktopapplication.tax.GenusName[id=" + genusNameId + "] (" + genusNameName + ")";
    }

    public String getGenusNamePresentation() {
        return "<html>" + "<font color=\"gray\">" + "[" + familyId.getFamilySignature() + "] <b>" + familyId.getFamilyName() + "</b> " + familyId.getFamilyAuthority() + "</font>" + "<br>" + "<b>" + getHybridSignPresentation() + genusNameName + "</b> " + genusNameAuthority + "";
    }

    protected String getHybridSignPresentation() {
        if ((genusNameHybridSign == null) || (genusNameHybridSign.equals(""))) {
            return "";
        } else {
            return "x";
        }
    }
}
