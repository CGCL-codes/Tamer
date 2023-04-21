package org.gdbi.api;

public interface GdbiIntrSour extends GdbiIntrRecord {

    public GdbiMiniSour getMiniSour();

    /**
     * Returns the name of the source, usually TITL.
     */
    public String getName();
}
