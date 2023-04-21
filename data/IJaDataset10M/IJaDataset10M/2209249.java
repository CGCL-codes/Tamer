package com.gwtaf.eventbus.shared;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtaf.core.shared.util.VOID;

/**
 * Event services.
 * <p>
 * Used only by {@link com.gwtaf.eventbus.client.EventBus}
 * 
 * @author Matthias Huebner
 * 
 */
public interface IRemoteEventBusRPCAsync {

    void register(IEventType<?> type, AsyncCallback<VOID> asyncRegister);

    void unregister(IEventType<?> type, AsyncCallback<VOID> voidCallback);

    void poll(AsyncCallback<List<IEvent>> asyncCallback);

    void send(IEvent event, AsyncCallback<VOID> voidcallback);
}
