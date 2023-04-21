package mil2525b;

import java.util.concurrent.ThreadFactory;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.LinkedList;
import java.io.IOException;
import java.net.Socket;

/**
 * <p>A robust class for establishing a TCP server and manipulating
 * its listening port.
 * The {@link Event}s and property change events make
 * it an appropriate tool in a threaded, GUI application.
 * It is almost identical in design to the UdpServer class that
 * should have accompanied this class when you downloaded it.</p>
 * 
 * <p>To start a TCP server, create a new TcpServer and call start():</p>
 * 
 * <pre> TcpServer server = new TcpServer();
 * server.start();</pre>
 * 
 * <p>Of course it won't be much help unless you register as a listener
 * so you'll know when a <tt>java.net.Socket</tt> has come in:</p>
 * 
 * <pre> server.addTcpServerListener( new TcpServer.Adapter(){
 *     public void tcpServerSocketReceived( TcpServer.Event evt ){
 *         Socket socket = evt.getSocket();
 *         ...
 *     }   // end socket received
 * });</pre>
 * 
 * <p>The server runs on one thread, and all events are fired on that thread.
 * Consider offloading heavy processing to another thread. Be aware that
 * you can register multiple listeners to respond to an incoming socket,
 * so be mindful of more than one listener being around to makes calls
 * on the new Socket.</p>
 * 
 * <p>The public methods are all synchronized on <tt>this</tt>, and great
 * care has been taken to avoid deadlocks and race conditions. That being said,
 * there may still be bugs (please contact the author if you find any), and
 * you certainly still have the power to introduce these problems yourself.</p>
 * 
 * <p>It's often handy to have your own class extend this one rather than
 * making an instance field to hold a TcpServer where you'd have to
 * pass along all the setPort(...) methods and so forth.</p>
 * 
 * <p>The supporting {@link Event}, {@link Listener}, and {@link Adapter}
 * classes are static inner classes in this file so that you have only one
 * file to copy to your project. You're welcome.</p>
 * 
 * <p>This code is released into the Public Domain.
 * Since this is Public Domain, you don't need to worry about
 * licensing, and you can simply copy this TcpServer.java file
 * to your own package and use it as you like. Enjoy.
 * Please consider leaving the following statement here in this code:</p>
 * 
 * <p><em>This <tt>TcpServer</tt> class was copied to this project from its source as 
 * found at <a href="http://iharder.net" target="_blank">iHarder.net</a>.</em></p>
 *
 * @author Robert Harder
 * @author rharder@users.sourceforge.net
 * @version 0.1
 * @see TcpServer
 * @see Adapter
 * @see Event
 * @see Listener
 */
public class TcpServer {

    private static final Logger LOGGER = Logger.getLogger(TcpServer.class.getName());

    /**
     * The port property <tt>port</tt> used with
     * the property change listeners and the preferences,
     * if a preferences object is given.
     */
    public static final String PORT_PROP = "port";

    private static final int PORT_DEFAULT = 8000;

    private int port = PORT_DEFAULT;

    /**
     * <p>One of four possible states for the server to be in:</p>
     * 
     * <ul>
     *  <li>STARTING</li>
     *  <li>STARTED</li>
     *  <li>STOPPING</li>
     *  <li>STOPPED</li>
     * </ul>
     */
    public static enum State {

        STARTING, STARTED, STOPPING, STOPPED
    }

    ;

    private State currentState = State.STOPPED;

    private Collection<TcpServer.Listener> listeners = new LinkedList<TcpServer.Listener>();

    private TcpServer.Event event = new TcpServer.Event(this);

    private PropertyChangeSupport propSupport = new PropertyChangeSupport(this);

    private TcpServer This = this;

    private ThreadFactory threadFactory;

    private Thread ioThread;

    private ServerSocket tcpServer;

    private Socket socket;

    /**
     * Constructs a new TcpServer that will listen on the default port 8000
     * (but not until {@link #start} is called).
     * The I/O thread will be in daemon mode.
     */
    public TcpServer() {
    }

    /**
     * Constructs a new TcpServer that will listen on the given port 
     * (but not until {@link #start} is called).
     * The I/O thread will be in daemon mode.
     */
    public TcpServer(int port) {
        this.port = port;
    }

