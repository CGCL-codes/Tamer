package javax.media.opengl;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.geom.*;
import java.beans.*;
import java.lang.reflect.*;
import java.security.*;
import com.sun.opengl.impl.*;

/** A heavyweight AWT component which provides OpenGL rendering
    support. This is the primary implementation of {@link GLDrawable};
    {@link GLJPanel} is provided for compatibility with Swing user
    interfaces when adding a heavyweight doesn't work either because
    of Z-ordering or LayoutManager problems. */
public class GLCanvas extends Canvas implements GLAutoDrawable {

    private static final boolean DEBUG = Debug.debug("GLCanvas");

    private GLDrawableHelper drawableHelper = new GLDrawableHelper();

    private GLDrawable drawable;

    private GLContextImpl context;

    private boolean autoSwapBufferMode = true;

    private boolean sendReshape = false;

    /** Creates a new GLCanvas component with a default set of OpenGL
      capabilities, using the default OpenGL capabilities selection
      mechanism, on the default screen device. */
    public GLCanvas() {
        this(null);
    }

    /** Creates a new GLCanvas component with the requested set of
      OpenGL capabilities, using the default OpenGL capabilities
      selection mechanism, on the default screen device. */
    public GLCanvas(GLCapabilities capabilities) {
        this(capabilities, null, null, null);
    }

    /** Creates a new GLCanvas component. The passed GLCapabilities
      specifies the OpenGL capabilities for the component; if null, a
      default set of capabilities is used. The GLCapabilitiesChooser
      specifies the algorithm for selecting one of the available
      GLCapabilities for the component; a DefaultGLCapabilitesChooser
      is used if null is passed for this argument. The passed
      GLContext specifies an OpenGL context with which to share
      textures, display lists and other OpenGL state, and may be null
      if sharing is not desired. See the note in the overview
      documentation on <a
      href="../../../overview-summary.html#SHARING">context
      sharing</a>. The passed GraphicsDevice indicates the screen on
      which to create the GLCanvas; the GLDrawableFactory uses the
      default screen device of the local GraphicsEnvironment if null
      is passed for this argument. */
    public GLCanvas(GLCapabilities capabilities, GLCapabilitiesChooser chooser, GLContext shareWith, GraphicsDevice device) {
        super(chooseGraphicsConfiguration(capabilities, chooser, device));
        if (!Beans.isDesignTime()) {
            drawable = GLDrawableFactory.getFactory().getGLDrawable(this, capabilities, chooser);
            context = (GLContextImpl) drawable.createContext(shareWith);
            context.setSynchronized(true);
        }
    }

    public GLContext createContext(GLContext shareWith) {
        return drawable.createContext(shareWith);
    }

    public void setRealized(boolean realized) {
    }

    public void display() {
        maybeDoSingleThreadedWorkaround(displayOnEventDispatchThreadAction, displayAction);
    }

