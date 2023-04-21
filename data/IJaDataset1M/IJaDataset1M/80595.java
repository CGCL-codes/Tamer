package com.msli.graphic.math;

import com.msli.core.exception.UnmodifiableStateException;
import com.msli.core.util.CoreUtils;
import com.msli.core.util.JavaUtils;
import com.msli.graphic.geom.Geometric;
import com.msli.graphic.geom.xform.Xformer;

/**
 * A Geometric 3D pose representing a position and orientation, which is defined
 * by a look-at-ray and a look-up-direction (LUD). The look-at-ray defines the
 * pose's look-from-position (LFP) and look-at-direction (LAD). Note that the
 * LAD and LUD of a pose are generally not orthogonal. In terms of an orthogonal
 * reference frame, the LAD corresponds to the frame's -Z axis, its X axes is
 * derived from the LAD and LUD cross product, and its Y axis is derived from
 * the LAD and X axis cross product.
 * <P>
 * Object equality is based on identity. Use
 * Quantitative methods for state-based equality.
 * <P>
 * Derived from gumbo3.graphic.math.Pose3.
 * @author jonb
 */
public interface Pose3 extends MathObject, Geometric {

    /**
	 * Sets the look from position of this pose. Does not affect the pose
	 * look-at-direction or look-up-direction.
	 * @param lfp Temp input position. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Pose3 setLookFromPosition(Tuple3 lfp);

    /**
	 * Sets the look at direction of this pose. Does not affect the pose
	 * look-from-position or look-up-direction.
	 * @param lad Temp input direction.  Never null.
	 * @return Reference to this object.  Never null.
	 */
    public Pose3 setLookAtDirection(Tuple3 lad);

    /**
	 * Sets the look up direction of this pose. Does not affect the pose
	 * look-from-position or look-at-direction.
	 * @param lud Temp input direction.  Never null.
	 * @return Reference to this object.  Never null.
	 */
    public Pose3 setLookUpDirection(Tuple3 lud);

    /**
	 * Sets the look at ray of this pose. Does not affect the pose
	 * look-up-direction.
	 * @param ray Temp input ray.  Never null.
	 * @return Reference to this object.  Never null.
	 */
    public Pose3 setLookAtRay(Line3 ray);

    /**
	 * Sets this pose to the specified value.
	 * @param lfp Temp input look from point.  Never null.
	 * @param lad Temp input look at direction.  Never null.
	 * @param lud Temp input look up direction.  Never null.
	 * @return Reference to this object.  Never null.
	 */
    public Pose3 set(Tuple3 lfp, Tuple3 lad, Tuple3 lud);

    /**
	 * Sets this pose to the specified value.
	 * @param ray Temp input look at ray (look from point and look
	 * at direction).  Never null.
	 * @param lud Temp input look up direction.  Never null.
	 * @return Reference to this object.  Never null.
	 */
    public Pose3 set(Line3 ray, Tuple3 lud);

    /**
	 * Sets this pose to the value of a target pose.
	 * @param val Temp input value.  Never null.
	 * @return Reference to this object.  Never null.
	 */
    public Pose3 set(Pose3 val);

    /**
	 * Sets the look-at-direction of this pose such that the look-at-direction
	 * intersects the specified point. Does not affect the pose
	 * look-from-position or look-up-direction.
	 * @param point Temp input point. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Pose3 lookAt(Tuple3 point);

    /**
	 * Sets the look-up-direction of this pose such that the look-up-direction
	 * intersects the specified point. Does not affect the pose
	 * look-from-position or look-at-direction.
	 * @param point Temp input point. Never null.
	 * @return Reference to this object. Never null.
	 */
    public Pose3 lookUp(Tuple3 point);

    /**
	 * Gets the look-from-position of this pose, which corresponds to the
	 *  the look-at-ray position.
	 * @return Temp output point.  Never null.
	 */
    public Point3 getLookFromPosition();

    /**
	 * Gets the look-at-direction of this pose, which corresponds to
	 *  the look-at-ray direction.
	 * @return Temp output direction.  Never null.
	 */
    public UnitVector3 getLookAtDirection();

    /**
	 * Gets the look-up-direction of this pose.
	 * @return Temp output direction.  Never null.
	 */
    public UnitVector3 getLookUpDirection();

    /**
	 * Gets the look-at-ray of this pose.
	 * @return Temp output ray.  Never null.
	 */
    public Ray3 getLookAtRay();

    /**
	 * Gets the X-axis (orthogonal look-right-direction) of the reference frame
	 * relative to this pose.
	 * @return Temp output X axis. Never null.
	 */
    public UnitVector3 getAxisX();

    /**
	 * Gets the X-axis (orthogonal look-up-direction) of the reference frame
	 * relative to this pose.
	 * @return Temp output Y axis. Never null.
	 */
    public UnitVector3 getAxisY();

    /**
	 * Gets the Z-axis (orthogonal look-back-direction) of the reference frame
	 * relative to this pose.
	 * @return Temp output Z axis. Never null.
	 */
    public UnitVector3 getAxisZ();

    @Override
    public Pose3 transformGeometry(Xformer xformer);

