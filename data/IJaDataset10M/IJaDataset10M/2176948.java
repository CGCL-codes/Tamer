package csel.controller.decoders;

import java.util.concurrent.LinkedBlockingQueue;
import csel.controller.dispatchers.Dispatcher;
import csel.controller.events.Event;
import csel.model.GameCommand;

/**
 * Purpose: Abstract class for EventDecoder
 * 
 * @author Nikolas Wolfe
 */
public abstract class EventDecoder<T extends Event> implements Runnable {

    private final LinkedBlockingQueue<T> queue;

    private final Dispatcher disp;

    public EventDecoder(Dispatcher disp) {
        this.disp = disp;
        this.queue = new LinkedBlockingQueue<T>();
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                GameCommand gc = parseEvent(queue.take());
                registerCommand(gc);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void registerCommand(GameCommand gc) {
        disp.registerCommand(gc);
    }

    public void registerEvent(T event) throws InterruptedException {
        queue.put(event);
    }

    protected abstract GameCommand parseEvent(Event event);
}
