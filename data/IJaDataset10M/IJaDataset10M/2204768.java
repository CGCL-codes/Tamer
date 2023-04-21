package org.javanuke.tests.model;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;
import org.javanuke.core.Const;
import org.javanuke.core.util.conf.Config;
import org.javanuke.newsletters.NewsletterVo;
import org.javanuke.newsletters.NewslettersManagerInf;
import org.javanuke.tests.smtp.SimpleSmtpServer;
import org.javanuke.tests.utils.TestsConfig;

public class NewslettersManagerTest extends TestCase {

    private NewslettersManagerInf manager;

    private NewsletterVo vo = null;

    public NewslettersManagerTest(String arg0) {
        super(arg0);
        if (manager == null) {
            manager = (NewslettersManagerInf) TestsConfig.getApplicationContext().getBean("newslettersManager");
        }
        vo = new NewsletterVo();
    }

    public void testSendToThoseWantToREceive() {
        SimpleSmtpServer smtp = SimpleSmtpServer.start(Config.getInstance().getIntProperty(Const.MAIL_SMTP_PORT));
        vo.setContent("Text bla bla bla");
        vo.setCreated(new Date());
        vo.setType(NewsletterVo.ENABLE_USERS);
        vo.setTitle("Subject");
        Config.getInstance().setProperty(Const.DEBUG_MODE, "false");
        manager.send(vo);
        assertNotNull(vo);
        smtp.stop();
    }
}
