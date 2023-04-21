package com.msli.core.util;

import com.msli.core.event.DisposalNotice;

/**
 * Abstract disposable base classes for wrapper classes that are not
 * WrapperAware. Wrapper classes require special attention to equality and
 * disposal, which this and the other included base classes address. Use this
 * base class for wrappers whose equality is undefined, such as for a Collection
 * (as opposed to a Set or List). Otherwise, use one of the concrete wrapper
 * base classes.
 * <P>
 * The included base classes are not WrapperAware, which means that the wrapper
 * will not test equal to the wrapper target unless equality is based on state,
 * such as a Set or List. Note that the classes provided here are not intended
 * for use as types, but only as convenient base classes.
 * <P>
 * As a wrapper, disposing the wrapper nulls the target reference, but does not
 * dispose the target. However, if the target is Disposable and it is disposed,
 * this wrapper will also be disposed.
 * @author jonb
 * @see WrapperAware
 */
public abstract class Wrapper extends Disposable.AbstractEquality {

    /**
	 * Creates an instance.
	 * @param target Shared exposed wrapper target.  Never null.
	 */
    public Wrapper(Object target) {
        CoreUtils.assertNonNullArg(target);
        _target = target;
        if (_target instanceof Disposable) {
            ((Disposable) _target).addDisposalObserver(_disposer);
        }
    }

    /**
	 * Serialization constructor. Not intended for extension or implementation
	 * (getTarget() will throw an exception).
	 */
    public Wrapper() {
    }

    /**
	 * Gets the target of this wrapper. Typically not exposed to the public
	 * to protect the target.
	 * @return Shared exposed wrapper target. Never null.
	 * @throws IllegalStateException if target is null.
	 */
    protected Object getWrapperTarget() {
        if (_target == null) throw new IllegalStateException("The target of this wrapper is null." + " The no-arg constructor must only be used for serialization.");
        return _target;
    }

    @Override
    public String toString() {
        return _target.toString();
    }

    @Override
    protected void implDispose() {
        super.implDispose();
        if (_target instanceof Disposable) {
            ((Disposable) _target).removeDisposalObserver(_disposer);
        }
        _target = null;
    }

    private Object _target;

    private DisposalNotice.Observer<Disposable> _disposer = new DisposalNotice.Observer<Disposable>() {

        @Override
        public void handleDisposal(Disposable observable) {
            dispose();
        }
    };

    /**
	 * Concrete base wrapper class in which equality is based on target state,
	 * with equality methods forwarding to the target. As such, the wrapper
	 * tests equal to the target, and the client MUST assure that the type of
	 * this wrapper and that of the target are compatible for equality.
	 * Disposing the wrapper nulls the target but does not dispose it.
	 */
    public static class StateEquality extends Wrapper {

        /**
		 * Creates an instance.
		 * @param target Shared exposed wrapper target.  Never null.
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
	 * Concrete base wrapper class in which equality is based on identity, with
	 * equality methods not forwarding to the target. As such, the wrapper does
	 * NOT test equal to the target. Disposing the wrapper nulls the target but
	 * does not dispose it.
	 */
    public static class IdentityEquality extends Wrapper {

        /**
		 * Creates an instance.
		 * @param target Shared exposed wrapper target.  Never null.
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
