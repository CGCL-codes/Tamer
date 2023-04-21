package com.realtime.crossfire.jxclient.gui.list;

import javax.swing.JViewport;

/**
 * A {@link JViewport} that allows updating the viewport state.
 * @author Andreas Kirschbaum
 */
public class GUIListViewport extends JViewport {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1;

    /**
     * Updates the viewport state.
     */
    public void update() {
        fireStateChanged();
    }
}
