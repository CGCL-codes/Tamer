package com.liferay.portalweb.portlet.samplejsp;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="SampleJSPTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SampleJSPTests extends BaseTests {

    public SampleJSPTests() {
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
        addTestSuite(DeletePageTest.class);
    }
}
