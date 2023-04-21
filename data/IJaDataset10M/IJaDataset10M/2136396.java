package ca.nanometrics.gflot.sample.client;

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.ImageDataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SeriesType;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.ImageSeriesOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImageExample implements GFlotExample {

    public String getName() {
        return "Image";
    }

    public Widget createExample() {
        final PlotModel model = new PlotModel();
        final PlotOptions plotOptions = new PlotOptions();
        plotOptions.setXAxisOptions(new AxisOptions().setMinimum(-8).setMaximum(4));
        plotOptions.setYAxisOptions(new AxisOptions().setMinimum(-8).setMaximum(4));
        SeriesHandler handlerImage = model.addSeries("Image series");
        handlerImage.getSeries().setSeriesOptions(SeriesType.IMAGE, new ImageSeriesOptions().setShow(true).setAlpha(0.5));
        handlerImage.add(new ImageDataPoint("images/hs-2004-27-a-large_web.jpg", -2, -2, 2, 2));
        SeriesHandler handlerLine = model.addSeries("Line series");
        handlerLine.add(new DataPoint(-8, -8));
        handlerLine.add(new DataPoint(-6, -4));
        handlerLine.add(new DataPoint(-2, -8));
        handlerLine.add(new DataPoint(4, 0));
        SimplePlot plot = new SimplePlot(model, plotOptions);
        plot.setLoadDataImages(true);
        plot.setSize("400px", "400px");
        final FlowPanel panel = new FlowPanel();
        panel.add(plot);
        return panel;
    }
}
