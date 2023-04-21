package coho.geom.twodim;

import coho.common.number.*;
import coho.geom.*;

public class BoundingBox implements GeomObj2, Comparable<BoundingBox> {

    private final Point ll, ur;

    private final CohoType type;

    public CohoType type() {
        return type;
    }

    public BoundingBox(Point p0, Point p1) {
        if (p0.type() != p1.type()) {
            throw new GeomException("Type for bounding box are not the same");
        }
        this.type = p0.type();
        ll = Point.create(p0.x().min(p1.x()), p0.y().min(p1.y()));
        ur = Point.create(p0.x().max(p1.x()), p0.y().max(p1.y()));
    }

    public static BoundingBox create(Point p0, Point p1) {
        return new BoundingBox(p0, p1);
    }

    public Point ll() {
        return ll;
    }

    public Point ur() {
        return ur;
    }

    public Point lr() {
        return new Point(ur.x(), ll.y());
    }

    public Point ul() {
        return new Point(ll.x(), ur.y());
    }

    public BoundingBox bbox() {
        return this;
    }

    public BoundingBox translate(Point offset) {
        return (new BoundingBox(ll.translate(offset), ur.translate(offset)));
    }

    public BoundingBox transpose() {
        return (new BoundingBox(ll.transpose(), ur.transpose()));
    }

    public BoundingBox negate() {
        return new BoundingBox(ur.negate(), ll.negate());
    }

    public GeomObj2 intersect(GeomObj2 g) {
        if (g instanceof Point) {
            Point p0 = (Point) g;
            if (((ll.x().compareTo(p0.x()) <= 0) && (ur.x().compareTo(p0.x()) >= 0)) && ((ll.y().compareTo(p0.y()) <= 0) && (ur.y().compareTo(p0.y()) >= 0))) {
                return p0;
            } else {
                return Empty.instance();
            }
        } else if (g instanceof BoundingBox) {
            BoundingBox b = (BoundingBox) g;
            Point p0 = new Point(ll.x().max(b.ll().x()), ll.y().max(b.ll().y()));
            Point p1 = new Point(ur.x().min(b.ur().x()), ur.y().min(b.ur().y()));
            if ((p0.x().compareTo(p1.x()) > 0) || (p0.y().compareTo(p1.y()) > 0)) {
                return Empty.instance();
            } else if (p0.compareTo(p1) == 0) {
                return p0;
            } else if (p0.x().equals(p1.x()) || p0.y().equals(p1.y())) {
                return new Segment(p0, p1);
            } else {
                return new BoundingBox(p0, p1);
            }
        } else {
            return g.intersect(this);
        }
    }

    public BoundingBox union(GeomObj2 g) {
        BoundingBox that = g.bbox();
        if (that == null) {
            return (this);
        } else {
            BoundingBox result = new BoundingBox(new Point(ll.x().min(that.ll().x()), ll.y().min(that.ll().y())), new Point(ur.x().max(that.ur().x()), ur.y().max(that.ur().y())));
            return result;
        }
    }

    public boolean contains(GeomObj2 g) {
        BoundingBox that = g.bbox();
        return ((ll.x().compareTo(that.ll().x()) <= 0) && (ur.x().compareTo(that.ur().x()) >= 0)) && ((ll.y().compareTo(that.ll().y()) <= 0) && (ur.y().compareTo(that.ur().y()) >= 0));
    }

    public BoundingBox[] split() {
        CohoNumber lx = ll().x();
        CohoNumber ly = ll().y();
        CohoNumber hx = ur().x();
        CohoNumber hy = ur().y();
        CohoNumber mx = lx.add(hx).div(2);
        CohoNumber my = ly.add(hy).div(2);
        Point center = Point.create(mx, my);
        BoundingBox[] bboxes = new BoundingBox[4];
        bboxes[0] = BoundingBox.create(ll(), center);
        bboxes[1] = BoundingBox.create(lr(), center);
        bboxes[2] = BoundingBox.create(ur(), center);
        bboxes[3] = BoundingBox.create(ul(), center);
        return bboxes;
    }

    public int compareTo(BoundingBox b) {
        int cmp = ll.compareTo(b.ll());
        if (cmp != 0) return cmp;
        return ur.compareTo(b.ur());
    }

    @Override
    public boolean equals(Object g) {
        try {
            return (g instanceof BoundingBox) && (compareTo((BoundingBox) g) == 0);
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ll.hashCode() ^ ur.hashCode() >> 16;
    }

    /**
	 * Other functions
	 */
    public boolean isPoint() {
        return ll.equals(ur);
    }

    public boolean isSegment() {
        return (ll.x().equals(ur.x())) || (ll.y().equals(ur.y()));
    }

    public BoundingBox specifyType(CohoType type) {
        return new BoundingBox(ll.specifyType(type), ur.specifyType(type));
    }

    public double maxError() {
        if (type instanceof ScaleType) return 0; else return Math.max(ll.maxError(), ur.maxError());
    }

    public String toString() {
        return "<" + ll + " " + ur + ">";
    }

    public static void main(String[] args) {
        BoundingBox b = new BoundingBox(new Point(1, 3), new Point(3, 5));
        BoundingBox bb = new BoundingBox(new Point(0.125, 0.5), new Point(6.25, 4.5));
        System.out.println("The lower-left of " + b.toString() + " is " + b.ll() + " and the upper-right is " + b.ur());
        System.out.println("The bbox of " + bb.toString() + "is " + bb.bbox());
        System.out.println("Translate " + b.toString() + " with " + (new Point(1, 2)).toString() + " is " + b.translate(new Point(1, 2)));
        System.out.println("The transpose of " + bb.toString() + " is " + bb.transpose());
        System.out.println(bb.toString() + " is a segment?: " + bb.isSegment());
        System.out.println(b.toString() + " is a segment?: " + b.isSegment());
        System.out.println(bb.toString() + " is a point?: " + bb.isPoint());
        System.out.println(b.toString() + " is a point?: " + b.isPoint());
        System.out.println("The intersection of " + b.toString() + " and " + bb.toString() + " is " + b.intersect(bb) + " or " + bb.intersect(b));
        System.out.println("The intersection of " + b.toString() + " and " + (new Point(1, 1)).toString() + " is " + b.intersect(new Point(1, 1)));
        System.out.println(b.toString() + " contains " + bb.toString() + " ? " + b.contains(bb));
        System.out.println("The union of " + b.toString() + " and " + bb.toString() + " is " + b.union(bb));
    }
}
