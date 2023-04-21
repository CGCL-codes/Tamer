package com.msli.graphic.geom.xform;

import com.msli.core.util.CoreUtils;
import com.msli.core.util.Disposable;
import com.msli.core.util.Quantitative;
import com.msli.graphic.locale.space.global.Globalizer;
import com.msli.graphic.locale.space.local.Localizer;
import com.msli.graphic.math.Tuple3;

/**
 * An object that represents a 3D spatial transform of geometric state.
 * Transforms can represent a change of reference space, or a simple
 * "deformation" of a shape's geometric state. Typically, Transformers are
 * modifiable and stateful. Equality is based on identity, by default. Use
 * Quantitative methods for value-based equality.
 * <P>
 * The tacit assumption is that the geometry of any shape can be reduced to
 * point and vector elements and, as such, any shape's geometry can be
 * transformed accordingly.
 * <P>
 * Derived from gumbo.graphic.generic.Transforming.
 * @author jonb
 * @see Localizer
 * @see Globalizer
 * @see UnityXformer "Null object Transformer."
 */
public interface Xformer extends Quantitative {

    /**
	 * Transforms a value, as a point, according to this transformer's
	 * operation.
	 * @param point Temp exposed point. Never null.
	 * @return Reference to {@code point}. Never null.
	 */
    public <P extends Tuple3> P transformPoint(P point);

    /**
	 * Transforms a value, as a vector, according to this transformer's
	 * operation.
	 * @param vect Temp exposed vector. Never null.
	 * @return Reference to {@code vect}. Never null.
	 */
    public <V extends Tuple3> V transformVector(V vect);

    /**
	 * Gets a transformer that performs an inverse operation.
	 * @return Temp output transformer. Never null.
	 * @throws UnsupportedOperationException if this transformer does not have
	 * an inverse.
	 */
    public Xformer inverseTranformer();

    /**
	 * {@inheritDoc}
	 * <P>
	 * Returns true if {@code obj} is a Transformer, and it produces the same
	 * value transformations as this object. Concrete implementations should
	 * declare this method final to assure the contract for quantitative
	 * equality cannot be violated.
	 */
    public boolean equalsValue(Object obj, double tolerance);

    /**
	 * Default abstract implementation, with default equality based on identity.
	 * Typically, subclasses support both constructors, and clients construct
	 * the inverse transformer first, and then the forward transformer, with the
	 * inverse transformer as the "bi-directional" constructor's argument.
	 */
    public abstract static class Base extends Disposable.IdentityEquality implements Xformer {

        /**
		 * Creates an instance, with no inverse transform (i.e. throws
		 * UnsupportedOperationException). Typically used as a "forward-only"
		 * transformer, or as the "inverse" transformer for a "bi-directional"
		 * transformer (see "bi-directional" constructor).
		 */
        public Base() {
        }

        /**
		 * Creates an instance, as a "bi-directional" transformer. This
		 * transformer will be set as the inverse transformer of the specified
		 * transformer.
		 */
        public Base(Xformer.Base inverse) {
            CoreUtils.assertNonNullArg(inverse);
            _inverse = inverse;
            inverse._inverse = this;
        }

        @Override
        public final Xformer inverseTranformer() {
            if (_inverse == null) throw new UnsupportedOperationException("This transformer does not provide an inverse transformer." + " this=" + CoreUtils.toShortString(this));
            return _inverse;
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _inverse = CoreUtils.dispose(_inverse);
        }

        private Xformer _inverse = null;
    }
}
