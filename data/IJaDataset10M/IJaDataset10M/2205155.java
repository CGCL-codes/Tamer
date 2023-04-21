package com.seaglasslookandfeel.state;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

/**
 * Is the toolbar on the (right) west side?
 */
public class ToolBarWestState extends State {

    /**
     * Creates a new ToolBarWestState object.
     */
    public ToolBarWestState() {
        super("West");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        JToolBar toolbar = (JToolBar) c;
        return SeaGlassLookAndFeel.resolveToolbarConstraint(toolbar) == BorderLayout.WEST;
    }
}
