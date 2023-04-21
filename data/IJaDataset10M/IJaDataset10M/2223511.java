package ch.epfl.arni.jtossim;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;

public class AppendOnlyList<T> implements ObservableListListener, ObservableList<T> {

    private ObservableList<T> storage;

    private AppendOnlyList<T> parent;

    private HashMap<Integer, Integer> idMaps;

    private boolean destroyed = false;

    private boolean stopped = false;

    private Filter<T> filter;

    public static interface Filter<T> {

        public boolean filter(T object);
    }

    public AppendOnlyList() {
        storage = ObservableCollections.observableList(new LinkedList<T>());
        setFiltering(false);
    }

    private AppendOnlyList(final Object monitor, AppendOnlyList<T> parent, final Filter filter) {
        this();
        this.filter = filter;
        this.parent = parent;
        AppendOnlyList.this.parent.addObservableListListener(AppendOnlyList.this);
        setFiltering(true);
        new Thread(new Runnable() {

            public void run() {
                int id = 0;
                setPercentageComplete(0);
                int max = AppendOnlyList.this.parent.size();
                for (int i = 0; i < max; i++) {
                    if (destroyed || stopped) break;
                    if (filter.filter(AppendOnlyList.this.parent.get(i))) {
                        synchronized (monitor) {
                            storage.add(id, (T) AppendOnlyList.this.parent.get(i));
                        }
                        if ((double) i / (double) max - getPercentageComplete() > 0.01) setPercentageComplete((double) i / (double) max);
                        id++;
                    }
                }
                setPercentageComplete(1);
                setFiltering(false);
            }
        }).start();
    }

    protected double percentageComplete = 1;

    public static final String PROP_PERCENTAGECOMPLETE = "percentageComplete";

    /**
     * Get the value of percentageComplete
     *
     * @return the value of percentageComplete
     */
    public double getPercentageComplete() {
        return percentageComplete;
    }

    /**
     * Set the value of percentageComplete
     *
     * @param percentageComplete new value of percentageComplete
     */
    private void setPercentageComplete(double percentageComplete) {
        double oldPercentageComplete = this.percentageComplete;
        this.percentageComplete = percentageComplete;
        propertyChangeSupport.firePropertyChange(PROP_PERCENTAGECOMPLETE, oldPercentageComplete, percentageComplete);
    }

    public void stopFiltering() {
        stopped = true;
    }

    public void destroy() {
        if (parent != null) parent.removeObservableListListener(this);
        destroyed = true;
    }

    protected boolean filtering;

    public static final String PROP_FILTERING = "filtering";

    public boolean isFiltering() {
        return filtering;
    }

    private void setFiltering(boolean filtering) {
        boolean oldFiltering = this.filtering;
        this.filtering = filtering;
        propertyChangeSupport.firePropertyChange(PROP_FILTERING, oldFiltering, filtering);
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public AppendOnlyList<T> getFilteredStorage(Object monitor, Filter filter) {
        return new AppendOnlyList<T>(monitor, this, filter);
    }

    public void removeObservableListListener(ObservableListListener listener) {
        storage.removeObservableListListener(listener);
    }

    public void addObservableListListener(ObservableListListener listener) {
        storage.addObservableListListener(listener);
    }

    public synchronized void listElementsAdded(ObservableList list, int index, int length) {
        if (index < storage.size()) {
            throw new UnsupportedOperationException("Only appending is supported");
        } else {
            for (int i = index; i < index + length; i++) {
                if (filter != null) {
                    if (filter.filter((T) list.get(i))) {
                        storage.add((T) list.get(i));
                    }
                }
            }
        }
    }

    public void listElementsRemoved(ObservableList list, int index, List oldElements) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void listElementReplaced(ObservableList list, int index, Object oldElement) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void listElementPropertyChanged(ObservableList list, int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean supportsElementPropertyChanged() {
        return false;
    }

    public synchronized <T> T[] toArray(T[] a) {
        return storage.toArray(a);
    }

    public synchronized Object[] toArray() {
        return storage.toArray();
    }

    public synchronized List<T> subList(int fromIndex, int toIndex) {
        return storage.subList(fromIndex, toIndex);
    }

    public synchronized int size() {
        return storage.size();
    }

    public T set(int index, T element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public T remove(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public ListIterator<T> listIterator(int index) {
        return storage.listIterator(index);
    }

    public ListIterator<T> listIterator() {
        return storage.listIterator();
    }

    public synchronized int lastIndexOf(Object o) {
        return storage.lastIndexOf(o);
    }

    public Iterator<T> iterator() {
        return storage.iterator();
    }

    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }

    public synchronized int indexOf(Object o) {
        return storage.indexOf(o);
    }

    public synchronized T get(int index) {
        return storage.get(index);
    }

    public boolean containsAll(Collection<?> c) {
        return storage.containsAll(c);
    }

    public boolean contains(Object o) {
        return storage.contains(o);
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public synchronized boolean addAll(int index, Collection<? extends T> c) {
        if (filter != null) throw new UnsupportedOperationException("Not supported.");
        return storage.addAll(index, c);
    }

    public synchronized boolean addAll(Collection<? extends T> c) {
        if (filter != null) throw new UnsupportedOperationException("Not supported.");
        return storage.addAll(c);
    }

    public synchronized void add(int index, T element) {
        if (filter != null) throw new UnsupportedOperationException("Not supported.");
        storage.add(index, element);
    }

    public synchronized boolean add(T e) {
        if (filter != null) throw new UnsupportedOperationException("Not supported.");
        return storage.add(e);
    }
}
