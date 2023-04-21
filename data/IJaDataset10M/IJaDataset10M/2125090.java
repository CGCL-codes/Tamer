package com.dcivision.dms.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.bean.AbstractBaseObject;

/**
 EmailMessage.java

 This class represent a email message.

 @author      Holmes Yan
 @company     DCIVision Limited
 @creation date   18/07/2004
 @version     $Revision: 1.8 $
 */
public class EmailMessage extends AbstractBaseObject {

    public static final String REVISION = "$Revision: 1.8 $";

    static final long serialVersionUID = 6845202943820835477L;

    private String subject;

    private String sender;

    private Date sendDate;

    private String to;

    private String cc;

    private int size;

    private Object content;

    private int priority;

    private boolean isTextOnly;

    private boolean isSaved;

    private List attachments = new ArrayList();

    private String messageID;

    private boolean analyzed = false;

    public static EmailMessage newInstance() {
        return new EmailMessage();
    }

    public EmailMessage() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isTextOnly() {
        return isTextOnly;
    }

    public void setIsTextOnly(boolean isTextOnly) {
        this.isTextOnly = isTextOnly;
    }

    public List getAttachments() {
        return attachments;
    }

    public void addAttachment(EmailAttachment emailAttachement) {
        attachments.add(emailAttachement);
    }

    public EmailAttachment getAttachment(int id) {
        return (EmailAttachment) attachments.get(id);
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String getSortValue(String SortName) {
        if (SortName == null) return this.getID().toString();
        SortName = SortName.trim();
        if ("SUBJECT".equals(SortName)) {
            return getSubject();
        } else if ("SENDER".equals(SortName)) {
            return getSender();
        } else if ("SEND_DATE".equals(SortName)) {
            String pattern = TextUtility.getDateTimeFormat().toPattern();
            return TextUtility.formatDate(getSendDate(), pattern);
        } else if ("SIZE".equals(SortName)) {
            return Integer.toString(getSize());
        } else {
            return this.getID().toString();
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public boolean getAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }

    /**
   * get the messge content 
   * 
   * @return this bean's content object toString();
   */
    public String getMessgeContentString() {
        return this.content == null ? "" : this.content.toString();
    }

    /**
   * get the bean's attatchment list's size 
   * @return
   */
    public int getAttatchmentNum() {
        return this.attachments == null ? 0 : this.attachments.size() - 1;
    }
}
