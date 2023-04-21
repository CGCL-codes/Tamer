package net.sourceforge.jaulp.designpattern.observer;

import net.sourceforge.jaulp.designpattern.observer.ifaces.DisplayViewElement;
import net.sourceforge.jaulp.designpattern.observer.ifaces.Observer;
import net.sourceforge.jaulp.designpattern.observer.ifaces.Subject;

/**
 * An abstract generic implementation from the Observer-Pattern.
 *
 * @param <T> the < t>
 */
public abstract class AbstractObserver<T> implements Observer<T>, DisplayViewElement {

    /** The subject. */
    private Subject<T> subject;

    /** The observable. */
    private T observable;

    /**
	 * This method is called when information about an Abstract
	 * which was previously requested using an asynchronous
	 * interface becomes available.
	 *
	 * @return the observable
	 */
    public T getObservable() {
        return observable;
    }

    /**
	 * This method is called when information about an Abstract
	 * which was previously requested using an asynchronous
	 * interface becomes available.
	 *
	 * @param subject the subject
	 */
    public AbstractObserver(Subject<T> subject) {
        super();
        this.subject = subject;
        this.subject.addObserver(this);
    }

    /**
	 * This method is called when information about an Abstract
	 * which was previously requested using an asynchronous
	 * interface becomes available.
	 *
	 * @param observable the observable
	 * {@inheritDoc}
	 * @see net.sourceforge.jaulp.designpattern.observer.ifaces.Observer#update(java.lang.Object)
	 */
    @Override
    public void update(T observable) {
        this.observable = observable;
        displayView();
    }
}
