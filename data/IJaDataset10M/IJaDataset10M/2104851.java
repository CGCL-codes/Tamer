package za.co.OO7J.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.Title;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import za.co.OO7J.utils.exporters.EPSExportUtil;
import za.co.OO7J.utils.exporters.SVGExportUtil;
import za.co.OO7J.utils.table.TableArray;

public class TraversalGraph {

    private static boolean HOT = true;

    private static boolean db40_basic_no_index = false;

    private static boolean VERSANT_WITHOUT_INDEX = false;

    private static boolean VERSANT_LARGE = true;

    private static String outputDir = "";

    static final Logger logger = Logger.getLogger(TraversalGraph.class);

    public static void createTraversalGraphs(String name, Hashtable<String, OO7Result> currentResults, List<String> persistenceConfigurations) {
        outputDir = name;
        HOT = true;
        createGraph(currentResults, persistenceConfigurations);
        HOT = false;
        createGraph(currentResults, persistenceConfigurations);
        calcutateCacheImprovement(currentResults, persistenceConfigurations);
    }

    private static void createGraph(Hashtable<String, OO7Result> currentResults, List<String> persistenceConfigurations) {
        String label = null;
        String fileName = null;
        String timeLabel = null;
        if (HOT) {
            label = "HOT";
            fileName = "hot_traversal_bar_graph.svg";
        } else {
            label = "COLD";
            fileName = "cold_traversal_bar_graph.svg";
        }
        if (SettingsUtil.USE_MILLISECONDS) {
            timeLabel = "Time In Milliseconds";
        } else {
            timeLabel = "Time In Seconds";
        }
        DefaultCategoryDataset dataset = addDataToGraph(currentResults, persistenceConfigurations);
        JFreeChart chart = ChartFactory.createBarChart(label + " traversal times", "Persistence mechanisms tested", timeLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        chart.getTitle().setPaint(Color.blue);
        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setBackgroundPaint(Color.white);
        categoryPlot.setRangeGridlinePaint(Color.red);
        final LogarithmicAxis rangeAxis = new LogarithmicAxis("Log(y) Time In Seconds");
        rangeAxis.setAllowNegativesFlag(false);
        rangeAxis.setLog10TickLabelsFlag(false);
        rangeAxis.setLowerMargin(0.0);
        rangeAxis.setUpperMargin(0.0);
        rangeAxis.setLowerBound(0.0001);
        rangeAxis.setUpperBound(400.0);
        if (HOT) {
            rangeAxis.setLowerBound(0.0001);
            rangeAxis.setUpperBound(50.0);
        }
        if (SettingsUtil.USE_RECOMMENDED && !HOT) {
            rangeAxis.setUpperBound(600.0);
        }
        categoryPlot.setRangeAxis(rangeAxis);
        {
            CategoryItemRenderer renderer = new BarRenderer();
            DecimalFormat decimalformat1 = new DecimalFormat("###.####");
            renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
            renderer.setItemLabelsVisible(true);
            renderer.setBaseItemLabelsVisible(true);
            ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.TOP_CENTER, TextAnchor.TOP_CENTER, 300.0);
            renderer.setPositiveItemLabelPosition(p);
            chart.getCategoryPlot().setRenderer(renderer);
        }
        try {
            File outputDirectory = new File(SettingsUtil.OUTPUT_DIRECTORY + outputDir);
            outputDirectory.delete();
            outputDirectory.mkdir();
            fileName = fileName.replace(".jpg", ".svg");
            File outputFile = new File(SettingsUtil.OUTPUT_DIRECTORY + outputDir + "/" + fileName);
            SVGExportUtil.exportChartAsSVG(chart, new Rectangle(new Dimension(1200, 400)), outputFile);
            fileName = fileName.replace(".svg", ".eps");
            outputFile = new File(SettingsUtil.OUTPUT_DIRECTORY + outputDir + "/" + fileName);
            EPSExportUtil.exportChartAsEPS(chart, new Rectangle(new Dimension(1200, 400)), outputFile);
            logger.debug("CREATED GRAPH:  " + label + " TRAVERSAL");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problem occurred creating traversal chart.");
        }
    }

    private static DefaultCategoryDataset addDataToGraph(Hashtable<String, OO7Result> currentResults, List<String> persistenceConfigurations) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        TableArray tableArrayHot = new TableArray();
        TableArray tableArrayCold = new TableArray();
        logger.debug("==========ADDING DATA TO GRAPH===============");
        for (Iterator iter = persistenceConfigurations.iterator(); iter.hasNext(); ) {
            String configuration = (String) iter.next();
            logger.info(" DB configuration: " + configuration);
            OO7Result result = currentResults.get(configuration);
            if (HOT) {
                Set operations = result.getWarmTraversals().keySet();
                Set sortedSet = new TreeSet<String>(operations);
                for (Iterator iterator = sortedSet.iterator(); iterator.hasNext(); ) {
                    String operation = (String) iterator.next();
                    Double time = result.getWarmTraversals().get(operation);
                    if (SettingsUtil.USE_MILLISECONDS) {
                        time *= 1000;
                    }
                    if (SettingsUtil.OUTPUT_AS_TABLE) {
                        tableArrayHot.addToTable(configuration, time, operation);
                    }
                    dataset.setValue(time, configuration, operation);
                }
            } else {
                Set operations = result.getColdTraversals().keySet();
                Set sortedSet = new TreeSet<String>(operations);
                for (Iterator iterator = sortedSet.iterator(); iterator.hasNext(); ) {
                    String operation = (String) iterator.next();
                    Double time = result.getColdTraversals().get(operation);
                    if (SettingsUtil.USE_MILLISECONDS) {
                        time *= 1000;
                    }
                    if (SettingsUtil.OUTPUT_AS_TABLE) {
                        tableArrayCold.addToTable(configuration, time, operation);
                    }
                    dataset.setValue(time, configuration, operation);
                }
            }
        }
        if (SettingsUtil.OUTPUT_AS_TABLE) {
            if (HOT) {
                System.out.println("======================Start hot table==================================");
                System.out.println(" HOT TRAVERSAL TABLE:");
                tableArrayHot.printTable();
                System.out.println("===============END HOT TABLE=========================================");
            } else {
                System.out.println("=========Start cold table===============================================");
                System.out.println(" COLD TRAVERSAL TABLE:");
                tableArrayCold.printTable();
                System.out.println("===============END COLD TABLE=========================================");
            }
        }
        return dataset;
    }

    public static void calcutateCacheImprovement(Hashtable<String, OO7Result> currentResults, List<String> persistenceConfigurations) {
        CacheUtil.calcutateCacheImprovement(outputDir, CacheUtil.TRAVERSALS, currentResults, persistenceConfigurations);
    }
}
