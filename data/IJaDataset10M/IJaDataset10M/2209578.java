package com.juant.market.source;

/**
 * Listener for new lines.
 */
public interface ParseListener {

    /**
     * Receives notification signals.
     */
    public void notify(final ParseResult result);
}
