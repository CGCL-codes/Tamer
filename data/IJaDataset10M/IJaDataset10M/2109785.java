package org.springframework.jms.support;

import java.lang.reflect.Constructor;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.JMSSecurityException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TransactionInProgressException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.JmsSecurityException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.util.ClassUtils;

/**
 * Generic utility methods for working with JMS. Mainly for internal use
 * within the framework, but also useful for custom JMS access code.
 *
 * @author Juergen Hoeller
 * @since 1.1
 */
public abstract class JmsUtils {

    private static final Log logger = LogFactory.getLog(JmsUtils.class);

    /**
	 * Close the given JMS Connection and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JMS code.
	 * @param con the JMS Connection to close
	 */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (JMSException ex) {
                logger.warn("Could not close JMS Connection", ex);
            }
        }
    }

    /**
	 * Close the given JMS Session and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JMS code.
	 * @param session the JMS Session to close
	 */
    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException ex) {
                logger.warn("Could not close JMS Session", ex);
            }
        }
    }

    /**
	 * Close the given JMS MessageProducer and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JMS code.
	 * @param producer the JMS MessageProducer to close
	 */
    public static void closeMessageProducer(MessageProducer producer) {
        if (producer != null) {
            try {
                producer.close();
            } catch (JMSException ex) {
                logger.warn("Could not close JMS MessageProducer", ex);
            }
        }
    }

    /**
	 * Close the given JMS MessageConsumer and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JMS code.
	 * @param consumer the JMS MessageConsumer to close
	 */
    public static void closeMessageConsumer(MessageConsumer consumer) {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (JMSException ex) {
                logger.warn("Could not close JMS MessageConsumer", ex);
            }
        }
    }

    /**
	 * Commit the Session if not within a JTA transaction.
	 * @param session the JMS Session to commit
	 * @throws JMSException if committing failed
	 */
    public static void commitIfNecessary(Session session) throws JMSException {
        try {
            session.commit();
        } catch (TransactionInProgressException ex) {
        } catch (javax.jms.IllegalStateException ex) {
        }
    }

    /**
	 * Rollback the Session if not within a JTA transaction.
	 * @param session the JMS Session to rollback
	 * @throws JMSException if committing failed
	 */
    public static void rollbackIfNecessary(Session session) throws JMSException {
        try {
            session.rollback();
        } catch (TransactionInProgressException ex) {
        } catch (javax.jms.IllegalStateException ex) {
        }
    }

    /**
	 * Convert the specified checked {@link javax.jms.JMSException JMSException} to
	 * a Spring runtime {@link org.springframework.jms.JmsException JmsException}
	 * equivalent.
	 * @param ex the original checked JMSException to convert
	 * @return the Spring runtime JmsException wrapping <code>ex</code>.
	 */
    public static JmsException convertJmsAccessException(JMSException ex) {
        if (ex instanceof JMSSecurityException) {
            return new JmsSecurityException((JMSSecurityException) ex);
        }
        if (JMSException.class.equals(ex.getClass().getSuperclass())) {
            String shortName = ClassUtils.getShortName(ex.getClass().getName());
            String longName = JmsException.class.getPackage().getName() + "." + shortName;
            try {
                Class clazz = Class.forName(longName);
                Constructor ctor = clazz.getConstructor(new Class[] { ex.getClass() });
                Object counterpart = ctor.newInstance(new Object[] { ex });
                return (JmsException) counterpart;
            } catch (Throwable ex2) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Couldn't resolve JmsException class [" + longName + "]", ex2);
                }
                return new UncategorizedJmsException(ex);
            }
        }
        return new UncategorizedJmsException(ex);
    }
}
