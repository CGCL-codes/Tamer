package org.appspy.perf.servlet.provider;

import java.net.InetAddress;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.appspy.client.common.CollectorFactory;
import org.appspy.client.common.CollectorInfo;
import org.appspy.perf.data.ServletTimerData;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class HostDataProvider extends AbstractDataProvider {

    protected static String sHostName = null;

    public void afterRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext, Throwable throwable) {
    }

    public void beforeRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext) {
        try {
            if (sHostName == null) {
                sHostName = InetAddress.getLocalHost().getCanonicalHostName();
            }
            servletTimerData.setHost(sHostName);
        } catch (Throwable t) {
            CollectorFactory.getCollectorInfo().setStatus(CollectorInfo.STATUS_ERROR, t);
        }
    }
}
