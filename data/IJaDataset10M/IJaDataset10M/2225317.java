package scribble;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ScribbleCanvasListener implements MouseListener, MouseMotionListener {

    protected PasswordGen password;

    private int xp0, yp0, xp1, yp1;

    public ScribbleCanvasListener(ScribbleCanvas canvas, PasswordGen password) {
        this.canvas = canvas;
        this.password = password;
    }

    protected ScribbleCanvas canvas;

    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        canvas.mouseButtonDown = true;
        canvas.x = p.x;
        canvas.y = p.y;
        password.setVars(canvas.width, canvas.height);
        xp0 = p.x;
        yp0 = p.y;
        xp1 = xp0 - 50000;
        yp1 = yp0 - 50000;
    }

    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        xp0 = p.x;
        yp0 = p.y;
        if (canvas.mouseButtonDown) {
            canvas.getOffScreenGraphics().drawLine(canvas.x, canvas.y, p.x, p.y);
            int x0 = Math.min(canvas.x, p.x);
            int y0 = Math.min(canvas.y, p.y);
            int dx = Math.abs(p.x - canvas.x) + 1;
            int dy = Math.abs(p.y - canvas.y) + 1;
            canvas.repaint(x0, y0, dx, dy);
            canvas.x = p.x;
            canvas.y = p.y;
            password.setPassord(xp0, yp0, xp1, yp1);
            xp1 = p.x;
            yp1 = p.y;
        }
    }

    public void mouseReleased(MouseEvent e) {
        canvas.mouseButtonDown = false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
