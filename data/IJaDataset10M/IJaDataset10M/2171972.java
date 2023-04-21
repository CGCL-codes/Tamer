package com.liferay.portalweb.portal.permissions.documentlibrary;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="Guest_AssertCannotEditConfigurationTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Guest_AssertCannotEditConfigurationTest extends BaseTestCase {

    public void testGuest_AssertCannotEditConfiguration() throws Exception {
        selenium.click(RuntimeVariables.replace("link=Document Library Permissions Test Page"));
        selenium.waitForPageToLoad("30000");
        assertFalse(selenium.isElementPresent("link=Configuration"));
    }
}
