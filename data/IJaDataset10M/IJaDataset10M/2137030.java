package uk.ac.ebi.intact.update.model.protein.events;

import org.apache.commons.collections.CollectionUtils;
import uk.ac.ebi.intact.model.Alias;
import uk.ac.ebi.intact.model.Annotation;
import uk.ac.ebi.intact.model.Protein;
import uk.ac.ebi.intact.model.Xref;
import uk.ac.ebi.intact.update.model.UpdateStatus;
import uk.ac.ebi.intact.update.model.protein.ProteinUpdateAnnotation;
import uk.ac.ebi.intact.update.model.protein.ProteinUpdateProcess;
import uk.ac.ebi.intact.update.model.protein.UpdatedAlias;
import uk.ac.ebi.intact.update.model.protein.UpdatedCrossReference;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Event for the basic update of a uniprot proteinAc
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>28-Oct-2010</pre>
 */
@Entity
@DiscriminatorValue("uniprot_update")
public class UniprotUpdateEvent extends ProteinEventWithShiftedRanges {

    /**
     * The new shortlabel
     */
    private String updatedShortLabel;

    /**
     * The new fullName
     */
    private String updatedFullName;

    /**
     * The uniprot query it has been updated for
     */
    private String uniprotQuery;

    /**
     * the list of updated xrefs
     */
    private Collection<UpdatedCrossReference> updatedXrefs = new ArrayList<UpdatedCrossReference>();

    /**
     * The list of updated annotations
     */
    private Collection<ProteinUpdateAnnotation> updatedAnnotations = new ArrayList<ProteinUpdateAnnotation>();

    /**
     * The list of updated aliases
     */
    private Collection<UpdatedAlias> updatedAliases = new ArrayList<UpdatedAlias>();

    public UniprotUpdateEvent() {
        super();
        this.updatedShortLabel = null;
        this.updatedFullName = null;
    }

    public UniprotUpdateEvent(ProteinUpdateProcess updateProcess, Protein protein, String shortlabel, String fullname, String uniprotEntry) {
        super(updateProcess, protein);
        this.updatedShortLabel = shortlabel;
        this.updatedFullName = fullname;
        this.uniprotQuery = uniprotEntry;
    }

    public UniprotUpdateEvent(ProteinUpdateProcess updateProcess, Protein protein, String uniprotEntry) {
        super(updateProcess, protein);
        this.updatedShortLabel = null;
        this.updatedFullName = null;
        this.uniprotQuery = uniprotEntry;
    }

    @Column(name = "shortlabel")
    public String getUpdatedShortLabel() {
        return updatedShortLabel;
    }

    public void setUpdatedShortLabel(String updatedShortLabel) {
        this.updatedShortLabel = updatedShortLabel;
    }

    @Column(name = "fullname")
    public String getUpdatedFullName() {
        return updatedFullName;
    }

    public void setUpdatedFullName(String updatedFullName) {
        this.updatedFullName = updatedFullName;
    }

    @Column(name = "query")
    public String getUniprotQuery() {
        return uniprotQuery;
    }

