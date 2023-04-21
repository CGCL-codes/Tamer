package com.od.jtimeseries.net.httpd;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.net.httpd.handler.*;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 02-Jan-2009
 * Time: 10:51:18
 */
public class DefaultHandlerFactory implements HandlerFactory {

    private TimeSeriesContext rootContext;

    public DefaultHandlerFactory(TimeSeriesContext rootContext) {
        this.rootContext = rootContext;
    }

    public HttpHandler getHandler(String uri, String method, Properties header, Properties params) {
        if (uri.toLowerCase().endsWith(ChartPngHandler.CHART_PNG_POSTFIX)) {
            return new ChartPngHandler(rootContext);
        } else if (uri.endsWith("/")) {
            return new ContextHandler(rootContext);
        } else if (isValidXslUri(uri)) {
            return new ClassPathResourceResponseHandler(rootContext, "application/xml");
        } else if (uri.endsWith(SeriesHandler.SERIES_POSTFIX)) {
            return new SeriesHandler(rootContext);
        } else if (uri.endsWith((TimeSeriesIndexHandler.INDEX_POSTFIX))) {
            return new TimeSeriesIndexHandler(rootContext);
        } else if (uri.endsWith(SnapshotHandler.SNAPSHOT_POSTFIX)) {
            return new SnapshotHandler(rootContext);
        } else {
            return null;
        }
    }

    private boolean isValidXslUri(String uri) {
        return uri.endsWith("xsl");
    }

    protected TimeSeriesContext getRootContext() {
        return rootContext;
    }
}
