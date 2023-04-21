package org.egonet.util.listbuilder;

public class Selection {

    private String string;

    private int index;

    private int value;

    private boolean adjacent;

    public Selection() {
        this.string = "";
        this.value = -1;
        this.index = -1;
        this.adjacent = false;
    }

    public Selection(String string, int value, int index, boolean adjacent) {
        this.string = string;
        this.value = value;
        this.index = index;
        this.adjacent = adjacent;
    }

    public String toString() {
        return string;
    }

    public boolean isAdjacent() {
        return adjacent;
    }

    public void setAdjacent(boolean adjacent) {
        this.adjacent = adjacent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean equals(Selection selection) {
        if (this.adjacent == selection.adjacent && this.index == selection.index && this.string.equals(selection.string) && this.value == selection.value) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.index;
    }
}
