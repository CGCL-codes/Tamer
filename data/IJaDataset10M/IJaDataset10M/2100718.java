package quickfix;

import quickfix.mina.EventHandlingStrategy;
import quickfix.mina.SingleThreadedEventHandlingStrategy;
import quickfix.mina.acceptor.AbstractSocketAcceptor;

/**
 * Accepts connections and uses a single thread to process messages for all
 * sessions.
 */
public class SocketAcceptor extends AbstractSocketAcceptor {

    private Boolean isStarted = Boolean.FALSE;

    private final Object lock = new Object();

    public SocketAcceptor(Application application, MessageStoreFactory messageStoreFactory, SessionSettings settings, LogFactory logFactory, MessageFactory messageFactory) throws ConfigError {
        super(application, messageStoreFactory, settings, logFactory, messageFactory);
    }

    public SocketAcceptor(Application application, MessageStoreFactory messageStoreFactory, SessionSettings settings, MessageFactory messageFactory) throws ConfigError {
        super(application, messageStoreFactory, settings, messageFactory);
    }

    public SocketAcceptor(SessionFactory sessionFactory, SessionSettings settings) throws ConfigError {
        super(settings, sessionFactory);
    }

    private SingleThreadedEventHandlingStrategy eventHandlingStrategy = new SingleThreadedEventHandlingStrategy(this);

    public void block() throws ConfigError, RuntimeError {
        initialize();
        eventHandlingStrategy.block();
    }

    public void start() throws ConfigError, RuntimeError {
        initialize();
        eventHandlingStrategy.blockInThread();
    }

    private void initialize() throws ConfigError {
        synchronized (lock) {
            if (isStarted.equals(Boolean.FALSE)) {
                startAcceptingConnections();
            }
            isStarted = Boolean.TRUE;
        }
    }

    public void stop() {
        stop(false);
    }

    public void stop(boolean forceDisconnect) {
        eventHandlingStrategy.stopHandlingMessages();
        stopAcceptingConnections();
        logoutAllSessions(forceDisconnect);
        stopSessionTimer();
        Session.unregisterSessions(getSessions());
        synchronized (lock) {
            isStarted = Boolean.FALSE;
        }
    }

    @Override
    protected EventHandlingStrategy getEventHandlingStrategy() {
        return eventHandlingStrategy;
    }
}
