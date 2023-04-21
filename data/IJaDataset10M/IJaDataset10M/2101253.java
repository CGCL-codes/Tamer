package org.sulweb.infureports.event;

/**
 *
 * @author lucio
 */
public final class HistogramUIEvent {

    private int height;

    /** Creates a new instance of HistogramUIEvent */
    public HistogramUIEvent(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}
