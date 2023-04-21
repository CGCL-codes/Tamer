package chapt03.stencil;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StencilAWT {

    static {
        GLProfile.initSingleton(false);
    }

    public static void main(String[] args) {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        glcapabilities.setStencilBits(8);
        final GLCanvas glcanvas = new GLCanvas(glcapabilities);
        glcanvas.addGLEventListener(new GLEventListener() {

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int w, int h) {
                Stencil.setup(glautodrawable.getGL().getGL2(), w, h);
            }

            @Override
            public void init(GLAutoDrawable glautodrawable) {
                FPSAnimator animator = new FPSAnimator(glautodrawable, 40);
                animator.start();
            }

            @Override
            public void dispose(GLAutoDrawable glautodrawable) {
            }

            @Override
            public void display(GLAutoDrawable glautodrawable) {
                int w = glautodrawable.getWidth();
                int h = glautodrawable.getHeight();
                Stencil.render(glautodrawable.getGL().getGL2(), w, h);
            }
        });
        final Frame frame = new Frame("Stencil Test");
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
