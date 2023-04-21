package org.sulweb.infureports.event;

import org.sulweb.infureports.BarSelectionListener;

/**
 *
 * @author lucio
 */
public abstract class BarSelectionAdapter implements BarSelectionListener {

    public void fireEvent(BarSelectionEvent bse) {
        selectionChanged(bse.getSelection1(), bse.getSelection2());
    }
}
