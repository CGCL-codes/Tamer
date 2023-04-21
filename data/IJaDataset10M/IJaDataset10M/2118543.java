package org.softnetwork.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.activation.FileDataSource;

/**
 * @author $Author$
 *
 * @version $Revision$
 */
public class MailBean implements Serializable, MailInterface {

    static final long serialVersionUID = 1511755386642725277L;

    private int mailId;

    private String mailFrom;

    private String mailSubject;

    private String mailMessage;

    private Date mailCreationDate;

    private String mailContentsType;

    private Hashtable headers;

    private Collection mailTo;

    private Collection mailToCc;

    private Collection mailToBcc;

    private Collection mailAtt;

    public MailBean() {
        mailId = -1;
        mailFrom = "";
        mailSubject = "";
        mailMessage = "";
        mailCreationDate = new Date();
        mailContentsType = TXT_CONTENTS;
        mailTo = new ArrayList();
        mailToCc = new ArrayList();
        mailToBcc = new ArrayList();
        mailAtt = new ArrayList();
        headers = new Hashtable();
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public Enumeration getHeaderNames() {
        return headers.keys();
    }

    public String getHeaderValue(String name) {
        return (String) headers.get(name);
    }

    public void setMailId(int newMailId) {
        mailId = newMailId;
    }

    public int getMailId() {
        return mailId;
    }

    public void setMailFrom(String newMailFrom) {
        mailFrom = newMailFrom;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void addMailTo(String newMailTo) {
        mailTo.add(newMailTo);
    }

    public String[] getMailTos() {
        return (String[]) mailTo.toArray(new String[0]);
    }

    public void addMailToCc(String newMailToCc) {
        mailToCc.add(newMailToCc);
    }

    public String[] getMailToCcs() {
        return (String[]) mailToCc.toArray(new String[0]);
    }

    public void addMailToBcc(String newMailToBcc) {
        mailToBcc.add(newMailToBcc);
    }

    public String[] getMailToBccs() {
        return (String[]) mailToBcc.toArray(new String[0]);
    }

    public void setMailSubject(String newMailSubject) {
        mailSubject = newMailSubject;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailMessage(String newMailMessage) {
        mailMessage = newMailMessage;
    }

    public String getMailMessage() {
        return mailMessage;
    }

    public void setMailCreationDate(Date newMailCreationDate) {
        mailCreationDate = newMailCreationDate;
    }

    public Date getMailCreationDate() {
        return mailCreationDate;
    }

    public void setMailContentsType(String newMailContentsType) {
        mailContentsType = newMailContentsType;
    }

    public String getMailContentsType() {
        return mailContentsType;
    }

    public void addMailAtt(FileDataSource newMailAtt) {
        mailAtt.add(newMailAtt);
    }

    public FileDataSource[] getMailAtts() {
        return (FileDataSource[]) mailAtt.toArray(new FileDataSource[0]);
    }
}
