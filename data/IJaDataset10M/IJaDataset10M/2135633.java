package org.toobsframework.util;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.toobsframework.base.strategy.ElementDisplayStrategy;
import org.toobsframework.pres.url.UrlDispatchInfo;
import org.toobsframework.util.cache.DispatchObjectCache;

public class BaseRequestManager {

    protected final Log log = LogFactory.getLog(getClass());

    private Configuration configuration;

    protected static ThreadLocal<IRequest> requestHolder = new ThreadLocal<IRequest>();

    public IRequest set(UrlDispatchInfo dispatchInfo, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Map<String, Object> params, boolean expectResponse, DispatchObjectCache dispatchObjectCache, ElementDisplayStrategy elementDisplayStrategy) {
        boolean gatherMetrics = decideToGatherMetrics();
        IRequest request = new BaseRequest(dispatchInfo, httpRequest, httpResponse, params, expectResponse, dispatchObjectCache, gatherMetrics, elementDisplayStrategy);
        if (get() != null) {
            log.warn("REQUEST ALREADY SET");
        }
        requestHolder.set(request);
        return request;
    }

    public IRequest get() {
        IRequest request = requestHolder.get();
        return request;
    }

    public void unset() {
        if (requestHolder.get().getDispatchObjectCache() != null) {
            requestHolder.get().getDispatchObjectCache().clear();
        }
        requestHolder.set(null);
    }

    protected boolean decideToGatherMetrics() {
        if (!configuration.isMetricsEnabledPerRequest()) {
            return false;
        }
        double metricsMagicNumber = Math.random() * (double) configuration.getMetricsSeed();
        boolean gatherMetrics = metricsMagicNumber > 0 && metricsMagicNumber < 1;
        return gatherMetrics;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
