package org.timepedia.chronoscope.client.browser.event;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.History;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import org.timepedia.chronoscope.client.Chart;
import org.timepedia.chronoscope.client.browser.SafariKeyboardConstants;

/**
 * Handles the event where the user releases a key
 * 
 * @author Chad Takahashi
 */
public final class ChartKeyUpHandler extends AbstractEventHandler<KeyUpHandler> implements KeyUpHandler {

    public static final double FULL_PAGE_SCROLL = 1.0;

    private static final double HALF_PAGE_SCROLL = 0.5;

    public void onKeyUp(KeyUpEvent event) {
        ChartState chartInfo = getChartState(event);
        Chart chart = chartInfo.chart;
        int keyCode = event.getNativeKeyCode();
        boolean handled = true;
        if (isPageUp(keyCode)) {
            chart.pageLeft(FULL_PAGE_SCROLL);
        } else if (event.isLeftArrow() || isKeyLeft(keyCode)) {
            chart.pageLeft(HALF_PAGE_SCROLL);
        } else if (isPageDown(keyCode)) {
            chart.pageRight(FULL_PAGE_SCROLL);
        } else if (event.isRightArrow() || isKeyRight(keyCode)) {
            chart.pageRight(HALF_PAGE_SCROLL);
        } else if (event.isUpArrow() || isNextZoom(keyCode)) {
            chart.nextZoom();
            ChartKeyDownHandler.UP_NUM = 0;
        } else if (event.isDownArrow() || isPrevZoom(keyCode)) {
            chart.prevZoom();
            ChartKeyDownHandler.DOWN_NUM = 0;
        } else if (keyCode == KeyCodes.KEY_BACKSPACE) {
            History.back();
        } else if (isMaxZoomOut(keyCode)) {
            chart.maxZoomOut();
        } else {
            handled = false;
        }
        chartInfo.setHandled(handled);
        if (handled) {
            event.stopPropagation();
            event.preventDefault();
        }
    }

    private static boolean isNextZoom(int keyCode) {
        return keyCode == KeyCodes.KEY_UP || keyCode == ChartKeyPressHandler.KEY_Z || keyCode == SafariKeyboardConstants.SAFARI_UP;
    }

    private static boolean isPrevZoom(int keyCode) {
        return keyCode == KeyCodes.KEY_DOWN || keyCode == SafariKeyboardConstants.SAFARI_DOWN || keyCode == ChartKeyPressHandler.KEY_X;
    }

    private static boolean isMaxZoomOut(int keyCode) {
        return keyCode == KeyCodes.KEY_HOME || keyCode == SafariKeyboardConstants.SAFARI_HOME;
    }

    private static boolean isKeyLeft(int keyCode) {
        return keyCode == KeyCodes.KEY_LEFT || keyCode == SafariKeyboardConstants.SAFARI_LEFT;
    }

    private static boolean isKeyRight(int keyCode) {
        return keyCode == KeyCodes.KEY_RIGHT || keyCode == SafariKeyboardConstants.SAFARI_RIGHT;
    }

    private static boolean isPageUp(int keyCode) {
        return keyCode == KeyCodes.KEY_PAGEUP || keyCode == SafariKeyboardConstants.SAFARI_PGUP;
    }

    private static boolean isPageDown(int keyCode) {
        return keyCode == KeyCodes.KEY_PAGEDOWN || keyCode == SafariKeyboardConstants.SAFARI_PDWN;
    }
}
