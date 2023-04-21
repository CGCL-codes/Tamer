package org.gyx.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class MailSender {

    public static void SendMessage(String emailto, String emailfrom, String smtphost, String smtpuser, String smtppwd, String msgSubject, String msgText, String sSpecific, String mimeType) throws Exception {
        if (sSpecific != null) SendMessageSpecific(emailto, emailfrom, smtphost, smtpuser, smtppwd, msgSubject, msgText, sSpecific, mimeType); else SendMessageNormal(emailto, emailfrom, smtphost, smtpuser, smtppwd, msgSubject, msgText, mimeType);
    }

    public static void SendMessage(Vector emailto, String emailfrom, String smtphost, String smtpuser, String smtppwd, String msgSubject, String msgText, String sSpecific, String mimeType) throws Exception {
        if (sSpecific != null) SendMessageSpecific(emailto, emailfrom, smtphost, smtpuser, smtppwd, msgSubject, msgText, sSpecific, mimeType); else SendMessageNormal(emailto, emailfrom, smtphost, smtpuser, smtppwd, msgSubject, msgText, mimeType);
    }

    private static void SendMessageNormal(Vector emailto, String emailfrom, String smtphost, String smtpuser, String smtppwd, String msgSubject, String msgText, String mimeType) throws Exception {
        try {
            final String username = smtpuser;
            final String password = smtppwd;
            class SMTPAuthenticator extends javax.mail.Authenticator {

                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            }
            Authenticator auth = null;
            Properties props = new Properties();
            props.put("mail.smtp.host", smtphost);
            if (!username.equals("none")) {
                props.put("mail.smtp.auth", "true");
                auth = new SMTPAuthenticator();
            }
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(false);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(emailfrom));
            InternetAddress[] address = new InternetAddress[emailto.size()];
            if (emailto.size() == 0) throw new Exception("Error during sending emails : no receiver defined !");
            String strEmail;
            for (int i = 0; i < emailto.size(); i++) {
                strEmail = (String) emailto.elementAt(i);
                address[i] = new InternetAddress(strEmail);
                checkEmail(address[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(MimeUtility.encodeText(msgSubject, "ISO-8859-1", "Q"));
            msg.setContent(msgText, mimeType);
            send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            throw new NoClassDefFoundError("Error during sending emails : receiver emails not well defined !");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SendMessageNormal(String emailto, String emailfrom, String smtphost, String smtpuser, String smtppwd, String msgSubject, String msgText, String mimeType) throws Exception {
        checkEmail(emailto);
        Vector vEmails = new Vector();
        vEmails.add(emailto);
        SendMessageNormal(vEmails, emailfrom, smtphost, smtpuser, smtppwd, msgSubject, msgText, mimeType);
    }

    private static void SendMessageSpecific(Vector emailto, String emailfrom, String smtphost, String smtpuser, String smtppwd, String msgSubject, String msgText, String sSpecific, String mimeType) throws Exception {
        final String username = smtpuser;
        final String password = smtppwd;
        class SMTPAuthenticator extends javax.mail.Authenticator {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        }
        Authenticator auth = null;
        Properties props = new Properties();
        props.put("mail.smtp.host", smtphost);
        if (!username.equals("none")) {
            props.put("mail.smtp.auth", "true");
            auth = new SMTPAuthenticator();
        }
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(true);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailfrom));
        InternetAddress[] address = new InternetAddress[1];
        address[0] = new InternetAddress(sSpecific);
        String strEmail;
        String strDestinataires = "";
        for (int i = 0; i < emailto.size(); i++) {
            strEmail = (String) emailto.elementAt(i);
            checkEmail(new InternetAddress(strEmail));
            strDestinataires += "TO: " + strEmail + "<br>";
        }
        String strSeparateur = "<hr size=1>";
        msgText = msgText + strDestinataires + strSeparateur;
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(MimeUtility.encodeText(msgSubject, "ISO-8859-1", "Q"));
        msg.setContent(msgText, mimeType);
        send(msg);
    }

    private static void send(Message msg) throws Exception {
        final Message leMsg = msg;
        Thread senderThread = new Thread() {

            public void run() {
                try {
                    Transport.send(leMsg);
                } catch (Exception ex) {
                    System.out.println("Error while sending mail: " + ex);
                }
            }
        };
        senderThread.start();
    }

    private static void SendMessageSpecific(String emailto, String emailfrom, String smtphost, String smtpuser, String smtppwd, String msgSubject, String msgText, String sSpecific, String mimeType) throws Exception {
        checkEmail(emailto);
        Vector vEmails = new Vector();
        vEmails.add(emailto);
        SendMessageSpecific(vEmails, emailfrom, smtphost, smtpuser, smtppwd, msgSubject, msgText, sSpecific, mimeType);
    }

    private static void checkEmail(String emailAddress) throws Exception {
        if (emailAddress == null) throw new Exception("MailSender : email address 'null' ! " + emailAddress);
        if (emailAddress.trim().equals("")) throw new Exception("MailSender :email address : 'empty string' !");
        if (emailAddress.indexOf("@") == -1) throw new Exception("MailSender : email address  invalid ! " + emailAddress);
    }

    private static void checkEmail(Address aAddress) throws Exception {
        String emailAddress = ((InternetAddress) aAddress).getAddress();
        checkEmail(emailAddress);
    }
}
