package homura.hde.core.scene.light;

import homura.hde.util.export.InputCapsule;
import homura.hde.util.export.JMEExporter;
import homura.hde.util.export.JMEImporter;
import homura.hde.util.export.OutputCapsule;
import homura.hde.util.maths.Vector3f;
import java.io.IOException;

/**
 * <code>DirectionalLight</code> defines a light that is assumed to be
 * infintely far away (something similar to the sun). This means the direction
 * of the light rays are all parallel. The direction the light is coming from
 * is defined by the class.
 * @author Mark Powell
 * @version $Id: DirectionalLight.java,v 1.9 2007/09/21 15:45:30 nca Exp $
 */
public class DirectionalLight extends Light {

    private static final long serialVersionUID = 1L;

    private Vector3f direction;

    /**
     * Constructor instantiates a new <code>DirectionalLight</code> object.
     * The initial light colors are white and the direction the light emits
     * from is (0,0,0).
     *
     */
    public DirectionalLight() {
        super();
        direction = new Vector3f();
    }

    /**
     * <code>getDirection</code> returns the direction the light is
     * emitting from.
     * @return the direction the light is emitting from.
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * <code>setDirection</code> sets the direction the light is emitting from.
     * @param direction the direction the light is emitting from.
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    /**
     * <code>getType</code> returns this light's type (LT_DIRECTIONAL).
     * @see homura.hde.core.scene.light.Light#getType()
     */
    public int getType() {
        return LT_DIRECTIONAL;
    }

    public void write(JMEExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(direction, "direction", Vector3f.ZERO);
    }

    public void read(JMEImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        direction = (Vector3f) capsule.readSavable("direction", Vector3f.ZERO.clone());
    }
}
