package com.liferay.portalweb.portal.permissions.blogs;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="Member_LoginTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Member_LoginTest extends BaseTestCase {

    public void testMember_Login() throws Exception {
        selenium.type("_58_login", RuntimeVariables.replace("member@liferay.com"));
        selenium.type("_58_password", RuntimeVariables.replace("test"));
        selenium.click(RuntimeVariables.replace("//input[@value='Sign In']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Blogs Permissions Page"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("link=Test Entry 1"));
    }
}
