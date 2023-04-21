package org.timepedia.chronoscope.client.browser.event;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Event;
import org.timepedia.chronoscope.client.Chart;
import org.timepedia.chronoscope.client.Cursor;
import org.timepedia.chronoscope.client.Overlay;
import org.timepedia.chronoscope.client.canvas.Bounds;

/**
 * Handles the event where the user depresses a mouse button.
 *
 * @author Chad Takahashi
 */
public final class ChartMouseDownHandler extends AbstractEventHandler<MouseDownHandler> implements MouseDownHandler {

    public void onMouseDown(MouseDownEvent event) {
        ChartState chartInfo = getChartState(event);
        Chart chart = chartInfo.chart;
        int x = getLocalX(event);
        int y = getLocalY(event);
        if (y > (chart.getView().getHeight() - chart.getPlot().getOverviewAxisPanel().getBounds().height)) {
            int overviewX = (int) chart.getPlot().getBounds().x;
            Bounds highlightBounds = chart.getPlot().getOverviewAxisPanel().getHighlightBounds();
            if (highlightBounds != null) {
                int hiliteX = (int) highlightBounds.x;
                System.out.println("   MOUSEDOWN    x:" + x + " overviewX:" + overviewX + " hiliteX:" + hiliteX);
                OverviewAxisMouseMoveHandler.setHiliteRelativeGrabX((double) ((x - overviewX) - hiliteX));
            }
        }
        boolean handled;
        if (event.getNativeButton() == Event.BUTTON_RIGHT) {
            handled = false;
        } else {
            CompoundUIAction uiAction = chartInfo.getCompoundUIAction();
            uiAction.setSource(getComponent(x, y, chart.getPlot()));
            if (event.isShiftKeyDown()) {
                chart.setCursor(Cursor.SELECTING);
                uiAction.setSelectAction(true);
            } else {
                chart.setCursor(Cursor.DRAGGING);
                uiAction.setSelectAction(false);
            }
            uiAction.setStartX(x);
            if (uiAction.getSource() instanceof Overlay) {
                handled = true;
            } else {
                chart.setPlotFocus(x, y);
                handled = true;
            }
        }
        chartInfo.setHandled(handled);
    }
}
