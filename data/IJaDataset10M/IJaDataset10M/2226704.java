package org.magicbox.service;

import java.util.List;
import javax.mail.internet.InternetAddress;
import org.magicbox.dto.Email;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0_08
 * @version 0.1
 */
public class MailSenderImpl implements MailSender {

    public boolean spedisciArrayEmailSemplici(InternetAddress[] indirizzi, String corpoHtml, String oggettoMail) {
        return false;
    }

    public boolean spedisciListaEmailSemplici(List indirizzi, Email mail) {
        return false;
    }

    public boolean spedisciSingolaEmailHtml(Email mail) {
        return false;
    }

    public boolean spedisciSingolaEmailSemplice(Email mail) {
        return false;
    }

    public void setServerSmtp(String serverSmtp) {
        this.serverSmtp = serverSmtp;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    private String serverSmtp;

    private String from;

    private String fromMail;
}
