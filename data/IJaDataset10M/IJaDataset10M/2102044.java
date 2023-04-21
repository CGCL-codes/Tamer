package net.sourceforge.jorgen.utils.pager;

public class PagedTableRange {

    private int index;

    private int beginRange;

    private int endRange;

    private boolean current = false;

    PagedTableRange(int index, int beginRange, int endRange) {
        this.index = index;
        this.beginRange = beginRange;
        this.endRange = endRange;
    }

    public int getIndex() {
        return index;
    }

    public int getBeginRange() {
        return beginRange;
    }

    public int getEndRange() {
        return endRange;
    }

    void setIsCurrent(boolean value) {
        current = value;
    }

    public boolean getIsCurrent() {
        return current;
    }
}
