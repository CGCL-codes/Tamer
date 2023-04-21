package com.bitgate.util.services.engine.tags.ext;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.w3c.dom.Node;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.node.NodeUtil;
import com.bitgate.util.services.engine.RenderEngine;

/**
 * This element creates a polar-based chart.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/services/engine/tags/ext/PolarChart.java#3 $
 */
public class PolarChart {

    private RenderEngine c;

    private HashMap tags;

    private String title, orientation;

    public PolarChart(String title, HashMap tags, String orientation, RenderEngine c) {
        this.title = title;
        this.tags = tags;
        this.orientation = orientation;
        this.c = c;
    }

    private Date date(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date result = calendar.getTime();
        return result;
    }

    public Color parseColor(String color) {
        if (color.equalsIgnoreCase("black")) {
            return Color.black;
        } else if (color.equalsIgnoreCase("blue")) {
            return Color.blue;
        } else if (color.equalsIgnoreCase("cyan")) {
            return Color.cyan;
        } else if (color.equalsIgnoreCase("darkgray")) {
            return Color.darkGray;
        } else if (color.equalsIgnoreCase("gray")) {
            return Color.gray;
        } else if (color.equalsIgnoreCase("green")) {
            return Color.green;
        } else if (color.equalsIgnoreCase("lightgray")) {
            return Color.lightGray;
        } else if (color.equalsIgnoreCase("magenta")) {
            return Color.magenta;
        } else if (color.equalsIgnoreCase("orange")) {
            return Color.orange;
        } else if (color.equalsIgnoreCase("pink")) {
            return Color.pink;
        } else if (color.equalsIgnoreCase("red")) {
            return Color.red;
        } else if (color.equalsIgnoreCase("white")) {
            return Color.white;
        } else if (color.equalsIgnoreCase("yellow")) {
            return Color.yellow;
        } else {
            return Color.black;
        }
    }

    public JFreeChart render(Node rootNode) {
        String logTime = null;
        if (c.isBreakState() || !c.canRender("u")) {
            return null;
        }
        int numChartData = Integer.parseInt(NodeUtil.walkNodeTree(rootNode, "count(chartdata)"));
        String domainLabel = NodeUtil.walkNodeTree(rootNode, "label/@domain");
        String rangeLabel = NodeUtil.walkNodeTree(rootNode, "label/@range");
        String unitLabel = NodeUtil.walkNodeTree(rootNode, "label/@unit");
        boolean hasSubtasks = false;
        if (domainLabel == null || domainLabel.equals("")) {
            domainLabel = "Task";
        }
        if (rangeLabel == null || rangeLabel.equals("")) {
            rangeLabel = "Date";
        }
        if (unitLabel == null || unitLabel.equals("")) {
            unitLabel = "Values";
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 1; i < (numChartData + 1); i++) {
            Node currentChartData = NodeUtil.selectNode(rootNode, "chartdata[" + i + "]");
            String varname = NodeUtil.walkNodeTree(currentChartData, "@var");
            String seriesName = NodeUtil.walkNodeTree(currentChartData, "@name");
            HashMap hMapRoot = c.getVariableContainer().getHashMap(varname);
            XYSeries series = new XYSeries(seriesName);
            int counter = 1;
            Debug.debug("Iterating data nodes in series name '" + seriesName + "'");
            while (true) {
                if (hMapRoot == null) {
                    Debug.debug("Hashmap for object '" + varname + "' does not exist.");
                    break;
                }
                HashMap hMap = null;
                if (hMapRoot.get("" + counter) != null) {
                    hMap = (HashMap) hMapRoot.get("" + counter);
                } else {
                    Debug.debug("No more objects after position '" + counter + "'");
                    break;
                }
                String argX = (String) hMap.get("x");
                String argY = (String) hMap.get("y");
                series.add(Float.parseFloat(argX), Float.parseFloat(argY));
                counter++;
            }
            dataset.addSeries(series);
        }
        JFreeChart chart = null;
        String chartMode = NodeUtil.walkNodeTree(rootNode, "@mode");
        if (chartMode == null || chartMode.equals("")) {
            chartMode = "flat";
        }
        chart = ChartFactory.createPolarChart(title, dataset, true, false, false);
        if (chart == null) {
            c.setExceptionState(true, "Unable to create a chart successfully.");
            return null;
        }
        return chart;
    }
}
