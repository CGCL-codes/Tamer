package org.apache.myfaces.view.facelets.component;

import java.io.Serializable;

/**
 * @version $Id$
 */
public final class RepeatStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final int count;

    private final int index;

    private final boolean first;

    private final boolean last;

    private final Integer begin;

    private final Integer end;

    private final Integer step;

    public RepeatStatus(boolean first, boolean last, int count, int index, Integer begin, Integer end, Integer step) {
        this.count = count;
        this.index = index;
        this.begin = begin;
        this.end = end;
        this.step = step;
        this.first = first;
        this.last = last;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isEven() {
        return ((count % 2) == 0);
    }

    public boolean isOdd() {
        return !isEven();
    }

    public Integer getBegin() {
        if (begin == -1) {
            return null;
        }
        return begin;
    }

    public Integer getEnd() {
        if (end == -1) {
            return null;
        }
        return end;
    }

    public int getIndex() {
        return index;
    }

    public Integer getStep() {
        if (step == -1) {
            return null;
        }
        return step;
    }
}
