package si.mk.k3.kbrowser;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.Node;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 * This class implements directional light. It is not implemented correctly 
 * according to X3d specification, but more closely to Java3D lights. This light
 * illuminates object in its BoundignSphere (Java3D like), instead of objects 
 * in its parent group (X3D specification).
 * The reason is not only simpler implementation but also simpler usage in KEna
 * scene graph.
 */
public class X3DDirectionalLight extends X3DNodeImpl {

    private Light m_light;

    private Color3f m_color;

    public X3DDirectionalLight() {
        m_light = new DirectionalLight();
        m_color = new Color3f(1.f, 1.f, 1.f);
    }

    public X3DDirectionalLight(Color3f color, Vector3f direction) {
        m_light = new DirectionalLight(color, direction);
        m_color = new Color3f(color);
    }

    public X3DDirectionalLight(String nodeName, float ambientIntensity, Color3f color, Vector3f direction, float intensity, boolean isEnabled, float radius) {
        super(nodeName);
        m_color = new Color3f(color);
        Color3f attenuatedColor = new Color3f(color);
        attenuatedColor.scale(intensity);
        m_light = new DirectionalLight(attenuatedColor, direction);
        m_light.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), radius));
        m_light.setEnable(isEnabled);
    }

    public void updateField(X3DField.FieldID fieldID) {
    }

    public Node getJ3DNode() {
        return m_light;
    }
}
