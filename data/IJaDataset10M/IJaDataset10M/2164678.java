package br.net.woodstock.rockframework.net.mail;

public interface MailSender {

    void send(Mail mail);

    void send(Mail[] mails);
}
