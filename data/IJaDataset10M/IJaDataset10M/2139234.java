package com.google.devtools.depan.eclipse.visualization.ogl;

import com.sun.opengl.util.Screenshot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.glu.GLU;

/**
 * An abstract GLScene. Handling OpenGL specific details.
 *
 * @author Yohann Coppel
 */
public abstract class GLScene {

    public static final float FACTOR = 1f;

    private static final int[] EMPTY_HIT_LIST = new int[0];

    private static final Logger logger = Logger.getLogger(GLScene.class.getName());

    private SceneGrip grip;

    private GLCanvas canvas;

    private final GLContext context;

    public final GL gl;

    public final GLU glu;

    public static boolean hyperbolic = false;

    /** Latest received mouse positions. */
    private int mouseX, mouseY;

    /** Latest mouse coordinate when rectangle selection started */
    private boolean drawSelectRectangle = false;

    private int startSelectX, startSelectY;

    private int selectionWidth, selectionHeight;

    private Color foregroundColor = Color.WHITE;

    public GLScene(Composite parent) {
        GLData data = new GLData();
        data.doubleBuffer = true;
        data.depthSize = 16;
        canvas = new GLCanvas(parent, SWT.NO_BACKGROUND, data);
        canvas.setCurrent();
        context = createGLContext();
        gl = context.getGL();
        glu = new GLU();
        this.canvas.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                resizeScene();
            }
        });
        this.canvas.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                dispose();
            }
        });
        grip = new SceneGrip(this);
        canvas.addMouseListener(grip);
        canvas.addMouseMoveListener(grip);
        canvas.addKeyListener(grip);
        canvas.addMouseWheelListener(grip);
        this.init();
    }

    /**
   * @return
   */
    private GLContext createGLContext() {
        try {
            logger.info("Create context...");
            GLContext result = GLDrawableFactory.getFactory().createExternalGLContext();
            logger.info("    Done.");
            return result;
        } catch (Throwable errGl) {
            errGl.printStackTrace();
            throw new RuntimeException(errGl);
        }
    }

    protected void resizeScene() {
        Rectangle rect = canvas.getClientArea();
        int width = rect.width;
        int height = Math.max(rect.height, 1);
        float aspect = (float) width / (float) height;
        canvas.setCurrent();
        context.makeCurrent();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(90.0f, aspect, 0.4f, 1000.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        context.release();
    }

    protected void dispose() {
        if (this.canvas != null) {
            this.canvas.dispose();
            this.canvas = null;
        }
    }

    protected void init() {
        this.initGLContext();
        this.initGL();
    }

    protected void initGLContext() {
        this.canvas.setCurrent();
    }

    protected void initGL() {
        context.makeCurrent();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        context.release();
    }

    public void render(float elapsedTime) {
        if (!this.canvas.isCurrent()) {
            this.canvas.setCurrent();
        }
        context.makeCurrent();
        gl.glPushMatrix();
        drawScene(elapsedTime);
        gl.glPopMatrix();
        if (drawSelectRectangle) {
            drawRectangle();
        }
        canvas.swapBuffers();
        context.release();
    }

    private int[] renderWithPicking() {
        if (!this.canvas.isCurrent()) {
            this.canvas.setCurrent();
        }
        context.makeCurrent();
        int[] viewPort = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort, 0);
        IntBuffer selectBuffer = getSelectBuffer();
        gl.glSelectBuffer(selectBuffer.capacity(), selectBuffer);
        gl.glRenderMode(GL.GL_SELECT);
        gl.glInitNames();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluPickMatrix(mouseX, viewPort[3] - mouseY, selectionWidth, selectionHeight, viewPort, 0);
        Rectangle rect = canvas.getClientArea();
        int width = rect.width;
        int height = Math.max(rect.height, 1);
        float aspect = (float) width / (float) height;
        glu.gluPerspective(90.0f, aspect, 1.0f, 1000.0f);
        drawScene(0f);
        gl.glPopMatrix();
        gl.glFlush();
        int hits = gl.glRenderMode(GL.GL_RENDER);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        context.release();
        return processHits(hits, selectBuffer);
    }

    public void printMousePos(int x, int y) {
        double[] m = getOGLPos(x, y);
        logger.info("Mouse " + m[0] + " : " + m[1] + " - " + m[2]);
    }

    public double[] getOGLPos(int x, int y) {
        return GLScene.getOGLPos(gl, glu, grip, x, y);
    }

    public int[] getViewport() {
        int[] viewPort = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort, 0);
        return viewPort;
    }

    public GLRegion getOGLViewport() {
        int[] osViewport = getViewport();
        double[] topLeft = getOGLPos(osViewport[0], osViewport[1]);
        double[] bottomRight = getOGLPos(osViewport[2], osViewport[3]);
        return new GLRegion(topLeft[0], topLeft[1], bottomRight[0], bottomRight[1]);
    }

    /**
   * Return the position in the openGL coordinates, given a window-relative
   * position. For instance to know what are the coordinate for the point
   * under the mouse, call this method with the mouse position relative to
   * the window.
   *
   * @param gl
   * @param glu
   * @param grip
   * @param x
   * @param y
   * @return
   */
    public static double[] getOGLPos(GL gl, GLU glu, SceneGrip grip, int x, int y) {
        int[] viewport = new int[4];
        double[] modelview = new double[16];
        double[] projection = new double[16];
        double[] wcoord0 = new double[3];
        double[] wcoord1 = new double[3];
        gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, modelview, 0);
        gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projection, 0);
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
        double winX = x;
        double winY = (double) viewport[3] - (double) y;
        glu.gluUnProject(winX, winY, 0, modelview, 0, projection, 0, viewport, 0, wcoord0, 0);
        glu.gluUnProject(winX, winY, 1.0, modelview, 0, projection, 0, viewport, 0, wcoord1, 0);
        double[] vector = { wcoord1[0] - wcoord0[0], wcoord1[1] - wcoord0[1], wcoord1[2] - wcoord0[2] };
        double[] norm = { vector[0] / vector[2], vector[1] / vector[2], 1.0f };
        float[] camera = grip.getCameraPosition();
        double[] res = { camera[0] + (-camera[2]) * norm[0], camera[1] + (-camera[2]) * norm[1], 0f };
        return res;
    }

    /**
   * Provide an IntBuffer that can be used to select from the current scene.
   * It should have enough room to allow every OGL entity in the scene to
   * be selected.
   * <p>
   * Ownership of the buffer remains with the provider (e.g. derived class),
   * and access is thread-safe.  But it should be a good place to dump the OGL
   * selection data.
   *
   * @return IntBuffer to collect OGL selection data
   */
    protected abstract IntBuffer getSelectBuffer();

    protected abstract boolean isSelected(int id);

    /**
   * Activate selection rendering for the exactly the listed nodes.
   */
    protected abstract void setSelection(int[] ids);

    /**
   * Turn on selection rendering for the listed nodes.
   */
    protected abstract void extendSelection(int[] ids);

    /**
   * Turn off selection rendering for the listed nodes.
   */
    protected abstract void reduceSelection(int[] ids);

    /**
   * Move every selected objects relatively. Movement is given relatively
   * to the openGL coordinates.
   *
   * @param dx movement on x axis.
   * @param dy movement on y axis.
   */
    protected abstract void moveSelectionDelta(double dx, double dy);

    /**
   * Move every selected objects relatively. Movement is given relatively
   * to window coordinates.
   *
   * @param x movement on x axis.
   * @param y movement on y axis.
   */
    public void moveSelectedObjectsTo(int x, int y) {
        double[] worldPosOrigin = getOGLPos(0, 0);
        double[] worldPos = getOGLPos(x, y);
        moveSelectionDelta(-worldPos[0] + worldPosOrigin[0], -worldPos[1] + worldPosOrigin[1]);
    }

    /**
   * A key stroke was received by the OpenGL canvas but it wasn't used by the
   * previous layers.  Give someone else a chance to use it.
   *
   * @param keyCode
   * @param character
   * @param keyCtrlState true if Ctrl key is pressed
   * @param keyAltState true if Alt key is pressed
   * @param keyShiftState true if Shift key is pressed
   */
    public abstract void uncaughtKey(int keyCode, char character, boolean keyCtrlState, boolean keyAltState, boolean keyShiftState);

    /**
   * Retrieve the openGL ids that were hited by a previous picking operation.
   *
   * @param hits
   * @param buffer
   * @return
   */
    private int[] processHits(int hits, IntBuffer buffer) {
        if (hits == 0) {
            return EMPTY_HIT_LIST;
        }
        if (hits < 0) {
            logger.warning("Too many hits!!" + " IntBuffer capacity = " + buffer.capacity());
            return EMPTY_HIT_LIST;
        }
        int[] hitsResults = new int[hits];
        int offset = 0;
        int names;
        for (int i = 0; i < hits; i++) {
            names = buffer.get(offset);
            offset++;
            offset++;
            offset++;
            for (int j = 0; j < names; j++) {
                if (j == (names - 1)) {
                    hitsResults[i] = buffer.get(offset);
                }
                offset++;
            }
        }
        logger.fine("hits = " + hits + "; offset = " + offset);
        return hitsResults;
    }

    /**
   * Pick the object at the given mouse position (in window-relative
   * coordinates)
   *
   * @param mouseX
   * @param mouseY
   * @return ids of picked objects.
   */
    public int[] pickObjectsAt(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.selectionWidth = 1;
        this.selectionHeight = 1;
        return renderWithPicking();
    }

    /**
   * The same as pickObjectAt, but with a rectangle.
   *
   * @param startX
   * @param startY
   * @param toX
   * @param toY
   * @return ids of picked objects.
   */
    public int[] pickRectangle(int startX, int startY, int toX, int toY) {
        if (startX < toX) {
            this.selectionWidth = toX - startX;
            this.mouseX = startX + selectionWidth / 2;
        } else {
            this.selectionWidth = startX - toX;
            this.mouseX = toX + selectionWidth / 2;
        }
        if (startY < toY) {
            this.selectionHeight = toY - startY;
            this.mouseY = startY + selectionHeight / 2;
        } else {
            this.selectionHeight = startY - toY;
            this.mouseY = toY + selectionHeight / 2;
        }
        drawSelectRectangle = false;
        return renderWithPicking();
    }

    /**
   * Activate and define an overlay rectangle that shows the selection area.
   *
   * @param fromX
   * @param fromY
   * @param mouseX
   * @param mouseY
   */
    public void activateSelectionRectangle(int fromX, int fromY, int mouseX, int mouseY) {
        drawSelectRectangle = true;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.startSelectX = fromX;
        this.startSelectY = fromY;
    }

    /**
   * Clear the scene and adjust the camera position.
   * @param elapsedTime time since previous frame.
   */
    protected void drawScene(float elapsedTime) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        grip.adjust();
    }

    /**
   * Start 2D rendering mode.
   */
    protected void go2D() {
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluOrtho2D(0.0, 1.0d, 0, 1.0d);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
    }

    /**
   * Return to 3D rendering mode.
   */
    protected void end2D() {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPopMatrix();
        gl.glEnable(GL.GL_DEPTH_TEST);
    }

    /**
   * Draw the 2D selection rectangle on top of everything.
   */
    protected void drawRectangle() {
        int[] viewPort = new int[4];
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort, 0);
        float sx = (float) startSelectX / (float) viewPort[2];
        float sy = (float) (viewPort[3] - startSelectY) / (float) viewPort[3];
        float ex = (float) mouseX / (float) viewPort[2];
        float ey = (float) (viewPort[3] - mouseY) / (float) viewPort[3];
        go2D();
        gl.glColor4f(0.0f, 0.0f, 0.8f, 0.3f);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(sx, sy);
        gl.glVertex2f(ex, sy);
        gl.glVertex2f(ex, ey);
        gl.glVertex2f(sx, ey);
        gl.glEnd();
        gl.glColor4f(0.0f, 0.0f, 0.6f, 1.0f);
        gl.glLineWidth(1.0f);
        gl.glBegin(GL.GL_LINE_STRIP);
        gl.glVertex2f(sx, sy);
        gl.glVertex2f(ex, sy);
        gl.glVertex2f(ex, ey);
        gl.glVertex2f(sx, ey);
        gl.glVertex2f(sx, sy);
        gl.glEnd();
        end2D();
    }

    public SceneGrip getGrip() {
        return grip;
    }

    /**
   * Setup scene colors.
   *
   * @param back
   * @param front
   */
    public void setColors(Color back, Color front) {
        gl.glClearColor(back.getRed() / 255f, back.getGreen() / 255f, back.getBlue() / 255f, back.getAlpha() / 255f);
        this.foregroundColor = front;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public GLCanvas getContext() {
        return canvas;
    }

    public BufferedImage takeScreenshot() {
        if (!this.canvas.isCurrent()) {
            this.canvas.setCurrent();
        }
        context.makeCurrent();
        BufferedImage image = Screenshot.readToBufferedImage(canvas.getSize().x, canvas.getSize().y);
        context.release();
        return image;
    }

    public static float[] P(float x, float y) {
        if (!hyperbolic) {
            return new float[] { x, y, 0.0f };
        } else {
            return convertVertex(x - FACTOR / 2.0f, y - FACTOR / 2.0f);
        }
    }

    public static void V(GL gl, float x, float y) {
        if (!hyperbolic) {
            gl.glVertex2f(x, y);
        } else {
            convertGLVertex(gl, x - FACTOR / 2.0f, y - FACTOR / 2.0f);
        }
    }

    public static void V(GL gl, double x, double y) {
        V(gl, (float) x, (float) y);
    }

    public static float[] convertVertex(float x, float y) {
        return convertVertex(x, y, 0, 1, 1, 1, 0, 0, 0);
    }

    public static float[] convertVertex(float x, float y, float z) {
        return convertVertex(x, y, z, 1, 1, 1, 0, 0, 0);
    }

    public static float[] convertVertex(float x, float y, float z, float a, float b, float c, float k, float h, float p) {
        float zoom = 1.0f;
        x /= zoom;
        y /= zoom;
        z += -Math.sqrt(c * c * ((x - k) * (x - k) / (a * a) + (y - h) * (y - h) / (b * b) + 1)) + p;
        return new float[] { x, y, z };
    }

    public static void convertGLVertex(GL gl, float x, float y) {
        convertGLVertex(gl, x, y, 0, 1, 1, 1, 0, 0, 0);
    }

    public static void convertGLVertex(GL gl, float x, float y, float z) {
        convertGLVertex(gl, x, y, z, 1, 1, 1, 0, 0, 0);
    }

    public static void convertGLVertex(GL gl, float x, float y, float z, float a, float b, float c, float k, float h, float p) {
        float zoom = 1.0f;
        x /= zoom;
        y /= zoom;
        z += -Math.sqrt(c * c * ((x - k) * (x - k) / (a * a) + (y - h) * (y - h) / (b * b) + 1)) + p;
        gl.glVertex3f(x, y, z);
    }
}
