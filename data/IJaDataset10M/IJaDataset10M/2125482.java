package org.dcm4chee.xero.controller;

import org.dcm4chee.xero.test.JSTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class XeroJSStringTest {

    static final Logger log = LoggerFactory.getLogger(XeroJSStringTest.class);

    private JSTemplate jst = new JSTemplate("rhinoAccess", "xeroTest", "xeroController", "xeroControllerTests");

    private boolean isVerbose = false;

    @Test
    public void testTrim_ShouldNotAlterString_WhenTheStringHasNoLeadingOrTrailingWhitespace() {
        jst.runTest("stringTest", "testTrim_ShouldNotAlterString_WhenTheStringHasNoLeadingOrTrailingWhitespace", isVerbose);
    }

    @Test
    public void testTrim_ShouldTrimLeadingWhitespace() {
        jst.runTest("stringTest", "testTrim_ShouldTrimLeadingWhitespace", isVerbose);
    }

    @Test
    public void testTrim_ShouldTrimTrailingWhitespace() {
        jst.runTest("stringTest", "testTrim_ShouldTrimTrailingWhitespace", isVerbose);
    }

    @Test
    public void testTrim_ShouldTrimLeadingAndTrailingWhitespace() {
        jst.runTest("stringTest", "testTrim_ShouldTrimLeadingAndTrailingWhitespace", isVerbose);
    }
}
