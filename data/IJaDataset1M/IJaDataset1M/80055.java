package j3dworkbench.props.proxy;

import j3dworkbench.props.appearance.AppearanceProperty;
import j3dworkbench.props.common.AbstractPropertyEnum;
import j3dworkbench.props.common.BooleanPropertyDescriptor;
import j3dworkbench.props.common.PropertyDescriptor;
import j3dworkbench.proxy.Shape3DProxy;
import java.util.List;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertySheetEntry;

public class Shape3DProperty extends NodeProperty {

    private static final String APPEARANCE = "APPEARANCE";

    public static final PropertyDescriptor PROPDESC_DIMENSIONAL_BOUNDS = new PropertyDescriptor(AbstractPropertyEnum.BOUNDS_DIMENSIONS, AbstractPropertyEnum.BOUNDS_DIMENSIONS.getDescription());

    public Shape3DProperty(Shape3DProxy p_node) {
        super(p_node);
    }

    @Override
    public Shape3DProxy getProxy() {
        return (Shape3DProxy) super.getProxy();
    }

    @Override
    public String getImageIconPath() {
        return "/icons/Object.gif";
    }

    @Override
    public List<IPropertyDescriptor> getDescriptors() {
        List<IPropertyDescriptor> props = super.getDescriptors();
        props.add(new PropertyDescriptor(APPEARANCE, "A set of properties such as material, rendering, & transparency,"));
        return props;
    }

    @Override
    public Object getProperty(Object id) {
        if (id == APPEARANCE) {
            return new AppearanceProperty(getProxy().getJ3DNode().getAppearance());
        }
        return super.getProperty(id);
    }

    @Override
    public void setProperty(final Object id, final Object value) {
        if (id == APPEARANCE) {
            return;
        }
        super.setProperty(id, value);
    }
}
