package net.sf.hippopotam.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <br>Author: Dmitry Ermakov dim_er@mail.ru
 * <br>Date: 11.07.2007
 */
public class WeakEventListenerList<T extends EventListener> implements Iterable<T> {

    protected LinkedList<WeakReference<T>> listeners = new LinkedList<WeakReference<T>>();

    protected LinkedList<WeakReference<WeakEventListenerList<T>>> externalEventListenerLists;

    private LinkedList<T> emptyView;

    public synchronized void clear() {
        listeners.clear();
    }

    public synchronized void clearExternalEventListenerLists() {
        externalEventListenerLists = null;
    }

    public synchronized void addListener(T listener) {
        if (listener == null) {
            return;
        }
        Iterator<WeakReference<T>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().get() == null) {
                iterator.remove();
            }
        }
        listeners.add(new WeakReference<T>(listener));
    }

    public synchronized void removeListener(T listener) {
        if (listener == null) {
            return;
        }
        Iterator<WeakReference<T>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            T cur = iterator.next().get();
            if (cur == null) {
                iterator.remove();
            } else if (listener.equals(cur)) {
                iterator.remove();
            }
        }
    }

    public synchronized void addExternalEventListenerList(WeakEventListenerList<T> externalEventListenerList) {
        if (externalEventListenerList == null) {
            return;
        }
        if (externalEventListenerLists == null) {
            externalEventListenerLists = new LinkedList<WeakReference<WeakEventListenerList<T>>>();
        } else {
            Iterator<WeakReference<WeakEventListenerList<T>>> iterator = externalEventListenerLists.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().get() == null) {
                    iterator.remove();
                }
            }
        }
        externalEventListenerLists.add(new WeakReference<WeakEventListenerList<T>>(externalEventListenerList));
    }

    public synchronized void removeExternalEventListenerList(WeakEventListenerList<T> externalEventListenerList) {
        if ((externalEventListenerList == null) || (externalEventListenerLists == null)) {
            return;
        }
        Iterator<WeakReference<WeakEventListenerList<T>>> iterator = externalEventListenerLists.iterator();
        while (iterator.hasNext()) {
            WeakEventListenerList<T> eventListenerList = iterator.next().get();
            if (eventListenerList == null) {
                iterator.remove();
            } else if (externalEventListenerList.equals(eventListenerList)) {
                iterator.remove();
            }
        }
    }

    public Iterator<T> iterator() {
        return getListenersView().iterator();
    }

    public synchronized List<T> getListenersView() {
        LinkedList<T> view = emptyView == null ? new LinkedList<T>() : emptyView;
        populateView(view);
        if (view.isEmpty()) {
            emptyView = view;
            return Collections.emptyList();
        } else {
            emptyView = null;
            return view;
        }
    }

    private synchronized void populateView(LinkedList<T> view) {
        Iterator<WeakReference<T>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            T cur = iterator.next().get();
            if (cur == null) {
                iterator.remove();
            } else {
                view.add(cur);
            }
        }
        if (externalEventListenerLists != null) {
            Iterator<WeakReference<WeakEventListenerList<T>>> itr = externalEventListenerLists.iterator();
            while (itr.hasNext()) {
                WeakEventListenerList<T> externalEventListenerList = itr.next().get();
                if (externalEventListenerList == null) {
                    itr.remove();
                } else {
                    externalEventListenerList.populateView(view);
                }
            }
        }
    }
}
