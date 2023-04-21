package fr.gfi.gfinet.server.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import fr.gfi.gfinet.server.EmailService;
import fr.gfi.gfinet.server.EmailServiceException;
import fr.gfi.gfinet.server.error.ServiceMessageCode;
import fr.gfi.gfinet.server.util.EmailAddress;

/**
 * Impl�mente EmailService. Regroupe quelques m�thodes permettant l'envoi de mails.
 * 
 * @author Jean DAT
 * @since 24 juil. 07
 */
public class EmailServiceImpl implements EmailService {

    protected static final Log logger = LogFactory.getLog(EmailServiceImpl.class);

    protected MailSender mailSender;

    protected SimpleMailMessage templateMessage;

    protected boolean isModeTest = false;

    /**
    * Default constructor.
    */
    public EmailServiceImpl() {
        if ("test".equals(System.getProperty("gfienv"))) {
            isModeTest = true;
        }
    }

    /**
	 * Send an email with predefined from address.
	 * @param to
	 * @param subject
	 * @param text
	 * @throws EmailServiceException
	 */
    public void sendMessage(String to, String subject, String text) throws EmailServiceException {
        logger.info("Debut de sendMessage. to = " + to + ", subject = " + subject);
        if (!isModeTest) {
            String message = null;
            Object[] messageArguments = { to };
            if (to == null) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            } else if (to.trim().length() == 0) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            }
            String from = EmailAddress.NOREPLY_MAIL.getString();
            messageArguments = new Object[] { from };
            if (from == null) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            } else if (from.trim().length() == 0) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            }
            templateMessage.setFrom(from);
            templateMessage.setSubject(subject);
            templateMessage.setTo(to);
            templateMessage.setText(text);
            SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
            try {
                this.mailSender.send(msg);
            } catch (MailException ex) {
                messageArguments = new Object[] { to, text, from };
                message = ServiceMessageCode.EMS_0002.description(messageArguments);
                throwException(message, ex);
            }
        }
        logger.info("Fin de sendMessage. to = " + to + ", subject = " + subject);
    }

    /** 
	 * Send an email.
	 * @param from
	 * @param to
	 * @param subject
	 * @param text
	 * @throws EmailServiceException
	 */
    public void sendMessage(String from, String to, String subject, String text) throws EmailServiceException {
        logger.info("Debut de sendMessage. from = " + from + ", to = " + to + ", subject = " + subject);
        if (!isModeTest) {
            String message = null;
            Object[] messageArguments = { to };
            if (to == null) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            } else if (to.trim().length() == 0) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            }
            messageArguments = new Object[] { from };
            if (from == null) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            } else if (from.trim().length() == 0) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            }
            templateMessage.setFrom(from);
            templateMessage.setSubject(subject);
            templateMessage.setTo(to);
            templateMessage.setText(text);
            SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
            try {
                this.mailSender.send(msg);
            } catch (MailException ex) {
                messageArguments = new Object[] { to, text, from };
                message = ServiceMessageCode.EMS_0002.description(messageArguments);
                throwException(message, ex);
            }
        }
        logger.info("Fin de sendMessage. from = " + from + ", to = " + to + ", subject = " + subject);
    }

    /**
	 * Send an email with predefined from address.
	 * @param to
	 * @param subject
	 * @param text
	 * @throws EmailServiceException
	 */
    public void sendMessage(String[] to, String subject, String text) throws EmailServiceException {
        logger.info("Debut de sendMessage. to = " + toStringExplicite(to) + ", subject = " + subject + ", text = " + text);
        if (!isModeTest) {
            String message = null;
            Object[] messageArguments = { toStringExplicite(to) };
            if (to == null) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            } else if (to.length == 0) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            }
            String from = EmailAddress.NOREPLY_MAIL.getString();
            messageArguments = new Object[] { from };
            if (from == null) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            } else if (from.trim().length() == 0) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            }
            templateMessage.setFrom(from);
            templateMessage.setSubject(subject);
            templateMessage.setTo(to);
            templateMessage.setText(text);
            SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
            try {
                this.mailSender.send(msg);
            } catch (MailException ex) {
                messageArguments = new Object[] { toStringExplicite(to), text, from };
                message = ServiceMessageCode.EMS_0002.description(messageArguments);
                throwException(message, ex);
            }
        }
        logger.info("Fin de sendMessage. to = " + toStringExplicite(to) + ", subject = " + subject + ", text = " + text);
    }

    /** 
	 * Send an email.
	 * @param from
	 * @param to
	 * @param subject
	 * @param text
	 * @throws EmailServiceException
	 */
    public void sendMessage(String from, String[] to, String subject, String text) throws EmailServiceException {
        logger.info("Debut de sendMessage. from = " + from + ", to = " + toStringExplicite(to) + ", subject = " + subject + ", text = " + text);
        if (!isModeTest) {
            String message = null;
            Object[] messageArguments = { toStringExplicite(to) };
            if (to == null) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            } else if (to.length == 0) {
                message = ServiceMessageCode.EMS_0001.description(messageArguments);
                throwException(message, null);
            }
            messageArguments = new Object[] { from };
            if (from == null) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            } else if (from.trim().length() == 0) {
                message = ServiceMessageCode.EMS_0003.description(messageArguments);
                throwException(message, null);
            }
            templateMessage.setFrom(from);
            templateMessage.setSubject(subject);
            templateMessage.setTo(to);
            templateMessage.setText(text);
            SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
            try {
                this.mailSender.send(msg);
            } catch (MailException ex) {
                messageArguments = new Object[] { toStringExplicite(to), text, from };
                message = ServiceMessageCode.EMS_0002.description(messageArguments);
                throwException(message, ex);
            }
        }
        logger.info("Fin de sendMessage. from = " + from + ", to = " + toStringExplicite(to) + ", subject = " + subject + ", text = " + text);
    }

    /**
	 * Jette une exception avec le message donn� et �venutellement l'exception qui la provoqu�e.
	 * @param message
	 * @param exc
	 * @throws EmailServiceException
	 */
    public void throwException(String message, Throwable exc) throws EmailServiceException {
        if (!isModeTest) {
            if (exc == null) {
                logger.error(message);
            } else {
                logger.error(message, exc);
            }
        }
        throw new EmailServiceException(message, exc);
    }

    /**
	 * Effectue un toString explicite. Affiche la valeur des adresses plutot que le type et la 
	 * r�f�rence retourn� par Object.toString().
	 * @param obj
	 * @return un String.
	 */
    public String toStringExplicite(String[] obj) {
        String values = "";
        for (String str : obj) {
            values += "<" + str + "> ";
        }
        return values;
    }

    /**
	 * set the mail sender.
	 */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
	 * set the template message.
	 * @param templateMessage
	 */
    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
}
