package com.bluesky.rivermanhp.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import com.bluesky.rivermanhp.domain.Property;
import com.bluesky.rivermanhp.domain.file.File;
import com.bluesky.rivermanhp.util.PMF;
import com.google.appengine.api.datastore.Blob;

public class SystemService {

    private static Logger logger = Logger.getLogger(SystemService.class);

    PersistenceManager pm = PMF.get().getPersistenceManager();

    public void notifyWebMaster(String content) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        PropertyService propSvc = ServiceFactory.getFactory().getPropertyService();
        String webMasterEmail = propSvc.readValue("web-master.email");
        String webMasterName = propSvc.readValue("web-master.name");
        String senderAddress = propSvc.readValue("web-app.email");
        String senderName = propSvc.readValue("web-app.name");
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderAddress, senderName));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(webMasterEmail, webMasterName));
            msg.setSubject("Notification");
            msg.setText(content);
            Transport.send(msg);
        } catch (Exception e) {
            logger.warn("send email failed.");
        }
    }

    public void saveFile(File file) {
        try {
            pm.makePersistent(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            pm.close();
        }
    }

    public File getFile(String name) {
        try {
            Query query = pm.newQuery("select from " + File.class.getName());
            query.setFilter("name=pName");
            query.declareParameters("String pName");
            query.setOrdering("id desc");
            List<File> list = (List<File>) query.execute(name);
            if (!list.isEmpty()) return list.get(0); else return null;
        } finally {
            pm.close();
        }
    }
}
