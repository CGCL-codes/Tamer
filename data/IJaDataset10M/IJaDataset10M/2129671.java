package org.openlogbooks.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void testEncodePassword() throws Exception {
        String password = "tomcat";
        String encrypted = "536c0b339345616c1b33caf454454d8b8a190d6c";
        assertEquals(StringUtil.encodePassword(password, "SHA"), encrypted);
    }
}
