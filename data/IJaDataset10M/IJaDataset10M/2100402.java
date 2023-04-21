package org.jcvi.assembly.contig;

import org.jcvi.assembly.Contig;
import org.jcvi.common.util.Range;
import org.jcvi.io.TextFileVisitor;
import org.jcvi.sequence.SequenceDirection;

/**
 * {@code ContigFileVisitor} is a {@link TextFileVisitor}
 * that visits files that contain {@link Contig} data.
 * @author dkatzel
 *
 *
 */
public interface ContigFileVisitor extends TextFileVisitor {

    /**
     * Begin visiting a new contig with the given ID.
     * @param contigId the id of the contig being visited.
     */
    void visitNewContig(String contigId);

    /**
     * Visit a line of basecalls for the current contig consensus.
     * @param lineOfBasecalls a String containing basecalls.
     */
    void visitConsensusBasecallsLine(String lineOfBasecalls);

    /**
     * Visit a line of basecalls for the current read.
     * @param lineOfBasecalls a String containing basecalls. 
     */
    void visitReadBasecallsLine(String lineOfBasecalls);

    /**
     * Visit an underlying read for the current contig being visited.
     * @param readId the id of the read.
     * @param offset the start offset of the read (0-based).
     * @param validRange the Range of the read that contains valid basecall data.
     * @param dir the {@link SequenceDirection} of this read.
     */
    void visitNewRead(String readId, int offset, Range validRange, SequenceDirection dir);
}
