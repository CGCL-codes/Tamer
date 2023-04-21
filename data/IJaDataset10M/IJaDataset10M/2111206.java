package net.cevn.gl;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

/**
 * The <code>Color</code> class extends the AWT Color class to make it easier to obtain color information
 * specific for rendering with OpenGL.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class GLColor extends java.awt.Color {

    /**
	 * The red color component.
	 */
    private static final int RED_COMP = 0;

    /**
	 * The green color component.
	 */
    private static final int GREEN_COMP = 1;

    /**
	 * The blue color component.
	 */
    private static final int BLUE_COMP = 2;

    /**
	 * The alpha channel.
	 */
    private static final int ALPHA_COMP = 3;

    /**
	 * Serial ID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Predefined black color.
	 */
    public static final GLColor BLACK = new GLColor(java.awt.Color.BLACK);

    /**
	 * Predefined blue color.
	 */
    public static final GLColor BLUE = new GLColor(java.awt.Color.BLUE);

    /**
	 * Predefined cyan color.
	 */
    public static final GLColor CYAN = new GLColor(java.awt.Color.CYAN);

    /**
	 * Predefined dark gray color.
	 */
    public static final GLColor DARK_GRAY = new GLColor(java.awt.Color.DARK_GRAY);

    /**
	 * Predefined gray color.
	 */
    public static final GLColor GRAY = new GLColor(java.awt.Color.GRAY);

    /**
	 * Predefined green color.
	 */
    public static final GLColor GREEN = new GLColor(java.awt.Color.GREEN);

    /**
	 * Predefined light gray color.
	 */
    public static final GLColor LIGHT_GRAY = new GLColor(java.awt.Color.LIGHT_GRAY);

    /**
	 * Predefined magenta color.
	 */
    public static final GLColor MAGENTA = new GLColor(java.awt.Color.MAGENTA);

    /**
	 * Predefined pink color.
	 */
    public static final GLColor PINK = new GLColor(java.awt.Color.PINK);

    /**
	 * Predefined orange color.
	 */
    public static final GLColor ORANGE = new GLColor(java.awt.Color.ORANGE);

    /**
	 * Predefined red color.
	 */
    public static final GLColor RED = new GLColor(java.awt.Color.RED);

    /**
	 * Predefined white color.
	 */
    public static final GLColor WHITE = new GLColor(java.awt.Color.WHITE);

    /**
	 * Predefined yellow color.
	 */
    public static final GLColor YELLOW = new GLColor(java.awt.Color.YELLOW);

    /**
	 * Creates a new <code>Color</code> instance.
	 * 
	 * @param red The amount of red, where 1.0f is all red and 0.0f is no red.
	 * @param green The amount of green, where 1.0f is all red and 0.0f is no green.
	 * @param blue The amount of blue, where 1.0f is all red and 0.0f is no blue.
	 */
    public GLColor(final float red, final float green, final float blue) {
        super(red, green, blue);
    }

    /**
	 * Creates a new <code>Color</code> instance based on integer values of the RGB.
	 * 
	 * @param red The amount of red, where 255 is all red and 0 is no red.
	 * @param green The amount of green, where 255 is all green and 0 is no green.
	 * @param blue The amount of blue, where 255 is all blue and 0 is no blue.
	 */
    public GLColor(final int red, final int green, final int blue) {
        super(red, green, blue);
    }

    /**
	 * Creates a new <code>Color</code> instance from a AWT Color. Primarly used
	 * to create the predefined color constants.
	 * 
	 * @param color The AWT color.
	 */
    public GLColor(final Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
	 * Gets a color from the color components of the parent class.
	 * 
	 * @param color Either the <code>RED</code>, <code>GREEN</code>, <code>BLUE</code>, or <code>ALPHA</code> constant.
	 * @return The fraction of the color in the total color where 1.0f is all and 0.0f is none.
	 */
    private float getColor(final int color) {
        final float[] colorComps = getColorComponents(null);
        return colorComps[color];
    }

    /**
	 * Gets the red. The value is between 0.0f and 1.0f.
	 * 
	 * @return The red color, no greater than 1.0f.
	 */
    public float getGLRed() {
        return getColor(RED_COMP);
    }

    /**
	 * Gets the green. The value is between 0.0f and 1.0f.
	 * 
	 * @return The green color, no greater than 1.0f.
	 */
    public float getGLGreen() {
        return getColor(GREEN_COMP);
    }

    /**
	 * Gets the blue. The value is between 0.0f and 1.0f.
	 * 
	 * @return The blue color, no greater than 1.0f.
	 */
    public float getGLBlue() {
        return getColor(BLUE_COMP);
    }

    /**
	 * Gets the alpha. The value is between 0.0f and 1.0f.
	 * 
	 * @return The alpha color, no greater than 1.0f.
	 */
    public float getGLAlpha() {
        return getColor(ALPHA_COMP);
    }

    /**
	 * Sets the current OpenGL color to this color minus the alpha channel.
	 */
    public void setGLColor() {
        GL11.glColor3f(getGLRed(), getGLGreen(), getGLBlue());
    }
}
