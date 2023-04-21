package org.jakiwiki.integration.mail;

import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jakiwiki.model.Pagina;

@Stateless
public class EmailIntegratorImplementation implements EmailIntegrator {

    public void sendEmail(String mittente, String destinatario, String commento, Pagina pagina) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.yahoo.it");
        Authenticator authenticator = new PasswordAuthenticator();
        Session session = Session.getDefaultInstance(props, authenticator);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(mittente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(pagina.getTitolo());
            message.setText(pagina.getContenuto());
            Transport.send(message);
        } catch (Throwable t) {
            throw new RuntimeException("Errore nella spedizione delle email", t);
        }
    }
}

class PasswordAuthenticator extends Authenticator {

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("utente", "password");
    }
}