    @Override
    public Pose3 dupe();

    /**
	 * Concrete serializable unmodifiable wrapper. All mutators throw
	 * UnmodifiableStateException.
	 */
    public static class Unmod extends MathObject.Wrapper implements Pose3 {

        /**
		 * Creates an unmodifiable wrapper for the target. State update
		 * observers will observe this wrapper, not its target.
		 * @param target Shared exposed target of this wrapper. Never null.
		 * @throws IllegalArgumentException if the target implements
		 * NotWrapperAware.
		 */
        public Unmod(Pose3 target) {
            super(target);
            _target = target;
        }

        @Override
        public Pose3 lookAt(Tuple3 point) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 lookUp(Tuple3 point) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 set(Pose3 val) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 set(Line3 ray, Tuple3 lud) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 set(Tuple3 lfp, Tuple3 lad, Tuple3 lud) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 setLookAtDirection(Tuple3 lad) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 setLookAtRay(Line3 ray) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 setLookFromPosition(Tuple3 lfp) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public Pose3 setLookUpDirection(Tuple3 lud) {
            UnmodifiableStateException.doThrow(this);
            return null;
        }

        @Override
        public final UnitVector3 getAxisX() {
            return _target.getAxisX();
        }

        @Override
        public final UnitVector3 getAxisY() {
            return _target.getAxisY();
        }

        @Override
        public final UnitVector3 getAxisZ() {
            return _target.getAxisZ();
        }

        @Override
        public final UnitVector3 getLookAtDirection() {
            return _target.getLookAtDirection();
        }

        @Override
        public final Ray3 getLookAtRay() {
            return _target.getLookAtRay();
        }

        @Override
        public final Point3 getLookFromPosition() {
            return _target.getLookFromPosition();
        }

        @Override
        public final UnitVector3 getLookUpDirection() {
            return _target.getLookUpDirection();
        }

        @Override
        public Pose3 transformGeometry(Xformer xformer) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Pose3 dupe() {
            return new Pose3.Unmod(_target);
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

        private Pose3 _target = null;
    }

    /**
	 * Default implementation for concrete type DD.
	 */
    public static class Impl extends MathObject.Base implements Pose3 {

        /**
		 * Creates an instance, with look-from-position at the origin,
		 * look-at-direction along the -Z axis, and look-up-direction along the
		 * Y axis.
		 */
        public Impl() {
            this(MathUtils.DEFAULT_POSITION3, MathUtils.MINUS_Z_DIRECTION3, MathUtils.PLUS_Y_DIRECTION3);
        }

        /**
		 * Creates an instance, as specified.
		 * @param lfp Temp input pose look-from-point.  Never null.
		 * @param lad Temp input pose look-at-direction.  Never null.
		 * @param lud Temp input pose look-up-direction.  Never null.
		 */
        public Impl(Tuple3 lfp, Tuple3 lad, Tuple3 lud) {
            set(lfp, lad, lud);
        }

        /**
		 * Creates an instance, as specified.
		 * @param ray Temp input pose look-at-ray.  Never null.
		 * @param lud Temp input pose look-up-direction.  Never null.
		 */
        public Impl(Ray3 ray, Tuple3 lud) {
            set(ray, lud);
        }

