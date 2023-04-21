package com.liferay.portalweb.portlet.organizationadmin;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="SearchOrganizationsTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SearchOrganizationsTest extends BaseTestCase {

    public void testSearchOrganizations() throws Exception {
        selenium.click(RuntimeVariables.replace("link=Organizations"));
        selenium.waitForPageToLoad("30000");
        selenium.type("toggle_id_enterprise_admin_organization_searchkeywords", RuntimeVariables.replace("liferay"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Organizations']"));
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isTextPresent("Liferay Hong Kong"));
        verifyTrue(selenium.isTextPresent("Liferay Los Angeles"));
        verifyTrue(selenium.isTextPresent("Liferay, Inc."));
        selenium.click(RuntimeVariables.replace("link=Return to Full Page"));
        selenium.waitForPageToLoad("30000");
    }
}
