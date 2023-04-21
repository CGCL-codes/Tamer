package com.golden.gamedev.gui.util;

import com.golden.gamedev.gui.TLabel;
import com.golden.gamedev.gui.theme.basic.BLabelRenderer;
import com.golden.gamedev.gui.toolkit.UIConstants;

public class LabelUtil {

    public static void setCenterAlign(TLabel... labels) {
        for (TLabel label : labels) {
            label.uiResource().put(BLabelRenderer.TEXT_HORIZONTAL_ALIGNMENT_INTEGER, UIConstants.CENTER);
            label.uiResource().put(BLabelRenderer.TEXT_VERTICAL_ALIGNMENT_INTEGER, UIConstants.CENTER);
        }
    }
}
