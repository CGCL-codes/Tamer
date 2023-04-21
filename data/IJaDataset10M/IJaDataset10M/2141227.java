package scikit.graphics.dim2;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.awt.Color;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import scikit.graphics.GLHelper;
import scikit.util.Bounds;
import com.sun.opengl.util.GLUT;

public class Gfx2DGL implements Gfx2D {

    private final GL _gl;

    private final GLUT _glut;

    private final Bounds _pixBds, _viewBds;

    private Bounds _proj;

    private static int FONT = GLUT.BITMAP_8_BY_13;

    private static int FONT_HEIGHT = 13;

    public Gfx2DGL(GLAutoDrawable glDrawable, Scene2D scene) {
        _gl = glDrawable.getGL();
        _glut = new GLUT();
        _pixBds = new Bounds(0, glDrawable.getWidth(), 0, glDrawable.getHeight());
        _proj = _viewBds = scene.viewBounds();
    }

    public Bounds viewBounds() {
        return _viewBds;
    }

    public Bounds pixelBounds() {
        return _pixBds;
    }

    public void setProjection(Bounds proj) {
        _gl.glMatrixMode(GL.GL_PROJECTION);
        _gl.glLoadIdentity();
        (new GLU()).gluOrtho2D(0, _pixBds.xmax, 0, _pixBds.ymax);
        _gl.glMatrixMode(GL.GL_MODELVIEW);
        _gl.glLoadIdentity();
        _proj = proj;
    }

    private double transX(double x) {
        return _pixBds.xmax * (x - _proj.xmin) / _proj.getWidth();
    }

    private double transY(double y) {
        return _pixBds.ymax * (y - _proj.ymin) / _proj.getHeight();
    }

    private void vertex2d(GL gl, double x, double y) {
        gl.glVertex2d(transX(x), transY(y));
    }

    public void setLineSmoothing(boolean b) {
        if (b) _gl.glEnable(GL.GL_LINE_SMOOTH); else _gl.glDisable(GL.GL_LINE_SMOOTH);
    }

    public void setColor(Color color) {
        _gl.glColor4fv(color.getComponents(null), 0);
    }

    public void drawPoint(double x, double y) {
        _gl.glBegin(GL.GL_POINTS);
        vertex2d(_gl, x, y);
        _gl.glEnd();
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        _gl.glBegin(GL.GL_LINES);
        vertex2d(_gl, x1, y1);
        vertex2d(_gl, x2, y2);
        _gl.glEnd();
    }

    public void drawLines(double[] xys) {
        _gl.glBegin(GL.GL_LINES);
        for (int i = 0; i < xys.length; i++) {
            vertex2d(_gl, xys[2 * i + 0], xys[2 * i + 1]);
        }
        _gl.glEnd();
    }

    public void drawRect(double x, double y, double w, double h) {
        _gl.glBegin(GL.GL_LINE_LOOP);
        vertex2d(_gl, x, y);
        vertex2d(_gl, x, y + h);
        vertex2d(_gl, x + w, y + h);
        vertex2d(_gl, x + w, y);
        _gl.glEnd();
    }

    public void fillRect(double x, double y, double w, double h) {
        _gl.glBegin(GL.GL_QUADS);
        vertex2d(_gl, x, y);
        vertex2d(_gl, x, y + h);
        vertex2d(_gl, x + w, y + h);
        vertex2d(_gl, x + w, y);
        _gl.glEnd();
    }

    private static final int EDGES = 32;

    private static double[] COS = new double[EDGES], SIN = new double[EDGES];

    static {
        for (int i = 0; i < EDGES; i++) {
            COS[i] = cos((i / (double) EDGES) * 2 * PI);
            SIN[i] = sin((i / (double) EDGES) * 2 * PI);
        }
    }

    public void drawCircle(double x, double y, double r) {
        _gl.glBegin(GL.GL_LINE_LOOP);
        for (int i = 0; i < EDGES; i++) {
            vertex2d(_gl, x + r * COS[i], y + r * SIN[i]);
        }
        _gl.glEnd();
    }

    public void fillCircle(double x, double y, double r) {
        _gl.glBegin(GL.GL_POLYGON);
        for (int i = 0; i < EDGES; i++) {
            vertex2d(_gl, x + r * COS[i], y + r * SIN[i]);
        }
        _gl.glEnd();
    }

    public double stringWidth(String str) {
        return _glut.glutBitmapLength(FONT, str) * _proj.getWidth() / _pixBds.getWidth();
    }

    public double stringHeight(String str) {
        return FONT_HEIGHT * _proj.getHeight() / _pixBds.getHeight();
    }

    public void drawString(String str, double x, double y) {
        _gl.glPushMatrix();
        _gl.glRasterPos2d(transX(x), transY(y));
        _glut.glutBitmapString(FONT, str);
        _gl.glPopMatrix();
    }

    public static GLJPanel createComponent(final Scene2D scene) {
        return GLHelper.createComponent(new GLEventListener() {

            public void display(GLAutoDrawable glDrawable) {
                glDrawable.getGL().glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                scene.drawAll(new Gfx2DGL(glDrawable, scene));
            }

            public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {
            }

            public void init(GLAutoDrawable glDrawable) {
                GL gl = glDrawable.getGL();
                gl.glClearColor(1f, 1f, 1f, 0.0f);
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
                gl.glEnable(GL.GL_LINE_SMOOTH);
                gl.glLineWidth(1.0f);
                gl.glPointSize(4.0f);
            }

            public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
                GL gl = glDrawable.getGL();
                gl.glViewport(0, 0, width, height);
            }
        });
    }
}
