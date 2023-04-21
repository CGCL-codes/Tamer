package com.googlecode.gflot.examples.client.examples.image;

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.ImageDataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.Series;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.ImageSeriesOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.examples.client.examples.DefaultActivity;
import com.googlecode.gflot.examples.client.resources.Resources;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesData;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesRaw;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesSource;

/**
 * @author Nicolas Morel
 */
@GFlotExamplesRaw(ImagePlace.UI_RAW_SOURCE_FILENAME)
public class ImageExample extends DefaultActivity {

    private static Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, ImageExample> {
    }

    /**
     * Plot
     */
    @GFlotExamplesData
    @UiField(provided = true)
    SimplePlot plot;

    public ImageExample(Resources resources) {
        super(resources);
    }

    /**
     * Create plot
     */
    @GFlotExamplesSource
    public Widget createPlot() {
        final PlotModel model = new PlotModel();
        final PlotOptions plotOptions = new PlotOptions();
        plotOptions.addXAxisOptions(new AxisOptions().setMinimum(-8).setMaximum(4));
        plotOptions.addYAxisOptions(new AxisOptions().setMinimum(-8).setMaximum(4));
        SeriesHandler handlerImage = model.addSeries(new Series("Image series").setImageSeriesOptions(new ImageSeriesOptions().setShow(true).setAlpha(0.5)));
        handlerImage.add(new ImageDataPoint("images/hs-2004-27-a-large_web.jpg", -2, -2, 2, 2));
        SeriesHandler handlerLine = model.addSeries("Line series");
        handlerLine.add(new DataPoint(-8, -8));
        handlerLine.add(new DataPoint(-6, -4));
        handlerLine.add(new DataPoint(-2, -8));
        handlerLine.add(new DataPoint(4, 0));
        plot = new SimplePlot(model, plotOptions);
        plot.setLoadDataImages(true);
        return binder.createAndBindUi(this);
    }
}
