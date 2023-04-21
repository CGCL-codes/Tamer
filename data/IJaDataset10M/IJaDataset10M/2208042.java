package com.bluebrim.swing.client;

import java.awt.*;

/**
 * Creation date: (2000-04-19 10:39:26)
 * @author Karin
 */
public class CoFocusRequestPanel extends CoPanel {

    /**
	 * CoFocusRequestPanel constructor comment.
	 */
    public CoFocusRequestPanel() {
        super();
    }

    /**
	 * CoFocusRequestPanel constructor comment.
	 * @param extraInsets java.awt.Insets
	 */
    public CoFocusRequestPanel(Insets extraInsets) {
        super(extraInsets);
    }

    /**
	 * CoFocusRequestPanel constructor comment.
	 * @param layoutManager java.awt.LayoutManager
	 */
    public CoFocusRequestPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    /**
	 * CoFocusRequestPanel constructor comment.
	 * @param layoutManager java.awt.LayoutManager
	 * @param extraInsets java.awt.Insets
	 */
    public CoFocusRequestPanel(LayoutManager layoutManager, java.awt.Insets extraInsets) {
        super(layoutManager, extraInsets);
    }

    /**
	 * CoFocusRequestPanel constructor comment.
	 * @param layoutManager java.awt.LayoutManager
	 * @param isDoubleBuffered boolean
	 */
    public CoFocusRequestPanel(LayoutManager layoutManager, boolean isDoubleBuffered) {
        super(layoutManager, isDoubleBuffered);
    }

    /**
	 * CoFocusRequestPanel constructor comment.
	 * @param layoutManager java.awt.LayoutManager
	 * @param isDoubleBuffered boolean
	 * @param extraInsets java.awt.Insets
	 */
    public CoFocusRequestPanel(LayoutManager layoutManager, boolean isDoubleBuffered, Insets extraInsets) {
        super(layoutManager, isDoubleBuffered, extraInsets);
    }

    /**
	 * CoFocusRequestPanel constructor comment.
	 * @param isDoubleBuffered boolean
	 */
    public CoFocusRequestPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    protected void processEvent(AWTEvent e) {
        if (e instanceof CoFocusRequestEvent) {
            processFocusRequestEvent((CoFocusRequestEvent) e);
            return;
        }
        super.processEvent(e);
    }

    private void processFocusRequestEvent(CoFocusRequestEvent e) {
        e.getFocusedComponent().requestFocus();
    }
}
