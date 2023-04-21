package gtkwire.widget;

import gtkwire.*;
import gtkwire.widget.utils.GtkResizeMode;

/**
*A container with two panes arranged vertically.
*Documentation for this class is from the gtk+ .c file that this class binds to. See original file for copyrights.
*/
public class GtkVPaned extends GtkPaned {

    public GtkVPaned() {
        super();
        this.widgetType = WT_GtkVPaned;
        gtkCreate();
    }

    public GtkVPaned(String name, GladeKey key) {
        super(name, key);
        this.widgetType = WT_GtkVPaned;
    }
}
