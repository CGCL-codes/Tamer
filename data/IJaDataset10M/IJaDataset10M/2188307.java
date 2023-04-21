package gumbo.tech.math.impl;

import gumbo.core.util.AssertUtils;
import gumbo.tech.impl.TechObjectImpl;
import gumbo.tech.math.Point2;
import gumbo.tech.math.Tuple2;

/**
 * Default implementation for Point2. For extension, see TechObjectImpl.
 * @see TechObjectImpl
 */
public class Point2Impl {

    private Point2Impl() {
    }

    public static class Constant extends Tuple2Impl.Constant implements Point2.Constant {

        /**
		 * Creates an instance, with a default valid state.
		 * @see Point2Impl.Helper#Helper()
		 */
        public Constant() {
            super(new Point2Impl.Helper());
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Constant(double valX, double valY) {
            super(new Point2Impl.Helper(valX, valY));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Constant(double... val) {
            super(new Point2Impl.Helper(val));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Constant(Tuple2 val) {
            super(new Point2Impl.Helper(val));
        }

        /**
		 * Creates an instance, with the specified helper delegate.
		 * @param helper Ceded helper delegate. Never null.
		 */
        public Constant(Point2Impl.Helper helper) {
            super(helper);
        }

        @Override
        public Point2.Exposed exposed() {
            return new Point2Impl.Exposed(this);
        }

        @Override
        protected Point2Impl.Helper getHelper() {
            return (Point2Impl.Helper) super.getHelper();
        }

        private static final long serialVersionUID = 1L;
    }

    public static class Exposed extends Tuple2Impl.Exposed implements Point2.Exposed {

        /**
		 * Creates an instance, with a default valid state.
		 * @see Point2Impl.Helper#Helper()
		 */
        public Exposed() {
            super(new Point2Impl.Helper());
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Exposed(double valX, double valY) {
            super(new Point2Impl.Helper(valX, valY));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Exposed(double... val) {
            super(new Point2Impl.Helper(val));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Exposed(Tuple2 val) {
            super(new Point2Impl.Helper(val));
        }

        /**
		 * Creates an instance, with the specified helper delegate.
		 * @param helper Ceded helper delegate. Never null.
		 */
        public Exposed(Point2Impl.Helper helper) {
            super(helper);
        }

        @Override
        public Point2.Exposed exposed() {
            return new Point2Impl.Exposed(this);
        }

        @Override
        protected Point2Impl.Helper getHelper() {
            return (Point2Impl.Helper) super.getHelper();
        }

        @Override
        public Point2.Exposed set(double valX, double valY) {
            super.set(valX, valY);
            return this;
        }

        @Override
        public Point2.Exposed set(Tuple2 val) {
            super.set(val);
            return this;
        }

        @Override
        public Point2.Exposed set(double val) {
            super.set(val);
            return this;
        }

        @Override
        public Point2.Exposed setX(double val) {
            super.setX(val);
            return this;
        }

        @Override
        public Point2.Exposed setY(double val) {
            super.setY(val);
            return this;
        }

        @Override
        public Point2.Exposed abs(Tuple2 tuple) {
            super.abs(tuple);
            return this;
        }

        @Override
        public Point2.Exposed clear() {
            super.clear();
            return this;
        }

        @Override
        public Point2.Exposed random() {
            super.random();
            return this;
        }

        @Override
        public Point2.Exposed noise() {
            super.noise();
            return this;
        }

        @Override
        public Point2.Exposed neg(Tuple2 tuple) {
            super.neg(tuple);
            return this;
        }

        @Override
        public Point2.Exposed round(Tuple2 tuple) {
            super.round(tuple);
            return this;
        }

        @Override
        public Point2.Exposed add(Tuple2 tuple, double val) {
            super.add(tuple, val);
            return this;
        }

        @Override
        public Point2.Exposed add(Tuple2 tupleA, Tuple2 tupleB) {
            super.add(tupleA, tupleB);
            return this;
        }

        @Override
        public Point2.Exposed sub(Tuple2 tuple, double val) {
            super.sub(tuple, val);
            return this;
        }

        @Override
        public Point2.Exposed sub(Tuple2 tupleA, Tuple2 tupleB) {
            super.sub(tupleA, tupleB);
            return this;
        }

        @Override
        public Point2.Exposed mult(Tuple2 tuple, double val) {
            super.mult(tuple, val);
            return this;
        }

        @Override
        public Point2.Exposed mult(Tuple2 tupleA, Tuple2 tupleB) {
            super.mult(tupleA, tupleB);
            return this;
        }

        @Override
        public Point2.Exposed div(Tuple2 tuple, double val) {
            super.div(tuple, val);
            return this;
        }

        @Override
        public Point2.Exposed div(double val, Tuple2 tuple) {
            super.div(val, tuple);
            return this;
        }

        @Override
        public Point2.Exposed div(Tuple2 tupleA, Tuple2 tupleB) {
            super.div(tupleA, tupleB);
            return this;
        }

        @Override
        public Point2.Exposed min(Tuple2 tuple, double val) {
            super.min(tuple, val);
            return this;
        }

        @Override
        public Point2.Exposed min(Tuple2 tupleA, Tuple2 tupleB) {
            super.min(tupleA, tupleB);
            return this;
        }

        @Override
        public Point2.Exposed max(Tuple2 tuple, double val) {
            super.max(tuple, val);
            return this;
        }

        @Override
        public Point2.Exposed max(Tuple2 tupleA, Tuple2 tupleB) {
            super.max(tupleA, tupleB);
            return this;
        }

        @Override
        public Point2.Exposed clampMin(Tuple2 tuple, double min) {
            super.clampMin(tuple, min);
            return this;
        }

        @Override
        public Point2.Exposed clampMin(Tuple2 tuple, Tuple2 min) {
            super.clampMin(tuple, min);
            return this;
        }

        public Point2.Exposed clampMax(Tuple2 tuple, double max) {
            super.clampMax(tuple, max);
            return this;
        }

        @Override
        public Point2.Exposed clampMax(Tuple2 tuple, Tuple2 max) {
            super.clampMax(tuple, max);
            return this;
        }

        @Override
        public Point2.Exposed neg() {
            super.neg();
            return this;
        }

        @Override
        public Point2.Exposed abs() {
            super.abs();
            return this;
        }

        @Override
        public Point2.Exposed round() {
            super.round();
            return this;
        }

        @Override
        public Point2.Exposed add(double val) {
            super.add(val);
            return this;
        }

        @Override
        public Point2.Exposed add(Tuple2 tuple) {
            super.add(tuple);
            return this;
        }

        @Override
        public Point2.Exposed sub(double val) {
            super.sub(val);
            return this;
        }

        @Override
        public Point2.Exposed sub(Tuple2 tuple) {
            super.sub(tuple);
            return this;
        }

        @Override
        public Point2.Exposed mult(double val) {
            super.mult(val);
            return this;
        }

        @Override
        public Point2.Exposed mult(Tuple2 tuple) {
            super.mult(tuple);
            return this;
        }

        @Override
        public Point2.Exposed div(double val) {
            super.div(val);
            return this;
        }

        @Override
        public Point2.Exposed divInv(double val) {
            super.divInv(val);
            return this;
        }

        @Override
        public Point2.Exposed div(Tuple2 tuple) {
            super.div(tuple);
            return this;
        }

        @Override
        public Point2.Exposed divInv(Tuple2 tuple) {
            super.divInv(tuple);
            return this;
        }

        @Override
        public Point2.Exposed min(double val) {
            super.min(val);
            return this;
        }

        @Override
        public Point2.Exposed min(Tuple2 tuple) {
            super.min(tuple);
            return this;
        }

        @Override
        public Point2.Exposed max(double val) {
            super.max(val);
            return this;
        }

        @Override
        public Point2.Exposed max(Tuple2 tuple) {
            super.max(tuple);
            return this;
        }

        @Override
        public Point2.Exposed clampMin(double min) {
            super.clampMin(min);
            return this;
        }

        @Override
        public Point2.Exposed clampMin(Tuple2 min) {
            super.clampMin(min);
            return this;
        }

        @Override
        public Point2.Exposed clampMax(double max) {
            super.clampMax(max);
            return this;
        }

        @Override
        public Point2.Exposed clampMax(Tuple2 max) {
            super.clampMax(max);
            return this;
        }

        @Override
        public Point2.Exposed clamp(double min, double max) {
            super.clamp(min, max);
            return this;
        }

        @Override
        public Point2.Exposed clamp(Tuple2 tuple, double min, double max) {
            super.clamp(tuple, min, max);
            return this;
        }

        @Override
        public Point2.Exposed clamp(Tuple2 min, Tuple2 max) {
            super.clamp(min, max);
            return this;
        }

        @Override
        public Point2.Exposed clamp(Tuple2 tuple, Tuple2 min, Tuple2 max) {
            super.clamp(tuple, min, max);
            return this;
        }

        @Override
        public Point2.Exposed set(int dim, double val) {
            super.set(val);
            return this;
        }

        @Override
        public Point2.Exposed set(double[] vals) {
            super.set(vals);
            return this;
        }

        private static final long serialVersionUID = 1L;
    }

    /**
	 * Intended for use as a helper delegate.
	 */
    public static class Helper extends Tuple2Impl.Helper {

        /**
		 * Creates an instance, with a default valid state.
		 * @see Tuple2Impl.Helper#Helper()
		 */
        public Helper() {
            super();
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Helper(double valX, double valY) {
            super(valX, valY);
        }

        /**
		 * Creates an instance, as specified.  Values can be in any order.
		 */
        public Helper(double... val) {
            super(val);
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Helper(Tuple2 val) {
            AssertUtils.assertNonNullArg(val);
            if (val instanceof Point2) {
                super.updateState(val);
            } else {
                updateState(val.getX(), val.getY());
            }
        }

        private static final long serialVersionUID = 1L;
    }
}
