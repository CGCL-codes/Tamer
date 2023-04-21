package org.timepedia.chronoscope.client.event;

import org.timepedia.chronoscope.client.XYPlot;

/**
 * Fired by plot implementations when the context menu is trigged.
 */
public class PlotContextMenuEvent extends PlotEvent<PlotContextMenuHandler> {

    public static Type<PlotContextMenuHandler> TYPE = new Type<PlotContextMenuHandler>();

    private int clickX;

    private int clickY;

    public PlotContextMenuEvent(XYPlot plot, int clickX, int clickY) {
        super(plot);
        this.clickX = clickX;
        this.clickY = clickY;
    }

    public int getClickX() {
        return clickX;
    }

    public int getClickY() {
        return clickY;
    }

    public Type getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PlotContextMenuHandler plotContextMenuHandler) {
        plotContextMenuHandler.onContextMenu(this);
    }
}
