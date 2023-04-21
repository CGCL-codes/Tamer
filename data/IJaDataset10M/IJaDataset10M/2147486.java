package org.kablink.teaming.gwt.client.widgets;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget that provides a 'layout based' VerticalPanel.
 * 
 * @author drfoster@novell.com
 */
public class VibeVerticalPanel extends VerticalPanel implements ProvidesResize, RequiresResize {

    /**
	 * Constructor method.
	 */
    public VibeVerticalPanel() {
        super();
        setWidth("100%");
        setHeight("100%");
    }

    /**
	 * Adds a row at the bottom of the panel that will fill it. 
	 */
    public void addBottomPad() {
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
