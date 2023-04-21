package org.makagiga.commons;

import java.beans.PropertyDescriptor;
import org.makagiga.commons.beans.AbstractBeanInfo;

/**
 * @since 3.8.6
 */
public final class MToggleButtonBeanInfo extends AbstractBeanInfo {

    public MToggleButtonBeanInfo() {
        super(MToggleButton.class, "Toggle Button");
        setIconName("ui/ok");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[] { createIconNamePropertyDescriptor() };
    }
}
