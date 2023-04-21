package org.xmlvm.iphone;

import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly(references = { CGPoint.class, CGSize.class })
public class CGRect {

    private static final CGRect NULL = new CGRect(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0, 0);

    private static final CGRect ZERO = new CGRect(0, 0, 0, 0);

    private static final CGRect INFINITE = new CGRect(-Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

    public CGPoint origin;

    public CGSize size;

    public CGRect(float x, float y, float width, float height) {
        origin = new CGPoint(x, y);
        size = new CGSize(width, height);
    }

    public CGRect(CGRect other) {
        origin = new CGPoint(other.origin);
        size = new CGSize(other.size);
        origin.x = other.origin.x;
        origin.y = other.origin.y;
        size.width = other.size.width;
        size.height = other.size.height;
    }

    public static CGRect Null() {
        return new CGRect(NULL);
    }

    public static CGRect Zero() {
        return new CGRect(ZERO);
    }

    public static CGRect Infinite() {
        return new CGRect(INFINITE);
    }

    public static CGRect Intersection(CGRect r1, CGRect r2) {
        if (r1 == null || r2 == null) {
            return Null();
        }
        float maxleft = r1.origin.x > r2.origin.x ? r1.origin.x : r2.origin.x;
        float maxtop = r1.origin.y > r2.origin.y ? r1.origin.y : r2.origin.y;
        float minright = (r1.origin.x + r1.size.width) < (r2.origin.x + r2.size.width) ? r1.origin.x + r1.size.width : r2.origin.x + r2.size.width;
        float minbottom = (r1.origin.y + r1.size.height) < (r2.origin.y + r2.size.height) ? r1.origin.y + r1.size.height : r2.origin.y + r2.size.height;
        if (maxleft > minright || maxtop > minbottom) {
            return Null();
        }
        return new CGRect(maxleft, maxtop, minright - maxleft, minbottom - maxtop);
    }

    public static CGRect Union(CGRect r1, CGRect r2) {
        throw new RuntimeException("Not implemented");
    }

    public boolean isNull() {
        return equals(NULL);
    }

    public boolean isEmpty() {
        return equals(ZERO);
    }

    public boolean isInfinite() {
        return equals(INFINITE);
    }

    @Override
    public String toString() {
        return "[" + origin.toString() + size.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CGRect)) {
            return false;
        }
        CGRect r = (CGRect) o;
        return origin.equals(r.origin) && size.equals(r.size);
    }
}
