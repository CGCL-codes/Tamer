package org.kablink.teaming.gwt.client.widgets;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget that provides a HorizontalPanel that can be used in a layout
 * based system.
 * 
 * @author drfoster@novell.com
 */
public class VibeHorizontalPanel extends HorizontalPanel implements ProvidesResize, RequiresResize {

    /**
	 * Constructor method.
	 */
    public VibeHorizontalPanel() {
        super();
        setWidth("100%");
        setHeight("100%");
    }

    /**
	 * Adds a cell at the right of the panel that will fill it. 
	 */
    public void addRightPad() {
        InlineLabel nbsp = new InlineLabel();
        nbsp.getElement().setInnerHTML("&nbsp;");
        add(nbsp);
        setCellHeight(nbsp, "100%");
    }

    /**
	 * Implements RequiresResize.onResize()
	 */
    @Override
    public void onResize() {
        for (Widget w : this) {
            if (w instanceof RequiresResize) {
                ((RequiresResize) w).onResize();
            }
        }
    }
}
