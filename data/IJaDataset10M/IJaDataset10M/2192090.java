package pl.org.minions.stigma.server;

/**
 * Observer for {@link Server} class.
 */
public interface ServerObserver {

    /**
     * Will be fired after state of {@link Server} changed.
     * @see Server#isBooting()
     * @see Server#isShuttingDown()
     * @see Server#isStopped()
     * @see Server#isWorking()
     */
    void stateChanged();
}
