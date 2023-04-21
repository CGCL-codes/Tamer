package gtkwire.widget;

import gtkwire.*;

/**
* A container which can hide its child.
*/
public class GtkExpander extends GtkBin {

    private String label;

    public GtkExpander(String label) {
        super();
        this.widgetType = WT_GtkExpander;
        this.label = label;
        gtkCreate();
    }

    public GtkExpander(String name, GladeKey key) {
        super(name, key);
        this.widgetType = WT_GtkExpander;
    }

    public String[] getCreateData() {
        String[] msg = { label };
        return msg;
    }
}
