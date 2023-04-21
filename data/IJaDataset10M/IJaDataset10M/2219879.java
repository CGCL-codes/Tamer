package com.anthonyeden.lib.event;

import java.util.EventListener;

/** Listener for ErrorEvents.

    @author Anthony Eden
*/
public interface ErrorListener extends EventListener {

    /** Invoked when an error occurs.
    
        @param evt The ErrorEvent
    */
    public void error(ErrorEvent evt);
}
