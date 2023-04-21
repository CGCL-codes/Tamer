package org.jcvi.common.core.seq.read.trace.sanger.chromat.scf;

import org.jcvi.common.core.seq.read.trace.sanger.chromat.ChromatogramFileVisitor;

/**
 * {@code SCFChromatogramFileVisitor} is a {@link ChromatogramFileVisitor}
 * that has additional visitXXX methods for SCF specific fields.
 * @author dkatzel
 *
 *
 */
public interface SCFChromatogramFileVisitor extends ChromatogramFileVisitor {

    /**
     * Visit the private data section of an SCF chromatogram 
     * file.
     * @param privateData the private data contained in this
     * SCF chromatogram (may be null).
     */
    void visitPrivateData(byte[] privateData);

    /**
     * Visit the confidence data section of an SCF chromatogram 
     * file that describes how confident the basecaller was
     * that the given basecall is not a substituion.
     * @param confidence the substitution data contained in this
     * SCF chromatogram (may be null or empty).
     */
    void visitSubstitutionConfidence(byte[] confidence);

    /**
     * Visit the confidence data section of an SCF chromatogram 
     * file that describes how confident the basecaller was
     * that the given basecall is not an insertion.
     * @param confidence the insertion data contained in this
     * SCF chromatogram (may be null or empty).
     */
    void visitInsertionConfidence(byte[] confidence);

    /**
     * Visit the confidence data section of an SCF chromatogram 
     * file that describes how confident the basecaller was
     * that the given basecall is not a deletion.
     * @param confidence the deletion data contained in this
     * SCF chromatogram (may be null or empty).
     */
    void visitDeletionConfidence(byte[] confidence);
}
