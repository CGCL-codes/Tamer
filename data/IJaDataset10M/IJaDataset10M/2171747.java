package makagiga.beans.infos;

import java.beans.PropertyDescriptor;
import org.makagiga.commons.MSmallColorChooser;
import org.makagiga.commons.beans.AbstractBeanInfo;

/**
 * @since 2.0
 */
public final class MSmallColorChooserBeanInfo extends AbstractBeanInfo {

    public MSmallColorChooserBeanInfo() {
        super(MSmallColorChooser.class, "Small Color Chooser");
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return new PropertyDescriptor[] { createPropertyDescriptor("color", BOUND | PREFERRED, "Selected color") };
    }
}
