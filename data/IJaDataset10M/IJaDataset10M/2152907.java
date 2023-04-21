package com.liferay.portalweb.portlet.organizationadmin;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="OrganizationAdminTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class OrganizationAdminTests extends BaseTests {

    public OrganizationAdminTests() {
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
        addTestSuite(SearchUsersTest.class);
        addTestSuite(SearchOrganizationsTest.class);
        addTestSuite(SearchUserGroupsTest.class);
        addTestSuite(DeletePageTest.class);
    }
}
