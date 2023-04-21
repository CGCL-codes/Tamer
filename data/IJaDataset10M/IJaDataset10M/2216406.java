package tracer.scene.shader;

import tracer.basicdatatypes.Color;
import tracer.scene.primitives.Hitpoint;
import tracer.scene.shader.AbstractColorShader;

/**
 * @author martin
 * Implements a Color shader with basic surface color and diffuse and
 * reflection properties.
 */
public class ColorShader extends AbstractColorShader {

    /**
	 * The surface color.
	 */
    private Color color;

    /**
	 * Creates a new ColorShader with surface color and diffuse/reflection
	 * properties.
	 * @param col The Color of the surface
	 * @param dif The diffuse property of the surface
	 * @param ref The reflection property of the surface
	 */
    public ColorShader(final Color col, final float dif, final float ref) {
        this.color = col;
        this.diffuse = dif;
        this.reflection = ref;
    }

    @Override
    public Color getColor(Hitpoint hitpoint) {
        return this.color;
    }
}
