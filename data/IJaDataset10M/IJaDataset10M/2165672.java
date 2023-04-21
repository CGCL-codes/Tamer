package com.liferay.portalweb.portlet.sampleportalclient;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="SamplePortalClientTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SamplePortalClientTests extends BaseTests {

    public SamplePortalClientTests() {
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
        addTestSuite(DeletePageTest.class);
    }
}
