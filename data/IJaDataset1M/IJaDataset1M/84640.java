package com.monad.homerun.log;

import java.util.Observer;
import java.util.Properties;
import java.util.logging.Handler;

/**
 * LogHandler is the interface that all logging handlers - the ultimate
 * consumers of logging data - must implement. Implementations of this
 * interface are usually containers for a java.util.logging.Handler object,
 * exposing methods to initialize, shutdown, and perform scheduled service
 * on the underlying Handler
 */
public interface LogHandler {

    /**
     * Initializes the log handler
     * @param props handler properties
     */
    public void init(Properties props);

    /**
     * Applies a management policy to the handler
     * @param props management properties
     */
    public void setPolicy(Properties props);

    /**
     * Adds a log observer
     * @param observer the observer
     */
    public void addObserver(Observer observer);

    /**
     * Retrieves the log handler object
     * @return a log handler
     */
    public Handler getHandler();

    /**
     * Shuts down the log handler
     */
    public void shutdown();
}
