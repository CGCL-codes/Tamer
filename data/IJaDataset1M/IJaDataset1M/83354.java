package com.liferay.portalweb.portlet.novellcollaboration;

import com.liferay.portalweb.portal.BaseTests;

/**
 * <a href="NovellCollaborationTests.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class NovellCollaborationTests extends BaseTests {

    public NovellCollaborationTests() {
        addTestSuite(AddPageTest.class);
        addTestSuite(AddPortletTest.class);
        addTestSuite(DeletePageTest.class);
    }
}
