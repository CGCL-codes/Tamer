package com.msli.graphic.math;

import com.msli.core.exception.UnmodifiableStateException;
import com.msli.core.util.CoreUtils;
import com.msli.core.util.JavaUtils;
import com.msli.graphic.geom.Geometric;
import com.msli.graphic.geom.xform.Xformer;

/**
 * A Geometric 3D plane of infinite extent, which is defined by a normal vector
 * from the origin and a signed distance from the origin along the normal. A
 * plane, as defined here, has an inside (back side) and an outside (front
 * side), with the outside (front side) being the one the normal is pointing
 * away from.
 * <P>
 * Object equality is based on identity. Use Quantitative methods for
 * state-based equality.
 * <P>
 * Derived from gumbo3.graphic.math.Plane3.
 * @author jonb
 */
public interface Plane3 extends MathObject, Geometric {

    /**
	 * Sets the distance from the origin to this plane along its normal vector.
	 * Does not affect the plane normal.
	 * @param dist Temp input signed distance from origin. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 setDistance(double dist);

    /**
	 * Sets the normal vector for this plane. Does not affect the plane
	 * distance.
	 * @param norm Temp input normal direction. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 setNormal(Tuple3 norm);

    /**
	 * Sets this plane to the value of the specified plane.
	 * @param val Temp input plane. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 set(Plane3 val);

    /**
	 * Sets this plane to the specified value.
	 * @param norm Temp input normal direction. Never null.
	 * @param dist Temp input signed distance from origin. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 set(Tuple3 norm, double dist);

    /**
	 * Sets this plane such that it contains the specified point, and has the
	 * specified normal direction.
	 * @param norm Temp input normal direction. Never null.
	 * @param point Temp input point. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 set(Tuple3 norm, Tuple3 point);

    /**
	 * Sets this plane such that it contains the specified point and vectors.
	 * The normal is constructed by crossing the first vector with the second
	 * one.
	 * @param point Temp input point. Never null.
	 * @param vectA Temp input first vector. Never null.
	 * @param vectB Temp input second vector. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 setVectors(Tuple3 point, Tuple3 vectA, Tuple3 vectB);

    /**
	 * Sets this plane such that it contains the specified points. The normal is
	 * constructed by crossing the vector from the reference point to the first
	 * point, with the vector from the reference point to the second point.
	 * @param point Temp input reference point. Never null.
	 * @param pointA Temp input first point. Never null.
	 * @param pointB Temp input second point. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Plane3 setPoints(Tuple3 point, Tuple3 pointA, Tuple3 pointB);

    /**
	 * Gets this plane's signed distance, measured from the origin to this 
	 * plane along its normal.
	 * @return Temp output signed distance. Never null.
	 */
    public Scalar getDistance();

    /**
	 * Gets this plane's normal direction, which is perpendicular to the plane
	 * and points from its front side (outside).
	 * @return Temp output normal direction. Never null.
	 */
    public UnitVector3 getNormal();

    /**
	 * Gets the point on this plane at the intersection of a vector from the
	 * origin along plane's normal. By definition, the distance from the origin
	 * to this point, in the direction of the normal, is that returned by
	 * getDistance().
	 * @return Temp output value. Never null.
	 */
    public Point3 getNormalPoint();

    /**
	 * Computes the signed distance from this plane along its normal to a point.
	 * Returns zero if the point is on the plane, and negative if the point is
	 * on the "back side" (inside) of the plane.
	 * @param point Temp input point. Never null.
	 * @return Temp output signed distance. Never null.
	 */
    public double distanceTo(Tuple3 point);

    /**
	 * Returns the signed distance from a line's position point to this plane
	 * along the line's direction vector. Returns zero if the point is on the
	 * plane, negative if the ray is pointing away from the plane, and NaN if
	 * the line is parallel to this plane.
	 * @param line Temp input line. Never null.
	 * @return Temp output signed distance. Never null.
	 */
    public double distanceFrom(Line3 line);

    /**
	 * Intersects a line with this plane, and returns the intersection point.
	 * Returns (NaN, NaN, NaN) if the line is parallel to this plane (no
	 * intersection).
	 * @param line Temp input line. Never null.
	 * @return Temp output point. Never null.
	 */
    public Point3 intersect(Line3 line);

    /**
	 * Determines whether a point is inside of, on, or outside of this plane.
	 * @param point Temp input point. Never null.
	 * @return -1 if inside, 0 if on, and 1 if outside.
	 */
    public int compare(Tuple3 point);

    @Override
    public Plane3 transformGeometry(Xformer xformer);

    @Override
    public Plane3 dupe();

    /**
	 * Concrete serializable unmodifiable wrapper. All mutators throw
	 * UnmodifiableStateException.
	 */
    public static class Unmod extends MathObject.Wrapper implements Plane3 {

