package doanlythuyet_javaoutlook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MyMail {

    private String smtp_host = "smtp.gmail.com";

    private String pop3_host = "pop.gmail.com";

    private String user = "blueskyairlines05@gmail.com";

    private String pass = "987412365";

    private String proxy = "";

    private String port = "";

    /**
     * @return the smtp_host
     */
    public String getSmtp_host() {
        return smtp_host;
    }

    /**
     * @param smtp_host the smtp_host to set
     */
    public void setSmtp_host(String smtp_host) {
        this.smtp_host = smtp_host;
    }

    /**
     * @return the pop3_host
     */
    public String getPop3_host() {
        return pop3_host;
    }

    /**
     * @param pop3_host the pop3_host to set
     */
    public void setPop3_host(String pop3_host) {
        this.pop3_host = pop3_host;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    public MyMail(String str_host, String str_pophost, String str_user, String str_pass, String str_proxy, String str_port) {
        smtp_host = str_host;
        pop3_host = str_pophost;
        user = str_user;
        pass = str_pass;
        proxy = str_proxy;
        port = str_port;
    }

    public Session getMailSession(boolean pophost) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        if (!proxy.equals("") && !proxy.equals("0")) {
            Properties p = System.getProperties();
            p.setProperty("proxySet", "true");
            p.setProperty("socksProxyHost", proxy);
            p.setProperty("socksProxyPort", port);
        }
        if (pophost) props.put("mail.smtp.host", getPop3_host()); else props.put("mail.smtp.host", getSmtp_host());
        props.put("mail.smtp.user", getUser());
        props.put("mail.smtp.password", getPass());
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        return Session.getInstance(props, null);
    }

    public Message[] layCacMessageTrongInbox(Session session) throws NoSuchProviderException, MessagingException, IOException {
        Folder folder;
        Store store = session.getStore("pop3s");
        store.connect(getPop3_host(), getUser(), getPass());
        folder = store.getFolder("Inbox");
        folder.open(Folder.READ_ONLY);
        Message Kq[] = folder.getMessages();
        folder.close(false);
        store.close();
        return Kq;
    }

    public void sendMail(String str_Subject, String str_ToAdd, String str_Content, String[] sAr_CCAdd, String[] sAr_BCCAdd, Boolean b_IsSendText) throws AddressException, MessagingException, Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", getSmtp_host());
        props.put("mail.smtp.user", getUser());
        props.put("mail.smtp.password", getPass());
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(getUser()));
        InternetAddress toAddress = new InternetAddress(str_ToAdd);
        message.addRecipient(Message.RecipientType.TO, toAddress);
        InternetAddress[] CCAddress = new InternetAddress[sAr_CCAdd.length];
        for (int i = 0; i < CCAddress.length; i++) {
            CCAddress[i] = new InternetAddress(sAr_CCAdd[i]);
        }
        for (int i = 0; i < CCAddress.length; i++) {
            message.addRecipient(Message.RecipientType.CC, CCAddress[i]);
        }
        InternetAddress[] BCCAddress = new InternetAddress[sAr_BCCAdd.length];
        for (int i = 0; i < BCCAddress.length; i++) {
            BCCAddress[i] = new InternetAddress(sAr_BCCAdd[i]);
        }
        for (int i = 0; i < BCCAddress.length; i++) {
            message.addRecipient(Message.RecipientType.BCC, BCCAddress[i]);
        }
        message.setSubject(str_Subject);
        String str_ContentType = "text/";
        str_ContentType += b_IsSendText ? "plain" : "html";
        message.setContent(str_Content, str_ContentType);
        Transport transport = session.getTransport("smtp");
        transport.connect(getSmtp_host(), getUser(), getPass());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public void sendMail(MimeMessage message) throws Exception {
        Session session = getMailSession(false);
        Transport transport = session.getTransport("smtp");
        transport.connect(getSmtp_host(), getUser(), getPass());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static ArrayList<Part> layCacAttachCuaMail(Message mess) throws IOException, MessagingException {
        ArrayList<Part> Kq = new ArrayList<Part>();
        Object content = mess.getContent();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) mess.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                Part part = multipart.getBodyPart(i);
                String disposition = part.getDisposition();
                if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT) || (disposition.equals(Part.INLINE))))) {
                    Kq.add(part);
                }
            }
        }
        return Kq;
    }

    public static Part layPartNoiDung(Message mess) throws IOException, MessagingException {
        Part Kq = null;
        Object content = mess.getContent();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) mess.getContent();
            while ((multipart.getBodyPart(0).getContent()) instanceof Multipart) {
                multipart = (Multipart) multipart.getBodyPart(0).getContent();
            }
            Part part = multipart.getBodyPart(0);
            Kq = part;
        }
        return Kq;
    }
}
