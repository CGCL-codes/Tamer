package org.digitall.extras.ekit;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/** Class for providing a JToggleButton that does not obtain focus
  */
public class JToggleButtonNoFocus extends JToggleButton {

    public JToggleButtonNoFocus() {
        super();
        this.setRequestFocusEnabled(false);
    }

    public JToggleButtonNoFocus(Action a) {
        super(a);
        this.setRequestFocusEnabled(false);
    }

    public JToggleButtonNoFocus(Icon icon) {
        super(icon);
        this.setRequestFocusEnabled(false);
    }

    public JToggleButtonNoFocus(String text) {
        super(text);
        this.setRequestFocusEnabled(false);
    }

    public JToggleButtonNoFocus(String text, Icon icon) {
        super(text, icon);
        this.setRequestFocusEnabled(false);
    }

    public boolean isFocusable() {
        return false;
    }

    public boolean isFocusTraversable() {
        return false;
    }
}
