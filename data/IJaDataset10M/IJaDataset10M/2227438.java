package gumbo.tech.space.util;

import gumbo.core.util.AssertUtils;
import gumbo.core.util.NullObject;
import gumbo.tech.math.Linear3;
import gumbo.tech.math.Planar3;
import gumbo.tech.math.Point3;
import gumbo.tech.math.UnitVector3;
import gumbo.tech.space.Intersectable;
import gumbo.tech.space.Intersectable3;
import gumbo.tech.space.Intersector;
import java.util.List;

public class IntersectableUtil {

    private IntersectableUtil() {
    }

    /**
	 * Empty space. Intersects no geometry or shapes.
	 */
    public static final Intersectable EMPTY_SPACE = new Intersectable() {

        @Override
        public boolean intersects(Object shape) {
            AssertUtils.assertNonNullArg(shape);
            return false;
        }
    };

    /**
	 * Infinite space. Intersects all geometry and shapes, except for
	 * EMPTY_SPACE.
	 */
    public static final Intersectable INFINITE_SPACE = new Intersectable() {

        @Override
        public boolean intersects(Object shape) {
            AssertUtils.assertNonNullArg(shape);
            if (shape == EMPTY_SPACE) return false;
            return true;
        }
    };

    /**
	 * Empty 3-space. Intersects no geometry or shapes.
	 */
    public static final Intersectable3 EMPTY_SPACE3 = new Intersectable3() {

        @Override
        public boolean intersectsPoint(Point3 point) {
            AssertUtils.assertNonNullArg(point);
            return false;
        }

        @Override
        public boolean intersectsLine(Linear3 linear) {
            AssertUtils.assertNonNullArg(linear);
            return false;
        }

        @Override
        public boolean intersectsPlane(Planar3 planar) {
            AssertUtils.assertNonNullArg(planar);
            return false;
        }

        @Override
        public boolean intersects(Object shape) {
            AssertUtils.assertNonNullArg(shape);
            return false;
        }
    };

    /**
	 * Infinite 3-space. Intersects all geometry and shapes, except for
	 * EMPTY_SPACE3.
	 */
    public static final Intersectable3 INFINITE_SPACE3 = new Intersectable3() {

        @Override
        public boolean intersectsPoint(Point3 point) {
            AssertUtils.assertNonNullArg(point);
            return true;
        }

        @Override
        public boolean intersectsLine(Linear3 linear) {
            AssertUtils.assertNonNullArg(linear);
            return true;
        }

        @Override
        public boolean intersectsPlane(Planar3 planar) {
            AssertUtils.assertNonNullArg(planar);
            return true;
        }

        @Override
        public boolean intersects(Object shape) {
            AssertUtils.assertNonNullArg(shape);
            if (shape == EMPTY_SPACE) return false;
            return true;
        }
    };

    /**
	 * The positive Z sub-space.
	 */
    public static final Intersectable3 PLUS_Z_SPACE3 = new Intersectable3() {

        @Override
        public boolean intersectsPoint(Point3 point) {
            AssertUtils.assertNonNullArg(point);
            return point.getZ() > 0.0;
        }

        @Override
        public boolean intersectsLine(Linear3 linear) {
            AssertUtils.assertNonNullArg(linear);
            UnitVector3 dir = linear.getDirection();
            Point3 head = linear.getHeadPoint();
            Point3 tail = linear.getTailPoint();
            if (linear.isFinite()) {
                if (intersectsPoint(head)) {
                    return true;
                }
                if (intersectsPoint(tail)) {
                    return true;
                }
                return false;
            } else if (linear.isSemiFinite()) {
                if (intersectsPoint(tail)) {
                    return true;
                }
                if (dir.getZ() > 0) {
                    return true;
                }
                return false;
            } else {
                if (dir.getZ() != 0) {
                    return true;
                }
                return false;
            }
        }

        @Override
        public boolean intersectsPlane(Planar3 planar) {
            AssertUtils.assertNonNullArg(planar);
            UnitVector3 norm = planar.getNormal();
            List<Point3> verts = planar.getVertices();
            if (planar.isFinite()) {
                for (Point3 vert : verts) {
                    if (intersectsPoint(vert)) {
                        return true;
                    }
                }
                return false;
            } else {
                if (norm.getZ() != 0) {
                    return true;
                }
                return false;
            }
        }

        @Override
        public boolean intersects(Object shape) {
            return IntersectableUtil.intersects(PLUS_Z_SPACE3, shape);
        }
    };

    /**
	 * Tests the intersection of a target intersectable and a shape. If
	 * possible, performs the operation directly, otherwise finds an Intersector
	 * to do it.
	 * @param target Temp input target intersectable. Never null.
	 * @param shape Temp input test shape. Never null.
	 * @return The result.
	 * @throws IllegalStateException if intersection cannot be performed.
	 */
    public static boolean intersects(Intersectable3 target, Object shape) {
        if (target == null) throw new IllegalArgumentException();
        if (shape == null) throw new IllegalArgumentException();
        if (shape instanceof Point3) {
            return target.intersectsPoint((Point3) shape);
        }
        if (shape instanceof Linear3) {
            return target.intersectsLine((Linear3) shape);
        }
        if (shape instanceof Planar3) {
            return target.intersectsPlane((Planar3) shape);
        }
        Intersector intersector = IntersectorUtil.findIntersectorAssuredly(target, shape);
        return intersector.intersects(target, shape);
    }

    public static Null getNull() {
        if (NULL == null) {
            NULL = new Null();
        }
        return NULL;
    }

    private static Null NULL = null;

    public static Null3 getNull3() {
        if (NULL3 == null) {
            NULL3 = new Null3();
        }
        return NULL3;
    }

    private static Null3 NULL3 = null;

    public static class Null implements NullObject, Intersectable {

        protected Null() {
        }

        @Override
        public boolean intersects(Object shape) {
            return false;
        }

        private Object readResolve() {
            return getNull();
        }

        private static final long serialVersionUID = 1L;
    }

    ;

    public static class Null3 extends Null implements Intersectable3 {

        protected Null3() {
        }

        @Override
        public boolean intersectsLine(Linear3 linear) {
            return false;
        }

        @Override
        public boolean intersectsPlane(Planar3 planar) {
            return false;
        }

        @Override
        public boolean intersectsPoint(Point3 point) {
            return false;
        }

        private Object readResolve() {
            return getNull();
        }

        private static final long serialVersionUID = 1L;
    }

    ;
}
