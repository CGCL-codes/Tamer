package org.gwanted.gwt.core.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Implementation of SourcesAlternativeEvents - controls and manages
 * onAlternative listeners
 *
 * @author Joshua Hewitt aka Sposh
 */
public class SourcesUpDownDispatcherImpl implements SourcesUpDownDispatcher {

    private final Collection listeners = new ArrayList();

    public boolean onUpPreview(final GwantedEvent event) {
        boolean allowContinue = true;
        final Iterator altIterator = this.listeners.iterator();
        while (altIterator.hasNext()) {
            final boolean thisAllowContinue = ((UpDownListener) altIterator.next()).onUpPreview(event);
            if (!thisAllowContinue) {
                allowContinue = false;
            }
        }
        return allowContinue;
    }

    public boolean onDownPreview(final GwantedEvent event) {
        boolean allowContinue = true;
        final Iterator altIterator = this.listeners.iterator();
        while (altIterator.hasNext()) {
            final boolean thisAllowContinue = ((UpDownListener) altIterator.next()).onDownPreview(event);
            if (!thisAllowContinue) {
                allowContinue = false;
            }
        }
        return allowContinue;
    }

    public boolean onUp(final GwantedEvent event) {
        boolean allowContinue = onUpPreview(event);
        final Iterator altIterator = this.listeners.iterator();
        while (altIterator.hasNext() && allowContinue) {
            allowContinue = ((UpDownListener) altIterator.next()).onUp(event);
        }
        return allowContinue;
    }

    public boolean onDown(final GwantedEvent event) {
        boolean allowContinue = onDownPreview(event);
        final Iterator altIterator = this.listeners.iterator();
        while (altIterator.hasNext() && allowContinue) {
            allowContinue = ((UpDownListener) altIterator.next()).onDown(event);
        }
        return allowContinue;
    }

    public void addUpDownListener(final UpDownListener listener) {
        this.listeners.add(listener);
    }

    public void removeUpDownListener(final UpDownListener listener) {
        this.listeners.remove(listener);
    }

    protected Collection getListeners() {
        return this.listeners;
    }
}
