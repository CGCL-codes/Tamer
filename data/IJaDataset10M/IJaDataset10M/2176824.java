package org.lindenb.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class DrawingArea extends JPanel implements MouseListener, MouseMotionListener {

    private static final long serialVersionUID = 1L;

    public DrawingArea() {
        super(null, true);
        setOpaque(true);
        setBackground(Color.WHITE);
        setToolTipText("");
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Graphics2D getGraphics2D() {
        return Graphics2D.class.cast(getGraphics());
    }

    protected static Rectangle createRect(int x1, int y1, int x2, int y2) {
        return new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintDrawingArea(Graphics2D.class.cast(g));
    }

    public void paintDrawingArea(Graphics2D g) {
    }

    @Override
    public void mouseClicked(MouseEvent mouse) {
    }

    @Override
    public void mouseEntered(MouseEvent mouse) {
    }

    @Override
    public void mouseExited(MouseEvent mouse) {
    }

    @Override
    public void mousePressed(MouseEvent mouse) {
    }

    @Override
    public void mouseReleased(MouseEvent mouse) {
    }

    @Override
    public void mouseDragged(MouseEvent mouse) {
    }

    @Override
    public void mouseMoved(MouseEvent mouse) {
    }

    @Override
    public String getToolTipText(MouseEvent mouse) {
        return getToolTip(mouse.getX(), mouse.getY());
    }

    public String getToolTip(int x, int y) {
        return null;
    }
}
