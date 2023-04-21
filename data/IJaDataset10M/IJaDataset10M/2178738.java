package org.apache.roller.presentation.filters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.RollerException;
import org.apache.roller.config.RollerConfig;
import org.apache.roller.model.RollerFactory;
import org.apache.roller.presentation.PlanetRequest;
import org.apache.roller.util.cache.ExpiringCacheEntry;

/**
 * Handles if-modified-since checking for planet resources.
 *
 * @web.filter name="IfPlanetModifiedFilter"
 *
 * @author David M Johnson
 */
public class IfPlanetModifiedFilter implements Filter {

    private static Log mLogger = LogFactory.getLog(IfPlanetModifiedFilter.class);

    private long timeout = 15 * 60 * 1000;

    private ExpiringCacheEntry lastUpdateTime = null;

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");

    /**
     * Filter processing.
     *
     * We check the incoming request for an "if-modified-since" header and
     * repond with a 304 NOT MODIFIED when appropriate.
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        PlanetRequest planetRequest = null;
        try {
            planetRequest = new PlanetRequest(request);
        } catch (Exception e) {
            mLogger.error("error creating planet request", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Date updateTime = null;
        try {
            if (this.lastUpdateTime != null) {
                updateTime = (Date) this.lastUpdateTime.getValue();
            }
            if (updateTime == null) {
                updateTime = RollerFactory.getRoller().getPlanetManager().getLastUpdated();
                if (updateTime == null) {
                    updateTime = new Date();
                    mLogger.warn("Can't get lastUpdate time, using current time instead");
                }
                this.lastUpdateTime = new ExpiringCacheEntry(updateTime, this.timeout);
            }
            request.setAttribute("updateTime", updateTime);
            Date sinceDate = new Date(request.getDateHeader("If-Modified-Since"));
            if (updateTime != null) {
                synchronized (dateFormatter) {
                    String date = dateFormatter.format(updateTime);
                    updateTime = new Date(date);
                }
                if (updateTime.compareTo(sinceDate) <= 0) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
        } catch (RollerException re) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("DisplayException", re);
            return;
        } catch (IllegalArgumentException e) {
        }
        if (updateTime != null) {
            response.setDateHeader("Last-Modified", updateTime.getTime());
        }
        chain.doFilter(request, response);
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        mLogger.info("Initializing if-modified planet filter");
        String timeoutString = RollerConfig.getProperty("cache.planet.timeout");
        try {
            long timeoutSecs = Long.parseLong(timeoutString);
            this.timeout = timeoutSecs * 1000;
        } catch (Exception e) {
        }
    }

    public void destroy() {
    }
}
