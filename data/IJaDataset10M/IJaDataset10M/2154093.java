package org.fao.geonet.services.password;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import jeeves.constants.Jeeves;
import jeeves.exceptions.BadResponseEx;
import jeeves.exceptions.OperationAbortedEx;
import jeeves.exceptions.OperationNotAllowedEx;
import jeeves.exceptions.UserNotFoundEx;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import jeeves.utils.Xml;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.kernel.setting.SettingInfo;
import org.fao.geonet.kernel.setting.SettingManager;
import org.jdom.Element;

/**
 * Send change password link email
 */
public class SendLink implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
        this.appPath = appPath;
        this.stylePath = appPath + FS + Geonet.Path.STYLESHEETS + FS;
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        String username = Util.getParam(params, Params.USERNAME);
        String template = Util.getParam(params, Params.TEMPLATE, CHANGE_EMAIL_XSLT);
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        Element elUser = dbms.select("SELECT * FROM Users WHERE username=?", username);
        if (elUser.getChildren().size() == 0) throw new UserNotFoundEx(username);
        if (!elUser.getChild("record").getChild("profile").getText().equals(Geonet.Profile.REGISTERED_USER)) throw new OperationNotAllowedEx("Only users with profile RegisteredUser can change their password using this option");
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        SettingManager sm = gc.getSettingManager();
        String host = sm.getValue("system/feedback/mailServer/host");
        String port = sm.getValue("system/feedback/mailServer/port");
        String adminEmail = sm.getValue("system/feedback/email");
        String thisSite = sm.getValue("system/site/name");
        SettingInfo si = new SettingInfo(context);
        String siteURL = si.getSiteUrl() + context.getBaseUrl();
        if (thisSite == null || host == null || port == null || adminEmail == null || thisSite.equals("dummy") || host.equals("") || port.equals("") || adminEmail.equals("")) {
            throw new IllegalArgumentException("Missing settings in System Configuration (see Administration menu) - cannot change passwords");
        }
        String scrambledPassword = elUser.getChild("record").getChildText(Params.PASSWORD);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String todaysDate = sdf.format(cal.getTime());
        String changeKey = Util.scramble(scrambledPassword + todaysDate);
        Element root = new Element("root");
        root.addContent(new Element("username").setText(username));
        root.addContent(new Element("email").setText(elUser.getChild("record").getChildText("email")));
        root.addContent(new Element("site").setText(thisSite));
        root.addContent(new Element("siteURL").setText(siteURL));
        root.addContent(new Element("changeKey").setText(changeKey));
        String emailXslt = stylePath + template;
        Element elEmail = Xml.transform(root, emailXslt);
        String subject = elEmail.getChildText("subject");
        String to = elEmail.getChildText("to");
        String content = elEmail.getChildText("content");
        if (!sendMail(host, Integer.parseInt(port), subject, adminEmail, to, content)) {
            throw new OperationAbortedEx("Could not send email");
        }
        return new Element(Jeeves.Elem.RESPONSE);
    }

    /**
	 * Send the mail to the user.
	 * 
	 * @param from
	 * @param to
	 * @param protocol
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
    boolean sendMail(String host, int port, String subject, String from, String to, String content) {
        boolean isSendout = false;
        Properties props = new Properties();
        props.put("mail.transport.protocol", PROTOCOL);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "false");
        Session mailSession = Session.getDefaultInstance(props);
        try {
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setText(content);
            Transport.send(msg);
            isSendout = true;
        } catch (AddressException e) {
            isSendout = false;
            e.printStackTrace();
        } catch (MessagingException e) {
            isSendout = false;
            e.printStackTrace();
        }
        return isSendout;
    }

    private static String FS = File.separator;

    private String appPath;

    private String stylePath;

    private static final String PROTOCOL = "smtp";

    private static final String CHANGE_EMAIL_XSLT = "password-forgotten-email.xsl";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
}
