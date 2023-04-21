package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Classe ne remplaçant pas toutes les méthodes et levant une notification lors de certaines modification.
 * add, addAll, remove, removeAll, clear et set.
 *
 * @param <T>
 */
public class ObservableArrayList<T> extends ArrayList<T> implements IdsObservable, Serializable {

    private static final long serialVersionUID = 8016626914507293211L;

    private transient ArrayList<IdsObserver> idsObservers;

    private final ObservableType observableType;

    private int currentIndex = 0;

    public ObservableArrayList(ObservableType t) {
        super();
        observableType = t;
        idsObservers = new ArrayList<IdsObserver>();
    }

    @Override
    public boolean add(T e) {
        boolean b = super.add(e);
        notifyObservers();
        return b;
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
        currentIndex = index;
        notifyObservers();
    }

    @Override
    public boolean addAll(Collection<? extends T> e) {
        return addAll(size(), e);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> e) {
        boolean b = super.addAll(index, e);
        currentIndex = index;
        notifyObservers();
        return b;
    }

    @Override
    public void clear() {
        super.clear();
        notifyObservers();
    }

    @Override
    public T remove(int index) {
        T e = super.remove(index);
        currentIndex = (index > 0) ? index - 1 : 0;
        notifyObservers();
        return e;
    }

    @Override
    public boolean remove(Object o) {
        boolean b = super.remove(o);
        currentIndex = 0;
        notifyObservers();
        return b;
    }

    @Override
    public T set(int index, T element) {
        T e = super.set(index, element);
        currentIndex = index;
        notifyObservers();
        return e;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public T get(int index) {
        currentIndex = index;
        return super.get(index);
    }

    public void addObserver(IdsObserver obs) {
        idsObservers.add(obs);
    }

    public void notifyObservers() {
        for (IdsObserver obs : idsObservers) obs.observableUpdated(this, observableType);
    }
}
