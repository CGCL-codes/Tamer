package de.schwarzrot.data.meta;

public class SortInfo {

    public SortInfo(String attrName) {
        this(attrName, true);
    }

    public SortInfo(String attrName, boolean ascending) {
        this.attrName = attrName;
        this.ascending = ascending;
    }

    public final String getAttrName() {
        return attrName;
    }

    public final boolean isAscending() {
        return ascending;
    }

    public final void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public final void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    private String attrName;

    private boolean ascending;
}
