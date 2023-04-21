package com.liferay.portalweb.portlet.sunmashup;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="SunMashupTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SunMashupTests extends BaseTests {

    public SunMashupTests() {
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
        addTestSuite(DeletePageTest.class);
    }
}
