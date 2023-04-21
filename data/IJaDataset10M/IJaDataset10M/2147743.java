package opengl.helper;

import java.awt.Color;
import javax.media.opengl.GL;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public final class DrawHelper {

    /**
	 * Avoids instanciation of utility class.
	 */
    private DrawHelper() {
    }

    /**
	 * Draws the axes of a coordinate system.
	 * @param gl the OpenGL graphics context
	 * @param length the length of the axes
	 * @param x the color of the first or {@code x}-axis
	 * @param y the color of the second or {@code y}-axis
	 * @param z the color of the third or {@code z}-axis
	 */
    public static void drawAxes(GL gl, double length, Color x, Color y, Color z) {
        gl.glBegin(GL.GL_LINES);
        gl.glColor4d(x.getRed() / 255., x.getGreen() / 255., x.getBlue() / 255., x.getAlpha() / 255.);
        gl.glVertex3d(-length, 0, 0);
        gl.glVertex3d(length, 0, 0);
        gl.glColor4d(y.getRed() / 255., y.getGreen() / 255., y.getBlue() / 255., y.getAlpha() / 255.);
        gl.glVertex3d(0, length, 0);
        gl.glVertex3d(0, -length, 0);
        gl.glColor4d(z.getRed() / 255., z.getGreen() / 255., z.getBlue() / 255., z.getAlpha() / 255.);
        gl.glVertex3d(0, 0, length);
        gl.glVertex3d(0, 0, -length);
        gl.glEnd();
    }
}
