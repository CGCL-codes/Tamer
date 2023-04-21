package com.rapidminer.operator.visualization;

import java.awt.Graphics;
import java.io.ObjectStreamException;
import java.util.Collections;
import com.rapidminer.datatable.DataTable;
import com.rapidminer.datatable.SimpleDataTable;
import com.rapidminer.gui.plotter.Plotter;
import com.rapidminer.gui.plotter.PlotterConfigurationModel;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.ResultObjectAdapter;
import com.rapidminer.report.Renderable;
import com.rapidminer.tools.OperatorService;

/**
 * This object can usually not be passed to other operators but can simply be
 * used for the inline visualization of a Lift Pareto chart (without a dialog).
 * 
 * @author Ingo Mierswa
 */
public class LiftParetoChart extends ResultObjectAdapter implements Renderable {

    static {
        OperatorService.registerIOObjects(Collections.<Class<? extends IOObject>>singletonList(LiftParetoChart.class));
    }

    private static final long serialVersionUID = 7559555964863472326L;

    private transient Plotter plotter;

    private final SimpleDataTable liftChartData;

    private final String targetValue;

    private final boolean showBarLabels;

    private final boolean showCumulativeLabels;

    private final boolean rotateLabels;

    public LiftParetoChart(SimpleDataTable liftChartData, String targetValue, boolean showBarLabels, boolean showCumulativeLabels, boolean rotateLabels) {
        this.liftChartData = liftChartData;
        this.targetValue = targetValue;
        this.showBarLabels = showBarLabels;
        this.showCumulativeLabels = showCumulativeLabels;
        this.rotateLabels = rotateLabels;
        PlotterConfigurationModel settings = new PlotterConfigurationModel(PlotterConfigurationModel.PARETO_PLOT, liftChartData);
        this.plotter = settings.getPlotter();
    }

    public DataTable getLiftChartData() {
        return this.liftChartData;
    }

    public String getTargetValue() {
        return this.targetValue;
    }

    public boolean showBarLabels() {
        return showBarLabels;
    }

    public boolean showCumulativeLabels() {
        return showCumulativeLabels;
    }

    public boolean rotateLabels() {
        return rotateLabels;
    }

    @Override
    public String getName() {
        return "Lift Chart";
    }

    @Override
    public String toString() {
        return "A visualization of the discretized confidences together with the counts for " + targetValue + ".";
    }

    public String getExtension() {
        return "lpc";
    }

    public String getFileDescription() {
        return "Lift Pareto Chart files";
    }

    public void prepareRendering() {
        plotter.prepareRendering();
    }

    public void finishRendering() {
        plotter.finishRendering();
    }

    public int getRenderHeight(int preferredHeight) {
        return plotter.getRenderHeight(preferredHeight);
    }

    public int getRenderWidth(int preferredWidth) {
        return plotter.getRenderWidth(preferredWidth);
    }

    public void render(Graphics graphics, int width, int height) {
        plotter.render(graphics, width, height);
    }

    private Object readResolve() throws ObjectStreamException {
        PlotterConfigurationModel settings = new PlotterConfigurationModel(PlotterConfigurationModel.PARETO_PLOT, liftChartData);
        this.plotter = settings.getPlotter();
        return this;
    }
}
