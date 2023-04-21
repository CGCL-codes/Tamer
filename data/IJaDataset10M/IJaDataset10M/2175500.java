package events;

import javax.swing.text.Style;

public class EvAddStyle implements RemoteEvent {

    private String nm;

    private Style parent;

    public EvAddStyle(String nm, Style parent) {
        this.nm = nm;
        this.parent = parent;
    }

    public String getNm() {
        return nm;
    }

    public Style getParent() {
        return parent;
    }
}
