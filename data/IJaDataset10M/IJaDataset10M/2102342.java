package se.mafro.sesam.net.smtp;

import org.junit.Assert;
import org.junit.Test;
import se.mafro.sesam.impl.SMTPUserAuthenticator;

/**
 * Testcase for testing SMTPMultilineReply.
 * 
 * @author mange.froberg
 *
 */
public class SMTPMultilineReplyTest {

    private static String AUTH_PLAIN_PATTERN = SMTPUserAuthenticator.AUTH_PLAIN_PATTERN;

    @Test
    public void testMatchesText_AuthPlain() {
        SMTPMultilineReply reply;
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "AUTH PLAIN LOGIN"));
        Assert.assertTrue(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "AUTH LOGIN PLAIN"));
        Assert.assertTrue(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "AUTH LOGIN PLAIN "));
        Assert.assertTrue(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "AUTH LOGIN"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "AUTH PLAINXXX"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "AUTH XXXPLAIN"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, " AUTH PLAIN"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "PLAIN"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "XXX PLAIN"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
        reply = new SMTPMultilineReply();
        reply.add(new SMTPReply(250, "XXX AUTH PLAIN"));
        Assert.assertFalse(reply.matchesText(AUTH_PLAIN_PATTERN));
    }
}
