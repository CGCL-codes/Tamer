package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.KeepTogetherAttributes;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This represents a keep together element.
 * <p>
 * The first time that this node is visited it will prevent itself being split
 * if there is not enough available space for the whole element. On subsequent
 * occasions it behaves as normal. In order to do this properly it requires that
 * the cost of itself be checked before it is processed.
 * <p>
 * In future it may be possible to force a break before or after. If a break
 * is forced before this element then it is not necessary to
 */
public class KeepTogether extends ElementAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(KeepTogether.class);

    private KeepTogetherAttributes attributes;

    /**
     * If true then a break should be forced before the keep together element.
     */
    private boolean forceBreakBefore;

    /**
     * If true then a break should be forced after the keep together element.
     */
    private boolean forceBreakAfter;

    public KeepTogether() {
        setMustCheckCost(true);
    }

    public void setForceBreakBefore(boolean forceBreakBefore) {
        this.forceBreakBefore = forceBreakBefore;
    }

    public boolean getForceBreakBefore() {
        return forceBreakBefore;
    }

    public void setForceBreakAfter(boolean forceBreakAfter) {
        this.forceBreakAfter = forceBreakAfter;
    }

    public boolean getForceBreakAfter() {
        return forceBreakAfter;
    }

    public int selectShardContents(Shard shard) throws DissectionException {
        if (forceBreakBefore && !shard.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Forcing break before " + this);
            }
            return NODE_CANNOT_FIT;
        }
        int result = super.selectShardContents(shard);
        if (forceBreakAfter && result == ADDED_NODE) {
            if (logger.isDebugEnabled()) {
                logger.debug("Forcing break after " + this);
            }
            shard.setAvailableSpace(-1);
        }
        return result;
    }

    /**
     * This method will refuse to dissect unless the force flag is set as that
     * is the whole point of this element.
     */
    protected int dissectNode(Shard shard, boolean mustDissect) throws DissectionException {
        if (!mustDissect) {
            return NODE_CANNOT_FIT;
        }
        return super.dissectNode(shard, mustDissect);
    }
}
