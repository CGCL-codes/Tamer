package com.ixora.rms.ui.dataviewboard.utils;

/**
 * A context that provides settings for a NumericValueDeltaHandler.
 * @author Daniel Moraru
 */
public interface NumericValueDeltaHandlerContext {

    /**
     * @return the size of the delta history
     */
    int getDeltaHistorySize();
}
