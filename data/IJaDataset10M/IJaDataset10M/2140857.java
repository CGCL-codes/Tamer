package org.gdbi.api;

public interface GdbiIntrIndi extends GdbiIntrRecord {

    public GdbiIndi toNextIndi();

    public GdbiIndi toPrevIndi();

    public GdbiMiniIndi getMiniIndi();
}
