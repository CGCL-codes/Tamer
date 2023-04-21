package jumbo.euclid;

import jumbo.xml.util.Choice3;
import jumbo.xml.util.Util;

/**
 Point3 - 3-dimensional point class
<P>
<P>
 Point3 represents a 3-dimensional point.   It is one of a set of
 primitives which can be combined to create and manipulate complex
 3-dimensional objects. Points can be transformed with rotation
 matrices or rotation-translation matrices (Transform3), can
 be calculated from other primitives or can be used to generate other
 primitives.
<P>
 Default point is 0.0, 0.0, 0.0
<P>
@author <A HREF=mailto:@p.murray-rust@mail.cryst.bbk.ac.uk>Peter Murray-Rust</A>
@see Vector3
@see Line3
@see Point3Vector
@see Plane3

@author (C) P. Murray-Rust, 1996
*/
public class Point3 extends Status {

    /** the coordinates of the point
*/
    protected double[] flarray = new double[3];

    public Point3() {
    }

    /** formed from point components 
*/
    public Point3(double x, double y, double z) {
        flarray[0] = x;
        flarray[1] = y;
        flarray[2] = z;
    }

    /** copy constructor
*/
    public Point3(Point3 p) {
        System.arraycopy(p.flarray, 0, flarray, 0, 3);
    }

    /** constructor from a double[] (or a RealArray)
*/
    public Point3(double[] f) {
        System.arraycopy(f, 0, flarray, 0, 3);
    }

    /**  make a point from a vector
@return creates the point at head of vector rooted at the origin
*/
    public Point3(Vector3 v) {
        System.arraycopy(v.flarray, 0, flarray, 0, 3);
    }

    public Point3 clone(Point3 p) {
        System.arraycopy(p.flarray, 0, flarray, 0, 3);
        return this;
    }

    /** overloaded assignment from a double[] (or a RealArray)
*/
    public Point3 clone(double[] f) {
        System.arraycopy(f, 0, flarray, 0, 3);
        return this;
    }

    /** get components as double[] 
*/
    public double[] getArray() {
        return flarray;
    }

    /** from Vector3
*/
    public Point3 clone(Vector3 v) {
        System.arraycopy(v.flarray, 0, flarray, 0, 3);
        return this;
    }

    /** are two points identical?
*/
    public boolean equals(Point3 p) {
        return Real.isEqual(3, flarray, p.flarray);
    }

    /** vector between two points 
*/
    public Vector3 subtract(Point3 p2) {
        Vector3 v1 = new Vector3(this);
        for (int i = 0; i < 3; i++) {
            v1.flarray[i] -= p2.flarray[i];
        }
        v1.vec_length = -1.0;
        return v1;
    }

    /** New point from point+point - used for finding centrois, etc.  
*/
    public Point3 plus(Point3 p) {
        Point3 p1 = new Point3();
        for (int i = 0; i < 3; i++) {
            p1.flarray[i] = flarray[i] + p.flarray[i];
        }
        return p1;
    }

    /** New point from point+vector 
*/
    public Point3 plus(Vector3 v) {
        Point3 p1 = new Point3();
        for (int i = 0; i < 3; i++) {
            p1.flarray[i] = flarray[i] + v.flarray[i];
        }
        return p1;
    }

    /** New point from point-vector 
*/
    public Point3 subtract(Vector3 v) {
        Point3 p1 = new Point3();
        for (int i = 0; i < 3; i++) {
            p1.flarray[i] = flarray[i] - v.flarray[i];
        }
        return p1;
    }

    /** scale point 
*/
    public Point3 multiplyBy(double f) {
        Point3 p1 = new Point3();
        for (int i = 0; i < 3; i++) {
            p1.flarray[i] = flarray[i] * f;
        }
        return p1;
    }

    public Point3 divideBy(double f) {
        Point3 p1 = new Point3();
        for (int i = 0; i < 3; i++) {
            p1.flarray[i] = flarray[i] / f;
        }
        return p1;
    }

