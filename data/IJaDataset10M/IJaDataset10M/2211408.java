package net.sourceforge.combean.mathprog.lp.model;

import net.sourceforge.combean.interfaces.mathprog.lp.model.LPConstraintSequence;
import net.sourceforge.combean.interfaces.mathprog.lp.model.LPModelIndex;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * An abstract base class for an LP constraint sequences.
 * 
 * Takes care of the offset id and the offset and makes own implementations of
 * LPConstraintSequence a bit simpler.
 * 
 * @author schickin
 *
 */
public abstract class AbstractLPConstraintSequence implements LPConstraintSequence {

    private String offsetId = "";

    private int offset = LPConstraintSequence.OFFSET_UNDEFINED;

    /**
     * constructor
     */
    public AbstractLPConstraintSequence(String offsetId) {
        super();
        this.offsetId = offsetId;
    }

    public void setConstrOffsetId(String offsetId) {
        this.offsetId = offsetId;
    }

    public String getConstrOffsetId() {
        return this.offsetId;
    }

    public void setConstrOffset(int offset) {
        this.offset = offset;
    }

    public int getConstrOffset() {
        return this.offset;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Create a row model index for a given local index
     * 
     * @param localRow the local index.
     * @return the corresponding row model index.
     */
    public LPModelIndex getRowModelIndex(int localRow) {
        return new LPModelIndexImpl(localRow, getConstrOffsetId());
    }
}
