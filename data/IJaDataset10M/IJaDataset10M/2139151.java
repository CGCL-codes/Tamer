package org.xith3d.render.jsr231;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * This class holds a reference to a shared GLU object.
 * 
 * As of jsr231, the GLU object can be instanciated from everywhere, but calls
 * to GLU routines must be done when the GL Context is available.
 * 
 * @author Lilian Chamontin
 * @author Marvin Froehlich (aka Qudus)
 * @author Julien Gouesse [JOGL 2.0 port]
 */
public class GLUSingleton {

    private static GLU instance = new GLUgl2();

    private static GLUT glutInstance = new GLUT();

    /**
     * Creates a new instance of GLUSingleton
     */
    private GLUSingleton() {
    }

    /**
     * @return the shared instance of the GLU used by this package objects.
     */
    public static GLU instance() {
        return (instance);
    }

    /**
     * @return the shared instance of the GLU used by this package objects.
     */
    public static GLUT getGLUT() {
        return (glutInstance);
    }
}
