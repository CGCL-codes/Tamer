package org.jtools.protocol.client;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import org.jtools.io.ByteArrayOutputFactory;
import org.jtools.io.serial.BackedOutput;
import org.jtools.protocol.Protocol;
import org.jtools.protocol.Sender;

public abstract class AbstractClient implements Client, RequestFactory, Sender<BackedOutput<ByteArrayOutputStream>, ResponseHandler> {

    private final Map<Class<? extends Protocol<?>>, Short> registeredProtocols = ClientUtils.newStandardProtocols();

    public <R> Request newInstance(Class<? extends Protocol<R>> protocol) {
        Short protocolId = this.registeredProtocols.get(protocol);
        if (protocolId == null) throw new RuntimeException();
        return new ClientRequest<BackedOutput<ByteArrayOutputStream>>(ByteArrayOutputFactory.getInstance(), this, Requests.getInstance().newRequestId(), protocolId);
    }

    @Override
    public RequestFactory getRequestFactory() {
        return this;
    }

    @Override
    public boolean installProtocol(Protocol<?> protocol) {
        Short id = registeredProtocols.get(protocol.getClass());
        if (id == null) {
            if (protocol.getWellKnownProtocolId() != -1) {
                id = protocol.getWellKnownProtocolId();
                registeredProtocols.put((Class<? extends Protocol<?>>) protocol.getClass(), id);
            }
        }
        return id != null;
    }
}
