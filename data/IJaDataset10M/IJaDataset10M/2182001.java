package cygnus.graph;

public class CElement {

    protected boolean goneThrough;

    boolean isGoneThrough() {
        return goneThrough;
    }

    void setGoneThrough() {
        goneThrough = true;
    }

    void resetGoneThrough() {
        goneThrough = false;
    }
}
