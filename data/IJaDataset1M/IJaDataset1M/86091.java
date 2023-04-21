package com.googlecode.semrs.server;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.googlecode.semrs.model.User;

public class NewPasswordNotifier {

    private JavaMailSender mailSender;

    private String from;

    private String title;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyNewPassword(User user, String newPassword) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject(title);
            helper.setText(String.format("Hola " + user.getName() + " <br> <br> Est� recibiendo este correo porque usted (o alguien pretendiendo ser usted) ha requerido que se le env&iacute;e una nueva contrase&ntilde;a para su cuenta en el sistema <b>SEMRS</b>. " + "Si usted no lo solicit�, y contin�a recibiendo correos de este tipo, por favor, contacte con el administrador. <br> <br> " + "Su nueva contrase&ntilde;a es: <b>" + newPassword + "</b> y su nombre de usuario es: <b>" + user.getUsername() + "</b>."), true);
        } catch (javax.mail.MessagingException e) {
            throw new MailParseException(e);
        }
        mailSender.send(message);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
