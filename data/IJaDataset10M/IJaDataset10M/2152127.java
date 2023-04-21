package gui.GLJPanels;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GLJPanelHoja extends GLJPanelNW {

    static final long serialVersionUID = 1L;

    public GLJPanelHoja() {
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                mousePressedEvent(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mouseReleasedEvent(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mouseDraggedEvent(evt);
            }

            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mouseMovedEvent(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                keyPressedEvent(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                keyReleasedEvent(evt);
            }
        });
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                MouseWheelMoved(evt);
            }
        });
    }

    public void MouseWheelMoved(MouseWheelEvent evt) {
        this.gllistener.mouseWheelMoved(evt);
        this.repaint();
    }

    public void keyReleasedEvent(KeyEvent evt) {
        this.gllistener.keyReleased(evt);
        this.repaint();
    }

    public void keyPressedEvent(KeyEvent evt) {
        this.getContext().makeCurrent();
        this.gllistener.keyPressed(evt);
        this.repaint();
    }

    public void mousePressedEvent(MouseEvent arg0) {
        this.gllistener.mousePressed(arg0, this.getWidth(), this.getHeight());
        this.repaint();
    }

    public void mouseReleasedEvent(MouseEvent arg0) {
        this.gllistener.mouseReleased(arg0);
        this.repaint();
    }

    public void mouseDraggedEvent(MouseEvent arg0) {
        this.gllistener.mouseDragged(arg0, this.getWidth(), this.getHeight());
        this.repaint();
    }

    private void mouseMovedEvent(MouseEvent evt) {
        this.gllistener.mouseMoved(evt, this.getWidth(), this.getHeight());
        this.repaint();
    }
}
