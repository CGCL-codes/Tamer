package gumbo.core.block;

import gumbo.core.life.impl.DisposableImpl;
import gumbo.core.update.UpdateBean;
import gumbo.core.update.UpdateEvent;
import gumbo.core.util.CoreUtils;
import gumbo.core.util.EventPhase;

/**
 * A stateful processing block that is activatable, and that exposes its
 * processing state as a bean.
 * <p>
 * Unless otherwise noted, while inactive, mutators throw DeactivatedException,
 * and the block generates no state update events. Unless otherwise noted, a
 * StatefulBlock is inactive by default.
 * @author jonb
 */
public interface StatefulBlock extends Activatable, UpdateBean {

    /**
	 * Abstract base class implementation, with default equality based on
	 * identity.
	 * @param <THIS> The recursive type of this type.
	 */
    public abstract static class Base extends DisposableImpl.IdentityEquality implements StatefulBlock {

        /**
		 * Called by subclasses to update activation. Updates activation state,
		 * notifies observers. If activation does not change nothing happens.
		 * Does not affect pick or sensor state directly. Calls implActivate()
		 * and implDeactivate() accordingly, which may affect pick and sensor
		 * state.
		 * @param isActive True to activate this sensor; false to deactivate it.
		 */
        protected void setActive(boolean isActive) {
            if (isActive == _isActive) return;
            doBeforeActivation();
            if (isActive) {
                _isActive = true;
                implAfterActivation();
            } else {
                implBeforeDeactivation();
                _isActive = false;
            }
            doAfterActivation();
        }

        /**
		 * Called by subclass mutators before an activation change. If there are
		 * bean listeners an UpdateEvent is created and fired, otherwise nothing
		 * happens.
		 */
        protected void doBeforeActivation() {
            if (hasBeanListeners(EventPhase.Prescribe.Before, Activatable.BEAN_ACTIVATED)) {
                if (_beforeActivationEvent == null) {
                    _beforeActivationEvent = new UpdateEvent(this, EventPhase.Describe.Before, Activatable.BEAN_ACTIVATED);
                }
                fireUpdateEvent(_beforeActivationEvent);
            }
        }

        /**
		 * Called by subclass mutators after an activation change. If there are
		 * bean listeners an UpdateEvent is created and fired, otherwise nothing
		 * happens.
		 */
        protected void doAfterActivation() {
            if (hasBeanListeners(EventPhase.Prescribe.After, Activatable.BEAN_ACTIVATED)) {
                if (_afterActivationEvent == null) {
                    _afterActivationEvent = new UpdateEvent(this, EventPhase.Describe.After, Activatable.BEAN_ACTIVATED);
                }
                fireUpdateEvent(_afterActivationEvent);
            }
        }

        /**
		 * Implemented by subclasses, called by the system just after activating
		 * this sensor. Default implementation does nothing. Depending on the
		 * nature of this block, a subclass may want to check its input state
		 * and update its output state, and start or resume previous operations
		 * as part of activation.
		 */
        protected void implAfterActivation() {
        }

        /**
		 * Implemented by subclasses, called by the system just before
		 * deactivating this sensor. Default implementation does nothing.
		 * Depending on the nature of this block, a subclass may want to suspend
		 * or terminate any active operations as part of deactivation.
		 */
        protected void implBeforeDeactivation() {
        }

        @Override
        public final boolean isActive() {
            return _isActive;
        }

        @Override
        protected void implDispose() {
            super.implDispose();
            _beforeActivationEvent = CoreUtils.dispose(_beforeActivationEvent);
            _afterActivationEvent = CoreUtils.dispose(_afterActivationEvent);
        }

        private static final long serialVersionUID = 1L;

        private boolean _isActive = false;

        private UpdateEvent _beforeActivationEvent = null;

        private UpdateEvent _afterActivationEvent = null;
    }
}
