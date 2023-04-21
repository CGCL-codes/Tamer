package org.dishevelled.observable;

import java.util.NavigableMap;
import org.dishevelled.observable.event.NavigableMapChangeEvent;
import org.dishevelled.observable.event.NavigableMapChangeListener;
import org.dishevelled.observable.event.NavigableMapChangeVetoException;
import org.dishevelled.observable.event.ObservableNavigableMapChangeSupport;
import org.dishevelled.observable.event.VetoableNavigableMapChangeEvent;
import org.dishevelled.observable.event.VetoableNavigableMapChangeListener;

/**
 * Abstract implementation of an observable navigable map
 * that decorates an instance of <code>NavigableMap</code>.
 *
 * @param <K> navigable map key type
 * @param <V> navigable map value type
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public abstract class AbstractObservableNavigableMap<K, V> extends AbstractNavigableMapDecorator<K, V> implements ObservableNavigableMap<K, V> {

    /** Observable navigable map change support. */
    private ObservableNavigableMapChangeSupport<K, V> support;

    /**
     * Create a new abstract observable navigable map that
     * decorates the specified navigable map.
     *
     * @param navigableMap navigable map to decorate
     */
    protected AbstractObservableNavigableMap(final NavigableMap<K, V> navigableMap) {
        super(navigableMap);
        support = new ObservableNavigableMapChangeSupport(this);
    }

    /**
     * Return the <code>ObservableNavigableMapChangeSupport</code>
     * class backing this abstract observable navigable map.
     *
     * @return the <code>ObservableNavigableMapChangeSupport</code>
     *    class backing this abstract observable navigable map
     */
    protected final ObservableNavigableMapChangeSupport<K, V> getObservableNavigableMapChangeSupport() {
        return support;
    }

    /** {@inheritDoc} */
    public final void addNavigableMapChangeListener(final NavigableMapChangeListener<K, V> l) {
        support.addNavigableMapChangeListener(l);
    }

    /** {@inheritDoc} */
    public final void removeNavigableMapChangeListener(final NavigableMapChangeListener<K, V> l) {
        support.removeNavigableMapChangeListener(l);
    }

    /** {@inheritDoc} */
    public final void addVetoableNavigableMapChangeListener(final VetoableNavigableMapChangeListener<K, V> l) {
        support.addVetoableNavigableMapChangeListener(l);
    }

    /** {@inheritDoc} */
    public final void removeVetoableNavigableMapChangeListener(final VetoableNavigableMapChangeListener<K, V> l) {
        support.removeVetoableNavigableMapChangeListener(l);
    }

    /** {@inheritDoc} */
    public final NavigableMapChangeListener<K, V>[] getNavigableMapChangeListeners() {
        return support.getNavigableMapChangeListeners();
    }

    /** {@inheritDoc} */
    public final int getNavigableMapChangeListenerCount() {
        return support.getNavigableMapChangeListenerCount();
    }

    /** {@inheritDoc} */
    public final VetoableNavigableMapChangeListener<K, V>[] getVetoableNavigableMapChangeListeners() {
        return support.getVetoableNavigableMapChangeListeners();
    }

    /** {@inheritDoc} */
    public final int getVetoableNavigableMapChangeListenerCount() {
        return support.getVetoableNavigableMapChangeListenerCount();
    }

    /** {@inheritDoc} */
    public void fireNavigableMapWillChange() throws NavigableMapChangeVetoException {
        support.fireNavigableMapWillChange();
    }

    /** {@inheritDoc} */
    public void fireNavigableMapWillChange(final VetoableNavigableMapChangeEvent<K, V> e) throws NavigableMapChangeVetoException {
        support.fireNavigableMapWillChange(e);
    }

    /** {@inheritDoc} */
    public void fireNavigableMapChanged() {
        support.fireNavigableMapChanged();
    }

    /** {@inheritDoc} */
    public void fireNavigableMapChanged(final NavigableMapChangeEvent<K, V> e) {
        support.fireNavigableMapChanged(e);
    }
}
