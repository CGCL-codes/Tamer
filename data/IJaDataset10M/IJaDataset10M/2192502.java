package gumbo.tech.math.util;

import gumbo.core.exception.UnmodifiableStateException;
import gumbo.core.util.NullObject;
import gumbo.tech.math.Point3;
import gumbo.tech.math.Tuple3;
import gumbo.tech.math.UnitVector3;
import gumbo.tech.math.impl.Tuple3Impl;
import gumbo.tech.math.util.MathUtils.D;

public class Tuple3Util {

    private Tuple3Util() {
    }

    public static Tuple3 unmod(Tuple3 val) {
        return new Tuple3Util.Unmod(val);
    }

    public static Tuple3 constant(Tuple3 val) {
        Tuple3 target = new Tuple3Impl(val);
        return Tuple3Util.unmod(target);
    }

    public static Tuple3 constant(double valX, double valY, double valZ) {
        Tuple3 target = new Tuple3Impl(valX, valY, valZ);
        return Tuple3Util.unmod(target);
    }

    /** Tuple with zero for all dimension values. Immutable. */
    public static final Tuple3 ZERO_TUPLE3 = Tuple3Util.constant(0.0, 0.0, 0.0);

    /** Tuple with positive infinity for all dimension values. Immutable. */
    public static final Tuple3 MAX_TUPLE3 = Tuple3Util.constant(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    /** Tuple with negative infinity for all dimension values. Immutable. */
    public static final Tuple3 MIN_TUPLE3 = Tuple3Util.constant(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

    /** Tuple with NaN for all dimension values. Immutable. */
    public static final Tuple3 NAN_TUPLE3 = Tuple3Util.constant(Double.NaN, Double.NaN, Double.NaN);

    /** Unit vector along the +X axis.  Immutable. */
    public static final UnitVector3 PLUS_X_DIRECTION3 = new UnitVector3.Unmod(new UnitVector3.Impl(1.0, 0.0, 0.0));

    /** Unit vector along the +Y axis.  Immutable. */
    public static final UnitVector3 PLUS_Y_DIRECTION3 = new UnitVector3.Unmod(new UnitVector3.Impl(0.0, 1.0, 0.0));

    /** Unit vector along the +Z axis.  Immutable. */
    public static final UnitVector3 PLUS_Z_DIRECTION3 = new UnitVector3.Unmod(new UnitVector3.Impl(0.0, 0.0, 1.0));

    /** Unit vector along the -X axis.  Immutable. */
    public static final UnitVector3 MINUS_X_DIRECTION3 = new UnitVector3.Unmod(new UnitVector3.Impl(-1.0, 0.0, 0.0));

    /** Unit vector along the -Y axis.  Immutable. */
    public static final UnitVector3 MINUS_Y_DIRECTION3 = new UnitVector3.Unmod(new UnitVector3.Impl(0.0, -1.0, 0.0));

    /** Unit vector along the -Z axis.  Immutable. */
    public static final UnitVector3 MINUS_Z_DIRECTION3 = new UnitVector3.Unmod(new UnitVector3.Impl(0.0, 0.0, -1.0));

    /** Point at the origin (0,0,0). Immutable. */
    public static final Point3 ORIGIN3 = new Point3.Unmod(new Point3.Impl(ZERO_TUPLE3));

    /** Unit vector along the +X axis.  Immutable. */
    public static final UnitVector3 X_AXIS3 = PLUS_X_DIRECTION3;

    /** Unit vector along the +Y axis.  Immutable. */
    public static final UnitVector3 Y_AXIS3 = PLUS_Y_DIRECTION3;

    /** Unit vector along the +Z axis.  Immutable. */
    public static final UnitVector3 Z_AXIS3 = PLUS_Z_DIRECTION3;

    /**
	 * Result of normalizing a zero length tuple, which is a normalized
	 * vector along the X-axis.  Immutable.
	 */
    public static final UnitVector3 NORMALIZED_ZERO_TUPLE3 = new UnitVector3.Unmod(new UnitVector3.Impl(D.NORMALIZED_ZERO_TUPLE3_X, D.NORMALIZED_ZERO_TUPLE3_Y, D.NORMALIZED_ZERO_TUPLE3_Z));

    /** Default position point, which is at the origin (0,0,0). Immutable. */
    public static final Point3 DEFAULT_POSITION3 = ORIGIN3;

    /**
	 * Default direction vector, which is a unit vector along the X-axis.
	 * Immutable.
	 */
    public static final UnitVector3 DEFAULT_DIRECTION3 = PLUS_X_DIRECTION3;

    /**
	 * Returns the axis vector corresponding to the specified axis dimension. 
	 * @param dimI Axis dimension index [0, 2].
	 * @return Shared output axis vector.
	 */
    public static UnitVector3 getAxis3(int dimI) {
        switch(dimI) {
            case 0:
                return X_AXIS3;
            case 1:
                return Y_AXIS3;
            case 2:
                return Z_AXIS3;
            default:
                throw new IndexOutOfBoundsException("Axis dimension must be [0, 2]. dimI=" + dimI);
        }
    }

    /**
	 * Concrete serializable unmodifiable wrapper. All exposers are
	 * unmodifiable. All mutators throw UnmodifiableStateException.
	 */
    public static class Unmod extends TupleUtil.Unmod implements Tuple3 {

        /**
		 * Creates an unmodifiable wrapper for the target. State update
		 * observers will observe this wrapper, not its target.
		 * @param target Shared exposed target of this wrapper. Never null.
		 * @throws IllegalArgumentException if the target implements
		 * NotWrapperAware.
		 */
        public Unmod(Tuple3 target) {
            super(target);
            _target = target;
        }

        @Override
        public Tuple3 abs() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 abs(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 add(Tuple3 tupleA, Tuple3 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 add(Tuple3 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 add(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 add(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clamp(Tuple3 tuple, Tuple3 min, Tuple3 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clamp(Tuple3 min, Tuple3 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clamp(Tuple3 tuple, double min, double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clamp(double min, double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMax(Tuple3 tuple, Tuple3 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMax(Tuple3 tuple, double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMax(Tuple3 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMax(double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMin(Tuple3 tuple, Tuple3 min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMin(Tuple3 tuple, double min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMin(Tuple3 min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clampMin(double min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 clear() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 div(Tuple3 tupleA, Tuple3 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 div(Tuple3 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 div(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 div(double val, Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 div(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 divInv(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 divInv(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 max(Tuple3 tupleA, Tuple3 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 max(Tuple3 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 max(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 max(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 min(Tuple3 tupleA, Tuple3 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 min(Tuple3 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 min(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 min(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 mult(Tuple3 tupleA, Tuple3 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 mult(Tuple3 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 mult(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 mult(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 neg() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 neg(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 noise() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 random() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 round() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 round(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 set(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 set(double x, double y, double z) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 set(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 setX(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 setY(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 setZ(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 sub(Tuple3 tupleA, Tuple3 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 sub(Tuple3 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 sub(Tuple3 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 sub(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public final double distance(Tuple3 point) {
            return _target.distance(point);
        }

        @Override
        public final double distanceSquared(Tuple3 point) {
            return _target.distanceSquared(point);
        }

        @Override
        public final double getX() {
            return _target.getX();
        }

        @Override
        public final double getY() {
            return _target.getY();
        }

        @Override
        public final double getZ() {
            return _target.getZ();
        }

        @Override
        public final boolean isGreaterEqual(Tuple3 tuple) {
            return _target.isGreaterEqual(tuple);
        }

        @Override
        public final boolean isInsideEqual(Tuple3 min, Tuple3 max) {
            return _target.isInsideEqual(min, max);
        }

        @Override
        public final boolean isLessEqual(Tuple3 tuple) {
            return _target.isLessEqual(tuple);
        }

        @Override
        public final boolean isOutside(Tuple3 min, Tuple3 max) {
            return _target.isOutside(min, max);
        }

        @Override
        public final boolean isZero() {
            return _target.isZero();
        }

        @Override
        public final boolean isZero(double tolerance) {
            return _target.isZero(tolerance);
        }

        @Override
        public final double length() {
            return _target.length();
        }

        @Override
        public final double lengthSquared() {
            return _target.lengthSquared();
        }

        @Override
        public final double sum() {
            return _target.sum();
        }

        @Override
        public Tuple3 set(int dim, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 set(double[] vals) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Tuple3 dupe() {
            return new Tuple3Util.Unmod(_target);
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _target = null;
        }

        private static final long serialVersionUID = 1L;

        private Tuple3 _target = null;
    }

    /**
	 * Singleton immutable null object implementation.  All mutators do nothing.
	 */
    public static class Null extends Tuple3Impl implements NullObject {

        @Override
        public void updateState(double valX, double valY, double valZ) {
            checkState(valX, valY, valZ);
        }

        @Override
        public Tuple3 dupe() {
            return this;
        }

        private Object readResolve() {
            return NULL;
        }

        private static final long serialVersionUID = 1L;

        private Null() {
            superUpdateState(Double.NaN, Double.NaN, Double.NaN);
        }

        /**
		 * Returns a singleton immutable empty StateSet.
		 */
        public static Tuple3Util.Null getInstance() {
            return NULL;
        }

        private static final Tuple3Util.Null NULL = new Tuple3Util.Null();
    }
}
