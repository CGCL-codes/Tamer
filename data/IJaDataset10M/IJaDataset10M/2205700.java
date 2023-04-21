package org.timepedia.chronoscopesamples.client;

import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.Datasets;
import org.timepedia.chronoscope.client.browser.ChartPanel;
import org.timepedia.chronoscope.client.browser.Chronoscope;
import org.timepedia.chronoscope.client.canvas.View;
import org.timepedia.chronoscope.client.canvas.ViewReadyCallback;
import org.timepedia.chronoscope.client.data.IncrementalDataResponse;
import org.timepedia.chronoscope.client.data.IncrementalHandler;
import org.timepedia.chronoscope.client.plot.DefaultXYPlot;
import org.timepedia.chronoscope.client.util.Interval;
import com.google.gwt.core.client.EntryPoint;

public class ChartDemoJagged implements EntryPoint {

    public void onModuleLoad() {
        Chronoscope.setMicroformatsEnabled(true);
        Chronoscope.initialize();
        Chronoscope chrono = Chronoscope.getInstance();
        ChartPanel chartPanel = Chronoscope.createTimeseriesChartById("chart", ChartDemoSlowIE.getJsons("jagged"), 480, 320, new ViewReadyCallback() {

            public void onViewReady(View view) {
                DefaultXYPlot theplot = (DefaultXYPlot) view.getChart().getPlot();
                theplot.setAutoZoomVisibleRange(0, true);
                theplot.setAnimationPreview(false);
                Datasets datasets = theplot.getDatasets();
                Dataset d = theplot.getDatasets().get(0);
                d.setIncrementalHandler(new IncrementalHandler() {

                    public void onDataNeeded(Interval domain, Dataset dataset, IncrementalDataResponse response) {
                    }
                });
                view.getChart().redraw();
            }
        });
    }

    ;
}
