package org.vardb.util;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import org.vardb.CVardbException;

@Transactional(readOnly = false)
public class CEmailServiceImpl implements IEmailService {

    @Resource(name = "freemarkerService")
    private IFreemarkerService freemarkerService;

    private MailSender mailSender;

    private List<String> emailExceptions = new ArrayList<String>();

    private String fromAddress;

    public MailSender getMailSender() {
        return this.mailSender;
    }

    @Required
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public List<String> getEmailExceptions() {
        return this.emailExceptions;
    }

    public void setEmailExceptions(List<String> emailExceptions) {
        this.emailExceptions = emailExceptions;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    @Required
    public void setFromAddress(final String fromAddress) {
        this.fromAddress = fromAddress;
    }

    private boolean isEmailException() {
        String server = CWebHelper.getServerName();
        return isEmailException(server);
    }

    private boolean isEmailException(String server) {
        return this.emailExceptions.contains(server.toUpperCase());
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        sendEmail(message);
    }

    public void sendEmail(List<String> to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(CStringHelper.convertToArray(to));
        message.setSubject(subject);
        message.setText(body);
        sendEmail(message);
    }

    public void sendEmail(String from, String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        sendEmail(message);
    }

    public void sendEmail(SimpleMailMessage message, String template, Object... args) {
        String body = this.freemarkerService.format(template, args);
        System.out.println("template=" + template);
        message.setText(body);
        sendEmail(message);
    }

    public void sendEmail(SimpleMailMessage message) {
        if (message.getTo().length == 0) throw new CVardbException("No To: addresses specified in email with subject: " + message.getSubject());
        try {
            if (isEmailException()) return;
            if (message.getFrom() == null) message.setFrom(this.fromAddress);
            this.mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
