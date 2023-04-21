package org.jaffa.modules.printing.services;

import java.util.EventListener;
import java.util.EventObject;

/** The listener interface for receiving DocumentPrinted Events.
 * A IDataBean typically creates DocimentPrintedListeners and this are registered
 * with the Form Printing Engine. Once a document has been printed all the 
 * listeners are invoked with the documentPrinted() method.
 *
 * @author PaulE
 */
public interface DocumentPrintedListener extends EventListener {

    /** Invoked when the Document has been printed
     * @param event the EventObject
     */
    public void documentPrinted(EventObject event);
}
