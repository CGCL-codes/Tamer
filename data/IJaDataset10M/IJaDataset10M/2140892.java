package com.lc.util;

import java.util.EventListener;
import java.util.EventObject;

/**
 * @author Laurent Caillette
 * @version $Revision: 1.2 $  $Date: 2002/02/27 14:30:34 $
 */
public abstract class FilteredEventSupport {

    private FilteredEventListenerList entries = new FilteredEventListenerList();

    protected final void add(Class eventClass, EventListener listener, IEventFilter filter) {
        ListenerEntry entry = new ListenerEntry(listener, filter);
        entries.add(eventClass, entry);
    }

    protected final void remove(Class eventClass, EventListener listener, IEventFilter filter) {
        entries.remove(eventClass, new ListenerEntry(listener, filter));
    }

    protected final void broadcast(IEventCaster eventCaster, EventObject event) {
        ListenerEntry[] lsEntries = entries.getListenerEntries(event.getClass());
        for (int i = 0; i < lsEntries.length; i++) {
            ListenerEntry lsEntry = lsEntries[i];
            IEventFilter filter = lsEntry.getFilter();
            if (filter.acceptsEvent(event)) {
                try {
                    eventCaster.castEvent(lsEntry.getListener(), event);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    public static final class ListenerEntry {

        private final EventListener listener;

        private final IEventFilter filter;

        public ListenerEntry(EventListener listener, IEventFilter filter) {
            if (listener == null) {
                throw new NullPointerException("listener");
            }
            if (filter == null) {
                throw new NullPointerException("filter");
            }
            this.listener = listener;
            this.filter = filter;
        }

        public EventListener getListener() {
            return listener;
        }

        public IEventFilter getFilter() {
            return filter;
        }

        /**
   * <tt>equals</tt>-like method testing compatibility between filters.
   */
        public boolean isSameAs(ListenerEntry other) {
            if (other == null) {
                return false;
            }
            return (listener == other.listener) && (filter.includesFilter(other.filter) || other.filter.includesFilter(filter));
        }
    }

    public interface IEventCaster {

        void castEvent(EventListener listener, EventObject event);
    }
}