    /** Overridden to cause OpenGL rendering to be performed during
      repaint cycles. Subclasses which override this method must call
      super.paint() in their paint() method in order to function
      properly. <P>

      <B>Overrides:</B>
      <DL><DD><CODE>paint</CODE> in class <CODE>java.awt.Component</CODE></DD></DL> */
    public void paint(Graphics g) {
        if (Beans.isDesignTime()) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            FontMetrics fm = g.getFontMetrics();
            String name = getName();
            if (name == null) {
                name = getClass().getName();
                int idx = name.lastIndexOf('.');
                if (idx >= 0) {
                    name = name.substring(idx + 1);
                }
            }
            Rectangle2D bounds = fm.getStringBounds(name, g);
            g.setColor(Color.WHITE);
            g.drawString(name, (int) ((getWidth() - bounds.getWidth()) / 2), (int) ((getHeight() + bounds.getHeight()) / 2));
            return;
        }
        display();
    }

    /** Overridden to track when this component is added to a container.
      Subclasses which override this method must call
      super.addNotify() in their addNotify() method in order to
      function properly. <P>

      <B>Overrides:</B>
      <DL><DD><CODE>addNotify</CODE> in class <CODE>java.awt.Component</CODE></DD></DL> */
    public void addNotify() {
        super.addNotify();
        if (!Beans.isDesignTime()) {
            disableBackgroundErase();
            drawable.setRealized(true);
        }
        if (DEBUG) {
            System.err.println("GLCanvas.addNotify()");
        }
    }

    /** Overridden to track when this component is removed from a
      container. Subclasses which override this method must call
      super.removeNotify() in their removeNotify() method in order to
      function properly. <P>

      <B>Overrides:</B>
      <DL><DD><CODE>removeNotify</CODE> in class <CODE>java.awt.Component</CODE></DD></DL> */
    public void removeNotify() {
        if (Beans.isDesignTime()) {
            super.removeNotify();
        } else {
            try {
                if (Threading.isSingleThreaded() && !Threading.isOpenGLThread()) {
                    if (Threading.isAWTMode() && Thread.holdsLock(getTreeLock())) {
                        destroyAction.run();
                    } else {
                        Threading.invokeOnOpenGLThread(destroyAction);
                    }
                } else {
                    destroyAction.run();
                }
            } finally {
                drawable.setRealized(false);
                super.removeNotify();
                if (DEBUG) {
                    System.err.println("GLCanvas.removeNotify()");
                }
            }
        }
    }

    /** Overridden to cause {@link GLDrawableHelper#reshape} to be
      called on all registered {@link GLEventListener}s. Subclasses
      which override this method must call super.reshape() in
      their reshape() method in order to function properly. <P>

      <B>Overrides:</B>
      <DL><DD><CODE>reshape</CODE> in class <CODE>java.awt.Component</CODE></DD></DL> */
    public void reshape(int x, int y, int width, int height) {
        super.reshape(x, y, width, height);
        sendReshape = true;
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void addGLEventListener(GLEventListener listener) {
        drawableHelper.addGLEventListener(listener);
    }

    public void removeGLEventListener(GLEventListener listener) {
        drawableHelper.removeGLEventListener(listener);
    }

    public GLContext getContext() {
        return context;
    }

    public GL getGL() {
        if (Beans.isDesignTime()) {
            return null;
        }
        return getContext().getGL();
    }

    public void setGL(GL gl) {
        if (!Beans.isDesignTime()) {
            getContext().setGL(gl);
        }
    }

    public void setAutoSwapBufferMode(boolean onOrOff) {
        drawableHelper.setAutoSwapBufferMode(onOrOff);
    }

    public boolean getAutoSwapBufferMode() {
        return drawableHelper.getAutoSwapBufferMode();
    }

    public void swapBuffers() {
        maybeDoSingleThreadedWorkaround(swapBuffersOnEventDispatchThreadAction, swapBuffersAction);
    }

    public GLCapabilities getChosenGLCapabilities() {
        if (drawable == null) return null;
        return drawable.getChosenGLCapabilities();
    }

    private void maybeDoSingleThreadedWorkaround(Runnable eventDispatchThreadAction, Runnable invokeGLAction) {
        if (Threading.isSingleThreaded() && !Threading.isOpenGLThread()) {
            Threading.invokeOnOpenGLThread(eventDispatchThreadAction);
        } else {
            drawableHelper.invokeGL(drawable, context, invokeGLAction, initAction);
        }
    }

    class InitAction implements Runnable {

        public void run() {
            drawableHelper.init(GLCanvas.this);
        }
    }

    private InitAction initAction = new InitAction();

    class DisplayAction implements Runnable {

        public void run() {
            if (sendReshape) {
                int width = getWidth();
                int height = getHeight();
                getGL().glViewport(0, 0, width, height);
                drawableHelper.reshape(GLCanvas.this, 0, 0, width, height);
                sendReshape = false;
            }
            drawableHelper.display(GLCanvas.this);
        }
    }

    private DisplayAction displayAction = new DisplayAction();

    class SwapBuffersAction implements Runnable {

        public void run() {
            drawable.swapBuffers();
        }
    }

    private SwapBuffersAction swapBuffersAction = new SwapBuffersAction();

    class DisplayOnEventDispatchThreadAction implements Runnable {

        public void run() {
            drawableHelper.invokeGL(drawable, context, displayAction, initAction);
        }
    }

    private DisplayOnEventDispatchThreadAction displayOnEventDispatchThreadAction = new DisplayOnEventDispatchThreadAction();

    class SwapBuffersOnEventDispatchThreadAction implements Runnable {

        public void run() {
            drawableHelper.invokeGL(drawable, context, swapBuffersAction, initAction);
        }
    }

    private SwapBuffersOnEventDispatchThreadAction swapBuffersOnEventDispatchThreadAction = new SwapBuffersOnEventDispatchThreadAction();

    class DestroyAction implements Runnable {

        public void run() {
            GLContext current = GLContext.getCurrent();
            if (current == context) {
                context.release();
            }
            context.destroy();
        }
    }

    private DestroyAction destroyAction = new DestroyAction();

    private static boolean disableBackgroundEraseInitialized;

    private static Method disableBackgroundEraseMethod;

    private void disableBackgroundErase() {
        if (!disableBackgroundEraseInitialized) {
            try {
                AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        try {
                            disableBackgroundEraseMethod = getToolkit().getClass().getDeclaredMethod("disableBackgroundErase", new Class[] { Canvas.class });
                            disableBackgroundEraseMethod.setAccessible(true);
                        } catch (Exception e) {
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
            }
            disableBackgroundEraseInitialized = true;
        }
        if (disableBackgroundEraseMethod != null) {
            try {
                disableBackgroundEraseMethod.invoke(getToolkit(), new Object[] { this });
            } catch (Exception e) {
            }
        }
    }

    private static GraphicsConfiguration chooseGraphicsConfiguration(GLCapabilities capabilities, GLCapabilitiesChooser chooser, GraphicsDevice device) {
        if (Beans.isDesignTime()) {
            return null;
        }
        AWTGraphicsConfiguration config = (AWTGraphicsConfiguration) GLDrawableFactory.getFactory().chooseGraphicsConfiguration(capabilities, chooser, new AWTGraphicsDevice(device));
        if (config == null) {
            return null;
        }
        return config.getGraphicsConfiguration();
    }
}
