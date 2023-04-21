package com.hp.hpl.jena.rdf.arp.impl;

/**
 * Minimal implemantation of {@link Taint}
 * @author Jeremy J. Carroll
 *
 */
public class TaintImpl implements Taint {

    private boolean tainted = false;

    public TaintImpl() {
    }

    public void taint() {
        tainted = true;
    }

    public boolean isTainted() {
        return tainted;
    }
}
