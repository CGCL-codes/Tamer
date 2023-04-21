package ie.lawlor.amvc.component.barchart;

import ie.lawlor.amvc.event.Event;
import ie.lawlor.amvc.swing.JPanelView;
import java.util.Collection;
import java.util.Iterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author blawlor
 *
 */
public class BarChartView extends JPanelView {

    JFreeChart chart;

    ChartPanel chartPanel;

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public BarChartView() {
        super();
    }

    public BarChartView(String name) {
        super(name);
        initComponents();
    }

    public void initComponents() {
        logger.debug("Initializing the chart view");
        chart = ChartFactory.createBarChart3D("", "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
        chartPanel = new ChartPanel(chart);
        add(chartPanel);
    }

    public void doUpdateView(Event e) {
        logger.debug("Entering");
        updatingView = true;
        BarChartModel model = (BarChartModel) e.getPayload();
        refreshData(model);
        chart = ChartFactory.createBarChart3D(model.getBarTitle(), model.getXTitle(), model.getYTitle(), dataset, PlotOrientation.VERTICAL, true, true, false);
        chartPanel.setChart(chart);
        updatingView = false;
    }

    private void refreshData(BarChartModel model) {
        dataset = new DefaultCategoryDataset();
        Collection barData = model.getBarData();
        for (Iterator itr = barData.iterator(); itr.hasNext(); ) {
            BarChartModel.BarData data = (BarChartModel.BarData) itr.next();
            dataset.setValue(data.getValue(), data.getCategoryName(), data.getSeriesName());
        }
    }
}
