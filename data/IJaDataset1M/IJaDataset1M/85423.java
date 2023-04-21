package evolaris.framework.smsservices.business;

import java.io.Serializable;
import java.util.Locale;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import org.apache.log4j.Logger;

/**
 * <!-- begin-xdoclet-definition -->
 * @ejb.bean name="TimerEventReceiverMd"
 *     acknowledge-mode="Auto-acknowledge"
 *     destination-type="javax.jms.Queue"
 *
 *     transaction-type="Bean"
 *     destination-jndi-name="queue/TimerEvent"
 *     connection-factory-jndi-name="ConnectionFactory"
 *
 *  @ejb.transaction="Supports"
 *  @ejb.resource-ref
 *    res-ref-name="jms/QCF"
 *    res-type="javax.jms.QueueConnectionFactory"
 *    res-auth="Application"
 *    jndi-name="ConnectionFactory"
 *
 *  @jboss.destination-jndi-name name="queue/TimerEvent"
 *
 *  <!-- due to the periodic problems with the timer service I will try to solve the problem with a singleton instance of this bean -->
 *  @jboss.container-configuration name="Singleton Message Driven Bean"
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 **/
public class TimerEventReceiverMdBean implements javax.ejb.MessageDrivenBean, javax.jms.MessageListener {

    private static final Logger LOGGER = Logger.getLogger(TimerEventReceiverMdBean.class);

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * The context for the message-driven bean, set by the EJB container.
	 * @generated
	 */
    private javax.ejb.MessageDrivenContext messageContext = null;

    /**
	 * Required method for container to set context.
	 * @generated
	 */
    public void setMessageDrivenContext(javax.ejb.MessageDrivenContext messageContext) throws javax.ejb.EJBException {
        this.messageContext = messageContext;
    }

    /**
	 * Required creation method for message-driven beans.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @ejb.create-method
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 */
    public void ejbCreate() {
    }

    /**
	 * Required removal method for message-driven beans.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void ejbRemove() {
        messageContext = null;
    }

    /**
	 * This method implements the business logic for the EJB.
	 *
	 * <p>Make sure that the business logic accounts for asynchronous message processing.
	 * For example, it cannot be assumed that the EJB receives messages in the order they were
	 * sent by the client. Instance pooling within the container means that messages are not
	 * received or processed in a sequential order, although individual onMessage() calls to
	 * a given message-driven bean instance are serialized.
	 *
	 * <p>The <code>onMessage()</code> method is required, and must take a single parameter
	 * of type javax.jms.Message. The throws clause (if used) must not include an application
	 * exception. Must not be declared as final or static.
	 *
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void onMessage(javax.jms.Message message) {
        LOGGER.debug("Message Driven Bean invoked");
        try {
            if (!(message instanceof ObjectMessage)) {
                LOGGER.warn("ignoring message of type " + message.getClass().getName());
                return;
            }
            ObjectMessage objectMessage = (ObjectMessage) message;
            Serializable messageObject = objectMessage.getObject();
            if (!(messageObject instanceof Long)) {
                LOGGER.warn("ignoring message object of type " + messageObject.getClass().getName());
                return;
            }
            Long messageTime = (Long) messageObject;
            long currentTime = System.currentTimeMillis();
            if (currentTime - messageTime > 10000) {
                LOGGER.warn("ignored message, because it is too old (current time: " + currentTime + ", message time: " + messageTime + ")");
                return;
            }
            LOGGER.debug("Calling the timer event handler immediately");
            TimerEventHandler timerEventHandler = new TimerEventHandler(Locale.GERMAN);
            timerEventHandler.handleDueEvents();
        } catch (JMSException e) {
            LOGGER.error("JMS exception", e);
        } catch (Throwable t) {
            LOGGER.error("Throwable occured during timer event", t);
        }
    }
}
