package edu.ucsd.ncmir.asynchronous_event;

/**
 * The <Code>AbstractAsynchronousEventListener</code> listens for
 * <Code>AsynchronousEvent</code>s. The class that is interested in
 * processing an action event instantiates this object, and the
 * object created with that class is registered and the listener
 * enabled, using the <code>enable()</code> method. When the
 * <code>AsynchronousEvent</code> occurs, the registered object method
 * is invoked.
 *
 * @author Steve Lamont
 * @version 2.0
 * @since Version 2.0
 */
public abstract class AbstractAsynchronousEventListener {

    private String _canonical_name;

    public AbstractAsynchronousEventListener() {
        String[] cracked_event_class_name = AsynchronousEventManager.crackName(this.getClass()).split("\\.");
        this._canonical_name = cracked_event_class_name[cracked_event_class_name.length - 1];
    }

    /**
     * The handler for this listener.  It is called whenever the event
     * is dispatched.  The handler is invoked from within a separate
     * thread.
     * @param event the event which invoked this listener.
     * @param object the object, if any, in the
     * <code>AsynchronousEvent.send()</code> method.  If no object is
     * passed, <code>object</code> will be <code>null</code>.
     */
    public abstract void handler(AsynchronousEvent event, Object object);

    /**
     * @return The canonical name of the class of this event listener. 
     */
    public final String getCanonicalName() {
        return this._canonical_name;
    }

    /**
     * Enables this <code>AbstractAsynchronousEventListener</code> by
     * registering the it with the
     * <code>AsynchronousEventManager</code>.
     */
    public void enable() {
        AsynchronousEventManager.add(this);
    }

    /**
     * Disables this <code>AbstractAsynchronousEventListener</code> by
     * deregistering the it from the
     * <code>AsynchronousEventManager</code>.
     */
    public void disable() {
        AsynchronousEventManager.remove(this);
    }
}
