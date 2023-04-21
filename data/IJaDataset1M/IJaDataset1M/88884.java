package gumbo.tech.phys.impl;

import gumbo.core.util.AssertUtils;
import gumbo.tech.math.Tuple3;
import gumbo.tech.math.impl.Tuple3Impl;
import gumbo.tech.phys.Color3;

/**
 * Default implementation for Color3. For extension, extend Helper and provide
 * an instance as the helper delegate.
 */
public class Color3Impl {

    private Color3Impl() {
    }

    public static class Constant extends Tuple3Impl.Constant implements Color3.Constant {

        /**
		 * Creates an instance, with a default valid state.
		 * @see #initState()
		 */
        public Constant() {
            super(new Color3Impl.Helper());
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Constant(double red, double grn, double blu) {
            super(new Color3Impl.Helper(red, grn, blu));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Constant(double... val) {
            super(new Color3Impl.Helper(val));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Constant(Tuple3 val) {
            super(new Color3Impl.Helper(val));
        }

        /**
		 * Creates an instance, with the specified helper delegate.
		 * @param helper Ceded helper delegate. Never null.
		 */
        public Constant(Color3Impl.Helper helper) {
            super(helper);
        }

        @Override
        public Color3.Exposed exposed() {
            return new Color3Impl.Exposed(this);
        }

        @Override
        protected Color3Impl.Helper getHelper() {
            return (Color3Impl.Helper) super.getHelper();
        }

        @Override
        public final double getRed() {
            return getHelper().getRed();
        }

        @Override
        public final double getGreen() {
            return getHelper().getGreen();
        }

        @Override
        public final double getBlue() {
            return getHelper().getBlue();
        }

        private static final long serialVersionUID = 1L;
    }

    public static class Exposed extends Tuple3Impl.Exposed implements Color3.Exposed {

        /**
		 * Creates an instance, with a default valid state.
		 * @see #initState()
		 */
        public Exposed() {
            super(new Color3Impl.Helper());
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Exposed(double red, double grn, double blu) {
            super(new Color3Impl.Helper(red, grn, blu));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Exposed(double... val) {
            super(new Color3Impl.Helper(val));
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Exposed(Tuple3 val) {
            super(new Color3Impl.Helper(val));
        }

        /**
		 * Creates an instance, with the specified helper delegate.
		 * @param helper Ceded helper delegate. Never null.
		 */
        public Exposed(Color3Impl.Helper helper) {
            super(helper);
        }

        @Override
        public Color3.Exposed exposed() {
            return new Color3Impl.Exposed(this);
        }

        @Override
        protected Color3Impl.Helper getHelper() {
            return (Color3Impl.Helper) super.getHelper();
        }

        @Override
        public void setBlue(double val) {
            setZ(val);
        }

        @Override
        public void setGreen(double val) {
            setY(val);
        }

        @Override
        public void setRed(double val) {
            setX(val);
        }

        @Override
        public Color3.Exposed set(double red, double grn, double blu) {
            super.set(red, grn, blu);
            return this;
        }

        @Override
        public Color3.Exposed set(Tuple3 val) {
            super.set(val);
            return this;
        }

        @Override
        public Color3.Exposed set(double val) {
            super.set(val);
            return this;
        }

        @Override
        public Color3.Exposed setX(double val) {
            super.setX(val);
            return this;
        }

        @Override
        public Color3.Exposed setY(double val) {
            super.setY(val);
            return this;
        }

        @Override
        public Color3.Exposed setZ(double val) {
            super.setZ(val);
            return this;
        }

        @Override
        public Color3.Exposed abs(Tuple3 tuple) {
            super.abs(tuple);
            return this;
        }

        @Override
        public Color3.Exposed clear() {
            super.clear();
            return this;
        }

        @Override
        public Color3.Exposed random() {
            super.random();
            return this;
        }

        @Override
        public Color3.Exposed noise() {
            super.noise();
            return this;
        }

        @Override
        public Color3.Exposed neg(Tuple3 tuple) {
            super.neg();
            return this;
        }

        @Override
        public Color3.Exposed round(Tuple3 tuple) {
            super.round(tuple);
            return this;
        }

        @Override
        public Color3.Exposed add(Tuple3 tuple, double val) {
            super.add(tuple, val);
            return this;
        }

        @Override
        public Color3.Exposed add(Tuple3 tupleA, Tuple3 tupleB) {
            super.add(tupleA, tupleB);
            return this;
        }

        @Override
        public Color3.Exposed sub(Tuple3 tuple, double val) {
            super.sub(tuple, val);
            return this;
        }

        @Override
        public Color3.Exposed sub(Tuple3 tupleA, Tuple3 tupleB) {
            super.sub(tupleA, tupleB);
            return this;
        }

        @Override
        public Color3.Exposed mult(Tuple3 tuple, double val) {
            super.mult(tuple, val);
            return this;
        }

        @Override
        public Color3.Exposed mult(Tuple3 tupleA, Tuple3 tupleB) {
            super.mult(tupleA, tupleB);
            return this;
        }

        @Override
        public Color3.Exposed div(Tuple3 tuple, double val) {
            super.div(tuple, val);
            return this;
        }

        @Override
        public Color3.Exposed div(double val, Tuple3 tuple) {
            super.div(val, tuple);
            return this;
        }

        @Override
        public Color3.Exposed div(Tuple3 tupleA, Tuple3 tupleB) {
            super.div(tupleA, tupleB);
            return this;
        }

        @Override
        public Color3.Exposed min(Tuple3 tuple, double val) {
            super.min(tuple, val);
            return this;
        }

        @Override
        public Color3.Exposed min(Tuple3 tupleA, Tuple3 tupleB) {
            super.min(tupleA, tupleB);
            return this;
        }

        @Override
        public Color3.Exposed max(Tuple3 tuple, double val) {
            super.max(tuple, val);
            return this;
        }

        @Override
        public Color3.Exposed max(Tuple3 tupleA, Tuple3 tupleB) {
            super.max(tupleA, tupleB);
            return this;
        }

        @Override
        public Color3.Exposed clampMin(Tuple3 tuple, double min) {
            super.clampMin(tuple, min);
            return this;
        }

        @Override
        public Color3.Exposed clampMin(Tuple3 tuple, Tuple3 min) {
            super.clampMin(tuple, min);
            return this;
        }

        public Color3.Exposed clampMax(Tuple3 tuple, double max) {
            super.clampMax(tuple, max);
            return this;
        }

        @Override
        public Color3.Exposed clampMax(Tuple3 tuple, Tuple3 max) {
            super.clampMax(tuple, max);
            return this;
        }

        @Override
        public Color3.Exposed neg() {
            super.neg();
            return this;
        }

        @Override
        public Color3.Exposed abs() {
            super.abs();
            return this;
        }

        @Override
        public Color3.Exposed round() {
            super.round();
            return this;
        }

        @Override
        public Color3.Exposed add(double val) {
            super.add(val);
            return this;
        }

        @Override
        public Color3.Exposed add(Tuple3 tuple) {
            super.add(tuple);
            return this;
        }

        @Override
        public Color3.Exposed sub(double val) {
            super.sub(val);
            return this;
        }

        @Override
        public Color3.Exposed sub(Tuple3 tuple) {
            super.sub(tuple);
            return this;
        }

        @Override
        public Color3.Exposed mult(double val) {
            super.mult(val);
            return this;
        }

        @Override
        public Color3.Exposed mult(Tuple3 tuple) {
            super.mult(tuple);
            return this;
        }

        @Override
        public Color3.Exposed div(double val) {
            super.div(val);
            return this;
        }

        @Override
        public Color3.Exposed divInv(double val) {
            super.divInv(val);
            return this;
        }

        @Override
        public Color3.Exposed div(Tuple3 tuple) {
            super.div(tuple);
            return this;
        }

        @Override
        public Color3.Exposed divInv(Tuple3 tuple) {
            super.divInv(tuple);
            return this;
        }

        @Override
        public Color3.Exposed min(double val) {
            super.min(val);
            return this;
        }

        @Override
        public Color3.Exposed min(Tuple3 tuple) {
            super.min(tuple);
            return this;
        }

        @Override
        public Color3.Exposed max(double val) {
            super.max(val);
            return this;
        }

        @Override
        public Color3.Exposed max(Tuple3 tuple) {
            super.max(tuple);
            return this;
        }

        @Override
        public Color3.Exposed clampMin(double min) {
            super.clampMin(min);
            return this;
        }

        @Override
        public Color3.Exposed clampMin(Tuple3 min) {
            super.clampMin(min);
            return this;
        }

        @Override
        public Color3.Exposed clampMax(double max) {
            super.clampMax(max);
            return this;
        }

        @Override
        public Color3.Exposed clampMax(Tuple3 max) {
            super.clampMax(max);
            return this;
        }

        @Override
        public Color3.Exposed clamp(double min, double max) {
            super.clamp(min, max);
            return this;
        }

        @Override
        public Color3.Exposed clamp(Tuple3 tuple, double min, double max) {
            super.clamp(tuple, min, max);
            return this;
        }

        @Override
        public Color3.Exposed clamp(Tuple3 min, Tuple3 max) {
            super.clamp(min, max);
            return this;
        }

        @Override
        public Color3.Exposed clamp(Tuple3 tuple, Tuple3 min, Tuple3 max) {
            super.clamp(tuple, min, max);
            return this;
        }

        @Override
        public Color3.Exposed set(int dim, double val) {
            super.set(val);
            return this;
        }

        @Override
        public Color3.Exposed set(double[] vals) {
            super.set(vals);
            return this;
        }

        @Override
        public double getRed() {
            return getHelper().getRed();
        }

        @Override
        public double getGreen() {
            return getHelper().getGreen();
        }

        @Override
        public double getBlue() {
            return getHelper().getBlue();
        }

        private static final long serialVersionUID = 1L;
    }

    /**
	 * Intended for use as a helper delegate.
	 */
    public static class Helper extends Tuple3Impl.Helper {

        /**
		 * Creates an instance, with a default valid state.
		 * @see #initState()
		 */
        public Helper() {
            super();
        }

        /**
		 * Creates an instance, as specified.
		 */
        public Helper(double red, double grn, double blu) {
            super(red, grn, blu);
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
        public Helper(Tuple3 val) {
            AssertUtils.assertNonNullArg(val);
            if (val instanceof Color3) {
                super.updateState(val);
            } else {
                updateState(val.getX(), val.getY(), val.getZ());
            }
        }

        @Override
        public void checkState(double red, double grn, double blu) {
            AssertUtils.assertNonNullArg(red);
            AssertUtils.assertNonNullArg(grn);
            AssertUtils.assertNonNullArg(blu);
            if (red < 0.0 || grn < 0.0 || blu < 0.0) throw new IllegalStateException("Color values must be >=0.0." + "  val=" + red + " " + grn + " " + blu);
            if (red > 1.0 || grn > 1.0 || blu > 1.0) throw new IllegalStateException("Color values must be <=1.0." + "  val=" + red + " " + grn + " " + blu);
        }

        public double getRed() {
            return getX();
        }

        public double getGreen() {
            return getY();
        }

        public double getBlue() {
            return getZ();
        }

        private static final long serialVersionUID = 1L;
    }
}
