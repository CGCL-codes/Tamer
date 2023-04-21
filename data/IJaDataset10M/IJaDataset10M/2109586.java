package com.softaspects.jsf.component.table.listener;

import com.softaspects.jsf.component.table.event.TableDeleteRowEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.AbortProcessingException;

public interface TableDeleteRowListener extends WGFListener {

    /**
     * Invoked when user calls method to delete row
     *
     * @param event
     * @throws javax.faces.event.AbortProcessingException
     *
     */
    public void processTableDeleteRow(TableDeleteRowEvent event) throws AbortProcessingException;
}
