package com.liferay.portalweb.portlet.sampleorbeonforms;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="SampleOrbeonFormsTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SampleOrbeonFormsTests extends BaseTests {

    public SampleOrbeonFormsTests() {
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
        addTestSuite(DeletePageTest.class);
    }
}
