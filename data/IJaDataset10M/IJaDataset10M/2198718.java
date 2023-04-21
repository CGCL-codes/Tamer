package com.incendiaryblue.servlet;

import com.incendiaryblue.appframework.ServerConfig;
import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import javax.servlet.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

/**
 * Title:        EmailTag
 * Description:  A tag which processes the body of it's contents and then sends
 *               it in an email to a recipient
 * Copyright:    Copyright (c) 2001
 * Company:      syzygy
 * @author Giles Taylor
 * @version 1.0
 */
public class EmailTag extends BodyTagSupport {

    private static Session mailSession;

    static {
        try {
            String mailhost = ServerConfig.get("mailhost");
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", mailhost);
            props.setProperty("mail.transport.protocol", "smtp");
            mailSession = Session.getDefaultInstance(props, null);
        } catch (Exception e) {
            System.err.println("An error occurred configuring EmailTag: " + e);
            e.printStackTrace();
        }
    }

    private String sender, recipient, subject, replyTo, charSet, contentType;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws JspException {
        try {
            StringWriter writer = new StringWriter();
            bodyContent.writeOut(writer);
            String body = writer.toString();
            Message msg = new MimeMessage(mailSession);
            Address[] reciadds = InternetAddress.parse(this.recipient);
            msg.setRecipients(Message.RecipientType.TO, reciadds);
            msg.setFrom(new InternetAddress(this.replyTo, this.sender));
            Address[] reply = { new InternetAddress(this.replyTo, this.sender) };
            msg.setReplyTo(reply);
            msg.setSubject(this.subject);
            String useCharSet = this.charSet;
            if (useCharSet == null) {
                useCharSet = "utf-8";
            }
            String useMimeType = this.contentType;
            if (useMimeType == null) {
                useMimeType = "text/plain";
            }
            System.err.println("Using content type: " + useMimeType);
            System.err.println("Using character set: " + useCharSet);
            msg.setContent(new String(body.getBytes()), useMimeType + ";charset=\"" + useCharSet + "\"");
            Transport trans = mailSession.getTransport();
            Transport.send(msg);
        } catch (Exception e) {
            System.err.println("Error sending email using EmailTag: " + e);
            e.printStackTrace();
        }
        this.charSet = null;
        this.contentType = null;
        return EVAL_PAGE;
    }
}
