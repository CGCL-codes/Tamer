package net.sf.shineframework.common.tx.context;

import org.apache.log4j.Logger;

/**
 * Thread Local Storage (tls) user context factory. This user context factory
 * stores the user context on the TLS. This type of factory is usefule usually
 * when running outside an application server.
 * 
 * @author amirk
 * 
 */
public abstract class TlsUserContextFactory implements UserContextFactory {

    /**
	 * a private implementation of a ThreadLocal that assigns an initial value
	 * 
	 * @author amirk
	 * 
	 */
    protected static final class UserContextThreadLocal extends ThreadLocal<UserContext> {

        private String classname;

        private Object messageDefProvider;

        public UserContext createUserContext() {
            try {
                BaseUserContext uc = (BaseUserContext) Class.forName(classname).newInstance();
                uc.setMessageDefProvider(messageDefProvider);
                return uc;
            } catch (Exception e) {
                logger.error("unable to instantiate UserContext for class: " + classname + ". reason: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        @Override
        public UserContext get() {
            UserContext result = super.get();
            if (result == null) {
                result = createUserContext();
            }
            return result;
        }

        public String getClassname() {
            return classname;
        }

        public Object getMessageDefProvider() {
            return messageDefProvider;
        }

        @Override
        protected UserContext initialValue() {
            return createUserContext();
        }

        public void setMessageDefProvider(Object messageDefProvider) {
            this.messageDefProvider = messageDefProvider;
        }

        public void setUserContextThreadLocal(String classname) {
            this.classname = classname;
        }
    }

    private static Logger logger = Logger.getLogger(TlsUserContextFactory.class.getName());

    protected static UserContextThreadLocal storage = new UserContextThreadLocal();

    /**
	 * constructs a user context factory. This constructor is here to make the
	 * user (which is usually a Spring bean definition) to pass the classname of
	 * the user context. This is usually a StandardUserContext.
	 * 
	 * @param userContextClassname
	 *            user context classname
	 * @see StandardUserContext
	 */
    public TlsUserContextFactory(String userContextClassname) {
        storage.setUserContextThreadLocal(userContextClassname);
    }

    /**
	 * clears the user context
	 */
    public void clearUserContext() {
        storage.remove();
    }

    /**
	 * retrieves the user context from the TLS
	 * 
	 * @return the current user context
	 */
    public UserContext getUserContext() {
        return storage.get();
    }
}
