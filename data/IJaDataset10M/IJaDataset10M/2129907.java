package marten.testing.util;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/** Class which implements workaround for full-screen bugs on Windows
    when <code>-Dsun.java2d.noddraw=true</code> is specified as well
    as a similar bug on Mac OS X. This code currently expects that the
    GLAutoDrawable will be placed in a containing Frame. */
public class JoglFullscreenWorkaround implements GLEventListener {

    private int width;

    private int height;

    /** Creates a full-screen workaround with the specified width and
      height to set the full-screen window to later. */
    public JoglFullscreenWorkaround(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init(GLAutoDrawable drawable) {
        final Frame frame = getParentFrame((Component) drawable);
        if (frame != null) {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    frame.setVisible(false);
                    frame.setBounds(0, 0, width, height);
                    frame.setVisible(true);
                    frame.toFront();
                }
            });
        }
    }

    public void display(GLAutoDrawable drawable) {
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    private static Frame getParentFrame(Component c) {
        while (c != null && (!(c instanceof Frame))) {
            c = c.getParent();
        }
        return (Frame) c;
    }
}
