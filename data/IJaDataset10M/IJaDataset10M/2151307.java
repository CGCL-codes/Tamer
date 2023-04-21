package net.sf.gamine.render;

import net.sf.gamine.common.*;
import javax.microedition.khronos.opengles.*;

/**
 * A DirectionalLight represents light from a distant source, such as the sun.  The light shines entirely
 * in one direction, and illuminates every part of the scene equally independent of position.
 */
public class DirectionalLight extends Light {

    private final Float3 direction;

    private final float tempArray[];

    /**
   * Create a new DirectionalLight.
   */
    public DirectionalLight() {
        direction = new Float3(0.0f, 0.0f, -1.0f);
        tempArray = new float[4];
    }

    /**
   * Store the light direction into an existing Float3.
   *
   * @param dir     the Float3 to store the direction in
   * @return the same Float3 object that was passed to this method
   */
    public Float3 getDirection(Float3 dir) {
        dir.set(direction);
        return dir;
    }

    /**
   * Set the light direction.
   *
   * @param dir    the new light direction
   * @return this object
   */
    public DirectionalLight setDirection(Float3 dir) {
        direction.set(dir);
        return this;
    }

    @Override
    public void prepareLight(RenderContext context, int lightIndex) {
        super.prepareLight(context, lightIndex);
        GL11 gl = context.getGL();
        tempArray[0] = -direction.x;
        tempArray[1] = -direction.y;
        tempArray[2] = -direction.z;
        tempArray[3] = 0.0f;
        gl.glLightfv(lightIndex, GL10.GL_POSITION, tempArray, 0);
    }
}
