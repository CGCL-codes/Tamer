package jogamp.opengl.macosx.cgl.awt;

import jogamp.opengl.macosx.cgl.*;
import java.awt.Graphics;
import javax.media.opengl.*;
import jogamp.opengl.*;
import jogamp.opengl.awt.*;
import jogamp.opengl.macosx.cgl.*;

/** MacOSXCGLContext implementation supporting the Java2D/JOGL bridge
 * on Mac OS X. The external GLDrawable mechanism does not work on Mac
 * OS X due to how drawables and contexts are operated upon on this
 * platform, so it is necessary to supply an alternative means to
 * create, make current, and destroy contexts on the Java2D "drawable"
 * on the Mac platform.
 */
public class MacOSXJava2DCGLContext extends MacOSXCGLContext implements Java2DGLContext {

    private Graphics graphics;

    MacOSXJava2DCGLContext(GLContext shareWith) {
        super(null, shareWith);
    }

    public void setGraphics(Graphics g) {
        this.graphics = g;
    }

    protected void makeCurrentImpl(boolean newCreated) throws GLException {
        if (!Java2D.makeOGLContextCurrentOnSurface(graphics, contextHandle)) {
            throw new GLException("Error making context current");
        }
    }

    protected boolean createImpl() {
        MacOSXCGLContext other = (MacOSXCGLContext) GLContextShareSet.getShareContext(this);
        long share = 0;
        if (other != null) {
            if (other instanceof MacOSXPbufferCGLContext) {
                MacOSXPbufferCGLContext ctx = (MacOSXPbufferCGLContext) other;
                ctx.setOpenGLMode(MacOSXCGLDrawable.CGL_MODE);
            } else {
                if (other.getOpenGLMode() != MacOSXCGLDrawable.CGL_MODE) {
                    throw new GLException("Can't share between NSOpenGLContexts and CGLContextObjs");
                }
            }
            share = other.getHandle();
        }
        if (DEBUG) {
            System.err.println("!!! Share context is " + toHexString(share) + " for " + getClass().getName());
        }
        long ctx = Java2D.createOGLContextOnSurface(graphics, share);
        if (ctx == 0) {
            return false;
        }
        setGLFunctionAvailability(true, 0, 0, CTX_PROFILE_COMPAT | CTX_OPTION_ANY);
        contextHandle = ctx;
        isNSContext = true;
        return true;
    }

    protected void releaseImpl() throws GLException {
    }

    protected void destroyImpl() throws GLException {
        Java2D.destroyOGLContext(contextHandle);
        if (DEBUG) {
            System.err.println("!!! Destroyed OpenGL context " + contextHandle);
        }
    }

    public void setOpenGLMode(int mode) {
        if (mode != MacOSXCGLDrawable.CGL_MODE) throw new GLException("OpenGL mode switching not supported for Java2D GLContexts");
    }

    public int getOpenGLMode() {
        return MacOSXCGLDrawable.CGL_MODE;
    }
}
