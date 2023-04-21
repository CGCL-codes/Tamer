package de.laures.cewolf;

import java.io.OutputStream;
import java.io.IOException;
import java.awt.Image;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.jrefinery.data.Dataset;
import com.jrefinery.chart.JFreeChart;
import com.jrefinery.chart.ChartFactory;
import com.jrefinery.chart.ChartUtilities;
import de.laures.cewolf.ChartDescription;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.util.ImageHelper;

/**
 * An abstract implementation of a ChartRenderer which provides some basic
 * services to its subclasses. It defines constant integer values for all
 * supported chart types which correspond to the index of types defined
 * in the ChartType class.
 * Additionally a subclass only needs to provide a JFreeChart instance. The
 * actual rendering process iis handled by this class.
 * @see ChartRenderer
 * @see ChartTypes
 * @see JFreeChart
 * @author  Guido Laures
 */
public abstract class AbstractChartRenderer implements ChartRenderer {

    public static final int AREA = 0;

    public static final int AREA_XY = 1;

    public static final int HORIZONTAL_BAR = 2;

    public static final int HORIZONTAL_BAR_3D = 3;

    public static final int LINE = 4;

    public static final int PIE = 5;

    public static final int SCATTER = 6;

    public static final int STACKED_HORIZONTAL_BAR = 7;

    public static final int STACKED_VERTICAL_BAR = 8;

    public static final int STACKED_VERTICAL_BAR_3D = 9;

    public static final int TIME_SERIES = 10;

    public static final int VERTICAL_BAR = 11;

    public static final int VERTICAL_BAR_3D = 12;

    public static final int XY = 13;

    public static final int CANDLE_STICK = 14;

    public static final int HIGH_LOW = 15;

    public static final int GANTT = 16;

    public static final int WIND = 17;

    public static final int SIGNAL = 18;

    public static final int VERRTICAL_XY_BAR = 19;

    public static final int PIE_3D = 20;

    /**
     * This is the implemenmtation of ChartRenderer. It uses the JFreeChart
     * rendering capabilities to render the chart provided by the concrete
     * subclasses into the given OutputStream as PNG.
     * @param cd the descriptio of the chart to render
     * @param out the stream to render intp
     * @throws ChartRenderingException if an errror occures during rendering
     * @throws IOException if an IO error occures during writing to the stream
     */
    public void renderChart(ChartDescription cd, OutputStream out) throws ChartRenderingException, IOException {
        final int width = cd.getWidth();
        final int height = cd.getHeight();
        final DatasetProducer dp = cd.getDatasetProducer();
        Dataset data = null;
        try {
            data = (Dataset) dp.produceDataset(cd.getDatasetProductionParams());
        } catch (DatasetProduceException dpex) {
            throw new ChartRenderingException(dpex.getMessage());
        }
        if (data == null) {
            throw new ChartRenderingException("no data for graphics generated");
        }
        JFreeChart chart = null;
        try {
            chart = getChartInstance(cd, data);
            String bgImg = cd.getBackgroundImage();
            chart.setAntiAlias(cd.getAntialias());
            if (bgImg != null) {
                Image img = ImageHelper.loadImage(bgImg);
                chart.setBackgroundImage(img);
                chart.setBackgroundImageAlpha(cd.getBackgroundImageAlpha());
            }
            if (cd.getPaint() != null) {
                chart.setBackgroundPaint(cd.getPaint());
            }
            if (!cd.getLegend()) {
                chart.setLegend(null);
            }
        } catch (ConfigurationException dpex) {
            throw new ChartRenderingException(dpex.getMessage());
        }
        ChartUtilities.writeChartAsPNG(out, chart, cd.getWidth(), cd.getHeight());
    }

    /**
     * Service method for subclasses to get an integer value for the string
     * representation of a chart type.
     * @param type the string represantation of a char type (e.g. <code>pie</code>
     * @return one of the constant int values of this class
     */
    protected static final int getChartTypeConstant(String type) {
        return ChartTypes.typeList.indexOf(type.toLowerCase());
    }

    /**
     * Subclasses need only implement this method to get thei charts rendered.
     * @param cd the chart despription of the chart to be rendered
     * @param data the dataset which has to be used by the chart to be rendered
     * @throws ConfigurationException if the provided description or dataset
     * are misconfigured.
     */
    protected abstract JFreeChart getChartInstance(ChartDescription cd, Dataset data) throws ConfigurationException;
}
