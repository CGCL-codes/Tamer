package br.ufmg.saotome.arangi.commons;

import java.util.Map;
import javax.mail.MessagingException;
import br.ufmg.saotome.arangi.dto.MailConfig;
import br.ufmg.saotome.arangi.model.ModelService;

public class ThreadMailSender extends Thread implements IMailSender {

    IMailSender mailSender;

    MailConfig mailConfig;

    public ThreadMailSender(MailConfig mailConfig) throws BasicException {
        mailSender = (IMailSender) ModelService.getBean(ArangiConstants.MAIL_SENDER);
        this.mailConfig = mailConfig;
    }

    public void send(String to, String from, String assunto, String template, Map<String, ?> modelo) throws MessagingException {
    }

    public void send(String to, String[] cc, String from, String assunto, String template, Map<String, ?> modelo) throws MessagingException {
    }

    public void send(String[] to, String from, String assunto, String template, Map<String, ?> modelo) throws MessagingException {
    }

    public void send(String[] to, String[] cc, String from, String assunto, String template, Map<String, ?> modelo) throws MessagingException {
    }

    public void send(MailConfig mailConfig) throws MessagingException {
        mailSender.send(mailConfig);
    }

    public void run() {
        try {
            this.send(mailConfig);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
