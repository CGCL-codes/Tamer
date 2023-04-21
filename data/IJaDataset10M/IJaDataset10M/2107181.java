package uk.ac.lkl.migen.mockup.polydials.model;

import java.util.HashMap;
import java.awt.Polygon;

/**
 * A rotatable regular shape.
 * 
 * Doesn't update original coordinates to avoid floating point errors.
 * 
 * @author  $Author: toontalk@gmail.com $
 * @version $Revision: 2696 $
 * @version $Date: 2009-05-25 13:13:08 -0400 (Mon, 25 May 2009) $
 * 
 */
public class RotatingShape {

    private int numSides;

    private int stepNumber;

    private double radius;

    private double centerX;

    private double centerY;

    private static HashMap<Integer, Polygon> BASE_POLYGONS = new HashMap<Integer, Polygon>();

    private Polygon basePolygon;

    public RotatingShape(int centerX, int centerY, double radius, int numSides) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.numSides = numSides;
        this.stepNumber = 0;
        initialiseBasePolygon();
    }

    private void initialiseBasePolygon() {
        basePolygon = BASE_POLYGONS.get(numSides);
        if (basePolygon == null) {
            basePolygon = createBasePolygon(centerX, centerY, radius, numSides);
            BASE_POLYGONS.put(numSides, basePolygon);
        }
    }

    private static Polygon createBasePolygon(double x, double y, double radius, int numSides) {
        Polygon polygon = new Polygon();
        return polygon;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public double getAngle() {
        return stepNumber * 2 * Math.PI / numSides;
    }
}
