package org.makagiga.commons.color;

import org.makagiga.commons.beans.ComponentBeanInfo;

/**
 * @since 3.8.6, 4.0 (org.makagiga.commons.color package)
 */
public final class MSmallColorChooserBeanInfo extends ComponentBeanInfo {

    public MSmallColorChooserBeanInfo() {
        super(MSmallColorChooser.class, "Small Color Chooser", "ui/color");
        addPropertyDescriptor(createValuePropertyDescriptor(true));
        addPropertyDescriptor("defaultValue", PREFERRED, "The default value");
        addPropertyDescriptor("resetActionVisible", PREFERRED, "Whether or not the \"Restore Default Values\" item is visible");
        addPropertyDescriptor("title", PREFERRED, "The title/text label");
    }
}
