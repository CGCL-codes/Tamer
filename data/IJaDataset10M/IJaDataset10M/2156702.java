package com.liferay.portalweb.portal.permissions.documentlibrary;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="Member_AssertViewCommentsTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Member_AssertViewCommentsTest extends BaseTestCase {

    public void testMember_AssertViewComments() throws Exception {
        selenium.click(RuntimeVariables.replace("link=Document Library Permissions Test Page"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Admin Permissions Subfolder 1"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=View"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Comments"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("I am a Community Admin and I can write comments!"));
    }
}
