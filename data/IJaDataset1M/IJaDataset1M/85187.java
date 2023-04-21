package ip.dip.commonsense.opengl;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.j2d.TextRenderer;

public class Main implements GLEventListener {

    private float LIGHT_POSITION[] = { 3.0f, 4.0f, 5.0f, 1.0f };

    private Animator animator;

    private int prevMouseX;

    private int prevMouseY;

    private float angleX;

    private float angleY;

    private Ground ground = new Ground();

    private Robot robot = new Robot();

    private TextRenderer tr;

    public Main() {
        Frame frame = new Frame("テスト");
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        canvas.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                prevMouseX = e.getX();
                prevMouseY = e.getY();
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Dimension size = e.getComponent().getSize();
                float thetaY = 360.0f * ((float) (x - prevMouseX) / size.width);
                float thetaX = 360.0f * ((float) (y - prevMouseY) / size.height);
                angleX -= thetaX;
                angleY += thetaY;
                prevMouseX = x;
                prevMouseY = y;
            }
        });
        frame.add(canvas);
        frame.setSize(300, 300);
        animator = new Animator(canvas);
        animator.start();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_NORMALIZE);
        gl.glDepthFunc(GL.GL_LEQUAL);
        tr = new TextRenderer(new Font("Default", Font.PLAIN, 9));
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, LIGHT_POSITION, 0);
        gl.glPushMatrix();
        gl.glRotatef(angleX, 1, 0, 0);
        gl.glRotatef(angleY, 0, 1, 0);
        ground.display(drawable);
        robot.display(drawable);
        tr.begin3DRendering();
        tr.setColor(0, 0, 255, 1);
        tr.draw3D("Hello World!", 0, 0, 0, 0.1f);
        tr.draw3D("Hello World!", -1, -0.5f, 0, 0.1f);
        tr.end3DRendering();
        gl.glPopMatrix();
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluPerspective(30.0, (double) w / (double) h, 1.0, 100.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }
}
