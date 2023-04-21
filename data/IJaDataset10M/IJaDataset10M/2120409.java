package gumbo.core.update.impl;

import gumbo.core.update.UpdateBean;
import gumbo.core.update.UpdateEvent;
import gumbo.core.util.CoreUtils;
import gumbo.core.util.AssertUtils;
import gumbo.core.util.EventPhase;
import gumbo.core.wrap.WrapperImpl;
import java.beans.PropertyChangeListener;

/**
 * Abstract disposable base class for an UpdateBean, with equality undefined.
 * Implemented by wrapping a delegate implementation, which may itself be an
 * UpdateBean.  If an UpdateBean, bean methods forward to the target.
 * <P>
 * As a wrapper, disposing the wrapper nulls the target reference, but does not
 * dispose the target. However, if the target is Disposable and it is disposed,
 * this wrapper will also be disposed.
 */
public abstract class UpdateBeanImplWrapper extends WrapperImpl implements UpdateBean {

    /**
	 * Creates an instance.
	 * @param target Shared exposed target of this wrapper. Never null.
	 */
    public UpdateBeanImplWrapper(Object target) {
        super(target);
        if (target instanceof UpdateBean) {
            _beanHelper = (UpdateBean) target;
        } else {
            _beanHelper = new UpdateBeanImpl.IdentityEquality();
        }
    }

    @Override
    public void addBeanListener(PropertyChangeListener listener, EventPhase.Prescribe phase, String propertyId) {
        AssertUtils.assertNotDisposed(this);
        _beanHelper.addBeanListener(listener, phase, propertyId);
    }

    @Override
    public void fireUpdateEvent(UpdateEvent event) {
        AssertUtils.assertNotDisposed(this);
        _beanHelper.fireUpdateEvent(event);
    }

    @Override
    public boolean hasBeanListeners() {
        AssertUtils.assertNotDisposed(this);
        return _beanHelper.hasBeanListeners();
    }

    @Override
    public boolean hasBeanListeners(EventPhase.Prescribe phase, String propertyId) {
        AssertUtils.assertNotDisposed(this);
        return _beanHelper.hasBeanListeners(phase, propertyId);
    }

    @Override
    public void removeBeanListener(PropertyChangeListener listener, EventPhase.Prescribe phase, String propertyId) {
        if (CoreUtils.isDisposed(this)) return;
        _beanHelper.removeBeanListener(listener, phase, propertyId);
    }

    @Override
    public void removeBeanListener(PropertyChangeListener listener) {
        if (CoreUtils.isDisposed(this)) return;
        AssertUtils.assertNotDisposed(this);
        _beanHelper.removeBeanListener(listener);
    }

    @Override
    protected void implDispose() {
        super.implDispose();
        if (_beanHelper != getWrapperTarget()) {
            CoreUtils.dispose(_beanHelper);
        }
        _beanHelper = null;
    }

    private UpdateBean _beanHelper;

    /**
	 * Implementing wrapper, with equality is based on target state. As such,
	 * the wrapper tests equal to the target, and the client MUST assure that
	 * the type of this wrapper and that of the target are compatible for
	 * equality. Disposing the wrapper nulls the target but does not dispose it.
	 */
    public static class StateEquality extends UpdateBeanImplWrapper {

        /**
		 * Creates an instance.
		 * @param target Shared exposed wrapper target. Never null.
		 */
        public StateEquality(Object target) {
            super(target);
        }

        @Override
        public final boolean equals(Object obj) {
            if (obj == this) return true;
            return getWrapperTarget().equals(obj);
        }

        @Override
        public final int hashCode() {
            return getWrapperTarget().hashCode();
        }
    }

    /**
	 * Implementing wrapper, with equality based on identity. As such, the
	 * wrapper does NOT test equal to the target. Disposing the wrapper nulls
	 * the target but does not dispose it.
	 */
    public static class IdentityEquality extends UpdateBeanImplWrapper {

        /**
		 * Creates an instance.
		 * @param target Shared exposed wrapper target. Never null.
		 */
        public IdentityEquality(Object target) {
            super(target);
        }

        @Override
        public final boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public final int hashCode() {
            return System.identityHashCode(this);
        }
    }
}
