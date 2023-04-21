package org.sulweb.mvc;

import java.util.*;

/**
 * This class implements basic listeners list operations. It is thread safe.
 * 
 */
public class GenericListenersList<T, L extends GenericListener<T>> {

    private List<L> listeners;

    private boolean firing;

    private List<PendingOp<L>> pendingAddsAndRemoves;

    private PendingOp<L> clearOp;

    public GenericListenersList() {
        listeners = new ArrayList<L>();
        pendingAddsAndRemoves = new LinkedList<PendingOp<L>>();
        clearOp = new PendingOp<L>();
    }

    public void addListener(L listener) {
        synchronized (listeners) {
            if (firing) {
                PendingOp<L> op = new PendingOp<L>();
                op.add = true;
                op.listener = listener;
                pendingAddsAndRemoves.add(op);
            } else if (!listeners.contains(listener)) listeners.add(listener);
        }
    }

    public void removeListener(L listener) {
        synchronized (listeners) {
            if (firing) {
                PendingOp<L> op = new PendingOp<L>();
                op.add = false;
                op.listener = listener;
                pendingAddsAndRemoves.add(op);
            } else {
                List<L> toRemove = new LinkedList<L>();
                for (L reggedListener : listeners) if (listener.equals(reggedListener)) toRemove.add(reggedListener);
                listeners.removeAll(toRemove);
            }
        }
    }

    public void clear() {
        synchronized (listeners) {
            if (firing) pendingAddsAndRemoves.add(clearOp); else listeners.clear();
        }
    }

    public void fireEvent(T event) {
        synchronized (listeners) {
            firing = true;
            try {
                for (L listener : listeners) listener.fireEvent(event);
            } finally {
                firing = false;
                processPendingOperations();
            }
        }
    }

    private void processPendingOperations() {
        for (PendingOp<L> op : pendingAddsAndRemoves) {
            if (op.add) addListener(op.listener); else if (op != clearOp) removeListener(op.listener); else clear();
        }
        pendingAddsAndRemoves.clear();
    }

    private static class PendingOp<L> {

        public boolean add;

        public L listener;
    }
}