    /**
     * Constructs a new TcpServer that will listen on the given port 
     * (but not until {@link #start} is called). The provided
     * ThreadFactory will be used when starting and running the server.
     */
    public TcpServer(int port, ThreadFactory factory) {
        this.port = port;
        this.threadFactory = factory;
    }

    /**
     * Attempts to start the server listening and returns immediately.
     * Listen for start events to know if the server was
     * successfully started.
     * 
     * @see Listener
     */
    public synchronized void start() {
        if (this.currentState == State.STOPPED) {
            assert ioThread == null : ioThread;
            Runnable run = new Runnable() {

                @Override
                public void run() {
                    runServer();
                    ioThread = null;
                    setState(State.STOPPED);
                }
            };
            if (this.threadFactory != null) {
                this.ioThread = this.threadFactory.newThread(run);
            } else {
                this.ioThread = new Thread(run, this.getClass().getName());
                this.ioThread.setDaemon(true);
            }
            setState(State.STARTING);
            this.ioThread.start();
        }
    }

    /**
     * Attempts to stop the server, if the server is in
     * the STARTED state, and returns immediately.
     * Be sure to listen for stop events to know if the server was
     * successfully stopped.
     * 
     * @see Listener
     */
    public synchronized void stop() {
        if (this.currentState == State.STARTED) {
            setState(State.STOPPING);
            if (this.tcpServer != null) {
                try {
                    this.tcpServer.close();
                } catch (IOException exc) {
                    LOGGER.log(Level.SEVERE, "An error occurred while closing the TCP server. " + "This may have left the server in an undefined state.", exc);
                }
            }
        }
    }

    /**
     * Returns the current state of the server, one of
     * STOPPED, STARTING, or STARTED.
     * @return state of the server
     */
    public synchronized State getState() {
        return this.currentState;
    }

    /**
     * Sets the state and fires an event. This method
     * does not change what the server is doing, only
     * what is reflected by the currentState variable.
     */
    protected synchronized void setState(State state) {
        this.currentState = state;
        fireTcpServerStateChanged();
    }

    /**
     * Fires an event declaring the current state of the server.
     * This may encourage lazy programming on your part, but it's
     * handy to set yourself up as a listener and then fire an
     * event in order to initialize this or that.
     */
    public synchronized void fireState() {
        fireTcpServerStateChanged();
    }

    /**
     * Resets the server, if it is running, otherwise does nothing.
     * This is accomplished by registering as a listener, stopping
     * the server, detecting the stop, unregistering, and starting
     * the server again. It's a useful design pattern, and you may
     * want to look at the source code for this method to check it out.
     */
    public synchronized void reset() {
        switch(this.currentState) {
            case STARTED:
                this.addTcpServerListener(new Adapter() {

                    @Override
                    public void tcpServerStateChanged(Event evt) {
                        if (evt.getState() == State.STOPPED) {
                            TcpServer server = (TcpServer) evt.getSource();
                            server.removeTcpServerListener(this);
                            server.start();
                        }
                    }
                });
                stop();
                break;
        }
    }

