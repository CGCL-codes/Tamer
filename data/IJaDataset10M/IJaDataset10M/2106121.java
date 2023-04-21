package com.msli.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.msli.core.util.Disposable;
import com.msli.core.util.CoreUtils;

/**
 * Event notification objects based on the observer pattern, where the
 * observable notifies its observers when its activation state changes. The
 * relation between observable and observer is intended to be one to many.
 * <p>
 * 
 * Notification occurs just after activation and just before deactivation. This
 * affords the observer the opportunity to observe the observable state while
 * it is active and presumably valid.
 * <p>
 * Observables typically implements Activatable, but it is not required.
 * Intended for use with objects whose behavior can be activated, especially
 * stateful ones. Typically, a stateful object is inactive until its state is
 * initialized. If in a processing chain, an object can also be inactive while
 * its input source is missing or invalid.
 * @see Activatable
 * @author jonb
 */
public class ActivationNotice {

    private ActivationNotice() {
    }

    /**
	 * Serves in the role of observer in an observer pattern.
	 * @param <T> The type of the observable that is sent to this observer.
	 */
    public interface Observer<T> {

        /**
		 * Called by the observable object when its activation changes. In all
		 * cases the observable is active and its state is presumed valid when
		 * this method is called.
		 * @param observable Shared exposed observable. Never null. Possibly an
		 * unmodifiable view.
		 * @param isDeactivating True just before deactivation; false just after
		 * activation.
		 */
        public void handleActivation(T observable, boolean isDeactivating);
    }

    /**
	 * Serves in the role of observable in an observer pattern.
	 * <P>
	 * This interface does not extend Disposable, with the assumption being that
	 * observers are responsible for removing themselves from their observable.
	 * Implementations, however, may want to implement Disposable to assure that
	 * observers are cleared. Furthermore, implementations may want to employ
	 * "lazy disposal" whereby observers are tested for disposal and, if needed,
	 * removed before being notified.
	 * @param <T> The type of the observable that is sent to the observers.
	 * Typically, the type of this object.
	 */
    public interface Observable<T> {

        /**
		 * Adds an observer of this object's activation to the observer group.
		 * The order in which observers will be notified is undefined.
		 * @param observer Shared semi-opaque observer. Never null. If duplicate,
		 * nothing may happen.
		 */
        public void addActivationObserver(Observer<? super T> observer);

        /**
		 * Removes an observer of this object's activation from the observer
		 * group.
		 * @param observer Temp semi-opaque observer. Possibly null. If missing,
		 * nothing happens.
		 */
        public void removeActivationObserver(Object observer);

        /**
		 * Delegate helper implementation. The host object of this
		 * helper must call notifyActivationObservers() after it changes its
		 * activation state.
		 * <P>
		 * Employs lazy disposal to automatically remove disposed observers (in
		 * case they forget to remove themselves).
		 */
        public static class Helper<T> extends Disposable.Helper implements Observable<T> {

            public Helper() {
            }

            /**
			 * Called by the host object of this helper. Notifies all observers
			 * that the observable's activation state has changed.
			 * @param observable Shared exposed observable. Never null.
			 * Typically the host object. Possibly an unmodifiable view.
			 * @param isDeactivating True just before deactivation; false just
			 * after activation.
			 */
            public final void notifyActivationObservers(T observable, boolean isDeactivating) {
                CoreUtils.assertNonNullArg(observable);
                CoreUtils.assertNotDisposed(this);
                if (_observers == null) return;
                Iterator<Observer<? super T>> observerI = _observers.iterator();
                while (observerI.hasNext()) {
                    Observer<? super T> observer = observerI.next();
                    if (observer == null || CoreUtils.isDisposed(observer)) {
                        observerI.remove();
                    } else {
                        observer.handleActivation(observable, isDeactivating);
                    }
                }
            }

            @Override
            public final void addActivationObserver(Observer<? super T> observer) {
                CoreUtils.assertNonNullArg(observer);
                CoreUtils.assertNotDisposed(this);
                if (_observers == null) {
                    _observers = new ArrayList<Observer<? super T>>();
                }
                _observers.add(observer);
            }

            @Override
            public final void removeActivationObserver(Object observer) {
                CoreUtils.assertNotDisposed(this);
                if (_observers == null) return;
                _observers.remove(observer);
            }

            @Override
            protected void implDispose() {
                super.implDispose();
                _observers = CoreUtils.dispose(_observers);
            }

            private Collection<Observer<? super T>> _observers = null;
        }

        /**
		 * Base class implementation. A subclass of this base class must call
		 * notifyActivationObservers() after it changes its activation state.
		 * @param <THIS> The type of this object, which is sent to observers.
		 */
        public static class Base<THIS extends Base<THIS>> extends Disposable.IdentityEquality implements Observable<THIS> {

            public Base() {
            }

            /**
			 * Called by a subclass of this base class. Notifies all observers
			 * that the observable's activation has changed.
			 * @param observable Shared exposed observable. Never null.
			 * Typically this object. Possibly an unmodifiable view.
			 * @param isDeactivating True just before deactivation; false just
			 * after activation.
			 */
            protected final void notifyActicationObservers(THIS observable, boolean isDeactivating) {
                _helper.notifyActivationObservers(observable, isDeactivating);
            }

            @Override
            public final void addActivationObserver(ActivationNotice.Observer<? super THIS> observer) {
                _helper.addActivationObserver(observer);
            }

            @Override
            public final void removeActivationObserver(Object observer) {
                _helper.removeActivationObserver(observer);
            }

            @Override
            protected void implDispose() {
                super.implDispose();
                _helper = CoreUtils.dispose(_helper);
            }

            private ActivationNotice.Observable.Helper<THIS> _helper = new ActivationNotice.Observable.Helper<THIS>();
        }
    }
}
