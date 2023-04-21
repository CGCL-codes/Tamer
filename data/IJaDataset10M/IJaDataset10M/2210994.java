package org.dyno.visual.swing.lnfs.windowsclassic;

import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJCheckBoxMenuItemValue extends WidgetValue {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ClassicJCheckBoxMenuItemValue() {
        put("visible", true);
        put("verifyInputWhenFocusTarget", true);
        put("horizontalAlignment", 10);
        put("opaque", true);
        put("contentAreaFilled", true);
        put("enabled", true);
        put("iconTextGap", 4);
        put("alignmentX", 0.5f);
        put("alignmentY", 0.5f);
        put("requestFocusEnabled", true);
        put("border", SYSTEM_VALUE);
    }
}
