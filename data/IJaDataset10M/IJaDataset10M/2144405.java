package com.xsm.gwt.remoteevent.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.xsm.lite.event.RemoteRequestEvent;
import com.xsm.lite.event.RemoteResponseEvent;

/**
 * A proxy for processing remote events.
 * 
 * @author Sony Mathew
 */
public interface RemoteEventProxy extends RemoteService {

    /**
     * Process the given event and return the response.
     * 
     * author Sony Mathew
     */
    public RemoteResponseEvent process(RemoteRequestEvent e);
}
