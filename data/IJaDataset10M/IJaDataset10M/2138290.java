package org.mtmi.ui;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Region;

/**
 * Graphical facilities. 
 * @author jgout
 *
 */
public class GraphicUtil {

    /** 
	 * Returns the outline of the round-cornered rectangle specified by 
	 * the arguments as a path. 
	 * The left and right edges of the rectangle are at <code>x</code> and <code>x + width</code>. 
	 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
	 * The <em>roundness</em> of the corners is specified by the 
	 * <code>arcWidth</code> and <code>arcHeight</code> arguments, which
	 * are respectively the width and height of the ellipse used to draw
	 * the corners.
	 *
	 * @param gc the graphic context used to draw returned path.
	 * @param x the x coordinate of the rectangle to be drawn
	 * @param y the y coordinate of the rectangle to be drawn
	 * @param width the width of the rectangle to be drawn
	 * @param height the height of the rectangle to be drawn
	 * @param arcWidth the width of the arc
	 * @param arcHeight the height of the arc
	 * @return the path of the round rectangle.
	 */
    public static Path getRoundRectangle(GC gc, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        Path p = new Path(gc.getDevice());
        if (width == 0 || height == 0) return p;
        if (arcWidth == 0 || arcHeight == 0) {
            p.addRectangle(x, y, width, height);
            return p;
        }
        if (width < 0) {
            x += width;
            width = -width;
        }
        if (height < 0) {
            y += height;
            height = -height;
        }
        if (arcWidth < 0) arcWidth = -arcWidth;
        if (arcHeight < 0) arcHeight = -arcHeight;
        if (arcWidth > width) arcWidth = width;
        if (arcHeight > height) arcHeight = height;
        if (arcWidth < width && arcHeight < height) {
            p.moveTo(x, y);
            p.addArc(x, y, arcWidth, arcHeight, 90, 90);
            p.addArc(x, y + height - arcHeight, arcWidth, arcHeight, 180, 90);
            p.addArc(x + width - arcWidth, y + height - arcHeight, arcWidth, arcHeight, -90, 90);
            p.addArc(x + width - arcWidth, y, arcWidth, arcHeight, 0, 90);
            p.close();
        }
        return p;
    }

    public static void drawRoundRectangle(GC gc, int x, int y, int width, int height, int arcWidth, int arcHeight) {
        Path p = getRoundRectangle(gc, x, y, width, height, arcWidth, arcHeight);
        gc.drawPath(p);
    }

    public static Region getRegion(Path p) {
        Region r = new Region();
        float[] fs = p.getPathData().points;
        int[] points = new int[fs.length];
        for (int i = 0; i < fs.length; i++) {
            points[i] = (int) fs[i];
        }
        r.add(points);
        return r;
    }
}