    /** subscript operator counts from ZERO
*/
    public double elementAt(int n) {
        return flarray[n];
    }

    /** transform a point; does NOT modify 'this'
*/
    public Point3 transform(Transform3 t) {
        RealArray col3 = new RealArray(4, 1.0);
        col3.setElements(0, this.flarray);
        RealArray result = null;
        try {
            result = t.multiply(col3);
        } catch (UnequalMatricesException e) {
            Util.bug(e);
        }
        Point3 pout = new Point3(result.getSubArray(0, 2).getArray());
        return pout;
    }

    /** distance of point from origin
*/
    public double getDistanceFromOrigin() {
        Vector3 v = new Vector3(this);
        return v.getLength();
    }

    /** distance of point from another point
*/
    public double getDistanceFromPoint(Point3 p2) {
        Vector3 v = new Vector3(p2.subtract(this));
        return v.getLength();
    }

    public double distanceFromPlane(Plane3 pl) {
        return pl.getDistanceFromPoint(this);
    }

    /** 
@param l any line
@return the point <TT>p</TT> on line <TT>l</TT> where <TT>this-p</TT>
 distance is shortest
*/
    public Point3 getClosestPointOnLine(Line3 l) {
        return l.getClosestPointTo(this);
    }

    boolean isOnLine(Line3 l) {
        return l.containsPoint(this);
    }

    boolean isOnPlane(Plane3 pl) {
        return pl.containsPoint(this);
    }

    public double distanceFromLine(Line3 l) {
        return l.getDistanceFromPoint(this);
    }

    /**mid-point of two points
*/
    public Point3 getMidPoint(Point3 p2) {
        Point3 p = new Point3();
        {
            for (int i = 0; i < 3; i++) {
                p.flarray[i] = (this.flarray[i] + p2.flarray[i]) / 2.0;
            }
        }
        return p;
    }

    /** angle (p1-p2-p3; vertex is p2)
@exception ZeroVectorException two points are coincident
*/
    public static Angle getAngle(Point3 p1, Point3 p2, Point3 p3) throws ZeroVectorException {
        Vector3 v1 = p1.subtract(p2);
        return v1.getAngleMadeWith(p3.subtract(p2));
    }

    /** torsion angle
@exception ZeroVectorException two points are coincident or three points are colinear
*/
    public static Angle getTorsion(Point3 p1, Point3 p2, Point3 p3, Point3 p4) throws ZeroVectorException {
        Vector3 v23 = p3.subtract(p2);
        Vector3 v13a = p2.subtract(p1);
        Vector3 v13 = v13a.cross(v23);
        Vector3 v24 = v23.cross(p4.subtract(p3));
        v13.normalise();
        v24.normalise();
        double ang = v13.getAngleMadeWith(v24).getAngle();
        if (v13.getScalarTripleProduct(v24, v23) < 0.0) {
            ang = -ang;
        }
        return new Angle(ang);
    }

    /** add point using internal coordinates
@exception ZeroVectorException two points are coincident or three points are colinear
*/
    public static Point3 calculateFromInternalCoordinates(Point3 p1, Point3 p2, Point3 p3, double length, Angle angle, Angle torsion) throws ZeroVectorException {
        Vector3 v32 = p2.subtract(p3);
        Vector3 v12 = p2.subtract(p1);
        Vector3 v13a = v12.cross(v32);
        Vector3 v13n = v13a.normalise();
        Vector3 v32n = v32.normalise();
        Vector3 v34 = v32n.multiplyBy(length);
        Transform3 t = new Transform3(v13n, angle);
        v34 = v34.transform(t);
        v32n = v32n.negative();
        Transform3 t1 = new Transform3(v32n, torsion);
        v34 = v34.transform(t1);
        Point3 p4 = p3.plus(v34);
        return p4;
    }

