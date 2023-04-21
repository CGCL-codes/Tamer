package gumbo.tech.math.impl;

import gumbo.core.exception.UnmodifiableStateException;
import gumbo.core.util.AssertUtils;
import gumbo.core.util.NullObject;
import gumbo.tech.math.Tuple2;
import gumbo.tech.math.UnitVector2;
import gumbo.tech.math.Vector2;
import gumbo.tech.math.util.Tuple2Util;

/**
 * Default implementation for concrete type DD.
 */
public class Vector2Impl extends Tuple2Impl implements Vector2 {

    /**
	 * Creates an instance, with value [0, 0, 0].
	 */
    public Vector2Impl() {
        super();
    }

    /**
	 * Creates an instance, as specified.
	 */
    public Vector2Impl(double valX, double valY) {
        super(valX, valY);
    }

    /**
	 * Creates an instance, as specified.
	 */
    public Vector2Impl(double[] val) {
        super(val);
    }

    /**
	 * Creates an instance, with the same value as the target.
	 */
    public Vector2Impl(Tuple2 val) {
        super(val);
    }

    @Override
    public Vector2 normalize(Tuple2 vect) {
        AssertUtils.assertNonNullArg(vect);
        double x = vect.getX();
        double y = vect.getY();
        if (vect instanceof UnitVector2) {
            return set(vect);
        } else if (x == 0.0 && y == 0.0) {
            return set(Tuple2Util.NORMALIZED_ZERO_TUPLE2);
        } else {
            double length = Math.sqrt(x * x + y * y);
            if (length == 0.0) {
                return set(Tuple2Util.NORMALIZED_ZERO_TUPLE2);
            } else {
                double lengthInv = 1.0 / length;
                x = lengthInv * x;
                y = lengthInv * y;
                return set(x, y);
            }
        }
    }

    @Override
    public Vector2 normalize() {
        return normalize(this);
    }

    @Override
    public Vector2 abs(Tuple2 tuple) {
        super.abs(tuple);
        return this;
    }

    @Override
    public Vector2 add(Tuple2 tuple, double val) {
        super.add(tuple, val);
        return this;
    }

    @Override
    public Vector2 add(Tuple2 tupleA, Tuple2 tupleB) {
        super.add(tupleA, tupleB);
        return this;
    }

    @Override
    public Vector2 clampMax(Tuple2 tuple, double max) {
        super.clampMax(tuple, max);
        return this;
    }

    @Override
    public Vector2 clampMax(Tuple2 tuple, Tuple2 max) {
        super.clampMax(tuple, max);
        return this;
    }

    @Override
    public Vector2 clampMin(Tuple2 tuple, double min) {
        super.clampMin(tuple, min);
        return this;
    }

    @Override
    public Vector2 clampMin(Tuple2 tuple, Tuple2 min) {
        super.clampMin(tuple, min);
        return this;
    }

    @Override
    public Vector2 clear() {
        super.clear();
        return this;
    }

    @Override
    public Vector2 div(double val, Tuple2 tuple) {
        super.div(val, tuple);
        return this;
    }

    @Override
    public Vector2 div(Tuple2 tuple, double val) {
        super.div(tuple, val);
        return this;
    }

    @Override
    public Vector2 div(Tuple2 tupleA, Tuple2 tupleB) {
        super.div(tupleA, tupleB);
        return this;
    }

    @Override
    public Vector2 max(Tuple2 tuple, double val) {
        super.max(tuple, val);
        return this;
    }

    @Override
    public Vector2 max(Tuple2 tupleA, Tuple2 tupleB) {
        super.max(tupleA, tupleB);
        return this;
    }

    @Override
    public Vector2 min(Tuple2 tuple, double val) {
        super.min(tuple, val);
        return this;
    }

    @Override
    public Vector2 min(Tuple2 tupleA, Tuple2 tupleB) {
        super.min(tupleA, tupleB);
        return this;
    }

    @Override
    public Vector2 mult(Tuple2 tuple, double val) {
        super.mult(tuple, val);
        return this;
    }

    @Override
    public Vector2 mult(Tuple2 tupleA, Tuple2 tupleB) {
        super.mult(tupleA, tupleB);
        return this;
    }

    @Override
    public Vector2 neg(Tuple2 tuple) {
        super.neg(tuple);
        return this;
    }

    @Override
    public Vector2 noise() {
        super.noise();
        return this;
    }

    @Override
    public Vector2 random() {
        super.random();
        return this;
    }

    @Override
    public Vector2 round(Tuple2 tuple) {
        super.round(tuple);
        return this;
    }

    @Override
    public Vector2 sub(Tuple2 tuple, double val) {
        super.sub(tuple, val);
        return this;
    }

    @Override
    public Vector2 sub(Tuple2 tupleA, Tuple2 tupleB) {
        super.sub(tupleA, tupleB);
        return this;
    }

    @Override
    public Vector2 abs() {
        super.abs();
        return this;
    }

    @Override
    public Vector2 add(double val) {
        super.add(val);
        return this;
    }

    @Override
    public Vector2 add(Tuple2 tuple) {
        super.add(tuple);
        return this;
    }

    @Override
    public Vector2 clamp(double min, double max) {
        super.clamp(min, max);
        return this;
    }

    @Override
    public Vector2 clamp(Tuple2 tuple, double min, double max) {
        super.clamp(tuple, min, max);
        return this;
    }

    @Override
    public Vector2 clamp(Tuple2 tuple, Tuple2 min, Tuple2 max) {
        super.clamp(tuple, min, max);
        return this;
    }

    @Override
    public Vector2 clamp(Tuple2 min, Tuple2 max) {
        super.clamp(min, max);
        return this;
    }

