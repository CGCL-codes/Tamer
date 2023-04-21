package org.javanuke.tests.jforum;

import junit.framework.TestCase;
import org.javanuke.core.util.StringUtils;
import org.javanuke.core.util.criptografy.MD5;

public class JForumTest extends TestCase {

    public void testFeedReader() throws Exception {
    }

    public void testHash() {
        String username = "misael";
        String expected = "0b442fe1deb094b945342d2e984fb889";
        String result = StringUtils.getUserHash(username);
        assertEquals(expected, result);
    }

    public void testCapitalizedHash() {
        String username = "Misael";
        String expected = "0b442fe1deb094b945342d2e984fb889";
        String result = StringUtils.getUserHash(username);
        assertEquals(expected, result);
    }
}
