package org.datanucleus.jdo;

import javax.jdo.JDODataStoreException;
import javax.jdo.datastore.Sequence;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.NucleusSequence;

/**
 * Basic generic implementation of a JDO2 datastore sequence.
 * Wraps a NucleusSequence.
 */
public class JDOSequence implements Sequence, NucleusSequence {

    /** Underlying NucleusSequence. */
    protected NucleusSequence sequence;

    /**
     * Constructor.
     * @param seq Underlying sequence
     */
    public JDOSequence(NucleusSequence seq) {
        this.sequence = seq;
    }

    /**
     * Accessor for the sequence name.
     * @return The sequence name
     */
    public String getName() {
        return sequence.getName();
    }

    /**
     * Method to allocate a set of elements.
     * @param additional The number of additional elements to allocate
     */
    public void allocate(int additional) {
        sequence.allocate(additional);
    }

    /**
     * Accessor for the next element in the sequence.
     * @return The next element
     */
    public Object next() {
        try {
            return sequence.next();
        } catch (NucleusException ne) {
            throw NucleusJDOHelper.getJDOExceptionForNucleusException(ne);
        }
    }

    /**
     * Accessor for the next element in the sequence as a long.
     * @return The next element
     * @throws JDODataStoreException Thrown if not numeric
     */
    public long nextValue() {
        try {
            return sequence.nextValue();
        } catch (NucleusException ne) {
            throw NucleusJDOHelper.getJDOExceptionForNucleusException(ne);
        }
    }

    /**
     * Accessor for the current element.
     * @return The current element.
     */
    public Object current() {
        try {
            return sequence.current();
        } catch (NucleusException ne) {
            throw NucleusJDOHelper.getJDOExceptionForNucleusException(ne);
        }
    }

    /**
     * Accessor for the current element in the sequence as a long.
     * @return The current element
     * @throws JDODataStoreException Thrown if not numeric
     */
    public long currentValue() {
        try {
            return sequence.currentValue();
        } catch (NucleusException ne) {
            throw NucleusJDOHelper.getJDOExceptionForNucleusException(ne);
        }
    }
}
