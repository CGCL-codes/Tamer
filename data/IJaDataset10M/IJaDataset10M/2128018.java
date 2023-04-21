package com.ca.directory.jxplorer.viewer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.URIException;
import org.apache.webdav.lib.WebdavResource;
import com.sun.mail.imap.ACL;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.Rights;

public class project extends BasicEditor {

    private String imap_username;

    private String imap_password;

    private String imap_server;

    private int imap_port;

    private boolean imap_ssl;

    private String imap_seperator;

    private String webdav_url;

    private String webdav_projectdir;

    private String webdav_username;

    private String webdav_password;

    public project() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("project.properties"));
        } catch (FileNotFoundException fileNotFoundException) {
            BasicEditor.log.severe("\"project.properties\" not found!");
        } catch (IOException ioException) {
            BasicEditor.log.severe("Error reading \"project.properties\"!");
        }
        imap_username = getProperty(properties, "imap_username");
        imap_password = getProperty(properties, "imap_password");
        imap_server = getProperty(properties, "imap_server");
        imap_seperator = getProperty(properties, "imap_seperator");
        try {
            imap_port = Integer.parseInt(getProperty(properties, "imap_port"));
        } catch (NumberFormatException numberFormatException) {
            BasicEditor.log.severe("imap_port in \"project.properties\" is not a number!");
        }
        try {
            imap_ssl = (Boolean.valueOf(getProperty(properties, "imap_ssl"))).booleanValue();
        } catch (NumberFormatException numberFormatException) {
            BasicEditor.log.severe("imap_ssl in \"project.properties\" is neither \"true\" or \"false\"!");
        }
        imap_username = getProperty(properties, "imap_username");
        webdav_username = getProperty(properties, "webdav_username");
        webdav_password = getProperty(properties, "webdav_password");
        webdav_url = getProperty(properties, "webdav_url");
        webdav_projectdir = getProperty(properties, "webdav_projectdir");
        HashMap choices = new HashMap();
        choices.put("projectStatus", new String[] { "active", "closed" });
        setChoices(choices);
        HashMap filter = new HashMap();
        filter.put("name", "Name");
        filter.put("projectStatus", "Status");
        filter.put("endDate", "End date");
        filter.put("startDate", "Start date");
        filter.put("memberUid", "Members");
        filter.put("projectManager", "Manager");
        filter.put("task", "Tasks");
        setLabelFilter(filter);
        setNotShown(new String[] { "projectGuid", "mailFolder", "objectClass", "opsDocumentPath" });
        setUnique(true);
    }

    private static String getProperty(Properties properties, String name) throws NullPointerException {
        String ret = properties.getProperty(name);
        if (ret == null) {
            throw new NullPointerException("Property: " + name + " is not set in the property file!");
        } else {
            return ret;
        }
    }

    public void attributeValueChanged(String attributeName, String oldValues[], String newValues[]) {
        if ("memberUid".equals(attributeName)) {
            boolean[] dontadd = new boolean[newValues.length];
            try {
                IMAPStore imapStore = getIMAPStore();
                IMAPFolder folder = (IMAPFolder) imapStore.getFolder("INBOX" + imap_seperator + getValue("mailFolder")[0]);
                if (!folder.exists()) {
                    folder.create(IMAPFolder.HOLDS_MESSAGES);
                }
                folder.open(IMAPFolder.READ_WRITE);
                ACL[] acls = folder.getACL();
                for (int i = 0; i < acls.length; i++) {
                    boolean found = false;
                    if (!"owner".equals(acls[i].getName())) {
                        for (int p = 0; p < newValues.length; p++) {
                            if (acls[i].getName().equals(newValues[p])) {
                                found = true;
                                dontadd[p] = true;
                            }
                        }
                        if (!found) {
                            folder.removeRights(acls[i]);
                        }
                    }
                }
                for (int i = 0; i < dontadd.length; i++) {
                    if (!dontadd[i]) {
                        Rights rights = new Rights(Rights.Right.READ);
                        rights.add(Rights.Right.WRITE);
                        rights.add(Rights.Right.CREATE);
                        rights.add(Rights.Right.DELETE);
                        rights.add(Rights.Right.INSERT);
                        rights.add(Rights.Right.KEEP_SEEN);
                        rights.add(Rights.Right.LOOKUP);
                        rights.add(Rights.Right.POST);
                        ACL acl = new ACL(newValues[i], rights);
                        folder.addACL(acl);
                    }
                }
                imapStore.close();
            } catch (MessagingException messagingException) {
                log.warning("Error communicating with IMAP: " + messagingException.getMessage());
            }
        }
    }

    public String[] setInitialValue(String attributeName, String[] values) {
        if ("startDate".equals(attributeName)) {
            values = new String[1];
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            values[0] = "" + addChar("" + gregorianCalendar.get(Calendar.DAY_OF_MONTH), '0', 2) + "-" + addChar("" + (gregorianCalendar.get(Calendar.MONTH) + 1), '0', 2) + "-" + gregorianCalendar.get(Calendar.YEAR);
        } else if ("endDate".equals(attributeName)) {
            values = new String[1];
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            values[0] = "" + addChar("" + gregorianCalendar.get(Calendar.DAY_OF_MONTH), '0', 2) + "-" + addChar("" + (gregorianCalendar.get(Calendar.MONTH) + 1), '0', 2) + "-" + gregorianCalendar.get(Calendar.YEAR);
        } else if ("projectGuid".equals(attributeName)) {
            values[0] = GuidGenerator.generateGuid(true);
        }
        return values;
    }

    public String newSingleValuedAttribute(String attributeName, String value) {
        if ("mailFolder".equals(attributeName)) {
            value = tidyName(getValue("name")[0]);
        } else if ("opsDocumentPath".equals(attributeName)) {
            String tidiedName = tidyName(getValue("name")[0]);
            value = webdav_url + webdav_projectdir + tidiedName;
            HttpURL fileURL;
            try {
                fileURL = new HttpURL(webdav_url);
                fileURL.setUserinfo(webdav_username, webdav_password);
                WebdavResource webdavResource = new WebdavResource(fileURL);
                if (!webdavResource.mkcolMethod(webdav_projectdir + tidiedName)) {
                    if (webdavResource.mkcolMethod(webdav_projectdir)) {
                        if (!webdavResource.mkcolMethod(webdav_projectdir + tidiedName)) {
                            log.warning("\"projects\" directory " + "created, but problem creating " + "project specific " + "directory using WebDav!");
                        }
                    } else {
                        log.warning("Cannot create project" + " directory using WebDav!");
                    }
                }
            } catch (URIException uriException) {
                log.severe("Webdav URL is incorrect!\r\n" + uriException.getMessage());
            } catch (IOException ioException) {
                log.severe("Error communicating with Webdav server!\r\n" + ioException.getMessage());
            }
        }
        return value;
    }

    private String tidyName(String name) {
        String value = "";
        for (int w = 0; w < name.length(); w++) {
            char check = name.charAt(w);
            if (Character.isLetterOrDigit(check) || Character.isWhitespace(check)) {
                value += check;
            }
        }
        return value;
    }

    public String[] newMultiValuedAttribute(String attributeName, String values[]) {
        return values;
    }

    private IMAPStore getIMAPStore() throws MessagingException, AuthenticationFailedException {
        Session s = Session.getInstance(System.getProperties());
        URLName earlName = new URLName(imap_server);
        IMAPStore imapStore = null;
        if (imap_ssl) {
            log.info("Connecting to IMAP server: " + imap_server + " at port: " + imap_port + ", SSL enabled.");
            imapStore = new IMAPSSLStore(s, earlName);
        } else {
            log.info("Connecting to IMAP server: " + imap_server + " at port: " + imap_port + ", SSL disabled.");
            imapStore = new IMAPStore(s, earlName);
        }
        imapStore.connect(imap_server, imap_port, imap_username, imap_password);
        return imapStore;
    }

    public String getName() {
        return "OPS project editor";
    }

    public String getToolTip() {
        return "OPS project editor for editing OPS projects.";
    }

    public String addChar(String string, char add, int preferedSize) {
        String ret = new String(string);
        while (ret.length() < preferedSize) {
            ret = add + ret;
        }
        return ret;
    }
}