    public void setUniprotQuery(String uniprot) {
        this.uniprotQuery = uniprot;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        final UniprotUpdateEvent event = (UniprotUpdateEvent) o;
        if (updatedShortLabel != null) {
            if (!updatedShortLabel.equals(event.getUpdatedShortLabel())) {
                return false;
            }
        } else if (event.getUpdatedShortLabel() != null) {
            return false;
        }
        if (updatedFullName != null) {
            if (!updatedFullName.equals(event.getUpdatedFullName())) {
                return false;
            }
        } else if (event.getUpdatedFullName() != null) {
            return false;
        }
        if (uniprotQuery != null) {
            if (!uniprotQuery.equals(event.getUniprotQuery())) {
                return false;
            }
        } else if (event.getUniprotQuery() != null) {
            return false;
        }
        return true;
    }

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH })
    public Collection<UpdatedCrossReference> getUpdatedXrefs() {
        return this.updatedXrefs;
    }

    public void addUpdatedReferencesFromXref(Collection<Xref> updatedRef, UpdateStatus status) {
        for (Xref ref : updatedRef) {
            UpdatedCrossReference reference = new UpdatedCrossReference(ref, status);
            if (this.updatedXrefs.add(reference)) {
                reference.setParent(this);
            }
        }
    }

    public void setUpdatedAnnotations(Collection<ProteinUpdateAnnotation> updatedAnnotations) {
        if (updatedAnnotations != null) {
            this.updatedAnnotations = updatedAnnotations;
        }
    }

    public void setUpdatedAliases(Collection<UpdatedAlias> updatedAliases) {
        if (updatedAliases != null) {
            this.updatedAliases = updatedAliases;
        }
    }

    public void setUpdatedXrefs(Collection<UpdatedCrossReference> updatedXrefs) {
        if (updatedXrefs != null) {
            this.updatedXrefs = updatedXrefs;
        }
    }

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH })
    public Collection<ProteinUpdateAnnotation> getUpdatedAnnotations() {
        return this.updatedAnnotations;
    }

    public void addUpdatedAnnotationFromAnnotation(Collection<Annotation> updatedAnn, UpdateStatus status) {
        for (uk.ac.ebi.intact.model.Annotation a : updatedAnn) {
            ProteinUpdateAnnotation annotation = new ProteinUpdateAnnotation(a, status);
            if (this.updatedAnnotations.add(annotation)) {
                annotation.setParent(this);
            }
        }
    }

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH })
    public Collection<UpdatedAlias> getUpdatedAliases() {
        return this.updatedAliases;
    }

    public void addUpdatedAliasesFromAlias(Collection<Alias> updatedAlias, UpdateStatus status) {
        for (Alias a : updatedAlias) {
            UpdatedAlias alias = new UpdatedAlias(a, status);
            if (this.updatedAliases.add(alias)) {
                alias.setParent(this);
            }
        }
    }

    public boolean addUpdatedXRef(UpdatedCrossReference xref) {
        if (this.updatedXrefs.add(xref)) {
            xref.setParent(this);
            return true;
        }
        return false;
    }

    public boolean removeUpdatedXRef(UpdatedCrossReference xref) {
        if (this.updatedXrefs.remove(xref)) {
            xref.setParent(null);
            return true;
        }
        return false;
    }

    public boolean addUpdatedAnnotation(ProteinUpdateAnnotation ann) {
        if (this.updatedAnnotations.add(ann)) {
            ann.setParent(this);
            return true;
        }
        return false;
    }

    public boolean removeUpdatedAnnotation(ProteinUpdateAnnotation ann) {
        if (this.updatedAnnotations.remove(ann)) {
            ann.setParent(null);
            return true;
        }
        return false;
    }

    public boolean addUpdatedAlias(UpdatedAlias alias) {
        if (this.updatedAliases.add(alias)) {
            alias.setParent(this);
            return true;
        }
        return false;
    }

    public boolean removeAlias(UpdatedAlias alias) {
        if (this.updatedAliases.remove(alias)) {
            alias.setParent(null);
            return true;
        }
        return false;
    }

    /**
     * This class overwrites equals. To ensure proper functioning of HashTable,
     * hashCode must be overwritten, too.
     *
     * @return hash code of the object.
     */
    @Override
    public int hashCode() {
        int code = 29;
        code = 29 * code + super.hashCode();
        if (updatedShortLabel != null) {
            code = 29 * code + updatedShortLabel.hashCode();
        }
        if (updatedFullName != null) {
            code = 29 * code + updatedFullName.hashCode();
        }
        if (uniprotQuery != null) {
            code = 29 * code + uniprotQuery.hashCode();
        }
        return code;
    }

    @Override
    public boolean isIdenticalTo(Object o) {
        if (!super.isIdenticalTo(o)) {
            return false;
        }
        final UniprotUpdateEvent event = (UniprotUpdateEvent) o;
        if (updatedShortLabel != null) {
            if (!updatedShortLabel.equals(event.getUpdatedShortLabel())) {
                return false;
            }
        } else if (event.getUpdatedShortLabel() != null) {
            return false;
        }
        if (updatedFullName != null) {
            if (!updatedFullName.equals(event.getUpdatedFullName())) {
                return false;
            }
        } else if (event.getUpdatedFullName() != null) {
            return false;
        }
        if (uniprotQuery != null) {
            if (!uniprotQuery.equals(event.getUniprotQuery())) {
                return false;
            }
        } else if (event.getUniprotQuery() != null) {
            return false;
        }
        if (!CollectionUtils.isEqualCollection(updatedAliases, event.getUpdatedAliases())) {
            return false;
        }
        if (!CollectionUtils.isEqualCollection(updatedAnnotations, event.getUpdatedAnnotations())) {
            return false;
        }
        return CollectionUtils.isEqualCollection(updatedXrefs, event.getUpdatedXrefs());
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString() + "\n");
        buffer.append("Uniprot update event : [ shortlabel = " + updatedShortLabel != null ? updatedShortLabel : "none" + ", fullname = " + updatedFullName != null ? updatedFullName : "none" + ", uniprot query = " + uniprotQuery != null ? uniprotQuery : "none");
        return buffer.toString();
    }
}
