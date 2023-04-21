package se.biobanksregistersyd.boakeity.entity;

import javax.persistence.Entity;

/**
 * Entity bean for storing consent decisions which regards a specific
 * purpose, like a specific research project.
 */
@Entity
public class CDspecificPurpose extends ConsentDecision {

    /** The journal number (Swedish: "diarienummer") that approved
     *  the specific research project (and hopefully identifies it
     *  uniquely). */
    private String reference;

    /**
     * Getter for field 'reference'.
     * 
     * @return a <code>String</code> with the reference number that
     *         identifies the purpose.
     * @see #setReference(String)
     */
    public String getReference() {
        return reference;
    }

    /**
     * Setter for field 'reference'.
     * 
     * @param referenceIn  a <code>String</code> which the reference number
     *                     that identifies the purpose.
     * @see #getReference()
     */
    public void setReference(final String referenceIn) {
        reference = referenceIn;
    }
}
