package ca.nanometrics.gflot.client.event;

import ca.nanometrics.gflot.client.jsni.Plot;

/**
 * @author Alexander De Leon
 */
public interface PlotClickListener {

    void onPlotClick(Plot plot, PlotPosition position, PlotItem item);
}
