package net.sourceforge.javagg.gsc;

import java.awt.Color;

/**
 * The behavior in this interface will give a programmer a lot of the familiar
 * old graphics primitives that he may have enjoyed at one time. This will
 * mostly wrap methods of the <code><a href="file:///java/docs/api/java/awt/Graphics.html">java.awt.Graphics</a></code>
 * and <code><a href="file:///java/docs/api/java/awt/Graphics2D.html">java.awt.Graphics2D</a></code> class in the Java2D api.
 * This will also add a few methods and remove a few. If you need the
 * funtionality that is in the Graphics class you may still use it by
 * obtaining the lower level objects. You will notice this class to be so
 * nearly like the Graphics class you may wonder why? Notice that I try to keep
 * the method signatures very very simple, as well as the selection of methods.
 * Also no inherited clutter here, all you get is what you see.
 * 
 * @author (Larry Gray)
 * @version 1.5
 * 
 * @see Imagry
 * @see GraphicsWriter
 */
public interface Graphics extends Enhancer {

    public void drawRect(int x1, int y1, int x2, int y2);

    /**
	 * This draws a circle given radius centered on 
	 * x,y.
	 * @see drawCircleDiameter(int,int,int)
	 * @see drawOval(int,int,int,int)  
	 *  
	 * @param x center of circle.
	 * @param y center of circle.
	 * @param radius of circle.
	 */
    public abstract void drawCircle(int x, int y, int radius);

    /**
	 * This draws a circle given diameter centered on x,y.
	 * 
	 * @see drawCircle(int,int,int)
	 * @see drawOval(int,int,int,int)
	 * 
	 * @param x center of circle.
	 * @param y center of circle.
	 * @param diameter of circle.
	 */
    public abstract void drawCircleDiameter(int x, int y, int diameter);

    /**
	 * Draws a line on the screen.
	 * 
	 * @see gsc.Graphics#drawPolyline(
		int[],
		int[],
		int)
	 * 
	 * @param x1 start x
	 * @param y1 start y
	 * @param x2 end x
	 * @param y2 end y
	 *  
	 */
    public abstract void drawLine(int x1, int y1, int x2, int y2);

    /**
	 * Draws an oval or elipse.
	 * @param x center of oval.
	 * @param y center of oval.
	 * @param w width of oval.
	 * @param h height of oval.
	 * 
	 * @see gsc.Graphics#drawCircle
	 * @see gsc.Graphics#drawCircleDiameter
	 */
    public abstract void drawOval(int x, int y, int w, int h);

    /**
	 * Draws the outline of a polygon.
	 *
	 * @see gsc.Graphics#drawLine
	 * 
	 * @param xPoints a set of x points for each endpoint for the lines.
	 * @param yPoints a set of y points for each enpoint for the lines.
	 * @param nPoints number of end points. I.e. 2 lines has 3 endpoints. 3 lines has 4 endpoints.
	 */
    public abstract void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);

    /**
	 * Draws a filled Rectangle.
	 * 
	 * @param x1 x start point.
	 * @param y1 y start point.
	 * @param x2 x end oposite corner.
	 * @param y2 y end oposite corner.
	 */
    public abstract void fillRect(int x1, int y1, int x2, int y2);

    /**
	 * Sets the current color being used to draw with or fill with
	 * for graphics operations.
	 *
	 * @param color a graphics color.
	 * @see java.awt#Color
	 */
    public abstract void setGraphicsColor(Color color);

    /**
	 * Draws an arbitrary line of text on anywhere on the screen. This
	 * uses default font settings a user of this library
	 * might want to extend the DefaultGraphics class to include 
	 * more font support. Otherwise see the GraphicsWriter Interface which
	 * has more font support.
	 * 
	 * @param x begin x position of front of String.
	 * @param y begin y position of lower front of String.
	 * @param s the string to draw.
	 * 
	 * @see gsc.GraphicsWriter
	 */
    public abstract void drawString(int x, int y, String s);
}
