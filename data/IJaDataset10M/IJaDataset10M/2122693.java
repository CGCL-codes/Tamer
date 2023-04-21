package org.gwanted.gwt.core.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Implementation of SourcesChangeEvents - controls and manages onChange
 * listeners
 * 
 * @author Joshua Hewitt aka Sposh
 */
public class SourcesChangeDispatcherImpl implements SourcesChangeDispatcher {

    private final Collection changeListeners = new ArrayList();

    public boolean onChangePreview(final GwantedEvent event) {
        boolean allowContinue = true;
        final Iterator changeIterator = this.changeListeners.iterator();
        while (changeIterator.hasNext()) {
            final boolean thisAllowContinue = ((ChangeListener) changeIterator.next()).onChangePreview(event);
            if (!thisAllowContinue) {
                allowContinue = false;
            }
        }
        return allowContinue;
    }

    public boolean onChange(final GwantedEvent event) {
        boolean allowContinue = onChangePreview(event);
        final Iterator changeIterator = this.changeListeners.iterator();
        while (changeIterator.hasNext() && allowContinue) {
            allowContinue = ((ChangeListener) changeIterator.next()).onChange(event);
        }
        return allowContinue;
    }

    public void addChangeListener(final ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    public void removeChangeListener(final ChangeListener listener) {
        this.changeListeners.remove(listener);
    }

    protected Collection getListeners() {
        return this.changeListeners;
    }
}
