package com.rhythm.commons.net;

import javax.mail.MessagingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jboyd
 */
public class Pop3EmailerTest {

    public Pop3EmailerTest() {
    }

    @Test
    public void testSendMessage() throws MessagingException {
        Pop3Emailer emailer = Pop3Emailer.of("mail.rhythmeis.com");
        assertNotNull(emailer);
        System.out.println("TODO:");
    }
}