        /**
		 * Creates an instance, with the same value as the target.
		 * @param val Temp input value.  Never null.
		 */
        public Impl(Pose3 val) {
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
		 * @param lfp Temp input pose look-from-position.  Never null.
		 * @param lad Temp input pose look-at-direction.  Never null.
		 * @param lud Temp input pose look-up-direction.  Never null.
		 */
        protected void checkState(Tuple3 lfp, Tuple3 lad, Tuple3 lud) {
            CoreUtils.assertNonNullArg(lfp);
            CoreUtils.assertNonNullArg(lad);
            CoreUtils.assertNonNullArg(lud);
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
		 * @param lfp Temp input pose look-from-position.  Never null.
		 * @param lad Temp input pose look-at-direction.  Never null.
		 * @param lud Temp input pose look-up-direction.  Never null.
		 */
        protected void updateState(Tuple3 lfp, Tuple3 lad, Tuple3 lud) {
            checkState(lfp, lad, lud);
            superUpdateState(lfp, lad, lud);
        }

        /**
		 * Similar to other updateState() methods but allows for shortcuts if
		 * the value is a recognized type known to satisfy all constraints.
		 * <P>
		 * If the value satisfies all the constraints of this object,
		 * implementors should call the base method to set the state rather than
		 * doing it directly with superUpdateState() so that any base class
		 * constraints are also imposed. The default implementation checks for
		 * null arguments, and call updateState(T) with the value.
		 * @param val Temp input value. Never null.
		 */
        protected void updateState(Pose3 val) {
            CoreUtils.assertNonNullArg(val);
            updateState(val.getLookFromPosition(), val.getLookAtDirection(), val.getLookUpDirection());
        }

        /**
		 * Updates the state without checks or constraints, then notifies update
		 * observers. Use with caution so that the input state value conforms to
		 * the constraints of the subclass as well as all super classes.
		 * <P>
		 * In general, should use super.updateState() instead of this method,
		 * which assures that super class constraints are imposed.
		 * @param lfp Temp input pose look-from-position.  Never null.
		 * @param lad Temp input pose look-at-direction.  Never null.
		 * @param lud Temp input pose look-up-direction.  Never null.
		 */
        protected final void superUpdateState(Tuple3 lfp, Tuple3 lad, Tuple3 lud) {
            _ray.setLine(lfp, lad);
            _lud.set(lud);
            notifyUpdateObservers();
        }

        @Override
        public Pose3 lookAt(Tuple3 point) {
            CoreUtils.assertNonNullArg(point);
            _dummyTuple.sub(point, _ray.getPosition());
            updateState(_ray.getPosition(), _dummyTuple, _lud);
            return this;
        }

        @Override
        public Pose3 lookUp(Tuple3 point) {
            CoreUtils.assertNonNullArg(point);
            _dummyTuple.sub(point, _ray.getPosition());
            updateState(_ray.getPosition(), _ray.getDirection(), _dummyTuple);
            return this;
        }

        @Override
        public Pose3 set(Pose3 val) {
            updateState(val);
            return this;
        }

        @Override
        public Pose3 set(Line3 ray, Tuple3 lud) {
            CoreUtils.assertNonNullArg(ray);
            updateState(ray.getPosition(), ray.getDirection(), lud);
            return this;
        }

        @Override
        public Pose3 set(Tuple3 lfp, Tuple3 lad, Tuple3 lud) {
            updateState(lfp, lad, lud);
            return this;
        }

        @Override
        public Pose3 setLookAtDirection(Tuple3 lad) {
            updateState(_ray.getPosition(), lad, _lud);
            return this;
        }

        @Override
        public Pose3 setLookAtRay(Line3 ray) {
            CoreUtils.assertNonNullArg(ray);
            updateState(ray.getPosition(), ray.getDirection(), _lud);
            return this;
        }

        @Override
        public Pose3 setLookFromPosition(Tuple3 lfp) {
            updateState(lfp, _ray.getDirection(), _lud);
            return this;
        }

        @Override
        public Pose3 setLookUpDirection(Tuple3 lud) {
            updateState(_ray.getPosition(), _ray.getDirection(), lud);
            return this;
        }

        @Override
        public final UnitVector3 getAxisX() {
            return _dummyAxisX.cross(_ray.getDirection(), _lud);
        }

        @Override
        public final UnitVector3 getAxisY() {
            return _dummyAxisY.cross(getAxisX(), _ray.getDirection());
        }

        @Override
        public final UnitVector3 getAxisZ() {
            return _dummyAxisZ.neg(_ray.getDirection());
        }

        @Override
        public final UnitVector3 getLookAtDirection() {
            return _ray.getDirection();
        }

        @Override
        public final Ray3 getLookAtRay() {
            return _dummyRay.set(_ray);
        }

        @Override
        public final Point3 getLookFromPosition() {
            return _ray.getPosition();
        }

        @Override
        public final UnitVector3 getLookUpDirection() {
            return _dummyLud.set(_lud);
        }

        @Override
        public Pose3 dupe() {
            return new Pose3.Impl(this);
        }

        @Override
        public Pose3 transformGeometry(Xformer xformer) {
            _lud.transformGeometry(xformer);
            _ray.transformGeometry(xformer);
            return this;
        }

        @Override
        public final boolean equalsValue(Object obj, double tolerance) {
            if (this == obj) return true;
            if (!(obj instanceof Pose3)) return false;
            Pose3 that = (Pose3) obj;
            if (!_ray.equalsValue(that.getLookAtRay(), tolerance)) return false;
            if (!_lud.equalsValue(that.getLookUpDirection(), tolerance)) return false;
            return true;
        }

        public String implToString() {
            return JavaUtils.toShortString(this) + " lfp=" + _ray.getPosition().toArrayString() + " lad=" + _ray.getDirection().toArrayString() + " lud=" + _lud.toArrayString();
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _ray = CoreUtils.dispose(_ray);
            _lud = CoreUtils.dispose(_lud);
            _dummyRay = CoreUtils.dispose(_dummyRay);
            _dummyLud = CoreUtils.dispose(_dummyLud);
            _dummyAxisX = CoreUtils.dispose(_dummyAxisX);
            _dummyAxisY = CoreUtils.dispose(_dummyAxisY);
            _dummyAxisZ = CoreUtils.dispose(_dummyAxisZ);
            _dummyTuple = CoreUtils.dispose(_dummyTuple);
        }

        private static final long serialVersionUID = 1L;

        private Ray3 _ray = new Ray3.Impl();

        private UnitVector3 _lud = new UnitVector3.Impl();

        private Ray3 _dummyRay = new Ray3.Impl();

        private UnitVector3 _dummyLud = new UnitVector3.Impl();

        private UnitVector3 _dummyAxisX = new UnitVector3.Impl();

        private UnitVector3 _dummyAxisY = new UnitVector3.Impl();

        private UnitVector3 _dummyAxisZ = new UnitVector3.Impl();

        private Tuple3 _dummyTuple = new Tuple3.Impl();
    }
}
