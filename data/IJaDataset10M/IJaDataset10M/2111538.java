package jogamp.opengl.x11.glx;

import javax.media.nativewindow.*;
import javax.media.nativewindow.x11.*;
import javax.media.opengl.*;
import jogamp.nativewindow.WrappedSurface;

public class X11ExternalGLXDrawable extends X11GLXDrawable {

    private X11ExternalGLXDrawable(GLDrawableFactory factory, NativeSurface surface) {
        super(factory, surface, true);
    }

    protected static X11ExternalGLXDrawable create(GLDrawableFactory factory, GLProfile glp) {
        long context = GLX.glXGetCurrentContext();
        if (context == 0) {
            throw new GLException("Error: current context null");
        }
        long display = GLX.glXGetCurrentDisplay();
        if (display == 0) {
            throw new GLException("Error: current display null");
        }
        long drawable = GLX.glXGetCurrentDrawable();
        if (drawable == 0) {
            throw new GLException("Error: attempted to make an external GLDrawable without a drawable current");
        }
        int[] val = new int[1];
        GLX.glXQueryContext(display, context, GLX.GLX_SCREEN, val, 0);
        X11GraphicsScreen x11Screen = (X11GraphicsScreen) X11GraphicsScreen.createScreenDevice(display, val[0]);
        GLX.glXQueryContext(display, context, GLX.GLX_FBCONFIG_ID, val, 0);
        X11GLXGraphicsConfiguration cfg = X11GLXGraphicsConfiguration.create(glp, x11Screen, val[0]);
        int w, h;
        GLX.glXQueryDrawable(display, drawable, GLX.GLX_WIDTH, val, 0);
        w = val[0];
        GLX.glXQueryDrawable(display, drawable, GLX.GLX_HEIGHT, val, 0);
        h = val[0];
        GLX.glXQueryContext(display, context, GLX.GLX_RENDER_TYPE, val, 0);
        if ((val[0] & GLX.GLX_RGBA_TYPE) == 0) {
            if (DEBUG) {
                System.err.println("X11ExternalGLXDrawable: WARNING: forcing GLX_RGBA_TYPE for newly created contexts (current 0x" + Integer.toHexString(val[0]) + ")");
            }
        }
        WrappedSurface ns = new WrappedSurface(cfg);
        ns.setSurfaceHandle(drawable);
        ns.setSize(w, h);
        return new X11ExternalGLXDrawable(factory, ns);
    }

    public GLContext createContext(GLContext shareWith) {
        return new Context(this, shareWith);
    }

    public void setSize(int newWidth, int newHeight) {
        throw new GLException("Should not call this");
    }

    class Context extends X11GLXContext {

        Context(X11GLXDrawable drawable, GLContext shareWith) {
            super(drawable, shareWith);
        }
    }
}
