package com.sun.opengl.impl.macosx;

import java.awt.Graphics;
import javax.media.opengl.*;
import com.sun.opengl.impl.*;

/** MacOSXGLContext implementation supporting the Java2D/JOGL bridge
 * on Mac OS X. The external GLDrawable mechanism does not work on Mac
 * OS X due to how drawables and contexts are operated upon on this
 * platform, so it is necessary to supply an alternative means to
 * create, make current, and destroy contexts on the Java2D "drawable"
 * on the Mac platform.
 */
public class MacOSXJava2DGLContext extends MacOSXGLContext implements Java2DGLContext {

    private Graphics graphics;

    public MacOSXJava2DGLContext(GLContext shareWith) {
        super(null, shareWith);
    }

    public void setGraphics(Graphics g) {
        this.graphics = g;
    }

    protected int makeCurrentImpl() throws GLException {
        boolean created = false;
        if (nsContext == 0) {
            if (!create()) {
                return CONTEXT_NOT_CURRENT;
            }
            if (DEBUG) {
                System.err.println("!!! Created GL nsContext for " + getClass().getName());
            }
            created = true;
        }
        if (!Java2D.makeOGLContextCurrentOnSurface(graphics, nsContext)) {
            throw new GLException("Error making context current");
        }
        if (created) {
            resetGLFunctionAvailability();
            return CONTEXT_CURRENT_NEW;
        }
        return CONTEXT_CURRENT;
    }

    protected boolean create() {
        MacOSXGLContext other = (MacOSXGLContext) GLContextShareSet.getShareContext(this);
        long share = 0;
        if (other != null) {
            if (other instanceof MacOSXPbufferGLContext) {
                MacOSXPbufferGLContext ctx = (MacOSXPbufferGLContext) other;
                ctx.setOpenGLMode(MacOSXGLDrawable.CGL_MODE);
            } else {
                if (other.getOpenGLMode() != MacOSXGLDrawable.CGL_MODE) {
                    throw new GLException("Can't share between NSOpenGLContexts and CGLContextObjs");
                }
            }
            share = other.getNSContext();
        }
        if (DEBUG) {
            System.err.println("!!! Share context is " + toHexString(share) + " for " + getClass().getName());
        }
        long ctx = Java2D.createOGLContextOnSurface(graphics, share);
        if (ctx == 0) {
            return false;
        }
        nsContext = ctx;
        return true;
    }

    protected void releaseImpl() throws GLException {
    }

    protected void destroyImpl() throws GLException {
        if (nsContext != 0) {
            Java2D.destroyOGLContext(nsContext);
            if (DEBUG) {
                System.err.println("!!! Destroyed OpenGL context " + nsContext);
            }
            nsContext = 0;
        }
    }

    public void setSwapInterval(int interval) {
    }

    public void setOpenGLMode(int mode) {
        if (mode != MacOSXGLDrawable.CGL_MODE) throw new GLException("OpenGL mode switching not supported for Java2D GLContexts");
    }

    public int getOpenGLMode() {
        return MacOSXGLDrawable.CGL_MODE;
    }
}
