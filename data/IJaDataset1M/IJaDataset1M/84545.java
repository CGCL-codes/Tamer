package org.ourgrid.peer.ui.async.util;

import org.jivesoftware.smack.XMPPException;
import junit.framework.TestCase;

public class XMPPUtilsTest extends TestCase {

    public void testTestWrongConnection() {
        try {
            new XMPPUtils().testConnection("unknownHost", 0);
            fail();
        } catch (XMPPException e) {
        }
    }

    public void testTestOKConnection() {
        try {
            new XMPPUtils().testConnection("krill.lsd.ufcg.edu.br", 5222);
        } catch (XMPPException e) {
            fail();
        }
    }
}
