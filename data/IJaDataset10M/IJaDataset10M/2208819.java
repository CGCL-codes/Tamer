package com.hs.mail.mailet;

import java.util.HashSet;
import java.util.Set;
import javax.mail.MessagingException;
import org.apache.log4j.Logger;
import com.hs.mail.container.config.Config;
import com.hs.mail.smtp.message.Recipient;
import com.hs.mail.smtp.message.SmtpMessage;

/**
 * Receives a Mail from SmtpMessageConsumer and takes care of delivery of the
 * message to local inboxes.
 * 
 * @author Won Chul Doh
 * @since Jun 7, 2010
 * 
 */
public class LocalDelivery extends AbstractMailet {

    static Logger logger = Logger.getLogger(LocalDelivery.class);

    /**
	 * Mailet that apply aliasing and forwarding
	 */
    private AliasingForwarding aliasingMailet;

    /**
	 * Mailet that actually stores the message
	 */
    private ToRepository deliveryMailet;

    public void init(MailetContext context) {
        super.init(context);
        aliasingMailet = new AliasingForwarding();
        aliasingMailet.init(context);
        deliveryMailet = new ToRepository();
        deliveryMailet.init(context);
    }

    public boolean accept(Set<Recipient> recipients, SmtpMessage message) {
        return message.getNode() == SmtpMessage.LOCAL || message.getNode() == SmtpMessage.ALL;
    }

    public void service(Set<Recipient> recipients, SmtpMessage message) throws MessagingException {
        Set<Recipient> temp = new HashSet<Recipient>();
        for (Recipient recipient : recipients) {
            if (Config.isLocal(recipient.getHost())) {
                temp.add(recipient);
            }
        }
        if (aliasingMailet.accept(temp, message)) {
            aliasingMailet.service(temp, message);
            if (deliveryMailet.accept(temp, message)) {
                deliveryMailet.service(temp, message);
            }
        }
    }
}
