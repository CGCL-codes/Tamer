package org.krayne.swivel.filter;

import java.util.EventListener;

/**
 * Provides a simple interface notification of changes in
 * {@code SimpleStringFilter}s.
 * 
 * @author dhsu
 */
public interface SimpleStringFilterListener extends EventListener {

    /**
     * This method is meant to be invoked by the observable when a
     * {@code SimpleStringFilter} changes.
     * 
     * @param newFilter the new filter
     */
    public void filterChanged(SimpleStringFilter newFilter);
}
