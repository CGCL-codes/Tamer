package com.wisc.csvParser.notificationProviders;

import javax.swing.JPanel;
import org.jdom.Element;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.Date;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author user
 */
public class EmailNotificationProvider extends GenericNotificationProvider {

    private final String encryptPassword = "$TiseOKpc79$|k)Ys[&8KztK(8v)vX";

    private boolean useAuth = false;

    private String user;

    private String pass;

    private int port;

    private String host;

    private String recipients;

    JPanelEmailNotification settingsPane;

    public EmailNotificationProvider() {
    }

    @Override
    protected void sendEventMessage(NotificationEvent event) {
        try {
            java.util.Properties props = System.getProperties();
            Session session = Session.getDefaultInstance(props, null);
            Transport transport = session.getTransport("smtp");
            if (useAuth) {
                transport.connect(host, port, user, pass);
            } else {
                transport.connect(host, null, null);
            }
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("noreply@gleon.org"));
            msg.setRecipients(MimeMessage.RecipientType.TO, recipients);
            msg.setSentDate(new Date());
            msg.setSubject(event.eventType + ": " + event.eventEntity);
            msg.setText("Timestamp:" + event.eventDetectionDateTime.toString() + "\n\n" + event.eventMessage);
            msg.saveChanges();
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Element e) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(encryptPassword);
        this.setHost(e.getChildText("host"));
        String isEncrypted = e.getChild("pass").getAttributeValue("encrypted");
        String encryptPass = e.getChildText("pass");
        if (isEncrypted == null || isEncrypted.compareToIgnoreCase("false") == 0 || encryptPass.compareToIgnoreCase("") == 0) {
            this.setPass(e.getChildText("pass"));
        } else {
            this.setPass(textEncryptor.decrypt(e.getChildText("pass")));
        }
        this.setUser(e.getChildText("user"));
        this.setRecipients(e.getChildText("recipients"));
        this.setPort(Integer.valueOf(e.getChildText("port")));
        useAuth = e.getChild("useAuth") != null;
    }

    @Override
    public Element getSettingsXml() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(encryptPassword);
        Element e = new Element(INotificationProvider.NOTIFICATION_PROVIDER_TAG);
        e.setAttribute("type", "com.wisc.csvParser.notificationProviders.EmailNotificationProvider");
        e.addContent(new Element("host").setText(host));
        e.addContent(new Element("recipients").setText(recipients));
        e.addContent(new Element("port").setText(Integer.toString(port)));
        e.addContent(new Element("user").setText(user));
        if (this.getPass() == null || this.getPass().compareToIgnoreCase("") == 0) {
            e.addContent(new Element("pass").setText(""));
        } else {
            e.addContent(new Element("pass").setText(textEncryptor.encrypt(pass)).setAttribute("encrypted", "true"));
        }
        if (useAuth) {
            e.addContent(new Element("useAuth"));
        }
        return e;
    }

    @Override
    public String getProviderName() {
        return "Email Notification Provider";
    }

    @Override
    public JPanel getStatusJPanel() {
        return new JPanelEmailNotification(this);
    }

    @Override
    public String getPanelID() {
        return "emailNotificationProvider";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public boolean isUseAuth() {
        return useAuth;
    }

    public void setUseAuth(boolean useAuth) {
        this.useAuth = useAuth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
