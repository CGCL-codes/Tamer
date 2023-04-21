package com.msli.graphic.form.shape;

import com.msli.core.exception.UnmodifiableStateException;
import com.msli.core.util.JavaUtils;
import com.msli.core.util.NullObject;
import com.msli.graphic.form.Form;
import com.msli.graphic.geom.Geometric;
import com.msli.graphic.geom.xform.Xformer;
import com.msli.graphic.math.MathObject;

/**
 * A Form that defines a geometric shape, such as for spatial bounding.
 * Typically combined with a Localizer to form a geometric entity in a reference
 * space. Equality is based on identity, by default. Use Quantitative methods
 * for value-based equality.
 * <P>
 * Shapes can be classified in numerous ways, such as being open or closed, or
 * as defining a surface (zero volume) or a solid (non-zero volume).
 * <P>
 * Derived from gumbo.graphic.form.shape.Shape.
 * @see MathObject
 * @author jonb
 * @param <THIS> The recursive concrete type of this type.
 */
public interface Shape<THIS extends Shape<THIS>> extends Form<THIS>, Geometric {

    @Override
    public THIS transformGeometry(Xformer xformer);

    /**
	 * Concrete serializable unmodifiable wrapper. All exposers are
	 * unmodifiable. All mutators throw UnmodifiableStateException. Equality
	 * (equals(), hashCode()) forwards to the wrapper target.
	 * @param <THIS> The recursive concrete type of this type.
	 */
    public abstract static class Unmod<THIS extends Shape<THIS>> extends Form.Unmod<THIS> implements Shape<THIS> {

        /**
		 * Creates an instance.
		 * @param target Shared exposed wrapper target.  Never null.
		 */
        public Unmod(THIS target) {
            super(target);
        }

        @Override
        public THIS transformGeometry(Xformer xformer) {
            UnmodifiableStateException.doThrow(this);
            return getThis();
        }

        protected THIS getWrapperTarget() {
            return super.getWrapperTarget();
        }

        private static final long serialVersionUID = 1L;
    }

    /**
	 * Abstract serializable base class.
	 * @param <THIS> The recursive concrete type of this type.
	 */
    public abstract static class Base<THIS extends Shape<THIS>> extends Form.Base<THIS> implements Shape<THIS> {

        /**
		 * For extension and test.
		 */
        public Base() {
        }

        @Override
        public abstract THIS transformGeometry(Xformer xformer);

        private static final long serialVersionUID = 1L;
    }

    /**
	 * Singleton null object empty implementation. All exposers return null
	 * objects. All mutators do nothing.
	 * @param <THIS> The recursive concrete type of this type.
	 */
    public static class Null<THIS extends Shape<THIS>> extends Shape.Base<THIS> implements NullObject, Shape<THIS> {

        @Override
        public THIS combine(THIS form) {
            return getThis();
        }

        @Override
        public THIS intersect(THIS form) {
            return getThis();
        }

        @Override
        public THIS setEmpty() {
            return getThis();
        }

        @Override
        public THIS setForm(THIS form) {
            return getThis();
        }

        @Override
        public boolean isContaining(THIS val) {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isIntersecting(THIS val) {
            return false;
        }

        @Override
        public THIS transformGeometry(Xformer xformer) {
            return getThis();
        }

        @Override
        public boolean equalsValue(Object obj, double tolerance) {
            if (!(obj instanceof Form<?>)) return false;
            Form<?> that = (Form<?>) obj;
            return that.isEmpty();
        }

        @Override
        public THIS dupe() {
            return getThis();
        }

        @SuppressWarnings("unchecked")
        public THIS getThis() {
            return (THIS) this;
        }

        @Override
        public String implToString() {
            return JavaUtils.toShortString(this);
        }

        private Object readResolve() {
            return NULL;
        }

        private static final long serialVersionUID = 1L;

        Null() {
            super();
        }

        /**
		 * Returns a singleton immutable empty Form.
		 */
        @SuppressWarnings("unchecked")
        public static <F extends Shape<F>> Shape.Null<F> getInstance() {
            return NULL;
        }

        @SuppressWarnings("unchecked")
        private static final Shape.Null NULL = new Shape.Null();
    }
}
