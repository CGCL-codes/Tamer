package j3dworkbench.props.proxy;

import j3dworkbench.props.common.PropertyDescriptor;
import j3dworkbench.props.common.Tuple4dProperty;
import j3dworkbench.proxy.PointLightProxy;
import java.util.List;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class PointLightProperty extends LightProperty {

    protected static final String[] XYZ_ATTENUATION = new String[] { "Constant", "Linear", "Quadratic" };

    private static final String PROP_ATTENUATION = "ATTENUATION";

    private static final String PROP_ATTENUATION_DESCR = "How this light's intensity falls away with distance";

    private static final String[] XYZ_ATTENUATION_DESCR = new String[] { "Attenuates the light at any distance", "Attenuates linearly as factor of distance from light source", "Attenuates as square of factor of distance from light source" };

    public PointLightProperty(PointLightProxy p_node) {
        super(p_node);
    }

    @Override
    public PointLightProxy getProxy() {
        return (PointLightProxy) super.getProxy();
    }

    @Override
    public List<IPropertyDescriptor> getDescriptors() {
        List<IPropertyDescriptor> props = super.getDescriptors();
        props.add(new PropertyDescriptor(PROP_ATTENUATION, PROP_ATTENUATION_DESCR));
        return props;
    }

    @Override
    public Object getProperty(Object id) {
        if (id == PROP_ATTENUATION) {
            return new Tuple4dProperty(new Point3d(getProxy().getAttenuation()), XYZ_ATTENUATION, XYZ_ATTENUATION_DESCR);
        }
        return super.getProperty(id);
    }

    @Override
    public void setProperty(Object id, Object value) {
        if (id == PROP_ATTENUATION) {
            getProxy().getJ3DNode().setAttenuation(new Point3f(((Tuple4dProperty) value).getVector3d()));
            return;
        }
        super.setProperty(id, value);
    }
}
