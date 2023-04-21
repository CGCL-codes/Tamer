package org.timepedia.chronoscope.client.event;

import org.timepedia.chronoscope.client.XYPlot;
import org.timepedia.chronoscope.client.util.Interval;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Export;

/**
 * Fired by plot implementations when the chart domain changes.
 */
@ExportPackage("chronoscope")
public class PlotMovedEvent extends PlotEvent<PlotMovedHandler> implements Exportable {

    public enum MoveType {

        DRAGGED, PAGED, ZOOMED, CENTERED
    }

    public static Type<PlotMovedHandler> TYPE = new Type<PlotMovedHandler>();

    private Interval domain;

    private MoveType moveType;

    public PlotMovedEvent(XYPlot plot, Interval domain, MoveType moveType) {
        super(plot);
        this.domain = domain;
        this.moveType = moveType;
    }

    @Export
    public Interval getDomain() {
        return domain;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public Type getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(PlotMovedHandler plotMovedHandler) {
        plotMovedHandler.onMoved(this);
    }
}
