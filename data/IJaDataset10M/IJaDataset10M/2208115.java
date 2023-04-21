package org.timepedia.chronoscope.client.browser.event;

import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import org.timepedia.chronoscope.client.Chart;

/**
 * Handles the event where  the user double-clicks on the chart.
 *
 * @author Chad Takahashi
 */
public class ChartDblClickHandler extends AbstractEventHandler<DoubleClickHandler> implements DoubleClickHandler {

    public void onDoubleClick(DoubleClickEvent event) {
        ChartState chartInfo = getChartState(event);
        Chart chart = chartInfo.chart;
        chart.setAnimating(false);
        boolean handled = false;
        if (chart.maxZoomTo(getLocalX(event), getLocalY(event))) {
            event.stopPropagation();
            event.preventDefault();
            handled = true;
        }
        chartInfo.setHandled(handled);
    }
}
