package ru.adv.repository.channel.client;

import ru.adv.event.QuestionEvent;
import ru.adv.event.Response;
import ru.adv.logger.TLogger;
import ru.adv.repository.channel.io.ChannelException;
import ru.adv.repository.channel.io.RemoteEventHandler;

/**
 * Реализует интерфейс для вызова методов {@link RemoteEventHandler} клиентом
 * через {@link ChannelClient} User: vic Date: 04.10.2004 Time: 19:29:37
 */
public class RemoteEventHandlerClient implements RemoteEventHandler {

    private Integer _remoteRef;

    private ChannelClient _channel;

    RemoteEventHandlerClient(ChannelClient channel, Integer remoteRef) {
        _channel = channel;
        _remoteRef = remoteRef;
    }

    protected Object invokeMethod(String methodName, Object[] params) throws ChannelException {
        return _channel.invokeRemoteMethod(_remoteRef, methodName, params);
    }

    public boolean hasEvent() throws ChannelException {
        return ((Boolean) invokeMethod("hasEvent", new Object[0])).booleanValue();
    }

    public QuestionEvent getEvent() throws ChannelException {
        return (QuestionEvent) invokeMethod("getEvent", new Object[0]);
    }

    public void setResponse(Response response) throws ChannelException {
        invokeMethod("setResponse", new Object[] { response });
    }

    public void destroy() {
        try {
            if (_channel != null && _channel.isConnected()) {
                _channel.destroyRemoteObject(_remoteRef);
            }
        } catch (ChannelException e) {
            TLogger.warning(RemoteEventHandlerClient.class, e.getMessage());
        }
    }
}
