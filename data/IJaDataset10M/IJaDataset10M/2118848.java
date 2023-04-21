package com.objectdraw.client.objectdraw;

import java.awt.*;
import java.awt.event.*;

/**
 * legacy class from ObjectDraw3.  Unused in collabDraw.
 * @author jgulik, cfruehan
 *
 */
public class EraserTool implements Tool {

    protected DrawingCanvas canvas;

    protected Point startingMousePosition;

    protected Color saveColor;

    public EraserTool(DrawingCanvas canvas) {
        this.canvas = canvas;
    }

    protected void drawErasure(int x, int y, int width, int height) {
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        iBGraphics.fillRect(x, y, width, height);
        canvas.repaint(x, y, width, height);
    }

    public void mousePressed(MouseEvent e) {
        startingMousePosition = e.getPoint();
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        saveColor = iBGraphics.getColor();
        iBGraphics.setColor(Color.white);
    }

    public void mouseDragged(MouseEvent e) {
        Point newMousePosition = e.getPoint();
        int x0 = Math.min(startingMousePosition.x, newMousePosition.x) - 2;
        int y0 = Math.min(startingMousePosition.y, newMousePosition.y) - 2;
        int dx = Math.abs(newMousePosition.x - startingMousePosition.x) + 5;
        int dy = Math.abs(newMousePosition.y - startingMousePosition.y) + 5;
        drawErasure(x0, y0, dx, dy);
        startingMousePosition = newMousePosition;
    }

    public void mouseReleased(MouseEvent e) {
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        iBGraphics.setColor(saveColor);
    }
}
