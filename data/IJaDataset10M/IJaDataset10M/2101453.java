package org.makagiga.commons;

import java.beans.PropertyDescriptor;
import org.makagiga.commons.beans.AbstractBeanInfo;

/**
 * @since 3.8
 */
public final class MColorPickerBeanInfo extends AbstractBeanInfo {

    public MColorPickerBeanInfo() {
        super(MColorPicker.class, "Color Picker");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[] { createPropertyDescriptor("color", BOUND | PREFERRED, "Selected color") };
    }
}
