package org.ras.bin.checklister.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author GZSVM
 */
@Entity
@Table(name = "cl_genus_synonyms")
public class GenusSynonym implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_SIGNATURE = "CLGS";

    @Id
    @Column(name = "cl_genus_synonym_id")
    private String genusSynonymId;

    @JoinColumn(name = "cl_genus_id", referencedColumnName = "cl_genus_id")
    @ManyToOne
    private Genus genusId;

    @JoinColumn(name = "genus_name_id", referencedColumnName = "genus_name_id")
    @ManyToOne
    private GenusName genusNameId;

    @Column(name = "cl_genus_synonym_type")
    private String genusSynonymType;

    @Column(name = "publication_id")
    private String publicationId;

    @Column(name = "cl_genus_synonym_coords")
    private String genusSynonymCoords;

    @Column(name = "cl_genus_synonym_note")
    private String genusSynonymNote;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genusSynonymId != null ? genusSynonymId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GenusSynonym)) {
            return false;
        }
        GenusSynonym other = (GenusSynonym) object;
        if ((this.genusSynonymId == null && other.genusSynonymId != null) || (this.genusSynonymId != null && !this.genusSynonymId.equals(other.genusSynonymId))) {
            return false;
        }
        return true;
    }

    public Genus getGenusId() {
        return genusId;
    }

    public void setGenusId(Genus genusId) {
        this.genusId = genusId;
    }

    public GenusName getGenusNameId() {
        return genusNameId;
    }

    public void setGenusNameId(GenusName genusNameId) {
        this.genusNameId = genusNameId;
    }

    public String getGenusSynonymCoords() {
        return genusSynonymCoords;
    }

    public void setGenusSynonymCoords(String genusSynonymCoords) {
        this.genusSynonymCoords = genusSynonymCoords;
    }

    public String getGenusSynonymId() {
        return genusSynonymId;
    }

    public void setGenusSynonymId(String genusSynonymId) {
        this.genusSynonymId = genusSynonymId;
    }

    public String getGenusSynonymNote() {
        return genusSynonymNote;
    }

    public void setGenusSynonymNote(String genusSynonymNote) {
        this.genusSynonymNote = genusSynonymNote;
    }

    public String getGenusSynonymType() {
        return genusSynonymType;
    }

    public void setGenusSynonymType(String genusSynonymType) {
        this.genusSynonymType = genusSynonymType;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    @Override
    public String toString() {
        return "test_dbdesktopapplication.cl.GenusSynonym[id=" + genusSynonymId + "]";
    }
}
