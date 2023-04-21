package vademecum.ui.visualizer.utils;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class ShapeUtils {

    /**
	 * s - size factor
	 */
    public static Shape getDiamond(float s) {
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(0.0f, -s);
        p0.lineTo(s, 0.0f);
        p0.lineTo(0.0f, s);
        p0.lineTo(-s, 0.0f);
        p0.closePath();
        return p0;
    }

    /**
	 * 
	 * @param l - length
	 * @param t - thickness
	 * @return
	 */
    public static Shape getCross(float l, float t) {
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(-l, t);
        p0.lineTo(-t, t);
        p0.lineTo(-t, l);
        p0.lineTo(t, l);
        p0.lineTo(t, t);
        p0.lineTo(l, t);
        p0.lineTo(l, -t);
        p0.lineTo(t, -t);
        p0.lineTo(t, -l);
        p0.lineTo(-t, -l);
        p0.lineTo(-t, -t);
        p0.lineTo(-l, -t);
        p0.closePath();
        return p0;
    }

    /**
	 * 
	 * @param s size factor
	 * @return
	 */
    public static Shape getUpTriangle(float s) {
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(0.0f, -s);
        p0.lineTo(s, s);
        p0.lineTo(-s, s);
        p0.closePath();
        return p0;
    }

    /**
	 * 
	 * @param s size factor
	 * @return
	 */
    public static Shape getDownTriangle(float s) {
        final GeneralPath p0 = new GeneralPath();
        p0.moveTo(0.0f, s);
        p0.lineTo(s, -s);
        p0.lineTo(-s, -s);
        p0.closePath();
        return p0;
    }

    public static Shape getCircle(double r) {
        Shape s = new Ellipse2D.Double(0, 0, r, r);
        return s;
    }

    public static Shape getTorus(double r) {
        Shape s = new Ellipse2D.Double(0, 0, r, r);
        Stroke stroke = new BasicStroke();
        s = stroke.createStrokedShape(s);
        return s;
    }

    /**
	 * 
	 * @param s - size factor
	 * @param t - thickness
	 * @return
	 */
    public static Shape getXCross(float s, float t) {
        return getCross(s, t);
    }

    public static ArrayList getMarkerCollection() {
        ArrayList<Shape> list = new ArrayList<Shape>();
        list.add(getCross(3, 3));
        list.add(getUpTriangle(3));
        list.add(getDownTriangle(3));
        list.add(getDiamond(3));
        list.add(getCircle(3));
        list.add(getTorus(3));
        return list;
    }
}
