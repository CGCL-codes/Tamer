package eu.roelbouwman.peugeot304.forms;

import static eu.roelbouwman.peugeot304.Application.getApp;
import java.util.ResourceBundle;
import org.karora.cooee.app.Border;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.Color;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Font;
import org.karora.cooee.app.Grid;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.TextField;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.WindowPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.karora.cooee.sandbox.chart.app.ChartDisplay;
import org.karora.cooee.app.Column;
import org.karora.cooee.app.ContentPane;

/**
 * The Class ChartForm.
 */
public class ChartForm extends ContentPane {

    /** The chart display. */
    private ChartDisplay chartDisplay;

    /** The col. */
    private Column col;

    /** The d. */
    private DefaultCategoryDataset d;

    /** The resource bundle. */
    private ResourceBundle resourceBundle;

    /**
         * Instantiates a new chart form.
         * 
         * @param dataset the dataset
         */
    public ChartForm(DefaultCategoryDataset dataset) {
        d = dataset;
        initComponents();
    }

    /**
         * Inits the components.
         */
    private void initComponents() {
        resourceBundle = ResourceBundle.getBundle("eu.roelbouwman.resources.localization.peugeot304_" + getApp().getLanguage());
        col = new Column();
        col.setInsets(new Insets(20, 20, 0, 0));
        add(col);
        JFreeChart chart = createChart();
        chartDisplay = new ChartDisplay(chart);
        chartDisplay.setWidth(new Extent(600));
        chartDisplay.setHeight(new Extent(300));
        chartDisplay.setBackground(Color.BLUE);
        col.add(chartDisplay);
    }

    /**
         * Creates the chart.
         * 
         * @return the j free chart
         */
    private JFreeChart createChart() {
        System.setProperty("java.awt.headless", "true");
        JFreeChart chart = ChartFactory.createLineChart(resourceBundle.getString("CHART.TITLE"), resourceBundle.getString("CHART.X"), resourceBundle.getString("CHART.Y"), d, PlotOrientation.VERTICAL, false, false, false);
        chart.setBorderVisible(true);
        chart.setBackgroundPaint(java.awt.Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(java.awt.Color.lightGray);
        plot.setRangeGridlinePaint(java.awt.Color.white);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setFillPaint(java.awt.Color.white);
        return chart;
    }
}
