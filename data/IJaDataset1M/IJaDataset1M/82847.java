package com.dukesoftware.utils.test.jogl.texture;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import com.sun.opengl.util.FPSAnimator;

/**
* 
 * {@link http://www.koders.com/java/fid0A87959E7E94070D73BE946C357476CCC6E49407.aspx}
 */
public class GLDisplay {

    private GLCanvas glCanvas;

    private FPSAnimator animator;

    private int width;

    private int height;

    private final JFrame frame;

    private MyHelpOverlayGLEventListener helpOverlayGLEventListener = new MyHelpOverlayGLEventListener();

    public static GLDisplay createGLDisplay(String title) {
        return new GLDisplay(title);
    }

    private GLDisplay(String title) {
        GLCapabilities caps = new GLCapabilities();
        caps.setRedBits(8);
        caps.setBlueBits(8);
        caps.setGreenBits(8);
        caps.setAlphaBits(8);
        caps.setDoubleBuffered(true);
        caps.setHardwareAccelerated(true);
        glCanvas = new GLCanvas(caps);
        glCanvas.setSize(width, height);
        glCanvas.setIgnoreRepaint(true);
        glCanvas.addGLEventListener(helpOverlayGLEventListener);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(glCanvas, BorderLayout.CENTER);
        try {
            Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "Custom Cursor");
            panel.setCursor(c);
        } catch (Exception exc) {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
        addKeyListener(new MyKeyAdapter());
        animator = new FPSAnimator(glCanvas, 60);
        animator.setRunAsFastAsPossible(false);
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(640, 480));
    }

    public void start() {
        frame.pack();
        frame.setVisible(true);
        glCanvas.requestFocus();
        animator.start();
    }

    public void stop() {
        animator.stop();
    }

    public void addGLEventListener(GLEventListener glEventListener) {
        this.helpOverlayGLEventListener.addGLEventListener(glEventListener);
    }

    public void removeGLEventListener(GLEventListener glEventListener) {
        this.helpOverlayGLEventListener.removeGLEventListener(glEventListener);
    }

    public void addKeyListener(KeyListener l) {
        glCanvas.addKeyListener(l);
    }

    public void addMouseListener(MouseListener l) {
        glCanvas.addMouseListener(l);
    }

    public void addMouseMotionListener(MouseMotionListener l) {
        glCanvas.addMouseMotionListener(l);
    }

    public void removeKeyListener(KeyListener l) {
        glCanvas.removeKeyListener(l);
    }

    public void removeMouseListener(MouseListener l) {
        glCanvas.removeMouseListener(l);
    }

    public void removeMouseMotionListener(MouseMotionListener l) {
        glCanvas.removeMouseMotionListener(l);
    }

    public void registerKeyStrokeForHelp(KeyStroke keyStroke, String description) {
        helpOverlayGLEventListener.registerKeyStroke(keyStroke, description);
    }

    public void registerMouseEventForHelp(int id, int modifiers, String description) {
        helpOverlayGLEventListener.registerMouseEvent(id, modifiers, description);
    }

    private class MyKeyAdapter extends KeyAdapter {

        public MyKeyAdapter() {
            registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "Show/hide help message");
            registerKeyStrokeForHelp(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Quit demo");
        }

        public void keyReleased(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    stop();
                    break;
                case KeyEvent.VK_F1:
                    helpOverlayGLEventListener.toggleHelp();
                    break;
            }
        }
    }

    private static class MyHelpOverlayGLEventListener implements GLEventListener {

        private List<GLEventListener> eventListeners = new ArrayList<GLEventListener>();

        private final HelpOverlay helpOverlay = new HelpOverlay();

        private boolean showHelp = false;

        public void toggleHelp() {
            showHelp = !showHelp;
        }

        public void registerKeyStroke(KeyStroke keyStroke, String description) {
            helpOverlay.registerKeyStroke(keyStroke, description);
        }

        public void registerMouseEvent(int id, int modifiers, String description) {
            helpOverlay.registerMouseEvent(id, modifiers, description);
        }

        public void addGLEventListener(GLEventListener glEventListener) {
            eventListeners.add(glEventListener);
        }

        public void removeGLEventListener(GLEventListener glEventListener) {
            eventListeners.remove(glEventListener);
        }

        public void display(GLAutoDrawable glDrawable) {
            for (int i = 0, size = eventListeners.size(); i < size; i++) {
                eventListeners.get(i).display(glDrawable);
            }
            if (showHelp) helpOverlay.display(glDrawable);
        }

        public void displayChanged(GLAutoDrawable glDrawable, boolean b, boolean b1) {
            for (int i = 0, size = eventListeners.size(); i < size; i++) {
                eventListeners.get(i).displayChanged(glDrawable, b, b1);
            }
        }

        public void init(GLAutoDrawable glDrawable) {
            for (int i = 0, size = eventListeners.size(); i < size; i++) {
                eventListeners.get(i).init(glDrawable);
            }
        }

        public void reshape(GLAutoDrawable glDrawable, int i0, int i1, int i2, int i3) {
            for (int i = 0, size = eventListeners.size(); i < size; i++) {
                eventListeners.get(i).reshape(glDrawable, i0, i1, i2, i3);
            }
        }
    }
}
