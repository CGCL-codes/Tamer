package gestalt.demo.workshop;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import gestalt.context.GLContext;
import gestalt.impl.jogl.context.JoglGLContext;
import gestalt.render.AnimatorRenderer;
import gestalt.render.bin.RenderBin;
import gestalt.shape.AbstractDrawable;

public class Lesson02 extends AnimatorRenderer {

    public static void main(String[] arg) {
        new Lesson02().init();
    }

    private Lesson02() {
    }

    public void setup() {
        RenderBin myRenderBin = new RenderBin(1);
        setBinRef(myRenderBin);
        myRenderBin.add(new MyDrawable());
    }

    private class MyDrawable extends AbstractDrawable {

        private boolean isInitialized = false;

        private int _myWidth;

        private int _myHeight;

        public void draw(final GLContext theContext) {
            GL gl = ((JoglGLContext) theContext).gl;
            GLU glu = ((JoglGLContext) theContext).glu;
            if (!isInitialized) {
                _myWidth = theContext.displaycapabilities.width;
                _myHeight = theContext.displaycapabilities.height;
                isInitialized = true;
                init(gl, glu);
            } else {
                display(gl, glu);
            }
        }

        public void init(GL gl, GLU glu) {
            gl.glViewport(0, 0, _myWidth, _myHeight);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            glu.gluPerspective(45.0f, (float) _myWidth / (float) _myHeight, 1.0, 20.0);
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        public void display(GL gl, GLU glu) {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
            gl.glTranslatef(-1.5f, 0.0f, -6.0f);
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glVertex3f(0.0f, 1.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, 0.0f);
            gl.glEnd();
            gl.glTranslatef(3.0f, 0.0f, 0.0f);
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex3f(-1.0f, 1.0f, 0.0f);
            gl.glVertex3f(1.0f, 1.0f, 0.0f);
            gl.glVertex3f(1.0f, -1.0f, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, 0.0f);
            gl.glEnd();
        }
    }
}
