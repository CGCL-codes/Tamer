package com.sample.test.servlet;

import com.liferay.portal.kernel.servlet.PortletSessionListenerLoader;

/**
 * <a href="TestPortletSessionListenerLoader.java.html"><b><i>View Source</i>
 * </b></a>
 *
 * <p>
 * This is a <code>javax.servlet.ServletContextListener</code> that loads a
 * <code>javax.servlet.http.HttpSessionListener</code> and ensures the hot
 * deployed WAR's session events are triggered along with the portal's session
 * events. This is only needed for certain application servers under certain
 * configurations. Otherwise, you can just load the the
 * <code>HttpSessionListener</code> directly in WEB-INF/web.xml.
 * </p>
 *
 * <p>
 * See http://support.liferay.com/browse/LEP-2299.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.sample.test.servlet.TestPortletSessionListener
 *
 */
public class TestPortletSessionListenerLoader extends PortletSessionListenerLoader {

    public TestPortletSessionListenerLoader() {
        super(new TestPortletSessionListener());
    }
}
