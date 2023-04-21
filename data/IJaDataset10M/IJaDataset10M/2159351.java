package ontorama.conf;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.jdom.Element;

/**
 * This class imports simple SVG images into AWT Shape objects.
 * 
 * Supported are: $lt;rect>, $lt;circle>, $lt;ellipse>, $lt;line>, $lt;polyline>, $lt;polygon>.
 * 
 * Styles are ignored, transformations are ignored. Group elements are
 * processed via recursion, but ignored otherwise, the result is the same as
 * if the group elements have been removed before. The methods work for simple
 * SVG, but have not been tested on anything complex.
 * 
 * @todo adding path would greatly enhance this
 * @todo add tests
 * 
 * @see http://weblogs.java.net/blog/kirillcool/archive/2006/10/svg_and_java_ui_3.html might be a more
 *      complete solution for this (although using Batik as dependency)
 */
public class SVG2Shape {

    public static Shape importShape(Element svgElement) {
        GeneralPath shape = importShapeUncentered(svgElement);
        return centerShape(shape);
    }

    @SuppressWarnings("unchecked")
    private static GeneralPath importShapeUncentered(Element svgElement) {
        GeneralPath shape = new GeneralPath();
        Iterator<Element> it = svgElement.getChildren().iterator();
        while (it.hasNext()) {
            Element cur = it.next();
            if (cur.getName().equals("rect")) {
                double x = Double.parseDouble(cur.getAttributeValue("x"));
                double y = Double.parseDouble(cur.getAttributeValue("y"));
                double width = Double.parseDouble(cur.getAttributeValue("width"));
                double height = Double.parseDouble(cur.getAttributeValue("height"));
                shape.append(new Rectangle2D.Double(x, y, width, height), false);
            } else if (cur.getName().equals("circle")) {
                double cx = Double.parseDouble(cur.getAttributeValue("cx"));
                double cy = Double.parseDouble(cur.getAttributeValue("cy"));
                double r = Double.parseDouble(cur.getAttributeValue("r"));
                shape.append(new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r), false);
            } else if (cur.getName().equals("ellipse")) {
                double cx = Double.parseDouble(cur.getAttributeValue("cx"));
                double cy = Double.parseDouble(cur.getAttributeValue("cy"));
                double rx = Double.parseDouble(cur.getAttributeValue("rx"));
                double ry = Double.parseDouble(cur.getAttributeValue("ry"));
                shape.append(new Ellipse2D.Double(cx - rx, cy - ry, 2 * rx, 2 * ry), false);
            } else if (cur.getName().equals("line")) {
                double x1 = Double.parseDouble(cur.getAttributeValue("x1"));
                double y1 = Double.parseDouble(cur.getAttributeValue("y1"));
                double x2 = Double.parseDouble(cur.getAttributeValue("x2"));
                double y2 = Double.parseDouble(cur.getAttributeValue("y2"));
                shape.append(new Line2D.Double(x1, y1, x2, y2), false);
            } else if (cur.getName().equals("polyline") || cur.getName().equals("polygon")) {
                StringTokenizer tokenizer = new StringTokenizer(cur.getAttributeValue("points"), " ,");
                boolean first = true;
                while (tokenizer.hasMoreElements()) {
                    float x = Float.parseFloat(tokenizer.nextToken());
                    float y = Float.parseFloat(tokenizer.nextToken());
                    if (first) {
                        shape.moveTo(x, y);
                        first = false;
                    } else {
                        shape.lineTo(x, y);
                    }
                }
                if (cur.getName().equals("polygon")) {
                    shape.closePath();
                }
            } else if (cur.getName().equals("g")) {
                shape.append(importShapeUncentered(cur), false);
            }
        }
        return shape;
    }

    private static Shape centerShape(Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();
        double xOffset = bounds.getX() - bounds.getWidth() / 2;
        double yOffset = bounds.getY() - bounds.getHeight() / 2;
        return AffineTransform.getTranslateInstance(xOffset, yOffset).createTransformedShape(shape);
    }

    public static Shape importShape(Element svgElement, double width, double height) {
        Shape untransformedShape = importShape(svgElement);
        float scaleX = (float) (width / untransformedShape.getBounds2D().getWidth());
        float scaleY = (float) (height / untransformedShape.getBounds2D().getHeight());
        AffineTransform transform;
        if (scaleX > scaleY) {
            transform = AffineTransform.getScaleInstance(scaleY, scaleY);
        } else {
            transform = AffineTransform.getScaleInstance(scaleX, scaleX);
        }
        return transform.createTransformedShape(untransformedShape);
    }
}
