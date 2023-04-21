package net.taylor.tracker.email;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import net.taylor.audit.AuditListener;
import net.taylor.audit.entity.AuditEntry;
import org.jboss.seam.annotations.Name;

/**
 * @author jgilbert01
 */
@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), @ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/net.taylor.AuditTopic"), @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = AuditListener.ENTITY_NAME + " LIKE 'net.taylor.tracker.entity.%'") })
@Name("onChangeEmailMDB")
public class OnChangeEmailMDB implements MessageListener {

    public void onMessage(Message msg) {
        try {
            AuditEntry ae = (AuditEntry) ((ObjectMessage) msg).getObject();
            OnChangeEmail.instance().send(ae);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
