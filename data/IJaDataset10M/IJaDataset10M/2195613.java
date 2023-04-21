package com.ivis.xprocess.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class SelectionChangeManager {

    private static List<ISelectionProvider> selectionProviders = Collections.synchronizedList(new ArrayList<ISelectionProvider>());

    private static List<ISelectionChangedListener> selectionListeners = Collections.synchronizedList(new ArrayList<ISelectionChangedListener>());

    private static List<ISelectionChangedListener> selectionChangeListeners = Collections.synchronizedList(new ArrayList<ISelectionChangedListener>());

    private SelectionChangeManager() {
    }

    /**
     * Add the selection provider to a central list so other UI xelements can
     * easily add themselves as listeners to selection changes in the other
     * parts of the UI via addSelectionListener.
     *
     * @param elementSelectionListener
     */
    public static void addSelectionProvider(ISelectionProvider elementSelectionListener) {
        if (!selectionProviders.contains(elementSelectionListener)) {
            selectionProviders.add(elementSelectionListener);
            Iterator<?> selectionListenersIterator = selectionListeners.iterator();
            while (selectionListenersIterator.hasNext()) {
                ISelectionChangedListener selectionChangeListener = (ISelectionChangedListener) selectionListenersIterator.next();
                elementSelectionListener.addSelectionChangedListener(selectionChangeListener);
            }
        }
    }

    public static void removeSelectionProvider(ISelectionProvider elementSelectionListener) {
        if (selectionProviders.contains(elementSelectionListener)) {
            selectionProviders.remove(elementSelectionListener);
            Iterator<?> selectionListenersIterator = selectionListeners.iterator();
            while (selectionListenersIterator.hasNext()) {
                ISelectionChangedListener selectionChangeListener = (ISelectionChangedListener) selectionListenersIterator.next();
                elementSelectionListener.removeSelectionChangedListener(selectionChangeListener);
            }
        }
    }

    public static void addSelectionListener(ISelectionChangedListener selectionChangeListener) {
        if (!selectionListeners.contains(selectionChangeListener)) {
            selectionListeners.add(selectionChangeListener);
        }
        Iterator<?> selectionProvidersIterator = selectionProviders.iterator();
        while (selectionProvidersIterator.hasNext()) {
            ISelectionProvider elementSelectionProvider = (ISelectionProvider) selectionProvidersIterator.next();
            elementSelectionProvider.addSelectionChangedListener(selectionChangeListener);
        }
    }

    public static void removeSelectionListener(ISelectionChangedListener selectionChangeListener) {
        Iterator<?> selectionProvidersIterator = selectionProviders.iterator();
        while (selectionProvidersIterator.hasNext()) {
            ISelectionProvider elementSelectionProvider = (ISelectionProvider) selectionProvidersIterator.next();
            elementSelectionProvider.removeSelectionChangedListener(selectionChangeListener);
        }
        if (selectionListeners.contains(selectionChangeListener)) {
            selectionListeners.remove(selectionChangeListener);
        }
    }

    public static void addSelectionChangeListener(ISelectionChangedListener selectionChangeListener) {
        if (!selectionChangeListeners.contains(selectionChangeListener)) {
            selectionChangeListeners.add(selectionChangeListener);
        }
    }

    public static void removeSelectionChangeListener(ISelectionChangedListener selectionChangeListener) {
        selectionChangeListeners.remove(selectionChangeListener);
    }

    public static void notifySelectionChangeListeners(SelectionChangedEvent event) {
        Iterator<?> selectionChangeListenersIterator = selectionChangeListeners.iterator();
        while (selectionChangeListenersIterator.hasNext()) {
            ISelectionChangedListener selectionChangedListener = (ISelectionChangedListener) selectionChangeListenersIterator.next();
            selectionChangedListener.selectionChanged(event);
        }
    }

    public static int sizeOfSelectionChangeListeners() {
        return selectionChangeListeners.size();
    }

    public static int sizeOfSelectionListeners() {
        return selectionListeners.size();
    }

    public static int sizeOfSelectionProviders() {
        return selectionProviders.size();
    }

    public static List<ISelectionChangedListener> getSelectionChangeListeners() {
        return selectionChangeListeners;
    }

    public static List<ISelectionProvider> getSelectionProviders() {
        return selectionProviders;
    }

    public static List<ISelectionChangedListener> getSelectionListeners() {
        return selectionListeners;
    }
}