        /**
		 * Creates an unmodifiable wrapper for the target. State update
		 * observers will observe this wrapper, not its target.
		 * @param target Shared exposed target of this wrapper. Never null.
		 * @throws IllegalArgumentException if the target implements
		 * NotWrapperAware.
		 */
        public Unmod(Plane3 target) {
            super(target);
            _target = target;
        }

        @Override
        public Plane3 set(Tuple3 norm, double dist) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 set(Plane3 val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 set(Tuple3 norm, Tuple3 point) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 setDistance(double dist) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 setNormal(Tuple3 norm) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 setPoints(Tuple3 point, Tuple3 pointA, Tuple3 pointB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 setVectors(Tuple3 point, Tuple3 vectA, Tuple3 vectB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public final int compare(Tuple3 point) {
            return _target.compare(point);
        }

        @Override
        public final double distanceFrom(Line3 line) {
            return _target.distanceFrom(line);
        }

        @Override
        public final double distanceTo(Tuple3 point) {
            return _target.distanceTo(point);
        }

        @Override
        public final Scalar getDistance() {
            return _target.getDistance();
        }

        @Override
        public final UnitVector3 getNormal() {
            return _target.getNormal();
        }

        @Override
        public Point3 getNormalPoint() {
            return _target.getNormalPoint();
        }

        @Override
        public final Point3 intersect(Line3 line) {
            return _target.intersect(line);
        }

        @Override
        public Plane3 transformGeometry(Xformer xformer) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Plane3 dupe() {
            return new Plane3.Unmod(_target);
        }

        @Override
        public final boolean equalsValue(Object obj, double tolerance) {
            return _target.equalsValue(obj, tolerance);
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _target = null;
        }

        private static final long serialVersionUID = 1L;

        private Plane3 _target = null;
    }

    /**
	 * Default implementation for concrete type DD.
	 */
    public static class Impl extends MathObject.Base implements Plane3 {

        /**
		 * Creates an instance, with default normal direction (X axis) and zero
		 * distance (i.e. the YZ plane).
		 */
        public Impl() {
            this(MathUtils.DEFAULT_DIRECTION3, 0.0);
        }

        /**
		 * Creates an instance, as specified.
		 * @param norm Temp input normal direction. Never null.
		 * @param dist Temp input signed distance from origin. Never null.
		 */
        public Impl(Tuple3 norm, double dist) {
            set(norm, dist);
        }

        /**
		 * Creates an instance, with the same value as the target.
		 * @param val Temp input value.  Never null.
		 */
        public Impl(Plane3 val) {
            set(val);
        }

        /**
		 * Called by the system and subclasses before initializing/updating this
		 * object's state. Intended to be overridden by subclasses to passively
		 * check value constraints (e.g. non-negative size) and to throw
		 * IllegalStateException if they are violated. All versions of
		 * checkState() in this class must be mutually consistent. Should NOT be
		 * called directly by the client.
		 * <P>
		 * Implementors should call the base method to assure that any base
		 * class constraints are imposed. The default implementation checks for
		 * null arguments.
		 * @param norm Temp input normal direction. Never null.
		 * @param dist Temp input signed distance from origin. Never null.
		 */
        protected void checkState(Tuple3 norm, double dist) {
            CoreUtils.assertNonNullArg(norm);
            CoreUtils.assertNonNullArg(dist);
        }

        /**
		 * Called by the system and subclasses to initialize/update this
		 * object's state, with a partial state value or a value computed by the
		 * system. Intended to be overridden by subclasses to actively constrain
		 * state values as they are set (e.g. unit vector length). All versions
		 * of updateState() in this class must be mutually consistent. Should
		 * NOT be called directly by the client.
		 * <P>
		 * Implementors should call the base method to set the state after
		 * constraining the values so that any base class constraints are also
		 * imposed. The default implementation checks for null arguments, calls
		 * checkState(), and then calls superUpdateState() with the specified
		 * element values.
		 * @param norm Temp input normal direction. Never null.
		 * @param dist Temp input signed distance from origin. Never null.
		 */
        protected void updateState(Tuple3 norm, double dist) {
            checkState(norm, dist);
            superUpdateState(norm, dist);
        }

        /**
		 * Similar to other updateState() methods but allows for shortcuts if
		 * the value is a recognized type known to satisfy all constraints.
		 * <P>
		 * If the value satisfies all the constraints of this object,
		 * implementors should call the base method to set the state rather than
		 * doing it directly with superUpdateState() so that any base class
		 * constraints are also imposed. The default implementation checks for
		 * null arguments, and call updateState() with the discrete state.
		 * @param val Temp input value. Never null.
		 */
        protected void updateState(Plane3 val) {
            CoreUtils.assertNonNullArg(val);
            updateState(val.getNormal(), val.getDistance().get());
        }

        /**
		 * Updates the state without checks or constraints, then notifies update
		 * observers. Use with caution so that the input state value conforms to
		 * the constraints of the subclass as well as all super classes.
		 * <P>
		 * In general, should use super.updateState() instead of this method,
		 * which assures that super class constraints are imposed.
		 * @param norm Temp input normal direction. Never null.
		 * @param dist Temp input signed distance from origin. Never null.
		 */
        protected final void superUpdateState(Tuple3 norm, double dist) {
            _norm.set(norm);
            _dist.set(dist);
            notifyUpdateObservers();
        }

        @Override
        public Plane3 set(Plane3 val) {
            updateState(val);
            return this;
        }

        @Override
        public Plane3 set(Tuple3 norm, double dist) {
            updateState(norm, dist);
            return this;
        }

        @Override
        public Plane3 set(Tuple3 norm, Tuple3 point) {
            double dist = norm.dot(point);
            updateState(norm, dist);
            return this;
        }

        @Override
        public Plane3 setDistance(double dist) {
            updateState(_norm, dist);
            return this;
        }

        @Override
        public Plane3 setNormal(Tuple3 norm) {
            updateState(norm, _dist.get());
            return this;
        }

        @Override
        public Plane3 setPoints(Tuple3 point, Tuple3 pointA, Tuple3 pointB) {
            _dummyTupleA.sub(pointA, point);
            _dummyTupleB.sub(pointB, point);
            _dummyUVect.cross(_dummyTupleA, _dummyTupleB);
            double dist = _dummyUVect.dot(point);
            set(_dummyUVect, dist);
            return this;
        }

        @Override
        public Plane3 setVectors(Tuple3 point, Tuple3 vectA, Tuple3 vectB) {
            _dummyUVect.cross(vectA, vectB);
            double dist = _dummyUVect.dot(point);
            set(_dummyUVect, dist);
            return this;
        }

        @Override
        public final int compare(Tuple3 point) {
            double dist = distanceTo(point);
            if (dist < 0.0) return -1;
            if (dist > 0.0) return 1;
            return 0;
        }

        @Override
        public final double distanceFrom(Line3 line) {
            CoreUtils.assertNonNullArg(line);
            double dirDot = _norm.dot(line.getDirection());
            if (dirDot == 0.0) return Double.NaN;
            double dist = -distanceTo(line.getPosition());
            return dist / dirDot;
        }

        @Override
        public final double distanceTo(Tuple3 point) {
            double dist = _norm.dot(point);
            return dist - _dist.get();
        }

        @Override
        public final Scalar getDistance() {
            return _dummyDist.set(_dist);
        }

        @Override
        public final UnitVector3 getNormal() {
            return _dummyNorm.set(_norm);
        }

        @Override
        public Point3 getNormalPoint() {
            return _dummyPoint.mult(_norm, _dist.get());
        }

        @Override
        public final Point3 intersect(Line3 line) {
            double dist = distanceFrom(line);
            return line.findPoint(dist);
        }

        @Override
        public Plane3 dupe() {
            return new Plane3.Impl(this);
        }

        @Override
        public Plane3 transformGeometry(Xformer xformer) {
            CoreUtils.assertNonNullArg(xformer);
            Point3 point = getNormalPoint();
            point.transformGeometry(xformer);
            _norm.transformGeometry(xformer);
            set(_norm, point);
            return this;
        }

        @Override
        public final boolean equalsValue(Object obj, double tolerance) {
            if (this == obj) return true;
            if (!(obj instanceof Plane3)) return false;
            Plane3 that = (Plane3) obj;
            if (!_dist.equalsValue(that.getDistance(), tolerance)) return false;
            if (!_norm.equalsValue(that.getNormal(), tolerance)) return false;
            return true;
        }

        public String implToString() {
            return JavaUtils.toShortString(this) + " norm=" + _norm.toArrayString() + " dist=" + _dist.toArrayString();
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _norm = CoreUtils.dispose(_norm);
            _dist = CoreUtils.dispose(_dist);
            _dummyNorm = CoreUtils.dispose(_dummyNorm);
            _dummyDist = CoreUtils.dispose(_dummyDist);
            _dummyTupleA = CoreUtils.dispose(_dummyTupleA);
            _dummyTupleB = CoreUtils.dispose(_dummyTupleB);
            _dummyPoint = CoreUtils.dispose(_dummyPoint);
            _dummyUVect = CoreUtils.dispose(_dummyUVect);
        }

        private static final long serialVersionUID = 1L;

        private UnitVector3 _norm = new UnitVector3.Impl();

        private Scalar _dist = new Scalar.Impl();

        private UnitVector3 _dummyNorm = new UnitVector3.Impl();

        private Scalar _dummyDist = new Scalar.Impl();

        private Tuple3 _dummyTupleA = new Tuple3.Impl();

        private Tuple3 _dummyTupleB = new Tuple3.Impl();

        private Point3 _dummyPoint = new Point3.Impl();

        private UnitVector3 _dummyUVect = new UnitVector3.Impl();
    }
}
