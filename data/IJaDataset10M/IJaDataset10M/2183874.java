package si.mk.k3.kbrowser;

import java.util.List;
import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import si.mk.k3.kbrowser.X3DField.FieldID;
import com.sun.j3d.utils.geometry.Sphere;

public class XSphere extends XGeometry {

    private Sphere m_sphere;

    public XSphere() {
        m_sphere = new Sphere();
    }

    public XSphere(String name, double r) {
        super(name);
        m_sphere = new Sphere((float) r, new Appearance());
    }

    public XSphere(double r) {
        m_sphere = new Sphere((float) r, new Appearance());
    }

    @Override
    public void addGeometry(Group group) {
        group.addChild(m_sphere);
    }

    @Override
    public void setAppearance(XAppearance appearance) {
        m_sphere.setAppearance(appearance.getJ3DNodeComponent());
    }

    public void updateField(FieldID fieldID) {
    }

    public void getShape3Ds(List<Shape3D> shapes) {
        shapes.add(m_sphere.getShape(0));
    }
}
