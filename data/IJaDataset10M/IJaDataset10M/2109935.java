package r2q2.processing.expression.operators;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.jdom.Element;
import r2q2.processing.expression.Expression;
import r2q2.processing.expression.ExpressionFactory;
import r2q2.processing.expression.InvalidArgumentsException;
import r2q2.variable.ItemVariable;
import r2q2.variable.ItemVariable.Shape;
import r2q2.variable.TransitoryVariable;
import r2q2.variable.ItemVariable.Cardinality;
import r2q2.variable.ItemVariable.BaseType;

/**
 * 
 * 'inside' operator expression
 * 
 * constraints: 
 *   takes a single sub-expression of base-type Point, 
 *   returns Boolean 'true' if the point lies within the given shapes boundaries
 *   if the expression is a container it returns 'true' if *any* of the points lie within the shape
 *   if the sub expression is NULL, NULL is returned
 */
public class Inside implements Expression {

    private Expression subpart;

    private double[] shapeData;

    private Shape shapeType;

    public Inside(Element _e) throws InvalidArgumentsException {
        ExpressionFactory of = ExpressionFactory.getInstance();
        Iterator children = _e.getChildren().iterator();
        String shapeTypeStr = _e.getAttributeValue("shape");
        if (shapeTypeStr.equals("circle")) shapeType = Shape.circle; else if (shapeTypeStr.equals("rect")) shapeType = Shape.rect; else if (shapeTypeStr.equals("poly")) shapeType = Shape.poly; else if (shapeTypeStr.equals("ellipse")) shapeType = Shape.ellipse; else if (shapeTypeStr.equals("default")) throw new InvalidArgumentsException("Error: default shape attribute not supported in this implementation, aborting"); else throw new InvalidArgumentsException("Error: shape attribute of unrecognised type, aborting");
        StringTokenizer st = new StringTokenizer(_e.getAttributeValue("coords"), ",");
        shapeData = new double[st.countTokens()];
        int i = 0;
        try {
            while (st.hasMoreTokens()) {
                shapeData[i] = Double.parseDouble(st.nextToken());
                i++;
            }
        } catch (NumberFormatException nfe) {
            throw new InvalidArgumentsException("Error: non-numerical attribute provided to the 'inside' method as a coord, aborting", nfe);
        }
        i = 0;
        try {
            while (children.hasNext()) {
                subpart = of.makeExpression((Element) children.next());
                i++;
            }
        } catch (InvalidArgumentsException iae) {
            throw new InvalidArgumentsException("Error occured in building childrens expression: ", iae);
        }
        if (i > 1 || i < 1) throw new InvalidArgumentsException("Error: usage violation, 'inside' can only be called with 1 expression, aborting");
    }

    public ItemVariable eval(HashMap<String, ItemVariable> vars) throws InvalidArgumentsException {
        ItemVariable test;
        try {
            test = subpart.eval(vars);
        } catch (InvalidArgumentsException iae) {
            throw new InvalidArgumentsException("Error: evaluation of children yielded unexpected exception", iae);
        }
        if (test == null) return null;
        if (test.varType != BaseType.point) throw new InvalidArgumentsException("Error: cannot perform inside with a non 'point' type primative surplied as an arguement, aborting");
        if (test.card == Cardinality.multiple || test.card == Cardinality.ordered) {
            Vector subParts = (Vector) test.value;
            for (int i = 0; i < subParts.size(); i++) {
                if (subParts.elementAt(i) instanceof Point2D) {
                    Point2D t = (Point2D) subParts.elementAt(i);
                    switch(shapeType) {
                        case rect:
                            if (insideRect(t, shapeData).booleanValue()) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true));
                        case poly:
                            if (insidePolygon(t, shapeData).booleanValue()) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true));
                        case circle:
                            if (insideCircle(t, shapeData).booleanValue()) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true));
                        case ellipse:
                            if (insideEllipse(t, shapeData).booleanValue()) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true));
                    }
                } else throw new InvalidArgumentsException("Error: sub expressions within collection must all be of type 'point', aborting");
            }
            return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
        } else {
            Point2D t = (Point2D) test.value;
            switch(shapeType) {
                case rect:
                    return new TransitoryVariable(BaseType.bool, Cardinality.single, insideRect(t, shapeData));
                case poly:
                    return new TransitoryVariable(BaseType.bool, Cardinality.single, insidePolygon(t, shapeData));
                case circle:
                    return new TransitoryVariable(BaseType.bool, Cardinality.single, insideCircle(t, shapeData));
                case ellipse:
                    return new TransitoryVariable(BaseType.bool, Cardinality.single, insideEllipse(t, shapeData));
                default:
                    throw new InvalidArgumentsException("Error: attempting to use a specification undefined shape type, aborting");
            }
        }
    }

    public static Boolean insideRect(Point2D testPoint, double[] rectData) {
        if (testPoint.getX() >= rectData[0] && testPoint.getX() <= rectData[2] && testPoint.getY() >= rectData[3] && testPoint.getY() <= rectData[1]) {
            return new Boolean(true);
        }
        return new Boolean(false);
    }

    public static Boolean insideCircle(Point2D testPoint, double[] circleData) {
        Point2D cen = new Point2D.Double(circleData[0], circleData[1]);
        if (cen.distance(testPoint) <= circleData[2]) return new Boolean(true);
        return new Boolean(false);
    }

    public static Boolean insidePolygon(Point2D testPoint, double[] polygonData) {
        int sz = polygonData.length / 2;
        int[] xpoints = new int[sz];
        int[] ypoints = new int[sz];
        int xpos = 0;
        int ypos = 0;
        for (int i = 0; i < polygonData.length; i++) {
            if (i % 2 == 0) {
                xpoints[xpos] = (int) polygonData[i];
                xpos++;
            } else {
                ypoints[ypos] = (int) polygonData[i];
                ypos++;
            }
        }
        Polygon test = new Polygon(xpoints, ypoints, sz);
        if (test.contains(testPoint)) return new Boolean(true);
        return new Boolean(false);
    }

    public static Boolean insideEllipse(Point2D testPoint, double[] ellipseData) {
        Ellipse2D ellipse = new Ellipse2D.Double(ellipseData[0], ellipseData[1], ellipseData[2], ellipseData[3]);
        if (ellipse.contains(testPoint)) return new Boolean(true);
        return new Boolean(false);
    }
}
