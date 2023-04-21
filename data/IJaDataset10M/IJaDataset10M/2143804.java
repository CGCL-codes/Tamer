package hambo.messaging;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import hambo.svc.database.*;

/**
 * Provides batch sending of messages.  This is a buffer for sending messages
 * via sms and / or email and "committing" the send as one operation.
 *
 * <p>The basic procedure is: get a <code>SendBuffer</code>, tell it what
 * messages to send, and when ready either tell it to send the batch or throw
 * it away.
 */
public class SendBuffer {

    /** How to send SMS (Todo: and email) */
    private MessageSender sender;

    private static Session sendEmailSession = null;

    private String userid;

    private PaymentAdapter purse;

    private Vector sendQueue;

    /**
     * Global initialization for <code>SendBuffer</code>s.
     */
    public static void init(Properties prop) {
        String smsClient = prop.getProperty("service.messagesender.sms.client");
        String smsTrailer = prop.getProperty("service.messagesender.sms.trailer");
        int smsCharLimit = Integer.parseInt(prop.getProperty("service.messagesender.sms.charlimit", "145"));
        int smsSendLimit = Integer.parseInt(prop.getProperty("service.messagesender.sms.sendlimit", "10"));
    }

    /**
     * Create a SendBuffer using the default {@link MessageSender}.
     * @param userid who sends the SMS's.
     * @param purse who pays (and how).
     */
    public SendBuffer(String userid, PaymentAdapter purse) {
        this(userid, purse, Messaging.getDefaultSender());
        sendQueue = new Vector();
    }

    /**
     * Create a SendBuffer.
     * @param userid who sends the SMS's.
     * @param purse who pays (and how).
     * @param sender how to send the actual SMS's.
     */
    public SendBuffer(String userid, PaymentAdapter purse, MessageSender sender) {
        this.userid = userid;
        this.purse = purse;
        this.sender = sender;
        sendQueue = new Vector();
    }

    public void sendMsgAsSMS(Message msg, String[] nrs, String senderNr, int maxNrOfSMS) {
        StringBuffer message = new StringBuffer();
        try {
            String fromAddress = (msg.getFrom() != null) ? ((InternetAddress) msg.getFrom()[0]).getAddress() : "????";
            String fromName = (msg.getFrom() != null) ? ((InternetAddress) msg.getFrom()[0]).getPersonal() : null;
            if (fromName != null) {
                message.append(fromName).append(' ');
            }
            message.append("<");
            message.append(fromAddress);
            message.append(">/");
        } catch (Exception ex) {
            message.append("??? <???@???>/");
        }
        try {
            message.append((msg.getSubject() != null) ? msg.getSubject() : "???");
        } catch (Exception ex) {
            message.append("???");
        }
        message.append("/");
        message.append(getFirstTextPart(msg));
        int size = message.length();
        try {
            addMessage(new Sms(userid, nrs, senderNr, message.toString(), maxNrOfSMS));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void forwardEmail(Message msg, String[] addresses) {
        if (addresses == null || msg == null) return;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            msg.writeTo(out);
            MimeMessage forwMsg = new MimeMessage(Session.getDefaultInstance(new Properties(), null), new ByteArrayInputStream(out.toByteArray()));
            if (forwMsg.getFrom() == null || forwMsg.getFrom().length == 0) {
                forwMsg.setFrom(new InternetAddress("MAILER-DAEMON@localhost"));
            }
            InternetAddress[] addrs = new InternetAddress[addresses.length];
            for (int i = 0; i < addresses.length; i++) {
                InternetAddress addr = InternetAddress.parse(addresses[i])[0];
                addrs[i] = new InternetAddress(addr.getAddress());
                if (addr.getPersonal() != null && !addr.getPersonal().equals("")) addrs[i].setPersonal(addr.getPersonal(), "iso-8859-1");
            }
            addMessage(new Email(forwMsg, addrs));
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Send the messages qued in this sender.  This is like a commit.
     */
    public void sendMessages() {
        if (sendQueue == null) return;
        for (int i = 0; i < sendQueue.size(); i++) {
            Object obj = sendQueue.elementAt(i);
            if (obj instanceof Email) {
                Email email = (Email) obj;
                try {
                    sender.sendEmail(email.msg, email.addrs);
                } catch (SendFailedException sfex) {
                    sfex.printStackTrace();
                }
            } else if (obj instanceof Sms) {
                Sms sms = (Sms) obj;
                DBConnection con = null;
                try {
                    sender.sendSMS(sms.nrs, (sms.senderNr == null) ? "-" : sms.senderNr, sms.message, "", sms.maxNrOfSMS, purse);
                } catch (SendFailedException sfex) {
                    sfex.getException().printStackTrace();
                } catch (Throwable th) {
                    th.printStackTrace();
                } finally {
                }
            } else {
                System.err.println("Unsupported Message type!");
            }
        }
        sendQueue = null;
    }

    /**
     * Remove the all messages from this queue without sending them.
     */
    public void deleteMessages() {
        sendQueue = null;
    }

    private void addMessage(Object obj) {
        if (sendQueue == null) {
            sendQueue = new Vector();
        }
        sendQueue.add(obj);
    }

    /**
     * Get text content from a message.
     * Todo: I don't think this works as it should.
     * @param part the message or part thereof
     */
    private static String getFirstTextPart(Object part) {
        try {
            if (part instanceof javax.mail.Message) {
                Object content = ((javax.mail.Message) part).getContent();
                if (content instanceof Multipart) {
                    int nrOfParts = ((Multipart) content).getCount();
                    for (int i = 0; i < nrOfParts; i++) {
                        MimeBodyPart bp = (MimeBodyPart) ((Multipart) content).getBodyPart(i);
                        try {
                            Object bpContent = bp.getContent();
                            return getFirstTextPart(bpContent);
                        } catch (Throwable th) {
                            System.err.println("Message had an unknown encoding type");
                            return "?????";
                        }
                    }
                } else {
                    return getFirstTextPart(content);
                }
            } else if (part instanceof String) {
                return (String) part;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "?????";
    }

    private static class Email {

        Message msg = null;

        Address[] addrs = null;

        Email(Message msg, Address[] addrs) {
            this.msg = msg;
            this.addrs = addrs;
        }
    }

    private static class Sms {

        String userId = null;

        String[] nrs = null;

        String senderNr = null;

        String message = null;

        int maxNrOfSMS = 0;

        Sms(String userId, String[] nrs, String senderNr, String message, int maxNrOfSMS) {
            this.userId = userId;
            this.nrs = nrs;
            this.senderNr = senderNr;
            this.message = message;
            this.maxNrOfSMS = maxNrOfSMS;
        }
    }
}
