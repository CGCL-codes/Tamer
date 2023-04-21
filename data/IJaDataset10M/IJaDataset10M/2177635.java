package org.paraj.prodcons;

import java.util.Collection;

public interface Engine<T> {

    Engine<T> addConsumer(Consumer<T> consumer);

    Engine<T> addProducer(Producer<T> producer);

    Engine<T> start();

    Collection<T> killEngine() throws InterruptedException;

    void stopGracefully() throws InterruptedException;

    long pendingSize();

    Collection<ProductError<T>> getErrors();
}
