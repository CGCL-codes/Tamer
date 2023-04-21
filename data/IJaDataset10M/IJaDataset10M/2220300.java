package gestalt.impl.jogl.shape.atom;

import javax.media.opengl.GL;
import static gestalt.Gestalt.*;
import gestalt.util.JoglUtil;

/** @todo create display lists for each origin. */
public class JoglAtomDisk {

    public static boolean USE_DISPLAY_LISTS = true;

    private static boolean _myIsCompiled = false;

    private static int _myDisplayList;

    public static final void draw(GL gl, int theOrigin) {
        gl.glPushMatrix();
        JoglUtil.applyOrigin(gl, theOrigin);
        if (USE_DISPLAY_LISTS) {
            if (!_myIsCompiled) {
                _myIsCompiled = true;
                _myDisplayList = gl.glGenLists(1);
                gl.glNewList(_myDisplayList, GL.GL_COMPILE);
                drawCircle(gl, 360 / 10);
                gl.glEndList();
            }
            if (_myIsCompiled) {
                gl.glCallList(_myDisplayList);
            }
        } else {
            drawCircle(gl, 360 / 10);
        }
    }

    private static void drawCircle(GL gl, float theRadius) {
        final float myResolution = TWO_PI / (theRadius / (PI / 4.0f));
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0, 0, 1);
        gl.glTexCoord2f(0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0);
        for (float i = 0; i < TWO_PI; i += myResolution) {
            final float x = (float) Math.sin(i) / 2 + 0.5f;
            final float y = (float) Math.cos(i) / 2 + 0.5f;
            gl.glTexCoord2f(x, y);
            gl.glVertex3f(x, y, 0);
        }
        gl.glTexCoord2f(0.5f, 1.0f);
        gl.glVertex3f(0.5f, 1.0f, 0);
        gl.glEnd();
        gl.glPopMatrix();
    }

    public static final void cleanup(GL gl) {
        if (USE_DISPLAY_LISTS) {
            gl.glDeleteLists(_myDisplayList, 1);
        }
    }
}
