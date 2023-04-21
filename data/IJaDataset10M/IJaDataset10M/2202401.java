package com.yands.test;

import java.io.File;
import javax.mail.AuthenticationFailedException;
import org.apache.log4j.Logger;
import com.yands.IMailbox;
import com.yands.Mailbox;
import junit.framework.TestCase;

public class MailboxInitialiseTest extends TestCase {

    Logger log = Logger.getLogger(MailboxInitialiseTest.class);

    String mailboxPropsPath = "war\\WEB-INF\\classes\\mailbox.properties";

    protected void setUp() throws Exception {
        log.debug("SOMETHING");
        if (log.isTraceEnabled()) {
            System.getProperties().list(System.out);
            File f = new File(mailboxPropsPath);
            log.info(f.getAbsolutePath() + f.exists());
        }
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInitialise() throws Exception {
        try {
            IMailbox mailbox = new Mailbox();
            mailbox.setProps(mailboxPropsPath);
            assertTrue(mailbox.initialise());
        } catch (AuthenticationFailedException e) {
            fail(e.getMessage());
            log.error(e);
            e.printStackTrace();
        }
    }
}
