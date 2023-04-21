package uk.ac.ebi.intact.uniprot.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.ac.ebi.intact.uniprot.service.UniprotService;

/**
 * Splice variant of a UniProt protein.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since <pre>15-Sep-2006</pre>
 */
public class UniprotSpliceVariant {

    public static final Log log = LogFactory.getLog(UniprotSpliceVariant.class);

    /**
     * Accession number of the splice variant.
     */
    private String primaryAc;

    /**
     * Secondary accession number of the splice variant.
     */
    private List<String> secondaryAcs;

    /**
     * Collection of synonyms.
     */
    private Collection<String> synomyms;

    /**
     * Sequence of the splice variant.
     */
    private String sequence;

    /**
     * Organism of a splice variant.
     */
    private Organism organism;

    /**
     * Start range of the splice variant.
     */
    private Integer start;

    /**
     * End range of the splice variant.
     */
    private Integer end;

    /**
     * Additional note of the splice variant.
     */
    private String note;

    public UniprotSpliceVariant(String primaryAc, Organism organism, String sequence) {
        setPrimaryAc(primaryAc);
        setOrganism(organism);
        setSequence(sequence);
    }

    /**
     * Returns accession number of the splice variant.
     *
     * @return accession number of the splice variant.
     */
    public String getPrimaryAc() {
        return primaryAc;
    }

    /**
     * Sets accession number of the splice variant.
     *
     * @param primaryAc accession number of the splice variant.
     */
    public void setPrimaryAc(String primaryAc) {
        if (primaryAc == null) {
            throw new IllegalArgumentException("A splice variant must have a primary AC.");
        }
        if (primaryAc.trim().equals("")) {
            throw new IllegalArgumentException("A splice variant must have a non empty primary AC.");
        }
        this.primaryAc = primaryAc;
    }

    /**
     * Returns secondary accession number of the splice variant.
     *
     * @return non null secondary accession number of the splice variant.
     */
    public List<String> getSecondaryAcs() {
        if (secondaryAcs == null) {
            secondaryAcs = new ArrayList<String>();
        }
        return secondaryAcs;
    }

    /**
     * Setter for property 'secondaryAcs'.
     *
     * @param secondaryAcs Value to set for property 'secondaryAcs'.
     */
    public void setSecondaryAcs(List<String> secondaryAcs) {
        this.secondaryAcs = secondaryAcs;
    }

    /**
     * Getter for property 'organism'.
     *
     * @return Value for property 'organism'.
     */
    public Organism getOrganism() {
        return organism;
    }

    /**
     * Setter for property 'organism'.
     *
     * @param organism Value to set for property 'organism'.
     */
    public void setOrganism(Organism organism) {
        if (organism == null) {
            throw new IllegalArgumentException("Organism must not be null.");
        }
        this.organism = organism;
    }

    /**
     * Returns collection of synonyms.
     *
     * @return collection of synonyms.
     */
    public Collection<String> getSynomyms() {
        if (synomyms == null) {
            synomyms = new ArrayList<String>();
        }
        return synomyms;
    }

    /**
     * Setter for property 'synomyms'.
     *
     * @param synomyms Value to set for property 'synomyms'.
     */
    public void setSynomyms(Collection<String> synomyms) {
        this.synomyms = synomyms;
    }

    /**
     * Returns sequence of the splice variant.
     *
     * @return sequence of the splice variant.
     */
    public String getSequence() {
        if (sequence == null || sequence.trim().length() == 0) {
            log.error("The sequence was null, the primary Ac of the splice variant is " + getPrimaryAc());
            throw new IllegalArgumentException("A splice variant must have a sequence.");
        }
        return sequence;
    }

    /**
     * Sets sequence of the splice variant.
     *
     * @param sequence sequence of the splice variant.
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Returns start range of the splice variant.
     *
     * @return start range of the splice variant.
     */
    public Integer getStart() {
        return start;
    }

    /**
     * Sets start range of the splice variant.
     *
     * @param start start range of the splice variant.
     */
    public void setStart(Integer start) {
        if (start != null) {
            if (end != null && start > end) {
                throw new IllegalArgumentException("Start (" + start + ") must be lower than end (" + end + ") !");
            }
            if (start < 1) {
                throw new IllegalArgumentException("Start must be 1 or greater.");
            }
        }
        this.start = start;
    }

    /**
     * Returns end range of the splice variant.
     *
     * @return end range of the splice variant.
     */
    public Integer getEnd() {
        return end;
    }

    /**
     * Sets end range of the splice variant.
     *
     * @param end end range of the splice variant.
     */
    public void setEnd(Integer end) {
        if (end != null) {
            if (start != null && start > end) {
                throw new IllegalArgumentException("End (" + end + ") must be greater than start (" + start + ") !");
            }
            if (end < 1) {
                throw new IllegalArgumentException("End must be 1 or greater.");
            }
        }
        this.end = end;
    }

    /**
     * Returns additional note of the splice variant.
     *
     * @return additional note of the splice variant.
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets additional note of the splice variant.
     *
     * @param note additional note of the splice variant.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniprotSpliceVariant that = (UniprotSpliceVariant) o;
        if (!primaryAc.equals(that.primaryAc)) {
            return false;
        }
        if (secondaryAcs != null ? !secondaryAcs.equals(that.secondaryAcs) : that.secondaryAcs != null) {
            return false;
        }
        if (!organism.equals(that.organism)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result;
        result = primaryAc.hashCode();
        result = 31 * result + (secondaryAcs != null ? secondaryAcs.hashCode() : 0);
        result = 31 * result + organism.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SpliceVariant");
        sb.append("{primaryAC='").append(primaryAc).append('\'');
        sb.append(",secondaryAcs=").append(secondaryAcs);
        sb.append(",sequence='").append(sequence).append('\'');
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
