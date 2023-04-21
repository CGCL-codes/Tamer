package net.krecan.watche.plugin.listener;

/**
 * Comon listener interface.
 * @author krecan
 *
 */
public interface WatcheListener {

    /**
	 * Registers the listener. Listener should register itself in this method.
	 */
    public abstract void register();

    /**
	 * Listener should unregister itself in this method.
	 */
    public abstract void unregister();
}
