package org.jtools.protocol.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jtools.io.serial.BackedOutput;
import org.jtools.protocol.Sender;

public class DelegationClient extends AbstractClient {

    private final Sender<BackedOutput<ByteArrayOutputStream>, ResponseHandler> delegate;

    public DelegationClient(Sender<BackedOutput<ByteArrayOutputStream>, ResponseHandler> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void send(BackedOutput<ByteArrayOutputStream> output, ResponseHandler handle) throws IOException {
        delegate.send(output, handle);
    }
}
