package net.livebookstore.raw.mail;

import javax.mail.MessagingException;
import org.junit.Test;

/**
 * Test our own MailSender which uses JavaMail API directly.
 * 
 * @author Xuefeng
 */
public class MailSenderTest {

    private final String from = "service@crackj2ee.com";

    private final String[] to = { "livebookstore@gmail.com", "livebookstore2@gmail.com" };

    private final String subject = "Test raw MailSender 配置";

    private final String text = "Configuration is OK if got this message. 中文显示正常。";

    @Test(timeout = 60000)
    public void sendWithAuth() throws MessagingException {
        MailSender ms = createMailSender();
        ms.setAuth(true);
        ms.send(from, to, subject, text);
    }

    @Test(expected = MessagingException.class, timeout = 60000)
    public void sendWithoutAuth() throws MessagingException {
        MailSender ms = createMailSender();
        ms.send(from, to, subject, text);
    }

    private MailSender createMailSender() {
        MailSender ms = new MailSender();
        ms.setHost("smtp.crackj2ee.com");
        ms.setUsername("service@crackj2ee.com");
        ms.setPassword("livebookstore");
        return ms;
    }
}