    @Override
    public Vector2 clampMax(double max) {
        super.clampMax(max);
        return this;
    }

    @Override
    public Vector2 clampMax(Tuple2 max) {
        super.clampMax(max);
        return this;
    }

    @Override
    public Vector2 clampMin(double min) {
        super.clampMin(min);
        return this;
    }

    @Override
    public Vector2 clampMin(Tuple2 min) {
        super.clampMin(min);
        return this;
    }

    @Override
    public Vector2 div(double val) {
        super.div(val);
        return this;
    }

    @Override
    public Vector2 div(Tuple2 tuple) {
        super.div(tuple);
        return this;
    }

    @Override
    public Vector2 divInv(double val) {
        super.divInv(val);
        return this;
    }

    @Override
    public Vector2 divInv(Tuple2 tuple) {
        super.divInv(tuple);
        return this;
    }

    @Override
    public Vector2 max(double val) {
        super.max(val);
        return this;
    }

    @Override
    public Vector2 max(Tuple2 tuple) {
        super.max(tuple);
        return this;
    }

    @Override
    public Vector2 min(double val) {
        super.min(val);
        return this;
    }

    @Override
    public Vector2 min(Tuple2 tuple) {
        super.min(tuple);
        return this;
    }

    @Override
    public Vector2 mult(double val) {
        super.mult(val);
        return this;
    }

    @Override
    public Vector2 mult(Tuple2 tuple) {
        super.mult(tuple);
        return this;
    }

    @Override
    public Vector2 neg() {
        super.neg();
        return this;
    }

    @Override
    public Vector2 round() {
        super.round();
        return this;
    }

    @Override
    public Vector2 set(double valX, double valY) {
        super.set(valX, valY);
        return this;
    }

    @Override
    public Vector2 set(double val) {
        super.set(val);
        return this;
    }

    @Override
    public Vector2 set(double[] val) {
        super.set(val);
        return this;
    }

    @Override
    public Vector2 set(int dim, double val) {
        super.set(dim, val);
        return this;
    }

    @Override
    public Vector2 set(Tuple2 tuple) {
        super.set(tuple);
        return this;
    }

    @Override
    public Vector2 setX(double val) {
        super.setX(val);
        return this;
    }

    @Override
    public Vector2 setY(double val) {
        super.setY(val);
        return this;
    }

    @Override
    public Vector2 sub(double val) {
        super.sub(val);
        return this;
    }

    @Override
    public Vector2 sub(Tuple2 tuple) {
        super.sub(tuple);
        return this;
    }

    @Override
    public Vector2 dupe() {
        return new Vector2Impl(this);
    }

    private static final long serialVersionUID = 1L;

    /**
	 * Concrete serializable unmodifiable wrapper. All mutators throw
	 * UnmodifiableStateException.
	 */
    public static class Unmod extends Tuple2Util.Unmod implements Vector2 {

        /**
		 * Creates an unmodifiable wrapper for the target. State update
		 * observers will observe this wrapper, not its target.
		 * @param target Shared exposed target of this wrapper. Never null.
		 * @throws IllegalArgumentException if the target implements
		 * NotWrapperAware.
		 */
        public Unmod(Tuple2 target) {
            super(target);
            _target = target;
        }

        @Override
        public Vector2 normalize() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 normalize(Tuple2 vect) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 abs() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 abs(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 add(Tuple2 tupleA, Tuple2 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 add(Tuple2 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 add(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 add(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clamp(Tuple2 tuple, Tuple2 min, Tuple2 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clamp(Tuple2 min, Tuple2 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clamp(Tuple2 tuple, double min, double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clamp(double min, double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMax(Tuple2 tuple, Tuple2 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMax(Tuple2 tuple, double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMax(Tuple2 max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMax(double max) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMin(Tuple2 tuple, Tuple2 min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMin(Tuple2 tuple, double min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMin(Tuple2 min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clampMin(double min) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 clear() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 div(Tuple2 tupleA, Tuple2 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 div(Tuple2 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 div(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 div(double val, Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 div(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 divInv(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 divInv(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 max(Tuple2 tupleA, Tuple2 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 max(Tuple2 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 max(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 max(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 min(Tuple2 tupleA, Tuple2 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 min(Tuple2 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 min(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 min(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 mult(Tuple2 tupleA, Tuple2 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 mult(Tuple2 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 mult(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 mult(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 neg() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 neg(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 noise() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 random() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 round() {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 round(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 set(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 set(double x, double y) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 set(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 setX(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 setY(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 sub(Tuple2 tupleA, Tuple2 tupleB) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 sub(Tuple2 tuple, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 sub(Tuple2 tuple) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 sub(double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 set(int dim, double val) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 set(double[] vals) {
            UnmodifiableStateException.doThrow(this);
            return this;
        }

        @Override
        public Vector2 dupe() {
            return new Vector2Impl.Unmod(_target);
        }

        @Override
        public void implDispose() {
            super.implDispose();
            _target = null;
        }

        private static final long serialVersionUID = 1L;

        private Tuple2 _target = null;
    }

    /**
	 * Singleton immutable null object implementation.  All mutators do nothing.
	 */
    public static class Null extends Vector2Impl implements NullObject {

        @Override
        public void updateState(double valX, double valY) {
            checkState(valX, valY);
        }

        @Override
        public Vector2 dupe() {
            return this;
        }

        private Object readResolve() {
            return NULL;
        }

        private static final long serialVersionUID = 1L;

        Null() {
            superUpdateState(Double.NaN, Double.NaN);
        }

        /**
		 * Returns a singleton immutable empty StateSet.
		 */
        public static Vector2Impl.Null getInstance() {
            return NULL;
        }

        private static final Vector2Impl.Null NULL = new Vector2Impl.Null();
    }
}
