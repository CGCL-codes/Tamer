package com.hongbo.cobweb.nmr.core;

import java.util.EventObject;
import com.hongbo.cobweb.nmr.api.NMR;

/**
 * Event representing an NMR instance having stopped.
 */
public class NmrStoppedEvent extends EventObject {

    private NMR nmr;

    public NmrStoppedEvent(NMR source) {
        super(source);
        this.nmr = source;
    }

    public NMR getNmr() {
        return nmr;
    }

    public String toString() {
        return "Stopped NMR: " + nmr.getId();
    }
}
