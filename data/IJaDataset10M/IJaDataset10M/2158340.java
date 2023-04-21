package com.sun.java.swing.plaf.windows;

import javax.swing.plaf.basic.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;

/**
 * Windows check box.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @version 1.21 11/17/05
 * @author Jeff Dinkins
 */
public class WindowsCheckBoxUI extends WindowsRadioButtonUI {

    private static final WindowsCheckBoxUI windowsCheckBoxUI = new WindowsCheckBoxUI();

    private static final String propertyPrefix = "CheckBox" + ".";

    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent c) {
        return windowsCheckBoxUI;
    }

    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        if (!defaults_initialized) {
            icon = UIManager.getIcon(getPropertyPrefix() + "icon");
            defaults_initialized = true;
        }
    }

    public void uninstallDefaults(AbstractButton b) {
        super.uninstallDefaults(b);
        defaults_initialized = false;
    }
}
