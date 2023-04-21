package com.leclercb.taskunifier.gui.commons.values;

import javax.swing.Icon;
import org.jdesktop.swingx.renderer.IconValue;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class IconValueSelected implements IconValue {

    public static final IconValueSelected INSTANCE = new IconValueSelected();

    private IconValueSelected() {
    }

    @Override
    public Icon getIcon(Object value) {
        if (value == null || !(value instanceof Boolean)) return ImageUtils.getResourceImage("checkbox.png", 16, 16);
        if ((Boolean) value) return ImageUtils.getResourceImage("checkbox_selected.png", 16, 16); else return ImageUtils.getResourceImage("checkbox.png", 16, 16);
    }
}
