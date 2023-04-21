package ds.asterisk.templates;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import ds.asterisk.general.Effects;
import ds.asterisk.general.Effects.Effect;

public class FrameTemplate extends JFrame implements MouseMotionListener, MouseListener, WindowListener {

    private Dimension originalSize = this.getSize();

    private Point point = new Point();

    private Effect effectClose = Effect.NONE;

    private Effect effectMinimize = Effect.NONE;

    private Effect effectDeminimize = Effect.NONE;

    private boolean minToTray = false;

    private float opacity = 1.0F;

    public FrameTemplate() {
        addWindowListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public Dimension getOriginalSize() {
        return originalSize;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setFrameOpacity(float frameOpacity) {
        opacity = frameOpacity;
    }

    public boolean isMinimizeToTrayEnabled() {
        return minToTray;
    }

    public void setEffectForClosing(Effect effect) {
        effectClose = effect;
    }

    public void setEffectForMinimizing(Effect effect) {
        effectMinimize = effect;
    }

    public void setEffectForDeminimizing(Effect effect) {
        effectDeminimize = effect;
    }

    public Effect getEffectForClosing() {
        return effectClose;
    }

    public Effect getEffectForDeminimizing() {
        return effectDeminimize;
    }

    public Effect getEffectForMinimizing() {
        return effectMinimize;
    }

    public void setMinimizeToTray(boolean minToTray) {
        this.minToTray = minToTray;
    }

    private void swapCursor() {
        if (this.getCursor().getType() != Cursor.MOVE_CURSOR) {
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        com.sun.awt.AWTUtilities.setWindowOpacity(this, opacity);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            Point p = this.getLocation();
            this.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            point.x = e.getX();
            point.y = e.getY();
            swapCursor();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            swapCursor();
        }
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        Effects.initEffect(effectClose, this);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        if (minToTray) {
            this.setVisible(false);
        } else {
            setExtendedState(FrameTemplate.ICONIFIED);
        }
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }
}
