package org.gwanted.gwt.widget.grid.client.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.gwanted.gwt.core.client.status.ProgressEvent;

public class SourcesFilterDispatcher implements FilterListener {

    private final Collection listeners = new ArrayList();

    public final boolean onFilterPreview(final PropertyEvent event) {
        boolean allowContinue = true;
        final Iterator filterIterator = listeners.iterator();
        while (filterIterator.hasNext() && allowContinue) {
            allowContinue = ((FilterListener) filterIterator.next()).onFilterPreview(event);
        }
        return allowContinue;
    }

    public final boolean onFilter(final PropertyEvent event) {
        boolean allowContinue = true;
        final Iterator filterIterator = listeners.iterator();
        while (filterIterator.hasNext() && allowContinue) {
            allowContinue = ((FilterListener) filterIterator.next()).onFilter(event);
        }
        return allowContinue;
    }

    public final void addFilterListener(final FilterListener listener) {
        listeners.add(listener);
    }

    public final void removeFilterListener(final FilterListener listener) {
        listeners.remove(listener);
    }
}
