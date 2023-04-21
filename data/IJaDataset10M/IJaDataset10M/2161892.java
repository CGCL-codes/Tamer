package org.sf.charttaglibs.charts;

import javax.servlet.jsp.JspException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Class that build bar chart and 3d bar chart
 * @author mmohamed |at| dcc |dot| uchile |dot| cl
 */
public class BarTag extends CategoryBasedChart {

    private static final long serialVersionUID = -6278901341728950872L;

    /**
     * Overrides the initChart method of BaseChartTag
     * @see org.sf.charttaglibs.BaseChartTag#initChart()
     */
    public void initChart() throws JspException {
        if (collection != null) {
            DefaultCategoryDataset dataset = getDataSet();
            chart = null;
            PlotOrientation po = null;
            if (plotOrientation != null && plotOrientation.equalsIgnoreCase("HORIZONTAL")) po = PlotOrientation.HORIZONTAL; else po = PlotOrientation.VERTICAL;
            if (threeD) {
                chart = ChartFactory.createBarChart3D(title, domainAxisLabel, rangeAxisLabel, dataset, po, createLegend, createTooltip, createUrl);
            } else {
                chart = ChartFactory.createBarChart(title, domainAxisLabel, rangeAxisLabel, dataset, po, createLegend, createTooltip, createUrl);
            }
        }
    }
}
