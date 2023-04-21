package net.sf.yavtags.rules;

import junit.framework.TestCase;
import net.sf.yavtags.YavConfig;

public class IntegerTest extends TestCase {

    YavRule rule;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        rule = YavRule.parseRule("xxx|integer", null, null, YavConfig.getDefaultConfig());
    }

    public void testEmptyData() throws Exception {
        assertNull(rule.checkError(""));
        assertNull(rule.checkError(null));
    }

    public void testValidData() throws Exception {
        assertNull(rule.checkError("5"));
        assertNull(rule.checkError("0"));
        assertNull(rule.checkError("-5"));
        assertNull(rule.checkError("1234567890123456789012345678901234567890"));
    }

    public void testInvalidData() throws Exception {
        assertNotNull(rule.checkError("abc"));
        assertNotNull(rule.checkError("5 "));
        assertNotNull(rule.checkError("3.5"));
        assertNotNull(rule.checkError("4567f"));
    }
}
