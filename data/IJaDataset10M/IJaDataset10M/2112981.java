package org.toobsframework.pres.metric.controller;

import org.apache.camel.Handler;
import org.toobsframework.pres.component.dataprovider.api.DispatchContext;
import org.toobsframework.util.Configuration;

public class PerformanceBoomerangSetup {

    Configuration configuration;

    @Handler
    public DispatchContext handle(DispatchContext context) {
        BoomerangVo vo = new BoomerangVo(context.getHttpServletRequest().getRemoteAddr(), configuration.isMetricsEnabled());
        context.setContextObject(vo);
        return context;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
