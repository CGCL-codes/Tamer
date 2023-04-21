package org.jcvi.common.core.assembly.clc.cas;

/**
 * {@code CasContigDescription} is an interface
 * which explains details about a contig
 * (reference).
 * @author dkatzel
 *
 *
 */
public interface CasReferenceDescription {

    /**
     * Get the length of this reference sequence.
     * @return the length of this reference as a positive long.
     */
    long getContigLength();

    /**
     * Is this reference circular.
     * @return {@code true} if this reference is circular;
     * {@code false} otherwise.
     */
    boolean isCircular();
}
