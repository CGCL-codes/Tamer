package test;

import hrc.tool.net.MailSender;
import junit.framework.TestCase;

public class MailSenderTest extends TestCase {

    MailSender sender;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MailSender.EmailConfig config = new MailSender.EmailConfig("123@163.com", "123");
        sender = new MailSender(config);
    }

    public void testSend() {
        sender.send("123@qq.com", "123", "123");
    }
}
