package org.nist.usarui.ui;

import java.awt.event.*;
import javax.swing.text.*;

/**
 * A class which automatically selects the contents of a text box when given focus.
 */
public class SelectOnFocus extends FocusAdapter {

    private final JTextComponent box;

    /**
	 * Creates a new SelectOnFocus that will use the target component.
	 *
	 * @param toSelect the component to select when focused
	 */
    public SelectOnFocus(JTextComponent toSelect) {
        box = toSelect;
    }

    public void focusGained(FocusEvent e) {
        if (!e.isTemporary() && e.getSource() == box) box.selectAll();
    }
}
