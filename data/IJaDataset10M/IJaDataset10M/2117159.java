package org.dyno.visual.swing.lnfs.windowsxp;

import java.awt.Color;
import org.dyno.visual.swing.lnfs.WidgetValue;

public class XpJAppletValue extends WidgetValue {

    private static final long serialVersionUID = 1L;

    public XpJAppletValue() {
        put("focusable", true);
        put("enabled", true);
        put("focusTraversalPolicyProvider", true);
        put("background", Color.white);
        put("foreground", Color.black);
    }
}
