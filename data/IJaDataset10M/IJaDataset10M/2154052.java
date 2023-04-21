package uk.ac.ebi.intact.update.model.protein.errors;

import org.apache.commons.collections.CollectionUtils;
import uk.ac.ebi.intact.dbupdate.prot.errors.IntactUpdateError;
import uk.ac.ebi.intact.dbupdate.prot.errors.UpdateError;
import uk.ac.ebi.intact.update.model.protein.ProteinUpdateProcess;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Errors for isoforms and feature chains having invalid collection of parents
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/08/11</pre>
 */
@Entity
@DiscriminatorValue("invalid_parent_collection")
public class InvalidCollectionOfParents extends DefaultPersistentUpdateError implements IntactUpdateError {

    /**
     * the collection of intact isoform updateProcess acs
     */
    private Set<String> isoformParents = new HashSet<String>();

    /**
     * the collection of intact chain updateProcess acs
     */
    private Set<String> chainParents = new HashSet<String>();

    /**
     * the intact protein ac
     */
    private String proteinAc;

    public InvalidCollectionOfParents() {
        super();
        this.proteinAc = null;
    }

    public InvalidCollectionOfParents(ProteinUpdateProcess process, String proteinAc, UpdateError errorLabel) {
        super(process, errorLabel, null);
        this.proteinAc = proteinAc;
    }

    @ElementCollection
    @JoinTable(name = "ia_err2isoform_parent", joinColumns = @JoinColumn(name = "error_id"))
    @Column(name = "parent_ac")
    public Collection<String> getIsoformParents() {
        return isoformParents;
    }

    @ElementCollection
    @JoinTable(name = "ia_err2chain_parent", joinColumns = @JoinColumn(name = "error_id"))
    @Column(name = "parent_ac")
    public Collection<String> getChainParents() {
        return chainParents;
    }

    @Override
    @Column(name = "protein_ac")
    public String getProteinAc() {
        return this.proteinAc;
    }

    public void setIsoformParents(Set<String> isoformParents) {
        this.isoformParents = isoformParents;
    }

    public void setChainParents(Set<String> chainParents) {
        this.chainParents = chainParents;
    }

    public void setProteinAc(String proteinAc) {
        this.proteinAc = proteinAc;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        final InvalidCollectionOfParents event = (InvalidCollectionOfParents) o;
        if (proteinAc != null) {
            if (!proteinAc.equals(event.getProteinAc())) {
                return false;
            }
        } else if (event.getProteinAc() != null) {
            return false;
        }
        return true;
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
        if (proteinAc != null) {
            code = 29 * code + proteinAc.hashCode();
        }
        return code;
    }

    @Override
    public boolean isIdenticalTo(Object o) {
        if (!super.isIdenticalTo(o)) {
            return false;
        }
        final InvalidCollectionOfParents event = (InvalidCollectionOfParents) o;
        if (proteinAc != null) {
            if (!proteinAc.equals(event.getProteinAc())) {
                return false;
            }
        } else if (event.getProteinAc() != null) {
            return false;
        }
        if (!CollectionUtils.isEqualCollection(isoformParents, event.getIsoformParents())) {
            return false;
        }
        return CollectionUtils.isEqualCollection(chainParents, event.getChainParents());
    }

    protected static void writeIntactAcs(StringBuffer error, Collection<String> acs) {
        int i = 0;
        for (String uniprot : acs) {
            error.append(uniprot);
            if (i < acs.size()) {
                error.append(", ");
            }
            i++;
        }
    }

    @Override
    public String toString() {
        if (this.proteinAc == null) {
            return "";
        }
        StringBuffer error = new StringBuffer();
        error.append("The protein ");
        error.append(proteinAc);
        error.append(" has " + this.isoformParents.size());
        error.append(" isoform parents : ");
        writeIntactAcs(error, this.isoformParents);
        error.append(" and has " + this.chainParents.size());
        error.append(" chain parents : ");
        writeIntactAcs(error, this.chainParents);
        return error.toString();
    }
}
