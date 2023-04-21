package org.mobicents.servlet.sip.testsuite.targeting;

import gov.nist.javax.sip.Utils;
import java.text.ParseException;
import java.util.Random;
import java.util.UUID;
import junit.framework.TestCase;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.mobicents.servlet.sip.core.session.SessionManagerUtil;
import org.mobicents.servlet.sip.core.session.SipApplicationSessionKey;
import org.mobicents.servlet.sip.core.session.SipSessionKey;

/**
 * Non Regression Test for Issue 2423
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public class SipSessionKeyParsingTest extends TestCase {

    /**
	 * @param name
	 */
    public SipSessionKeyParsingTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSipSessionKeyAndParsing() throws ParseException {
        SipSessionKey sipSessionKey = new SipSessionKey("" + new Random().nextInt(10000000), null, Utils.getInstance().generateCallIdentifier("" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ""), "" + UUID.randomUUID(), "ApplicationNameTest");
        String sipSessionKeyStringified = sipSessionKey.toString();
        SipSessionKey sipSessionKeyParsed = SessionManagerUtil.parseSipSessionKey(sipSessionKeyStringified);
        System.out.println(sipSessionKey);
        System.out.println(sipSessionKeyParsed);
        assertEquals(sipSessionKey, sipSessionKeyParsed);
        assertEquals(sipSessionKey.toString(), sipSessionKeyParsed.toString());
    }

    public void testSipSessionKeyToTagSet() throws ParseException {
        SipSessionKey sipSessionKeyNoToTag = new SipSessionKey("" + new Random().nextInt(10000000), null, Utils.getInstance().generateCallIdentifier("" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ""), "" + UUID.randomUUID(), "ApplicationNameTest");
        SipSessionKey sipSessionKeyDifferentFromTagAndToTag = new SipSessionKey("" + new Random().nextInt(10000000), "" + new Random().nextInt(10000000), Utils.getInstance().generateCallIdentifier("" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ""), "" + UUID.randomUUID(), "ApplicationNameTest");
        SipSessionKey sipSessionKeySameFromTagAndDifferentToTag = new SipSessionKey(sipSessionKeyNoToTag.getFromTag(), "" + new Random().nextInt(10000000), sipSessionKeyNoToTag.getCallId(), sipSessionKeyNoToTag.getApplicationSessionId(), "ApplicationNameTest");
        SipSessionKey sipSessionKeySameFromTagAndToTag = new SipSessionKey(sipSessionKeyNoToTag.getFromTag(), sipSessionKeySameFromTagAndDifferentToTag.getToTag(), sipSessionKeyNoToTag.getCallId(), sipSessionKeyNoToTag.getApplicationSessionId(), "ApplicationNameTest");
        String sipSessionKeySameFromTagAndToTagStringified = sipSessionKeySameFromTagAndToTag.toString();
        System.out.println(sipSessionKeyNoToTag);
        System.out.println(sipSessionKeyDifferentFromTagAndToTag);
        System.out.println(sipSessionKeySameFromTagAndDifferentToTag);
        System.out.println(sipSessionKeySameFromTagAndToTag);
        SipSessionKey sipSessionKeyParsed = SessionManagerUtil.parseSipSessionKey(sipSessionKeySameFromTagAndToTagStringified);
        System.out.println(sipSessionKeyParsed);
        assertEquals(sipSessionKeySameFromTagAndToTag, sipSessionKeyParsed);
        assertEquals(sipSessionKeySameFromTagAndToTag.toString(), sipSessionKeyParsed.toString());
        assertFalse(sipSessionKeyNoToTag.equals(sipSessionKeyDifferentFromTagAndToTag));
        assertTrue(sipSessionKeyNoToTag.equals(sipSessionKeySameFromTagAndDifferentToTag));
        assertTrue(sipSessionKeyNoToTag.equals(sipSessionKeySameFromTagAndToTag));
        assertTrue(sipSessionKeySameFromTagAndDifferentToTag.equals(sipSessionKeySameFromTagAndToTag));
    }

    public void testEquals() {
        EqualsVerifier.forClass(SipSessionKey.class).verify();
    }
}
