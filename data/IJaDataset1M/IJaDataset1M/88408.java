package org.moyoman.comm.client;

import org.moyoman.util.*;

/** This interface is implemented by a class that wishes to receive request move events.*/
public interface RequestMoveListener {

    /** This method is called when a request move event occurs on the player for whom the listener is registered.
	  * @param rme The RequestMovEvent object.
	  */
    public void requestMoveEventOccurred(RequestMoveEvent rme);
}
