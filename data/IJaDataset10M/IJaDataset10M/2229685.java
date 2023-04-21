package mecca.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MailSender {

    public MailSender() {
    }

    public void send(String strToAddress, String strFromAddress, String strCcAddress, String strBccAddress, String strMailHost, String strSubject, String strMessage) throws Exception {
        boolean debug = true;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", strMailHost);
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);
        if (strCcAddress.trim().equals("")) {
            strCcAddress = null;
        }
        if (strBccAddress.trim().equals("")) {
            strBccAddress = null;
        }
        if (strSubject.trim().equals("")) {
            strSubject = null;
        }
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strFromAddress));
            InternetAddress[] toaddress = { new InternetAddress(strToAddress) };
            msg.setRecipients(Message.RecipientType.TO, toaddress);
            if (strCcAddress != null) {
                InternetAddress[] ccaddress = { new InternetAddress(strCcAddress) };
                msg.setRecipients(Message.RecipientType.CC, ccaddress);
            }
            if (strBccAddress != null) {
                InternetAddress[] bccaddress = { new InternetAddress(strBccAddress) };
                msg.setRecipients(Message.RecipientType.BCC, bccaddress);
            }
            if (strSubject != null) {
                msg.setSubject(strSubject);
            }
            msg.setHeader("MailSender", "Anonymous Mail Sender, (c)United Multimedia Sdn Bhd");
            msg.setText(strMessage);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
