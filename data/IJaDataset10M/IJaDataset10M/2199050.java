package org.coos.publishsubscribe;

import org.coos.messaging.AsyncCallback;
import org.coos.messaging.Consumer;
import org.coos.messaging.Exchange;
import org.coos.messaging.Processor;
import org.coos.messaging.Producer;
import org.coos.messaging.impl.DefaultEndpoint;

public class NotificationEndpoint extends DefaultEndpoint {

    private Consumer consumer;

    public NotificationEndpoint() {
    }

    public NotificationEndpoint(String uri, Processor processor) {
        super(uri, processor);
    }

    public Consumer createConsumer() {
        if (consumer == null) {
            consumer = new NotificationConsumer(this);
        }
        return consumer;
    }

    public Producer createProducer() {
        return null;
    }
}
