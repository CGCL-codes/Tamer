package com.liferay.portalweb.portlet.mysummary;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="MySummaryTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class MySummaryTests extends BaseTests {

    public MySummaryTests() {
        addTestSuite(DownloadPortletTest.class);
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
    }
}
