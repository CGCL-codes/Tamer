package org.apache.myfaces.trinidad.event;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesListener;

/**
 * Listener for ExpansionEvents.
 */
public interface RowDisclosureListener extends FacesListener {

    public void processDisclosure(RowDisclosureEvent event) throws AbortProcessingException;
}
