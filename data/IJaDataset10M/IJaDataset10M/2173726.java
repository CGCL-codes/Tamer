package org.acegisecurity.captcha;

import junit.framework.*;
import org.acegisecurity.captcha.AlwaysTestAfterTimeInMillisCaptchaChannelProcessor;

/**
 * WARNING! This test class make some assumptions concerning the compute speed!
 * For example the two following instructions should be computed in the same
 * millis or the test is not valid.
 * <pre><code>
 * context.setHuman();
 * assertFalse(alwaysTestAfterTimeInMillisCaptchaChannelProcessor.isContextValidConcerningHumanity(context));
 * </code></pre>
 * This should be the case for most environements unless
 * 
 * <ul>
 * <li>
 * you run it on a good old TRS-80
 * </li>
 * <li>
 * you start M$office during this test ;)
 * </li>
 * </ul>
 */
public class AlwaysTestAfterTimeInMillisCaptchaChannelProcessorTests extends TestCase {

    AlwaysTestAfterTimeInMillisCaptchaChannelProcessor alwaysTestAfterTimeInMillisCaptchaChannelProcessor;

    public void testEqualsThresold() {
        CaptchaSecurityContext context = new CaptchaSecurityContextImpl();
        assertFalse(alwaysTestAfterTimeInMillisCaptchaChannelProcessor.isContextValidConcerningHumanity(context));
        context.setHuman();
        assertFalse(alwaysTestAfterTimeInMillisCaptchaChannelProcessor.isContextValidConcerningHumanity(context));
    }

    public void testIsContextValidConcerningHumanity() throws Exception {
        CaptchaSecurityContext context = new CaptchaSecurityContextImpl();
        alwaysTestAfterTimeInMillisCaptchaChannelProcessor.setThresold(100);
        context.setHuman();
        while ((System.currentTimeMillis() - context.getLastPassedCaptchaDateInMillis()) < alwaysTestAfterTimeInMillisCaptchaChannelProcessor.getThresold()) {
            assertTrue(alwaysTestAfterTimeInMillisCaptchaChannelProcessor.isContextValidConcerningHumanity(context));
            context.incrementHumanRestrictedRessoucesRequestsCount();
            long now = System.currentTimeMillis();
            while ((System.currentTimeMillis() - now) < 1) {
            }
            ;
        }
        assertFalse(alwaysTestAfterTimeInMillisCaptchaChannelProcessor.isContextValidConcerningHumanity(context));
    }

    public void testNewContext() {
        CaptchaSecurityContext context = new CaptchaSecurityContextImpl();
        assertFalse(alwaysTestAfterTimeInMillisCaptchaChannelProcessor.isContextValidConcerningHumanity(context));
    }

    protected void setUp() throws Exception {
        super.setUp();
        alwaysTestAfterTimeInMillisCaptchaChannelProcessor = new AlwaysTestAfterTimeInMillisCaptchaChannelProcessor();
    }
}
