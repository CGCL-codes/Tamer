package ch.iserver.ace.collaboration.jupiter;

import ch.iserver.ace.util.ParameterValidator;
import ch.iserver.ace.util.ThreadDomain;

/**
 * The default implementation of the SessionFactory interface.
 */
public class SessionFactoryImpl implements SessionFactory {

    /**
	 * The ThreadDomain for the created Sessions.
	 */
    private ThreadDomain threadDomain;

    /**
	 * The UserRegistry of the application.
	 */
    private UserRegistry registry;

    /**
	 * The factory for AcknowledgeStrategy objects.
	 */
    private AcknowledgeStrategyFactory acknowledgeStrategyFactory;

    /**
	 * Sets the thread domain passed to the session implementations.
	 * 
	 * @param threadDomain the new thread domain
	 */
    public void setThreadDomain(ThreadDomain threadDomain) {
        ParameterValidator.notNull("threadDomain", threadDomain);
        this.threadDomain = threadDomain;
    }

    /**
	 * Gets the current thread domain passed to the sessions.
	 * 
	 * @return the thread domain
	 */
    public ThreadDomain getThreadDomain() {
        return threadDomain;
    }

    /**
	 * Sets the user registry used by the factory.
	 * 
	 * @param registry the user registry
	 */
    public void setUserRegistry(UserRegistry registry) {
        this.registry = registry;
    }

    /**
	 * Gets the user registry used by the factory.
	 * 
	 * @return the user registry
	 */
    public UserRegistry getUserRegistry() {
        return registry;
    }

    /**
	 * Gets the factory for AcknowledgeStrategy objects.
	 * 
	 * @return the factory
	 */
    public AcknowledgeStrategyFactory getAcknowledgeStrategyFactory() {
        return acknowledgeStrategyFactory;
    }

    /**
	 * Sets the factory for the AcknowledgeStrategy objects.
	 * 
	 * @param factory the factory
	 */
    public void setAcknowledgeStrategyFactory(AcknowledgeStrategyFactory factory) {
        this.acknowledgeStrategyFactory = factory;
    }

    /**
	 * @see ch.iserver.ace.collaboration.jupiter.SessionFactory#createSession()
	 */
    public ConfigurableSession createSession() {
        SessionImpl session = new SessionImpl();
        session.setAcknowledgeStrategy(getAcknowledgeStrategyFactory().createStrategy());
        session.setThreadDomain(getThreadDomain());
        session.setUserRegistry(getUserRegistry());
        return session;
    }
}
