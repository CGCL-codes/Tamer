package org.dyno.visual.swing.lnfs.nimbus;

import java.awt.Color;
import org.dyno.visual.swing.lnfs.WidgetValue;

public class JListValue extends WidgetValue {

    private static final long serialVersionUID = 1L;

    public JListValue() {
        put("selectionMode", 2);
        put("model", null);
        put("visible", true);
        put("fixedCellHeight", -1);
        put("verifyInputWhenFocusTarget", true);
        put("opaque", true);
        put("autoscrolls", true);
        put("enabled", true);
        put("focusable", true);
        put("selectedIndex", -1);
        put("visibleRowCount", 8);
        put("alignmentX", 0.5f);
        put("fixedCellWidth", -1);
        put("alignmentY", 0.5f);
        put("requestFocusEnabled", true);
        put("selectionBackground", new Color(57, 105, 138));
        put("selectionForeground", new Color(255, 255, 255));
        put("border", SYSTEM_VALUE);
    }
}
