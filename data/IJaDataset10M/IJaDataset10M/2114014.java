package net.jetrix;

/**
 * A service bound to a port.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 794 $, $Date: 2009-02-17 14:08:39 -0500 (Tue, 17 Feb 2009) $
 */
public interface Listener extends Runnable, Service {

    /**
     * Return the name of the listener.
     */
    String getName();

    /**
     * Return the listening port.
     */
    int getPort();

    /**
     * Set the port used the next time the listener is started.
     */
    void setPort(int port);

    /**
     * Start the listener.
     */
    void start();

    /**
     * Stop the listener.
     */
    void stop();
}
