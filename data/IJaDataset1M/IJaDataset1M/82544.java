package org.dyno.visual.swing.lnfs.nimbus;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class JLabelValue extends WidgetValue {

    private static final long serialVersionUID = 1L;

    public JLabelValue() {
        put("horizontalAlignment", 10);
        put("visible", true);
        put("verifyInputWhenFocusTarget", true);
        put("enabled", true);
        put("focusable", true);
        put("iconTextGap", 4);
        put("alignmentY", 0.5f);
        put("inheritsPopupMenu", true);
        put("requestFocusEnabled", true);
        put("border", SYSTEM_VALUE);
    }
}
