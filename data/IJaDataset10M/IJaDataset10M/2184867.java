package org.go.domain;

public class MailInfo {

    private String cc;

    private String contentType;

    private String from;

    private String message;

    private String replyTo;

    private String smtpHost;

    private String subject;

    private String to;

    public String getCc() {
        return cc;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return to;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "'" + getSubject() + "' to: " + getTo();
    }
}
