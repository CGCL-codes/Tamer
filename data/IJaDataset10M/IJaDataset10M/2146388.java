package org.jactr.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import javolution.util.FastList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.concurrent.ExecutorServices;
import org.jactr.core.utils.collections.CachedCollection;

/**
 * class that handles the nitty gritty of tracking listeners, executors, and
 * propogating that the firing events correctly
 * 
 * @author developer
 */
public class ACTREventDispatcher<S, L> {

    /**
   * logger definition
   */
    public static final Log LOGGER = LogFactory.getLog(ACTREventDispatcher.class);

    private FastList<Pair> _actualListeners;

    public ACTREventDispatcher() {
    }

    public synchronized void clear() {
        if (_actualListeners != null) {
            FastList.recycle(_actualListeners);
            _actualListeners = null;
        }
    }

    public synchronized void addListener(L listener, Executor executor) {
        if (listener == null) throw new IllegalArgumentException("Listener must not be null");
        if (executor == null) executor = ExecutorServices.INLINE_EXECUTOR;
        Pair p = new Pair(listener, executor);
        if (_actualListeners == null) _actualListeners = FastList.newInstance();
        _actualListeners.add(p);
    }

    public synchronized void removeListener(L listener) {
        if (_actualListeners == null) return;
        ListIterator<Pair> itr = _actualListeners.listIterator();
        while (itr.hasNext()) {
            Pair pair = itr.next();
            if (pair.hasListener(listener)) itr.remove();
        }
        if (_actualListeners.size() == 0) {
            FastList.recycle(_actualListeners);
            _actualListeners = null;
        }
    }

    public synchronized boolean hasListeners() {
        return _actualListeners != null && _actualListeners.size() != 0;
    }

    public void fire(IACTREvent<S, L> event) {
        FastList<Pair> container = null;
        synchronized (this) {
            if (_actualListeners == null) return;
            container = FastList.newInstance();
            container.addAll(_actualListeners);
        }
        for (Pair pair : container) pair.fire(event);
        FastList.recycle(container);
    }

    private class Pair {

        private Executor _executor;

        private L _listener;

        public Pair(L listener, Executor executor) {
            _executor = executor;
            _listener = listener;
        }

        public boolean hasListener(L listener) {
            return _listener.equals(listener);
        }

        public void fire(final IACTREvent<S, L> event) {
            Runnable runner = new Runnable() {

                public void run() {
                    try {
                        event.fire(_listener);
                    } catch (Exception e) {
                        LOGGER.error("Uncaught exception during event firing of " + event + " to " + _listener, e);
                    }
                }
            };
            try {
                _executor.execute(runner);
            } catch (RejectedExecutionException ree) {
                if (LOGGER.isWarnEnabled()) LOGGER.warn(_executor + " rejected firing of event ");
            }
        }
    }

    /**
   * @return
   */
    public synchronized Collection<L> getListeners() {
        if (_actualListeners == null) return Collections.EMPTY_LIST;
        FastList<L> listeners = FastList.newInstance();
        for (Pair pair : _actualListeners) listeners.add(pair._listener);
        return listeners;
    }
}
