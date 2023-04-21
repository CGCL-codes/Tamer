package org.restlet.service;

import org.restlet.Context;

/**
 * Generic service associated to a component or an application. The life cycle
 * of a service is tightly related to the one of the associated component or
 * application.<br>
 * <br>
 * If you want to use a specific service, you can always disable it before it is
 * actually started via the {@link #setEnabled(boolean)} method.
 * 
 * @author Jerome Louvel
 */
public abstract class Service {

    /** The context. */
    private volatile Context context;

    /** Indicates if the service has been enabled. */
    private volatile boolean enabled;

    /** Indicates if the service was started. */
    private volatile boolean started;

    /**
     * Constructor. Enables the service by default.
     */
    public Service() {
        this(true);
    }

    /**
     * Constructor.
     * 
     * @param enabled
     *            True if the service has been enabled.
     */
    public Service(boolean enabled) {
        this.context = null;
        this.enabled = enabled;
    }

    /**
     * Create the filter that should be invoked for incoming calls.
     * 
     * @param context
     *            The current context.
     * @return The new filter or null.
     */
    public org.restlet.routing.Filter createInboundFilter(org.restlet.Context context) {
        return null;
    }

    /**
     * Create the filter that should be invoked for outgoing calls.
     * 
     * @param context
     *            The current context.
     * @return The new filter or null.
     * @see Context#getClientDispatcher()
     */
    public org.restlet.routing.Filter createOutboundFilter(org.restlet.Context context) {
        return null;
    }

    /**
     * Returns the context.
     * 
     * @return The context.
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Indicates if the service should be enabled.
     * 
     * @return True if the service should be enabled.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Indicates if the service is started.
     * 
     * @return True if the service is started.
     */
    public boolean isStarted() {
        return this.started;
    }

    /**
     * Indicates if the service is stopped.
     * 
     * @return True if the service is stopped.
     */
    public boolean isStopped() {
        return !this.started;
    }

    /**
     * Sets the context.
     * 
     * @param context
     *            The context.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Indicates if the service should be enabled.
     * 
     * @param enabled
     *            True if the service should be enabled.
     */
    public synchronized void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** Starts the Restlet. */
    public synchronized void start() throws Exception {
        if (isEnabled()) {
            this.started = true;
        }
    }

    /** Stops the Restlet. */
    public synchronized void stop() throws Exception {
        if (isEnabled()) {
            this.started = false;
        }
    }
}
