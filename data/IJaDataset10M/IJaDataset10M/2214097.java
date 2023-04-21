package org.h2.index;

/**
 * A page store index.
 */
public abstract class PageIndex extends BaseIndex {

    /**
     * The root page of this index.
     */
    protected int rootPageId;

    public int getRootPageId() {
        return rootPageId;
    }

    public int getHeadPos() {
        return 0;
    }
}
