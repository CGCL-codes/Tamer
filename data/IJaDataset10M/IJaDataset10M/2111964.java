package com.hs.mail.mailet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.hs.mail.container.config.Config;
import com.hs.mail.imap.user.Alias;
import com.hs.mail.imap.user.User;
import com.hs.mail.smtp.message.Recipient;
import com.hs.mail.smtp.message.SmtpMessage;

/**
 * 
 * @author Won Chul Doh
 * @since Jun 24, 2010
 * 
 */
public class AliasingForwarding extends AbstractMailet {

    static Logger logger = Logger.getLogger(AliasingForwarding.class);

    public boolean accept(Set<Recipient> recipients, SmtpMessage message) {
        return CollectionUtils.isNotEmpty(recipients);
    }

    public void service(Set<Recipient> recipients, SmtpMessage message) throws MessagingException {
        List<Recipient> newRecipients = new ArrayList<Recipient>();
        List<Recipient> errors = new ArrayList<Recipient>();
        for (Iterator<Recipient> it = recipients.iterator(); it.hasNext(); ) {
            Recipient rcpt = it.next();
            User user = getUserManager().getUserByAddress(rcpt.getMailbox());
            if (user != null) {
                if (StringUtils.isNotEmpty(user.getForwardTo())) {
                    try {
                        it.remove();
                        Recipient forwardTo = new Recipient(user.getForwardTo(), false);
                        if (Config.isLocal(forwardTo.getHost())) {
                            long id = getUserManager().getUserID(forwardTo.getMailbox());
                            if (id != 0) {
                                forwardTo.setID(id);
                                newRecipients.add(forwardTo);
                            } else {
                                throw new AddressException("Forwarding address not found.");
                            }
                        } else {
                            message.setNode(SmtpMessage.ALL);
                            message.addRecipient(forwardTo);
                        }
                    } catch (Exception e) {
                        StringBuffer errorBuffer = new StringBuffer(128).append("Failed to forwarding ").append(rcpt.getMailbox()).append(" to ").append(user.getForwardTo());
                        logger.error(errorBuffer.toString());
                        errors.add(rcpt);
                    }
                } else {
                    rcpt.setID(user.getID());
                }
            } else {
                List<Alias> expanded = getUserManager().expandAlias(rcpt.getMailbox());
                it.remove();
                if (CollectionUtils.isNotEmpty(expanded)) {
                    for (Alias alias : expanded) {
                        newRecipients.add(new Recipient((Long) alias.getDeliverTo(), (String) alias.getUserID(), false));
                    }
                } else {
                    StringBuffer errorBuffer = new StringBuffer(64).append("Permanent exception delivering mail (").append(message.getName()).append("): ").append("Account for ").append(rcpt.getMailbox()).append(" not found");
                    logger.error(errorBuffer.toString());
                    errors.add(rcpt);
                }
            }
        }
        if (newRecipients.size() > 0) {
            recipients.addAll(newRecipients);
        }
    }
}
