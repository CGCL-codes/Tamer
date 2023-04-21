package org.pz.sal;

import java.util.concurrent.*;

/**
 * Implementation of an actor (abstract class)
 *
 * @author alb
 */
public abstract class Actor implements IActor, Runnable {

    LinkedBlockingQueue<Object> queue;

    private String name;

    private ActorRegistry registry;

    boolean running = true;

    /**
     * Creates the data structures and adds the actor to the registry; then starts the actor.
     *
     * @param assignedName actor's name
     */
    public Actor(final String assignedName) {
        assert (assignedName != null);
        queue = new LinkedBlockingQueue<Object>();
        name = assignedName;
    }

    public void run() {
        Object msg;
        while (running) {
            try {
                msg = queue.take();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                continue;
            }
            if (isShutdown(msg)) {
                return;
            }
            processMessage(msg);
        }
    }

    /**
     * This method should be implemented by each instantiated actor
     *
     * @param msg The object passed to the actor and retrieved from its incoming queue
     */
    public void processMessage(final Object msg) {
    }

    /**
     * Is the current message being processed a shutdown message?
     *
     * @param msg current message
     * @return true, if it's a shutdown message; false, otherwise
     */
    private boolean isShutdown(Object msg) {
        assert (msg != null);
        if (msg.equals(Commands.SHUTDOWN)) {
            return (true);
        }
        return (false);
    }

    /**
     * Shuts down this actor by placing a shutdown message into its queue
     * (thereby assuring that all previous messages will be processed correctly,
     * but all subsequent ones ignored).
     *
     * It also removes the actor from the registry.
     *
     */
    public void shutdown() {
        registry.removeActor(name);
        queue.add(Commands.SHUTDOWN);
    }

    public String getName() {
        return (name);
    }

    public LinkedBlockingQueue<Object> getQueue() {
        return (queue);
    }

    public void setRegistry(final ActorRegistry reg) {
        registry = reg;
    }
}
