package org.jcvi.assembly.ace.consed;

import org.jcvi.assembly.AssemblyUtil;
import org.jcvi.assembly.Contig;
import org.jcvi.common.util.Range;

/**
 * 
 * {@code ConsensusNavigationElement} is a {@link NavigationElement}
 * that tells consed how to navigate to a particular feature 
 * of a specific contig.
 * @author dkatzel
 *
 *
 */
public class ConsensusNavigationElement extends AbstractNavigationElement {

    /**
     * Constructs a new {@link ConsensusNavigationElement}.
     * @param targetId the id of the target of that is to be navigated.
     * @param ungappedPositionRange the ungapped position
     * range of this element; cannot be null.
     * @param comment a comment that describes why this element exists
     * (may be null).
     * @throws NullPointerException if type, targetId or 
     * ungappedPositionRange are null.
     */
    public ConsensusNavigationElement(String contigId, Range ungappedPositionRange, String comment) {
        super(Type.CONSENSUS, contigId, ungappedPositionRange, comment);
    }

    /**
     * Constructs a new {@link ConsensusNavigationElement}.
     * @param targetId the id of the target of that is to be navigated.
     * @param ungappedPositionRange the ungapped position
     * range of this element; cannot be null.
     * @throws NullPointerException if type, targetId or 
     * ungappedPositionRange are null.
     */
    public ConsensusNavigationElement(String contigId, Range ungappedPositionRange) {
        super(Type.CONSENSUS, contigId, ungappedPositionRange);
    }

    /**
     * Build a new {@link ConsensusNavigationElement} for the given
     * contig, that will navigate to the given GAPPED range.  This
     * is a convenience method that handles converting the gapped
     * range into an ungapped range required by the consed.
     * This is the same as {@link #buildConsensusNavigationElement(Contig, Range, String)
     * buildConsensusNavigationElement(contig, gappedFeatureRange,null)}
     * @param <C> a Contig object.
     * @param contig the contig to make a {@link ConsensusNavigationElement}
     * for; cannot be null.
     * @param gappedFeatureRange the gapped feature range coordinates; cannot be null.
     * @return a new ConsensusNavigationElement.
     * @see #buildConsensusNavigationElement(Contig, Range, String)
     */
    public static <C extends Contig<?>> ConsensusNavigationElement buildConsensusNavigationElement(C contig, Range gappedFeatureRange) {
        return buildConsensusNavigationElement(contig, gappedFeatureRange, null);
    }

    /**
     * Build a new {@link ConsensusNavigationElement} for the given
     * contig, that will navigate to the given GAPPED range.  This
     * is a convenience method that handles converting the gapped
     * range into an ungapped range required by the consed.
     * @param <C> a Contig object.
     * @param contig the contig to make a {@link ConsensusNavigationElement}
     * for; cannot be null.
     * @param gappedFeatureRange the gapped feature range coordinates; cannot be null.
     * @param comment a comment that describes why this element exists
     * (may be null).
     * @return a new ConsensusNavigationElement.
     */
    public static <C extends Contig<?>> ConsensusNavigationElement buildConsensusNavigationElement(C contig, Range gappedFeatureRange, String comment) {
        if (gappedFeatureRange == null) {
            throw new NullPointerException("feature range can not be null");
        }
        Range ungappedRange = AssemblyUtil.convertGappedRangeIntoUngappedRange(contig.getConsensus(), gappedFeatureRange);
        return new ConsensusNavigationElement(contig.getId(), ungappedRange, comment);
    }
}
