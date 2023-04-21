package org.dyno.visual.swing.lnfs.windowsxp;

import javax.swing.WindowConstants;
import org.dyno.visual.swing.lnfs.WidgetValue;

public class ClassicJDialogValue extends WidgetValue {

    private static final long serialVersionUID = 1L;

    public ClassicJDialogValue() {
        put("defaultCloseOperation", WindowConstants.HIDE_ON_CLOSE);
        put("focusCycleRoot", true);
        put("enabled", true);
        put("focusable", true);
        put("focusableWindowState", true);
        put("resizable", true);
    }
}
