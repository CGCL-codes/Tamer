package org.appspy.perf.servlet.provider;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.appspy.client.common.CollectorFactory;
import org.appspy.client.common.CollectorInfo;
import org.appspy.perf.data.ServletTimerData;
import org.appspy.perf.servlet.AppSpyServletResponseWrapper;

/**
 * @author Olivier HEDIN / olivier@appspy.org
 */
public class ResponseCodeDataProvider extends AbstractDataProvider {

    public void afterRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext, Throwable throwable) {
        try {
            if (throwable != null) {
                servletTimerData.setResult(-1);
            } else {
                servletTimerData.setResult(1);
            }
            if (res instanceof AppSpyServletResponseWrapper) {
                int responseCode = ((AppSpyServletResponseWrapper) res).getStatus();
                servletTimerData.setResponseCode(responseCode);
                if (responseCode >= 400) {
                    servletTimerData.setResult(-1);
                }
            }
        } catch (Throwable t) {
            CollectorFactory.getCollectorInfo().setStatus(CollectorInfo.STATUS_ERROR, t);
        }
    }

    public void beforeRequest(ServletTimerData servletTimerData, HttpServletRequest req, HttpServletResponse res, ServletContext servletContext) {
    }
}
