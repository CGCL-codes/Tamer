package chapt08.sphereworld;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SphereWorldAWT {

    static {
        GLProfile.initSingleton(false);
    }

    public static void main(String[] args) {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        final GLCanvas glcanvas = new GLCanvas(glcapabilities);
        glcanvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int w, int h) {
                SphereWorld.setup(glautodrawable.getGL().getGL2(), w, h);
            }

            @Override
            public void init(GLAutoDrawable glautodrawable) {
                FPSAnimator animator = new FPSAnimator(glautodrawable, 60);
                animator.start();
            }

            @Override
            public void dispose(GLAutoDrawable glautodrawable) {
            }

            @Override
            public void display(GLAutoDrawable glautodrawable) {
                int w = glautodrawable.getWidth();
                int h = glautodrawable.getHeight();
                SphereWorld.render(glautodrawable.getGL().getGL2(), w, h);
            }
        });
        glcanvas.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_UP) SphereWorld.frameCamera.moveForward(0.1f); else if (key == KeyEvent.VK_DOWN) SphereWorld.frameCamera.moveForward(-0.1f); else if (key == KeyEvent.VK_LEFT) SphereWorld.frameCamera.rotateLocalY(0.1f); else if (key == KeyEvent.VK_RIGHT) SphereWorld.frameCamera.rotateLocalY(-0.1f);
                glcanvas.display();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        final Frame frame = new Frame("OpenGL SphereWorld Demo + Texture Maps");
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
