package org.simpleframework.http;

import java.io.IOException;
import java.io.OutputStream;
import org.simpleframework.http.store.Storage;

class Monitor {

    private Reactor handler;

    private Storage storage;

    private Channel channel;

    public Monitor(Reactor handler, Storage storage, Channel channel) {
        this.channel = channel;
        this.handler = handler;
        this.storage = storage;
    }

    public void close(Sender sender) throws IOException {
        sender.close();
    }

    public void error(Sender sender) throws IOException {
        sender.close();
    }

    public void ready(Sender sender) throws IOException {
        try {
            sender.flush();
            handler.handle(new StartEvent(channel, storage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