    /** is a point at Origin?
*/
    public boolean isOrigin() {
        for (int i = 0; i < 3; i++) {
            if (Real.isZero(flarray[i])) return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(" ");
        for (int i = 0; i < 3; i++) {
            sb.append(flarray[i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    /**tests Point3 routines = new Point3
*/
    public static void main(String args[]) {
        System.out.println("*--------------Testing Point3-------------\n");
        Point3 p0 = new Point3();
        System.out.println("p0: " + p0 + "\n");
        System.out.println("................................................\n");
        Point3 p1 = new Point3(0, 0, 0);
        System.out.println("p0: " + p0 + "\n");
        System.out.println("................................................\n");
        boolean i1 = p1.isOrigin();
        System.out.println("i1: " + i1 + "\n");
        System.out.println("................................................\n");
        Point3 p2 = new Point3(1., 2., 3.);
        System.out.println("p2: " + p2 + "\n");
        System.out.println("................................................\n");
        double f1 = p2.elementAt(2);
        System.out.println("f1 : " + f1 + "\n");
        System.out.println("................................................\n");
        f1 = p2.elementAt(1);
        System.out.println("f1 : " + f1 + "\n");
        System.out.println("................................................\n");
        p1 = p2;
        System.out.println("p1: " + p1 + "\n");
        System.out.println("................................................\n");
        boolean lt = (p2.equals(p1));
        System.out.println("lt: " + lt + "\n");
        System.out.println("................................................\n");
        Vector3 v1 = new Vector3(3., 2., 1.);
        System.out.println("v1: " + v1 + "\n");
        System.out.println("................................................\n");
        p2 = new Point3(v1);
        System.out.println("p2: " + p2 + "\n");
        System.out.println("................................................\n");
        Point3 p3 = p1.plus(v1);
        System.out.println("p3: " + p3 + "\n");
        System.out.println("................................................\n");
        p3 = p3.plus(v1);
        System.out.println("p3: " + p3 + "\n");
        System.out.println("................................................\n");
        p3 = p3.multiplyBy(1.5);
        System.out.println("p3: " + p3 + "\n");
        System.out.println("................................................\n");
        p3 = p3.multiplyBy(1 / 2.5);
        System.out.println("p3: " + p3 + "\n");
        System.out.println("................................................\n");
        Vector3 v2 = p2.subtract(p3);
        System.out.println("v2: " + v2 + "\n");
        System.out.println("................................................\n");
        Angle rot = new Angle(1.57);
        Transform3 t3 = null;
        t3 = new Transform3(Choice3.X, rot);
        System.out.println("t3: " + t3 + "\n");
        System.out.println("................................................\n");
        p3 = p3.transform(t3);
        System.out.println("p3: " + p3 + "\n");
        System.out.println("................................................\n");
        Point3 p4 = p3.getMidPoint(p2);
        System.out.println("p4: " + p4 + "\n");
        System.out.println("................................................\n");
        double d1 = p2.getDistanceFromOrigin();
        System.out.println("di: " + d1 + "\n");
        System.out.println("................................................\n");
        d1 = p2.getDistanceFromPoint(p3);
        System.out.println("di: " + d1 + "\n");
        System.out.println("................................................\n");
        try {
            Point3 pp1 = new Point3(0.1, 0.2, 0.3);
            Point3 pp2 = new Point3(1.4, 0.3, 0.1);
            Point3 pp3 = new Point3(1.7, 2.0, 1.0);
            Angle ang = new Angle(110, Angle.DEGREES);
            Angle tors = new Angle(50, Angle.DEGREES);
            Point3 pp4 = Point3.calculateFromInternalCoordinates(pp1, pp2, pp3, 1.0, ang, tors);
            System.out.println("pp4  " + pp4 + "\n");
            System.out.println("angle  (" + ang + ") =" + Point3.getAngle(pp2, pp3, pp4) + "\n");
            System.out.println("torsion  (" + tors + ") =" + Point3.getTorsion(pp1, pp2, pp3, pp4) + "\n");
            System.out.println("................................................\n");
        } catch (Exception e) {
            System.out.println("EXC: " + e);
        }
    }
}
