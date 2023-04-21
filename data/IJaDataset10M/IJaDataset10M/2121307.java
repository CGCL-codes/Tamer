package org.gwanted.gwt.widget.chart.client.util.logging.sinks;

import org.gwanted.gwt.core.client.util.logging.sinks.UserErrorSink;
import org.gwanted.gwt.widget.chart.client.ui.FlashNotInstalledException;
import org.gwanted.gwt.widget.chart.client.util.logging.ui.DefaultChartErrorWidget;
import org.gwanted.gwt.widget.chart.client.util.logging.ui.EWFlashException;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Diego Fernandez Santos
 */
public class ChartWidgetUserSink extends UserErrorSink {

    protected Widget createErrorWidget(final Throwable error) {
        Widget widget;
        if (error instanceof FlashNotInstalledException) {
            widget = new EWFlashException((FlashNotInstalledException) error);
        } else {
            widget = new DefaultChartErrorWidget(error);
        }
        return widget;
    }
}