    /**
     * This method starts up and listens indefinitely
     * for TCP packets. On entering this method,
     * the state is assumed to be STARTING. Upon exiting
     * this method, the state will be STOPPING.
     */
    protected void runServer() {
        try {
            this.tcpServer = new ServerSocket(getPort());
            LOGGER.info("TCP Server established on port " + getPort());
            setState(State.STARTED);
            LOGGER.info("TCP Server listening...");
            while (!this.tcpServer.isClosed()) {
                synchronized (This) {
                    if (this.currentState == State.STOPPING) {
                        LOGGER.info("Stopping TCP Server by request.");
                        this.tcpServer.close();
                    }
                }
                if (!this.tcpServer.isClosed()) {
                    this.socket = this.tcpServer.accept();
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("TCP Server incoming socket: " + socket);
                    }
                    fireTcpServerSocketReceived();
                }
            }
        } catch (Exception exc) {
            synchronized (This) {
                if (this.currentState == State.STOPPING) {
                    try {
                        this.tcpServer.close();
                        LOGGER.info("TCP Server closed normally.");
                    } catch (IOException exc2) {
                        LOGGER.log(Level.SEVERE, "An error occurred while closing the TCP server. " + "This may have left the server in an undefined state.", exc2);
                    }
                } else {
                    LOGGER.log(Level.WARNING, "Server closed unexpectedly: " + exc.getMessage(), exc);
                }
            }
        } finally {
            setState(State.STOPPING);
            if (this.tcpServer != null) {
                try {
                    this.tcpServer.close();
                    LOGGER.info("TCP Server closed normally.");
                } catch (IOException exc2) {
                    LOGGER.log(Level.SEVERE, "An error occurred while closing the TCP server. " + "This may have left the server in an undefined state.", exc2);
                }
            }
            this.tcpServer = null;
        }
    }

    /**
     * Returns the last Socket received.
     */
    public synchronized Socket getSocket() {
        return this.socket;
    }

    /**
     * Returns the port on which the server is or will be listening.
     * @return The port for listening.
     */
    public synchronized int getPort() {
        return this.port;
    }

    /**
     * Sets the new port on which the server will attempt to listen.
     * If the server is already listening, then it will attempt to
     * restart on the new port, generating start and stop events.
     * @param port the new port for listening
     * @throws IllegalArgumentException if port is outside 0..65535
     */
    public synchronized void setPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Cannot set port outside range 0..65535: " + port);
        }
        int oldVal = this.port;
        this.port = port;
        if (getState() == State.STARTED) {
            reset();
        }
        firePropertyChange(PORT_PROP, oldVal, port);
    }

    /** Adds a {@link Listener}. */
    public synchronized void addTcpServerListener(TcpServer.Listener l) {
        listeners.add(l);
    }

    /** Removes a {@link Listener}. */
    public synchronized void removeTcpServerListener(TcpServer.Listener l) {
        listeners.remove(l);
    }

    /** Fires event on calling thread. */
    protected synchronized void fireTcpServerSocketReceived() {
        TcpServer.Listener[] ll = listeners.toArray(new TcpServer.Listener[listeners.size()]);
        for (TcpServer.Listener l : ll) {
            l.tcpServerSocketReceived(this.event);
        }
    }

    /** Fires event on calling thread. */
    protected synchronized void fireTcpServerStateChanged() {
        TcpServer.Listener[] ll = listeners.toArray(new TcpServer.Listener[listeners.size()]);
        for (TcpServer.Listener l : ll) {
            l.tcpServerStateChanged(this.event);
        }
    }

    /**
     * Fires property chagne events for all current values
     * setting the old value to null and new value to the current.
     */
    public synchronized void fireProperties() {
        firePropertyChange(PORT_PROP, null, getPort());
    }

    /**
     * Fire a property change event on the current thread.
     * 
     * @param prop      name of property
     * @param oldVal    old value
     * @param newVal    new value
     */
    protected synchronized void firePropertyChange(final String prop, final Object oldVal, final Object newVal) {
        try {
            propSupport.firePropertyChange(prop, oldVal, newVal);
        } catch (Exception exc) {
            LOGGER.log(Level.WARNING, "A property change listener threw an exception: " + exc.getMessage(), exc);
        }
    }

    /** Add a property listener. */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(listener);
    }

    /** Add a property listener for the named property. */
    public synchronized void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(property, listener);
    }

    /** Remove a property listener. */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propSupport.removePropertyChangeListener(listener);
    }

    /** Remove a property listener for the named property. */
    public synchronized void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propSupport.removePropertyChangeListener(property, listener);
    }

    /**
     * Static method to set the logging level using Java's
     * <tt>java.util.logging</tt> package. Example:
     * <code>TcpServer.setLoggingLevel(Level.OFF);</code>.
     * 
     * @param level the new logging level
     */
    public static void setLoggingLevel(Level level) {
        LOGGER.setLevel(level);
    }

    /**
     * Static method returning the logging level using Java's
     * <tt>java.util.logging</tt> package.
     * @return the logging level
     */
    public static Level getLoggingLevel() {
        return LOGGER.getLevel();
    }

    /**
     * An interface for listening to events from a {@link TcpServer}.
     * A single {@link Event} is shared for all invocations
     * of these methods.
     * 
     * <p>This code is released into the Public Domain.
     * Since this is Public Domain, you don't need to worry about
     * licensing, and you can simply copy this TcpServer.java file
     * to your own package and use it as you like. Enjoy.
     * Please consider leaving the following statement here in this code:</p>
     * 
     * <p><em>This <tt>TcpServer</tt> class was copied to this project from its source as 
     * found at <a href="http://iharder.net" target="_blank">iHarder.net</a>.</em></p>
     *
     * @author Robert Harder
     * @author rharder@users.sourceforge.net
     * @version 0.1
     * @see TcpServer
     * @see Adapter
     * @see Event
     */
    public static interface Listener extends java.util.EventListener {

        /**
         * Called when the state of the server has changed, such as
         * "starting" or "stopped."
         * @param evt the event
         * @see TcpServer.State
         */
        public abstract void tcpServerStateChanged(TcpServer.Event evt);

        /**
         * Called when a packet is received. This is called on the IO thread,
         * so don't take too long, and if you want to offload the processing
         * to another thread, be sure to copy the data out of the datagram
         * since it will be clobbered the next time around.
         * 
         * @param evt the event
         */
        public abstract void tcpServerSocketReceived(TcpServer.Event evt);
    }

    /**
     * A helper class that implements all methods of the
     * {@link TcpServer.Listener} interface with empty methods.
     * 
     * <p>This code is released into the Public Domain.
     * Since this is Public Domain, you don't need to worry about
     * licensing, and you can simply copy this TcpServer.java file
     * to your own package and use it as you like. Enjoy.
     * Please consider leaving the following statement here in this code:</p>
     * 
     * <p><em>This <tt>TcpServer</tt> class was copied to this project from its source as 
     * found at <a href="http://iharder.net" target="_blank">iHarder.net</a>.</em></p>
     *
     * @author Robert Harder
     * @author rharder@users.sourceforge.net
     * @version 0.1
     * @see TcpServer
     * @see Listener
     * @see Event
     */
    public class Adapter implements TcpServer.Listener {

        /**
         * Empty call for {@link TcpServer.Listener#tcpServerStateChanged}.
         * @param evt the event
         */
        public void tcpServerStateChanged(TcpServer.Event evt) {
        }

        /**
         * Empty call for {@link TcpServer.Listener#tcpServerSocketReceived}.
         * @param evt the event
         */
        public void tcpServerSocketReceived(TcpServer.Event evt) {
        }
    }

    /**
     * An event representing activity by a {@link TcpServer}.
     * 
     * <p>This code is released into the Public Domain.
     * Since this is Public Domain, you don't need to worry about
     * licensing, and you can simply copy this TcpServer.java file
     * to your own package and use it as you like. Enjoy.
     * Please consider leaving the following statement here in this code:</p>
     * 
     * <p><em>This <tt>TcpServer</tt> class was copied to this project from its source as 
     * found at <a href="http://iharder.net" target="_blank">iHarder.net</a>.</em></p>
     *
     * @author Robert Harder
     * @author rharder@users.sourceforge.net
     * @version 0.1
     * @see TcpServer
     * @see Adapter
     * @see Listener
     */
    public static class Event extends java.util.EventObject {

        /**
         * Creates a Event based on the given {@link TcpServer}.
         * @param src the source of the event
         */
        public Event(TcpServer src) {
            super(src);
        }

        /**
         * Returns the source of the event, a {@link TcpServer}.
         * Shorthand for <tt>(TcpServer)getSource()</tt>.
         * @return the server
         */
        public TcpServer getTcpServer() {
            return (TcpServer) getSource();
        }

        /**
         * Shorthand for <tt>getTcpServer().getState()</tt>.
         * @return the state of the server
         * @see TcpServer.State
         */
        public TcpServer.State getState() {
            return getTcpServer().getState();
        }

        /**
         * Returns the most recent datagram packet received
         * by the {@link TcpServer}. Shorthand for
         * <tt>getTcpServer().getPacket()</tt>.
         * @return the most recent datagram
         */
        public Socket getSocket() {
            return getTcpServer().getSocket();
        }
    }
}
