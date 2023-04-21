package j3dworkbench.props.common;

import j3dworkbench.core.J3DWorkbenchConstants;
import j3dworkbench.core.J3DWorkbenchUtility;
import j3dworkbench.proxy.NodeProxy;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class TransformProperty extends AbstractProperty {

    public static final String AT = " @ ";

    public static final IPropertyDescriptor PROPDESC_HOME_TRANSFORM = new PropertyDescriptor(AbstractPropertyEnum.HOME_TRANSFORM);

    public static final IPropertyDescriptor PROPDESC_TRANSLATION = new NullButtonPropertyDescriptor(AbstractPropertyEnum.TRANSLATION);

    public static final IPropertyDescriptor PROPDESC_TRANSLATION_FACTORS = new NullButtonPropertyDescriptor(AbstractPropertyEnum.TRANSLATION_FACTORS);

    public static final IPropertyDescriptor PROPDESC_ROTATION = new NullButtonPropertyDescriptor(AbstractPropertyEnum.ROTATION);

    public static final IPropertyDescriptor PROPDESC_ROTATION_FACTORS = new NullButtonPropertyDescriptor(AbstractPropertyEnum.ROTATION_FACTORS);

    public static final IPropertyDescriptor PROPDESC_SCALE = new NullButtonPropertyDescriptor(AbstractPropertyEnum.SCALE);

    public static final IPropertyDescriptor PROPDESC_LOCKED = new BooleanPropertyDescriptor(AbstractPropertyEnum.TRANSFORM_LOCKED);

    StringBuffer stringBuffer = new StringBuffer();

    protected NodeProxy proxy;

    public TransformProperty(NodeProxy p_node) {
        proxy = p_node;
    }

    @Override
    public String getImageIconPath() {
        return "/icons/FlowGraph.gif";
    }

    public NodeProxy getNodeProxy() {
        return proxy;
    }

    @Override
    protected List<IPropertyDescriptor> getDescriptors() {
        List<IPropertyDescriptor> props = new ArrayList<IPropertyDescriptor>(15);
        props.add(PROPDESC_ROTATION);
        if (proxy.isSuppressCorporeal()) {
            return props;
        }
        props.add(PROPDESC_HOME_TRANSFORM);
        props.add(PROPDESC_LOCKED);
        props.add(PROPDESC_TRANSLATION);
        props.add(PROPDESC_SCALE);
        props.add(PROPDESC_TRANSLATION_FACTORS);
        props.add(PROPDESC_ROTATION_FACTORS);
        return props;
    }

    @Override
    public Object getProperty(final Object id) {
        if (!(id instanceof AbstractPropertyEnum)) {
            throw new IllegalArgumentException("No such property:" + id);
        }
        switch((AbstractPropertyEnum) id) {
            case TRANSFORM_LOCKED:
                return proxy.isTransformLocked();
            case TRANSLATION:
                return new Tuple4dProperty(proxy.getPosition());
            case TRANSLATION_FACTORS:
                Tuple3d tuple = new Point3d();
                proxy.getTransFactors(tuple);
                return new Tuple4dProperty(tuple);
            case ROTATION:
                return new EulerToMatrix3dProperty(proxy.getRotation());
            case ROTATION_FACTORS:
                Tuple3d tuple1 = new Point3d();
                proxy.getRotFactors(tuple1);
                return new Tuple4dProperty(tuple1);
            case SCALE:
                return new Tuple4dProperty(proxy.getScale());
            case HOME_TRANSFORM:
                Vector3d vec = proxy.getPosition();
                Matrix3d rot = proxy.getRotation();
                Transform3D t = proxy.getHomeTransform();
                t.get(vec);
                t.get(rot);
                stringBuffer.delete(0, stringBuffer.length());
                stringBuffer.append(J3DWorkbenchUtility.buildXYZString(vec, AbstractProperty.PLACES, J3DWorkbenchConstants.SEPARATOR));
                stringBuffer.append(AT);
                stringBuffer.append(J3DWorkbenchUtility.buildXYZStringNoDecimal(J3DWorkbenchUtility.getDegreeTupleFromMatrix(rot), J3DWorkbenchConstants.DEGREE));
                stringBuffer.append(J3DWorkbenchConstants.DEGREE);
                return stringBuffer.toString();
            default:
                return null;
        }
    }

    @Override
    public void setProperty(final Object id, Object value) {
        if (!(id instanceof AbstractPropertyEnum)) {
            throw new IllegalArgumentException("No such property: " + id);
        }
        switch((AbstractPropertyEnum) id) {
            case TRANSFORM_LOCKED:
                proxy.setTransformLocked((Boolean) value);
                return;
            case ROTATION:
                if (proxy.isCollisionAvoid()) {
                    proxy.setCollisionAvoid(false);
                    proxy.setRotation(((EulerToMatrix3dProperty) value).getMatrix3f());
                    proxy.setCollisionAvoid(true);
                    return;
                }
                proxy.setRotation(((EulerToMatrix3dProperty) value).getMatrix3f());
                return;
            case ROTATION_FACTORS:
                proxy.setRotFactors((Vector3d) ((Tuple4dProperty) value).getVector3d());
                return;
            case TRANSLATION:
                if (proxy.isCollisionAvoid()) {
                    proxy.setCollisionAvoid(false);
                    proxy.setPosition((Vector3d) ((Tuple4dProperty) value).getVector3d());
                    proxy.setCollisionAvoid(true);
                    return;
                }
                proxy.setPosition((Vector3d) ((Tuple4dProperty) value).getVector3d());
                return;
            case TRANSLATION_FACTORS:
                proxy.setTransFactors((Vector3d) ((Tuple4dProperty) value).getVector3d());
                return;
            case SCALE:
                proxy.setScale((Vector3d) ((Tuple4dProperty) value).getVector3d());
                return;
        }
    }

    @Override
    public void resetPropertyValue(Object id) {
        if (!(id instanceof AbstractPropertyEnum)) {
            return;
        }
        switch((AbstractPropertyEnum) id) {
            case TRANSLATION:
                proxy.setPosition(new Vector3d());
                break;
            case ROTATION:
                Matrix3d m3d = proxy.getRotation();
                m3d.setIdentity();
                proxy.setRotation(m3d);
                break;
            case SCALE:
                proxy.setScale(new Vector3d(1, 1, 1));
        }
    }
}
