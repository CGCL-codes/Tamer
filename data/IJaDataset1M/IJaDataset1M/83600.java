package com.objectdraw.client.objectdraw;

import java.awt.*;
import java.awt.event.*;

/**
 * controls mouse events for drawing rectangles
 * @author jgulik, cfruehan
 *
 */
public class RectangleTool implements Tool {

    protected DrawingCanvas canvas;

    protected Point startingMousePosition;

    protected Color saveColor;

    /**
	 * instantiates the tool
	 * @param canvas the canvas upon which the tool acts
	 */
    public RectangleTool(DrawingCanvas canvas) {
        this.canvas = canvas;
    }

    /**
	 * draw an rectangle at position (x,y) with supplied height and width
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
    protected void drawRectangle(int x, int y, int width, int height) {
        int rectX = x;
        int rectY = y;
        int rectWidth = width;
        int rectHeight = height;
        if (width < 0) {
            rectX = x + width;
            rectWidth = -width;
        }
        if (height < 0) {
            rectY = y + height;
            rectHeight = -height;
        }
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        iBGraphics.drawRect(rectX, rectY, rectWidth, rectHeight);
        canvas.repaint();
    }

    /**
	 * start a new rectangle object at point supplied in event e
	 */
    public void mousePressed(MouseEvent e) {
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        startingMousePosition = e.getPoint();
        saveColor = canvas.penColor;
        iBGraphics.setColor(Color.lightGray);
        drawRectangle(startingMousePosition.x, startingMousePosition.y, 0, 0);
    }

    /**
	 * draw a temporary rectangle using the initial point and the current mouse location
	 */
    public void mouseDragged(MouseEvent e) {
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        iBGraphics.setColor(Color.lightGray);
        Point newMousePosition = e.getPoint();
        drawRectangle(startingMousePosition.x, startingMousePosition.y, newMousePosition.x - startingMousePosition.x, newMousePosition.y - startingMousePosition.y);
    }

    /**
	 * draw a rectangle using the initial point and the release point.  push the rectangle object 
	 * onto the object list
	 */
    public void mouseReleased(MouseEvent e) {
        Graphics iBGraphics = canvas.getImageBufferGraphics();
        iBGraphics.setColor(saveColor);
        drawRectangle(startingMousePosition.x, startingMousePosition.y, e.getPoint().x - startingMousePosition.x, e.getPoint().y - startingMousePosition.y);
        RectangleObject myrect = new RectangleObject(startingMousePosition, e.getPoint(), canvas.getImageBufferGraphics().getColor());
        IDrawingObjectLink myrectLink = new IDrawingObjectLink(myrect);
        canvas.push(myrectLink);
    }
}
