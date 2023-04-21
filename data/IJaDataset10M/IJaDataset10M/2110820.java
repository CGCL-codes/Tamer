package edu.asu.commons.net;

import edu.asu.commons.event.AbstractEvent;

/**
 * $Id: ClientReadyEvent.java 1 2008-07-23 22:15:18Z alllee $
 * 
 * This Event signifies that the ForagerClient is ready and willing to
 * join the game whenever the next one starts.  
 * 
 * @author Allen Lee
 * @version $Revision: 1 $
 */
public class ClientReadyEvent extends AbstractEvent {

    private static final long serialVersionUID = -3226895587981309363L;

    public ClientReadyEvent(Identifier id) {
        super(id);
    }
}
