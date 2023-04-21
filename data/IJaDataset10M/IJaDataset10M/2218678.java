package net.sf.dz3.event;

/**
 * This interface should be implemented by objects that allow listeners.
 * 
 * @param <Source> defines the type of the listener. Normally, you would want to use
 * the following construct: {@code interface X extends EventSource<X, xStateSnapshot>}.
 * 
 * @param <Signal> defines the type of the event object that is being broadcast.
 * 
 * @see EventListener
 * 
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org"> Vadim Tkachenko</a> 2001-2009
 */
public interface EventSource<Source, Signal> {

    /**
     * Add the listener.
     * 
     * @param listener Listener to add.
     */
    void addListener(EventListener<Source, Signal> listener);

    /**
     * Remove the listener.
     * 
     * @param listener Listener to remove.
     */
    void removeListener(EventListener<Source, Signal> listener);
}
