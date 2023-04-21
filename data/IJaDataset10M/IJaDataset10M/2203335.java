package org.webalerter.business.recipients;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.webalerter.domain.email.Email;
import org.webalerter.domain.recipients.Recipient;
import org.webalerter.domain.target.Target;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parses the recipient configuration file.
 *
 * @version $Revision: 1.1 $
 */
public class RecipientParser {

    private static final Logger log = Logger.getLogger(RecipientParser.class);

    SAXBuilder builder = new SAXBuilder(true);

    public List<Target> loadRecipients() {
        List<Target> targets = new ArrayList<Target>();
        Document document = null;
        try {
            document = builder.build(new File("config/recipients.xml"));
        } catch (JDOMException e) {
            log.info("", e);
        } catch (IOException e) {
            log.info("", e);
        }
        Element root = document.getRootElement();
        List targetsNode = root.getChildren();
        for (Iterator<Element> targetItr = targetsNode.iterator(); targetItr.hasNext(); ) {
            Target target = new Target();
            String emailSubject = null;
            String stylesheet = null;
            Element targetNode = targetItr.next();
            if (targetNode.getChild("url") != null) {
                URL url = null;
                try {
                    url = new URL(targetNode.getChildText("url"));
                } catch (MalformedURLException e) {
                    log.info("", e);
                }
                target.setUrl(url);
            }
            if (targetNode.getChild("email-subject") != null) {
                emailSubject = targetNode.getChildText("email-subject");
            }
            if (targetNode.getChild("stylesheet") != null) {
                stylesheet = targetNode.getChildText("stylesheet");
            }
            if (targetNode.getChild("recipients") != null) {
                List<Recipient> recipients = new ArrayList<Recipient>();
                Element recipientsNode = targetNode.getChild("recipients");
                List recipientNodes = recipientsNode.getChildren("recipient");
                for (Iterator<Element> recipientsItr = recipientNodes.iterator(); recipientsItr.hasNext(); ) {
                    Recipient recipient = new Recipient();
                    Element recipientNode = recipientsItr.next();
                    recipient.setFirstname(recipientNode.getChildText("firstname"));
                    recipient.setLastname(recipientNode.getChildText("lastname"));
                    if (recipientNode.getChild("email-subject") != null) {
                        emailSubject = recipientNode.getChildText("email-subject");
                    }
                    if (recipientNode.getChild("stylesheet") != null) {
                        stylesheet = recipientNode.getChildText("stylesheet");
                    }
                    Email email = new Email();
                    email.setEmailAddress(recipientNode.getChildText("email-address"));
                    email.setSubject(emailSubject);
                    recipient.setEmailDetails(email);
                    recipient.setStylesheet(new File(stylesheet));
                    recipients.add(recipient);
                }
                target.setRecipients(recipients);
            }
            targets.add(target);
        }
        return targets;
    }
}
