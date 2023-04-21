package org.appspy.perf.servlet.provider;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.appspy.client.common.CollectorFactory;
import org.appspy.client.common.CollectorInfo;
import org.appspy.perf.data.ServletTimerData;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ServletContextBasedVersionDataProvider extends AbstractDataProvider {

    public static final String VERSION_PARAM_NAME = "org.appspy.version";

    public void afterRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext, Throwable throwable) {
    }

    public void beforeRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext) {
        try {
            servletTimerData.setVersion(servletContext.getInitParameter(VERSION_PARAM_NAME));
        } catch (Throwable t) {
            CollectorFactory.getCollectorInfo().setStatus(CollectorInfo.STATUS_ERROR, t);
        }
    }
}
