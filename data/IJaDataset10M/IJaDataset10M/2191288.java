package net.sf.jml;

import junit.framework.TestCase;

/**
 * @author Roger Chen
 */
public class EmailTest extends TestCase {

    public void testParse() {
        assertNotNull(Email.parseStr("abc@cba.a"));
        assertNull(Email.parseStr("sldfkj"));
        assertNull(Email.parseStr("sfd sdf@b.com"));
        assertNull(Email.parseStr("adf@bcs"));
        assertNull(Email.parseStr("@bcs.abc"));
        assertNotNull(Email.parseStr("abc@abc.com.cn"));
        assertNotNull(Email.parseStr("abc@abc-ac.com"));
    }

    public void testEquals() {
        assertEquals(Email.parseStr("abc@abc.com"), Email.parseStr("abc@abc.com"));
    }

    public void testEmail() {
        assertEquals("abcdefg@google.com", Email.parseStr("abcdefg@google.com").getEmailAddress());
    }
}
