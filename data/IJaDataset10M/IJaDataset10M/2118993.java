package com.sun.facelets.tag.jstl.core;

import java.io.Serializable;

/**
 * @author Jacob Hookom
 * @version $Id: IterationStatus.java,v 1.3 2006/03/19 03:11:35 jhook Exp $
 */
public final class IterationStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final int index;

    private final boolean first;

    private final boolean last;

    private final Integer begin;

    private final Integer end;

    private final Integer step;

    /**
     * 
     */
    public IterationStatus(boolean first, boolean last, int index, Integer begin, Integer end, Integer step) {
        this.index = index;
        this.begin = begin;
        this.end = end;
        this.step = step;
        this.first = first;
        this.last = last;
    }

    public boolean isFirst() {
        return this.first;
    }

    public boolean isLast() {
        return this.last;
    }

    public Integer getBegin() {
        return begin;
    }

    public Integer getEnd() {
        return end;
    }

    public int getIndex() {
        return index;
    }

    public Integer getStep() {
        return step;
    }
}
