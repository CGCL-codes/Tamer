package com.msli.graphic.geom.xform;

import com.msli.core.block.StatefulBlock;
import com.msli.core.exception.NotYetImplementedException;
import com.msli.core.util.CoreUtils;
import com.msli.graphic.math.AxisAngle3;
import com.msli.graphic.math.Tuple3;

/**
 * A Transformer that performs spatial axis-angle rotation in a stateful manner.
 * The rotation is right-handed and occurs about an axis vector through the
 * local space origin, which remains unchanged.
 * <P>
 * Derived from gumbo.graphic.generic.RotationTransformer.
 * @author jonb
 */
public interface RotationXformer extends Xformer, StatefulBlock<RotationXformer> {

    /**
	 * Sets the axis-angle rotation state. Defaults to a zero angle
	 * rotation.
	 * @param rot Temp input rotation. Never null.
	 */
    public void setRotation(AxisAngle3 rot);

    /**
	 * Gets the axis-angle rotation state.
	 * @return Temp output rotation. Never null.
	 */
    public AxisAngle3 getRotation();

    /**
	 * A Transformer (not a RotationTransformer) that performs bi-directional
	 * axis-angle rotation. Intended for use as a core implementation. Can be
	 * used as an immutable object, a base class, or as a mutable delegate
	 * helper.
	 * @author jonb
	 */
    public static class Core extends Xformer.Base {

        /**
		 * Creates an instance, with no inverse transform (i.e. throws
		 * UnsupportedOperationException). Typically used as a "forward-only"
		 * transformer, or as the "inverse" transformer for a "bi-directional"
		 * transformer (see "bi-directional" constructor). If val is immutable
		 * so too will be the instance.
		 * @param state Shared input state object. Never null.
		 */
        public Core(AxisAngle3 state) {
            super();
            CoreUtils.assertNonNullArg(state);
            _state = state;
        }

        /**
		 * Creates an instance, as a "bi-directional" transformer. This
		 * transformer will be set as the inverse transformer of the specified
		 * transformer. If state is immutable so too will be the instance.
		 * @param state Shared input state object. Never null.
		 */
        public Core(AxisAngle3 state, Core inverse) {
            super(inverse);
            CoreUtils.assertNonNullArg(state);
            _state = state;
        }

        @Override
        public <P extends Tuple3> P transformPoint(P point) {
            NotYetImplementedException.doThrow(this);
            return point;
        }

        @Override
        public <V extends Tuple3> V transformVector(V vect) {
            NotYetImplementedException.doThrow(this);
            return vect;
        }

        @Override
        public boolean equalsValue(Object obj, double tolerance) {
            return _state.equalsValue(obj, tolerance);
        }

        private AxisAngle3 _state;

        /**
		 * Returns an inverse rotation state.
		 * @param state Temp input state.  Never null.
		 * @param retVal Temp output inverse state.  Never null.
		 * @return Reference to retVal. Never null.
		 */
        public static <R extends AxisAngle3> R inverseState(AxisAngle3 state, R retVal) {
            CoreUtils.assertNonNullArg(state);
            CoreUtils.assertNonNullArg(retVal);
            retVal.set(state);
            retVal.setAngle(retVal.getAngle().neg().get());
            return retVal;
        }
    }

    /**
	 * Default concrete implementation, with default equality based on
	 * identity.
	 */
    public static class Impl extends StatefulBlock.Base<RotationXformer> implements RotationXformer {

        public Impl() {
            super();
            _xform = new Core(_state, new Core(_stateInv));
        }

        public void setActive(boolean isActive) {
            super.setActive(isActive);
        }

        @Override
        public AxisAngle3 getRotation() {
            return _dummyState.set(_state);
        }

        @Override
        public void setRotation(AxisAngle3 rot) {
            CoreUtils.assertActive(this);
            _state.set(rot);
            Core.inverseState(_state, _stateInv);
            notifyUpdateObservers(this);
        }

        @Override
        public Xformer inverseTranformer() {
            return _xform.inverseTranformer();
        }

        @Override
        public <P extends Tuple3> P transformPoint(P point) {
            return _xform.transformPoint(point);
        }

        @Override
        public <V extends Tuple3> V transformVector(V vect) {
            return _xform.transformVector(vect);
        }

        @Override
        public boolean equalsValue(Object obj, double tolerance) {
            return _xform.equalsValue(obj, tolerance);
        }

        @Override
        public RotationXformer getThis() {
            return this;
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _state = CoreUtils.dispose(_state);
            _stateInv = CoreUtils.dispose(_stateInv);
            _xform = CoreUtils.dispose(_xform);
            _dummyState = CoreUtils.dispose(_dummyState);
        }

        private AxisAngle3 _state = new AxisAngle3.Impl();

        private AxisAngle3 _stateInv = new AxisAngle3.Impl();

        private Core _xform;

        private AxisAngle3 _dummyState = new AxisAngle3.Impl();
    }
}
