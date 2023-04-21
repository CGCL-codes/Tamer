package br.net.woodstock.rockframework.net.mail;

import java.util.Collection;
import java.util.LinkedHashSet;

public class SimpleMail implements Mail {

    private static final long serialVersionUID = -7704634381340126487L;

    private String text;

    private String subject;

    private String from;

    private boolean html;

    private Collection<String> replyTo;

    private Collection<String> to;

    private Collection<String> bcc;

    private Collection<String> cc;

    private Collection<Attachment> attach;

    public SimpleMail() {
        super();
        this.text = "";
        this.subject = "";
        this.from = "";
        this.html = false;
        this.replyTo = new LinkedHashSet<String>();
        this.to = new LinkedHashSet<String>();
        this.bcc = new LinkedHashSet<String>();
        this.cc = new LinkedHashSet<String>();
        this.attach = new LinkedHashSet<Attachment>();
    }

    public void addAttach(final Attachment attach) {
        this.attach.add(attach);
    }

    @Override
    public Collection<Attachment> getAttach() {
        return this.attach;
    }

    public void setAttach(final Collection<Attachment> attach) {
        this.attach = attach;
    }

    public void addBcc(final String bcc) {
        this.bcc.add(bcc);
    }

    @Override
    public Collection<String> getBcc() {
        return this.bcc;
    }

    public void setBcc(final Collection<String> bcc) {
        this.bcc = bcc;
    }

    public void addCc(final String cc) {
        this.cc.add(cc);
    }

    @Override
    public Collection<String> getCc() {
        return this.cc;
    }

    public void setCc(final Collection<String> cc) {
        this.cc = cc;
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    @Override
    public boolean isHtml() {
        return this.html;
    }

    public void setHtml(final boolean html) {
        this.html = html;
    }

    public void addReplyTo(final String replyTo) {
        this.replyTo.add(replyTo);
    }

    @Override
    public Collection<String> getReplyTo() {
        return this.replyTo;
    }

    public void setReplyTo(final Collection<String> replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void addTo(final String to) {
        this.to.add(to);
    }

    @Override
    public Collection<String> getTo() {
        return this.to;
    }

    public void setTo(final Collection<String> to) {
        this.to = to;
    }
}
