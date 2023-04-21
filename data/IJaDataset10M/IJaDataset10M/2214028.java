package com.seaglasslookandfeel.state;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/**
 * Is the window for this close button not focused?
 */
public class TitlePaneCloseButtonWindowNotFocusedState extends State {

    /**
     * Creates a new TitlePaneCloseButtonWindowNotFocusedState object.
     */
    public TitlePaneCloseButtonWindowNotFocusedState() {
        super("WindowNotFocused");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        Component parent = c;
        while (parent.getParent() != null) {
            if (parent instanceof JInternalFrame) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent instanceof JInternalFrame) {
            return !(((JInternalFrame) parent).isSelected());
        }
        return false;
    }
}
