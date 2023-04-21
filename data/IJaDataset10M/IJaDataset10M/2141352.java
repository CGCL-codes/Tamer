package com.liferay.portalweb.portlet.blogsaggregator;

import com.liferay.portalweb.portal.BaseTestCase;

/**
 * <a href="ViewBlogsTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ViewBlogsTest extends BaseTestCase {

    public void testViewBlogs() throws Exception {
        assertTrue(selenium.isElementPresent("link=BA Setup Test Entry"));
        assertTrue(selenium.isTextPresent("This is a BA setup test entry!"));
    }
}
