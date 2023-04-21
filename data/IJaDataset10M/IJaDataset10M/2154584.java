package org.jquantlib.cashflow;

import java.util.List;
import org.jquantlib.Settings;
import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.time.Date;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;
import org.jquantlib.util.PolymorphicVisitable;
import org.jquantlib.util.PolymorphicVisitor;
import org.jquantlib.util.Visitor;

/**
 * This class is the base class for all financial events.
 *
 * @author Richard Gomes
 */
public abstract class Event implements Observable, PolymorphicVisitable {

    protected Event() {
    }

    /**
     * Keeps the date at which the event occurs
     */
    public abstract Date date();

    /**
     * Returns true if an event has already occurred before a date
     * <p>
     * If {@link Settings#isTodaysPayments()} is true, then a payment event has not
     * occurred if the input date is the same as the event date,
     * and so includeToday should be defaulted to true.
     * <p>
     * This should be the only place in the code that is affected
     * directly by {@link Settings#isTodaysPayments()}
     */
    public boolean hasOccurred(final Date d) {
        return hasOccurred(d, new Settings().isTodaysPayments());
    }

    /**
     * Returns true if an event has already occurred before a date where it is
     * explicitly defined whether the current date must considered.
     *
     * @param d is a Date
     * @return true if an event has already occurred before a date
     */
    public boolean hasOccurred(final Date d, final boolean includeToday) {
        if (includeToday) {
            return date().compareTo(d) < 0;
        } else {
            return date().compareTo(d) <= 0;
        }
    }

    /**
     * Implements multiple inheritance via delegate pattern to an inner class
     *
     * @see Observable
     * @see DefaultObservable
     */
    private final DefaultObservable delegatedObservable = new DefaultObservable(this);

    @Override
    public void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

    @Override
    public void accept(final PolymorphicVisitor pv) {
        final Visitor<Event> v = (pv != null) ? pv.visitor(this.getClass()) : null;
        if (v != null) {
            v.visit(this);
        } else {
            throw new LibraryException("null event visitor");
        }
    }
}
