package ca.nanometrics.gflot.client.event;

import ca.nanometrics.gflot.client.jsni.Plot;

/**
 * @author Alexander De Leon
 */
public interface PlotHoverListener {

    void onPlotHover(Plot plot, PlotPosition position, PlotItem item);
}
