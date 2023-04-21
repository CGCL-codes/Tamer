package org.timepedia.chronoscope.client.browser.event;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Event;
import org.timepedia.chronoscope.client.Chart;

/**
 * Handles the event where the user depresses a key.
 *
 * @author Chad Takahashi
 */
public class ChartKeyDownHandler extends AbstractEventHandler<KeyDownHandler> implements KeyDownHandler {

    public static int DOWN_NUM = 0;

    public static int UP_NUM = 0;

    public final int KEY_DOWN_MAX = 5;

    public void onKeyDown(KeyDownEvent event) {
        ChartState chartInfo = getChartState(event);
        Chart chart = chartInfo.chart;
        boolean handled = true;
        int keyCode = event.getNativeKeyCode();
        if (keyCode == KeyCodes.KEY_PAGEUP) {
        } else if (keyCode == KeyCodes.KEY_PAGEDOWN) {
        } else if (keyCode == KeyCodes.KEY_UP) {
            if (UP_NUM < KEY_DOWN_MAX) {
                if (UP_NUM != 0) {
                    chart.nextZoom();
                }
                UP_NUM++;
            }
        } else if (keyCode == KeyCodes.KEY_DOWN) {
            if (DOWN_NUM < KEY_DOWN_MAX) {
                if (DOWN_NUM != 0) {
                    chart.prevZoom();
                }
                DOWN_NUM++;
            }
        } else if (keyCode == KeyCodes.KEY_TAB) {
            handled = handleTabKey((Event) event.getNativeEvent(), chartInfo, keyCode, event.isShiftKeyDown());
        } else {
            handled = false;
        }
        chartInfo.setHandled(handled);
        if (handled) {
            event.stopPropagation();
            event.preventDefault();
        }
    }
}
