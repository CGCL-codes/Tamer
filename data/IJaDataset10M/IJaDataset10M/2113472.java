package eu.planets_project.tb.impl.chart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import eu.planets_project.tb.api.data.util.DigitalObjectRefBean;
import eu.planets_project.tb.api.model.Experiment;
import eu.planets_project.tb.api.persistency.ExperimentPersistencyRemote;
import eu.planets_project.tb.impl.data.util.DataHandlerImpl;
import eu.planets_project.tb.impl.model.eval.mockup.TecRegMockup;
import eu.planets_project.tb.impl.model.exec.BatchExecutionRecordImpl;
import eu.planets_project.tb.impl.model.exec.ExecutionRecordImpl;
import eu.planets_project.tb.impl.model.exec.ExecutionStageRecordImpl;
import eu.planets_project.tb.impl.persistency.ExperimentPersistencyImpl;

/**
 * @author AnJackson
 *
 */
public class ExperimentExecutionCharts {

    /** */
    private static Log log = LogFactory.getLog(ExperimentExecutionCharts.class);

    public JFreeChart createXYChart(String expId) {
        ExperimentPersistencyRemote edao = ExperimentPersistencyImpl.getInstance();
        long eid = Long.parseLong(expId);
        log.info("Building experiment chart for eid = " + eid);
        Experiment exp = edao.findExperiment(eid);
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final String expName = exp.getExperimentSetup().getBasicProperties().getExperimentName();
        List<Boolean> success = new ArrayList<Boolean>();
        for (BatchExecutionRecordImpl batch : exp.getExperimentExecutable().getBatchExecutionRecords()) {
            int i = 1;
            for (ExecutionRecordImpl exr : batch.getRuns()) {
                if (exr != null && exr.getStages() != null) {
                    DigitalObjectRefBean dh = new DataHandlerImpl().get(exr.getDigitalObjectReferenceCopy());
                    String dobName = "Object " + i;
                    if (dh != null) dobName = dh.getName();
                    for (ExecutionStageRecordImpl exsr : exr.getStages()) {
                        Double time = exsr.getDoubleMeasurement(TecRegMockup.PROP_SERVICE_TIME);
                        if (time != null) {
                            dataset.addValue(time, expName, dobName);
                            if (exsr.isMarkedAsSuccessful()) {
                                success.add(Boolean.TRUE);
                            } else {
                                success.add(Boolean.FALSE);
                            }
                        }
                    }
                }
                i++;
            }
        }
        JFreeChart chart = ChartFactory.createBarChart(null, "Digital Object", "Time [s]", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        Paint[] customColours = new Paint[success.size()];
        for (int i = 0; i < success.size(); i++) {
            Boolean b = success.get(i);
            if (Boolean.TRUE.equals(b)) {
                customColours[i] = Color.GREEN;
            } else {
                customColours[i] = Color.RED;
            }
        }
        CategoryItemRenderer renderer = new CustomRenderer(customColours);
        plot.setRenderer(renderer);
        final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, Color.blue);
        final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, Color.pink);
        final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, Color.lightGray);
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator("xy_chart.jsp", "series", "section"));
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0));
        return chart;
    }

    /**
     * A custom renderer that returns a different color for each item in a
     * single series.
     */
    static class CustomRenderer extends BarRenderer {

        /**
		 * 
		 */
        private static final long serialVersionUID = 2695951723656220566L;

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour
         * inherited from AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(int row, int column) {
            return this.colors[column % this.colors.length];
        }
    }
}
