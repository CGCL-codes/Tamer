package at.ac.tuwien.j3dvnaddoncollection.visual;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import com.sun.j3d.utils.geometry.Primitive;
import at.ac.tuwien.j3dvn.control.Connector;
import at.ac.tuwien.j3dvn.control.DataConnector;
import at.ac.tuwien.j3dvn.control.IProperty;
import at.ac.tuwien.j3dvn.control.Property;
import at.ac.tuwien.j3dvn.view.VisualAddon;

/**
 *
 */
public class Sphere implements VisualAddon {

    public static final String NAME = "Sphere";

    public static final String COLOR_ATTRIBUTE = "color";

    private final com.sun.j3d.utils.geometry.Sphere spherePrimitive;

    private final TransformGroup transformGroup;

    private DataConnector data;

    private IProperty<Color3f> pColor = new IProperty<Color3f>() {

        public Color3f getValue() {
            Color3f result = new Color3f();
            spherePrimitive.getAppearance().getMaterial().getAmbientColor(result);
            return result;
        }

        public void setValue(Color3f value) {
            Material newMaterial = new Material();
            newMaterial.setAmbientColor(value);
            Appearance newAppearance = new Appearance();
            newAppearance.setMaterial(newMaterial);
            spherePrimitive.setAppearance(newAppearance);
        }
    };

    public Sphere() {
        spherePrimitive = new com.sun.j3d.utils.geometry.Sphere(1, Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY | Primitive.ENABLE_GEOMETRY_PICKING, 30, null);
        spherePrimitive.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        spherePrimitive.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        Color3f newColor = new Color3f(1, 1, .5f);
        Material newMaterial = new Material();
        newMaterial.setAmbientColor(newColor);
        Appearance newAppearance = new Appearance();
        newAppearance.setMaterial(newMaterial);
        spherePrimitive.setAppearance(newAppearance);
        transformGroup = new TransformGroup();
        transformGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        transformGroup.addChild(spherePrimitive);
    }

    public Group getGroup() {
        return transformGroup;
    }

    public DataConnector getModelData() {
        return data;
    }

    public void setModelData(DataConnector modelData) {
        data = modelData;
    }

    public String getName() {
        return NAME;
    }

    public <T> void propertyChanged(Connector sender, String propertyName, T oldValue, T newValue) {
    }

    @Property(COLOR_ATTRIBUTE)
    public IProperty<Color3f> color() {
        return pColor;
    }
}
