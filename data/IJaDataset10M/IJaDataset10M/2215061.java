package com.yands;

import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;

public class MarketingMessage implements IMarketingMessage {

    private Message internalMessage;

    private UIDFolder uidFolder;

    private Session session;

    private Transport transport;

    private static Logger log = Logger.getLogger(MarketingMessage.class);

    public MarketingMessage() {
    }

    public MarketingMessage(Message internalMessage) {
        setInternalMessage(internalMessage);
    }

    public MarketingMessage(Message internalMessage, Transport transport) {
        setTransport(transport);
        setInternalMessage(internalMessage);
    }

    private void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Message getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(Message internalMessage) {
        this.internalMessage = internalMessage;
        log.trace("new message declared");
        synchronized (this.internalMessage) {
            Folder folder = internalMessage.getFolder();
            if (folder instanceof UIDFolder) {
                uidFolder = (UIDFolder) folder;
                log.trace("message is from UID folder");
            }
        }
    }

    public long getUID() throws MessagingException {
        synchronized (this.internalMessage) {
            try {
                if (this.uidFolder != null) return uidFolder.getUID(internalMessage); else return -1;
            } catch (MessagingException e) {
                log.error(e);
                e.printStackTrace();
                throw e;
            }
        }
    }

    public void forward(String destination) throws MessagingException {
        log.info("forwarding message to <" + destination + ">");
        try {
            Address[] recipients = { new InternetAddress(destination) };
            transport.sendMessage(copyMessage(), recipients);
        } catch (MessagingException e) {
            log.error(e);
            e.printStackTrace();
            throw e;
        } catch (SecurityException e) {
            log.error(e);
            e.printStackTrace();
            throw e;
        }
    }

    public Message copyMessage() throws MessagingException {
        return copyMessage(internalMessage);
    }

    public Message copyMessage(Message sourceMessage) throws MessagingException {
        try {
            Message forward = new MimeMessage(session);
            forward.setSubject(sourceMessage.getSubject());
            forward.setFrom(sourceMessage.getFrom()[0]);
            BodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(sourceMessage.getDataHandler());
            multipart.addBodyPart(messageBodyPart);
            forward.setContent(multipart);
            return forward;
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error(e);
            throw e;
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
