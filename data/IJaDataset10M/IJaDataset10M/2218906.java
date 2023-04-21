package org.sulweb.infureports.event;

/**
 *
 * @author lucio
 */
public abstract class DateSelectionAdapter implements DateSelectionListener {

    public final void fireEvent(DateSelectionEvent dse) {
        selectionChanged(dse);
    }
}
