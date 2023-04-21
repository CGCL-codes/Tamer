package com.threecam.geometry;

import com.threecam.math.*;
import com.threecam.toolpath.*;

public class Triangle implements Cloneable {

    public Vector3D Vone;

    public Vector3D Vtwo;

    public Vector3D Vthree;

    public Vector3D normal;

    public Triangle(Vector3D Vone, Vector3D Vtwo, Vector3D Vthree) {
        this.Vone = Vone;
        this.Vtwo = Vtwo;
        this.Vthree = Vthree;
        calculateNormal();
    }

    /**
	 * Calculate normal using right hand rule and store
	 *
	 */
    public void calculateNormal() {
        Vector3D d1 = new Vector3D(Vtwo).subtract(Vone);
        Vector3D d2 = new Vector3D(Vthree).subtract(Vone);
        normal = Vector3D.cross(d1, d2).getUnitVector();
    }

    public Double lineSegmentIntersection(LineSegment line) {
        Double result = lineIntersection(line);
        if (result == null) {
            return null;
        } else {
            if (result >= 0 && result <= 1) {
                return result;
            } else {
                return null;
            }
        }
    }

    /**
	 * 
	 * @param line
	 * @return
	 */
    public Double lineIntersection(LineSegment line) {
        XYZBounds bounds = getCartesianBoundingBox();
        if (!bounds.isIntersected(line)) {
            return null;
        }
        Plane tempP = new Plane(this);
        Double param = tempP.lineSegmentIntersect(line);
        if (param == null) {
            return null;
        }
        Vector3D poi = line.evalParam(param);
        if (pointIsInTriangle(poi)) {
            return param;
        } else {
            return null;
        }
    }

    public boolean pointIsInTriangle(Vector3D point) {
        return pointIsInTriangle(point, false);
    }

    public boolean pointIsInTriangle(Vector3D point, boolean debug) {
        Vector3D one = new Vector3D(Vone).subtract(point);
        Vector3D two = new Vector3D(Vtwo).subtract(point);
        Vector3D three = new Vector3D(Vthree).subtract(point);
        Vector3D oneunit = new Vector3D(Vone).subtract(point).getUnitVector();
        Vector3D twounit = new Vector3D(Vtwo).subtract(point).getUnitVector();
        Vector3D threeunit = new Vector3D(Vthree).subtract(point).getUnitVector();
        double angleone = Math.acos(oneunit.dot(twounit));
        double angletwo = Math.acos(twounit.dot(threeunit));
        double anglethree = Math.acos(threeunit.dot(oneunit));
        double anglesum = angleone + angletwo + anglethree;
        double etol = 1e-7;
        if (debug) {
            System.out.println("Vectors : 1:" + one + " 2:" + two + " 3:" + three);
            System.out.println("UVectors: 1:" + oneunit + " 2:" + twounit + " 3:" + threeunit);
            System.out.println("Dot Prod: 1:" + oneunit.dot(twounit) + " 2:" + twounit.dot(threeunit) + " 3:" + threeunit.dot(oneunit));
            System.out.println("Angles  : 1:" + angleone + " 2:" + angletwo + " 3:" + anglethree);
            System.out.println("Sum     : " + (angleone + angletwo + anglethree) + " != " + 2 * Math.PI);
            System.out.println("ETOL    : " + etol);
        }
        return Math.abs(anglesum - 2.0 * Math.PI) <= etol || Double.isNaN(anglesum) || one.getMagnitude() <= etol || two.getMagnitude() <= etol || three.getMagnitude() <= etol;
    }

    /**
	 * Calculate the angle between this triangle's normal and a vector.
	 * @param vector - The other vector.
	 * @return The calculated angle.
	 */
    public double angleTo(Vector3D vector) {
        return Math.acos(vector.getUnitVector().dot(this.normal));
    }

    /**
	 * Convenience method to calculate the angle to the Z axis using angleTo()
	 * @return The calculated angle.
	 */
    public double angleToZ() {
        return angleTo(new Vector3D(0, 0, 1));
    }

    public double calcToolZMin(double x, double y, Tool t) {
        return 0;
    }

    public XYZBounds getCartesianBoundingBox() {
        XYZBounds bounds = new XYZBounds();
        bounds.Xmax = Vone.x;
        if (Vtwo.x > bounds.Xmax) {
            bounds.Xmax = Vtwo.x;
        }
        ;
        if (Vthree.x > bounds.Xmax) {
            bounds.Xmax = Vthree.x;
        }
        ;
        bounds.Xmin = Vone.x;
        if (Vtwo.x < bounds.Xmin) {
            bounds.Xmin = Vtwo.x;
        }
        ;
        if (Vthree.x < bounds.Xmin) {
            bounds.Xmin = Vthree.x;
        }
        ;
        bounds.Ymax = Vone.y;
        if (Vtwo.y > bounds.Ymax) {
            bounds.Ymax = Vtwo.y;
        }
        ;
        if (Vthree.y > bounds.Ymax) {
            bounds.Ymax = Vthree.y;
        }
        ;
        bounds.Ymin = Vone.y;
        if (Vtwo.y < bounds.Ymin) {
            bounds.Ymin = Vtwo.y;
        }
        ;
        if (Vthree.y < bounds.Ymin) {
            bounds.Ymin = Vthree.y;
        }
        ;
        bounds.Zmax = Vone.z;
        if (Vtwo.z > bounds.Zmax) {
            bounds.Zmax = Vtwo.z;
        }
        ;
        if (Vthree.z > bounds.Zmax) {
            bounds.Zmax = Vthree.z;
        }
        ;
        bounds.Zmin = Vone.z;
        if (Vtwo.z < bounds.Zmin) {
            bounds.Zmin = Vtwo.z;
        }
        ;
        if (Vthree.z < bounds.Zmin) {
            bounds.Zmin = Vthree.z;
        }
        ;
        return bounds;
    }

    public String toString() {
        return new String("{" + Vone + "," + Vtwo + "," + Vthree + "} " + normal);
    }
}
