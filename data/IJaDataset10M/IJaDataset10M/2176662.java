package org.jcvi.glk.elvira;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.jcvi.glk.Coordinated;
import org.jcvi.glk.EUID;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.Feature;
import org.jcvi.glk.Library;
import org.jcvi.glk.SangerTrash;
import org.jcvi.glk.SequenceDirection;
import org.jcvi.glk.SequenceRead;
import org.jcvi.glk.TrimSequence;
import org.jcvi.glk.TrimSequenceAttribute;

/**
 * <code>AmpliconExtent</code> is an {@link Extent} that
 * represents an Amplicon created from genomic segment.
 *
 *
 * @author jsitz
 * @author dkatzel
 */
@Entity
@DiscriminatorValue("AMPLICON")
public class AmpliconExtent extends Extent implements Coordinated {

    private Long start, end;

    public static String COORDINATE_ATTR = "coordinate";

    /**
     * Creates a new <code>AmpliconExtent</code>.
     *
     */
    public AmpliconExtent() {
        super();
    }

    /**
     * Creates a new <code>AmpliconExtent</code>.
     *
     * @param id
     */
    public AmpliconExtent(EUID id) {
        super(id);
    }

    /**
     * Creates a new <code>AmpliconExtent</code>.
     *
     * @param type
     * @param reference
     * @param parent
     * @param description
     * @param library
     */
    public AmpliconExtent(ExtentType type, String reference, Extent parent, String description, Library library) {
        super(type, reference, parent, description, library);
    }

    /**
     * Creates a new <code>AmpliconExtent</code>.
     *
     * @param type
     * @param reference
     * @param parent
     * @param desc
     */
    public AmpliconExtent(ExtentType type, String reference, Extent parent, String desc) {
        super(type, reference, parent, desc);
    }

    /**
     * Creates a new <code>AmpliconExtent</code>.
     *
     * @param type
     * @param reference
     * @param parent
     */
    public AmpliconExtent(ExtentType type, String reference, Extent parent) {
        super(type, reference, parent);
    }

    /**
     * Creates a new <code>AmpliconExtent</code>.
     *
     * @param type
     * @param reference
     * @param description
     */
    public AmpliconExtent(ExtentType type, String reference, String description) {
        super(type, reference, description);
    }

    /**
     * Returns the start coordinate of this Amplicon.
     * @return the start coordinate;
     * null if the start coordinate does not exists,
     * or Long.MIN_VALUE if the start coordinate can not be parsed.
     */
    @Transient
    public synchronized Long getStartCoordinate() {
        if (start == null) {
            if (getLibrary() == null) {
                System.out.println("library");
            }
            if (getLibrary().getCloningSystem() == null) {
                System.out.println("cloning system");
            }
            if (getLibrary().getCloningSystem().getTrimSequences() == null) {
                System.out.println("trim seqs");
            }
            for (TrimSequence trimSeq : getLibrary().getCloningSystem().getTrimSequences()) {
                if (trimSeq.getDirection().equals(SequenceDirection.FORWARD)) {
                    start = getCoordinateFor(trimSeq);
                }
            }
        }
        return start;
    }

    public synchronized void setStartCoordinate(Long start) {
        this.start = start;
    }

    /**
     * Returns the end coordinate of this Amplicon.
     * @return the end coordinate;
     * null if the end coordinate does not exists,
     * or Long.MIN_VALUE if the end coordinate can not be parsed.
     */
    @Transient
    public synchronized Long getEndCoordinate() {
        if (end == null) {
            for (TrimSequence trimSeq : getLibrary().getCloningSystem().getTrimSequences()) {
                if (trimSeq.getDirection().equals(SequenceDirection.REVERSE)) {
                    end = getCoordinateFor(trimSeq);
                }
            }
        }
        return end;
    }

    public synchronized void setEndCoordinate(Long end) {
        this.end = end;
    }

    @Transient
    protected Long getCoordinateFor(TrimSequence trimSeq) {
        Long along = null;
        for (TrimSequenceAttribute attribute : trimSeq.getAllAttributes()) {
            if (COORDINATE_ATTR.equals(attribute.getType().getName())) {
                try {
                    along = Long.valueOf(attribute.getValue()) - 1;
                } catch (NumberFormatException e) {
                    return Long.MIN_VALUE;
                }
            }
        }
        return along;
    }

    public static boolean isAmplicon(Extent extent) {
        return AmpliconExtent.class.isInstance(extent);
    }

    @Transient
    public int getForwardCoverageStart() {
        return getMinCLRFor(SequenceDirection.FORWARD) - 1;
    }

    @Transient
    public int getReverseCoverageStart() {
        return getMinCLRFor(SequenceDirection.REVERSE) - 1;
    }

    @Transient
    private int getMinCLRFor(final SequenceDirection direction) {
        int start = getEndCoordinate().intValue();
        for (SequenceRead read : this.getSequences()) {
            if (read.getDirection().equals(direction) && read.getSequence().getTrash() == SangerTrash.NONE) {
                Feature clr = getClearRangeFor(read);
                final int end5 = clr.getEnd5();
                if (end5 < start) {
                    start = end5;
                }
            }
        }
        return start;
    }

    @Transient
    public int getForwardCoverageEnd() {
        return getMaxCLRFor(SequenceDirection.FORWARD) - 1;
    }

    @Transient
    public int getReverseCoverageEnd() {
        return getMaxCLRFor(SequenceDirection.REVERSE) - 1;
    }

    @Transient
    private int getMaxCLRFor(final SequenceDirection direction) {
        int end = getStartCoordinate().intValue();
        for (SequenceRead read : this.getSequences()) {
            if (read.getDirection().equals(direction) && read.getSequence().getTrash() == SangerTrash.NONE) {
                Feature clr = getClearRangeFor(read);
                final int end3 = clr.getEnd3();
                if (end3 > end) {
                    end = end3;
                }
            }
        }
        return end;
    }

    @Transient
    protected Feature getClearRangeFor(SequenceRead read) {
        return read.getSequence().getFeaturesByType().get(Feature.Type.CLR);
    }
}
