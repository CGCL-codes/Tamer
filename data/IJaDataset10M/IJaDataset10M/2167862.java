package net.sourceforge.mailprobe.smtp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.sourceforge.mailprobe.util.Protocol;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Tesche <thomas.tesche@clustersystems.de>
 */
public class SMTPSenderTest {

    Properties props;

    public SMTPSenderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("smtp-tests.properties");
        props = new Properties();
        props.load(inputStream);
    }

    @After
    public void tearDown() {
    }

    /**
   * Test of sendStandardMessage method in class SMTPSender.
   */
    @Test
    public void testSendMessage() throws Exception {
        System.out.println("sendStandardMessage");
        String toAddress = props.getProperty("testSendMessage.toAddress.valid");
        String invalidToAddress = props.getProperty("testSendMessage.toAddress.invalid");
        String fromAddress = props.getProperty("testSendMessage.fromAddress");
        String content = "UnitTest #1: SMTP, starttls, user-auth, valid recipient/sender, standard SMTP-port.";
        SMTPSender smtpSender = new SMTPSender(Protocol.SMTP, props.getProperty("testSendMessage.smtpHost"), props.getProperty("testSendMessage.localhostName"), null, props.getProperty("testSendMessage.auth.user"), props.getProperty("testSendMessage.auth.password"), true);
        assertEquals(true, smtpSender.sendStandardMessage(toAddress, fromAddress, UUID.randomUUID().toString(), content).isSuccess());
        content = "UnitTest #2: SMTP, starttls, user-auth, invalid recipient/valid sender, standard SMTP-port.";
        assertEquals(false, smtpSender.sendStandardMessage(invalidToAddress, fromAddress, UUID.randomUUID().toString(), content).isSuccess());
        content = "UnitTest #3: SMTP, no starttls, user-auth, valid recipient/sender, standard SMTP-port.";
        smtpSender = new SMTPSender(Protocol.SMTP, props.getProperty("testSendMessage.smtpHost"), props.getProperty("testSendMessage.localhostName"), null, props.getProperty("testSendMessage.auth.user"), props.getProperty("testSendMessage.auth.password"), false);
        assertEquals(true, smtpSender.sendStandardMessage(toAddress, fromAddress, UUID.randomUUID().toString(), content).isSuccess());
        content = "UnitTest #4: SMTPS, starttls, user-auth, valid recipient/sender, standard SMTPS-port.";
        smtpSender = new SMTPSender(Protocol.SMTPS, props.getProperty("testSendMessage.smtpHost"), props.getProperty("testSendMessage.localhostName"), null, props.getProperty("testSendMessage.auth.user"), props.getProperty("testSendMessage.auth.password"), true);
        assertEquals(true, smtpSender.sendStandardMessage(toAddress, fromAddress, UUID.randomUUID().toString(), content).isSuccess());
        content = "UnitTest #5: SMTPS, starttls, user-auth, valid recipient/sender, wrong SMTPS-port.";
        smtpSender = new SMTPSender(Protocol.SMTPS, props.getProperty("testSendMessage.smtpHost"), props.getProperty("testSendMessage.localhostName"), "1", props.getProperty("testSendMessage.auth.user"), props.getProperty("testSendMessage.auth.password"), true);
        assertEquals(false, smtpSender.sendStandardMessage(toAddress, fromAddress, UUID.randomUUID().toString(), content).isSuccess());
    }
}
