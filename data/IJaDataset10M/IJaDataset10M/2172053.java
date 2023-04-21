package chapt03.pstipple;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PStippleAWT {

    static {
        GLProfile.initSingleton(false);
    }

    public static void main(String[] args) {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        final GLCanvas glcanvas = new GLCanvas(glcapabilities);
        glcanvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
                PStipple.setup(glautodrawable.getGL().getGL2(), width, height);
            }

            @Override
            public void init(GLAutoDrawable glautodrawable) {
            }

            @Override
            public void dispose(GLAutoDrawable glautodrawable) {
            }

            @Override
            public void display(GLAutoDrawable glautodrawable) {
                int w = glautodrawable.getWidth();
                int h = glautodrawable.getHeight();
                PStipple.render(glautodrawable.getGL().getGL2(), w, h);
            }
        });
        glcanvas.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) PStipple.xRot -= 5.0f; else if (key == KeyEvent.VK_DOWN) PStipple.xRot += 5.0f; else if (key == KeyEvent.VK_LEFT) PStipple.yRot -= 5.0f; else if (key == KeyEvent.VK_RIGHT) PStipple.yRot += 5.0f;
                glcanvas.display();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        final Frame frame = new Frame("Polygon Stippling Example");
        frame.add(glcanvas);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent) {
                frame.remove(glcanvas);
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
