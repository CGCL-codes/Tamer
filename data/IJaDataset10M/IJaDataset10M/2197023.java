package equilibrium.commons.report.chart.spider;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.RadarChartDataSet;
import org.krysalis.jcharts.nonAxisChart.RadarChart;
import org.krysalis.jcharts.properties.ChartProperties;
import org.krysalis.jcharts.properties.RadarChartProperties;
import org.krysalis.jcharts.properties.util.ChartFont;
import equilibrium.commons.bean.BeansUtil;
import equilibrium.commons.report.chart.ChartType;
import equilibrium.commons.report.chart.EasyReportsChart;
import equilibrium.commons.report.chart.EasyReportsChartDataset;
import equilibrium.commons.report.chart.EasyReportsChartFactory;
import equilibrium.commons.report.chart.EasyReportsChartProperties;
import equilibrium.commons.report.chart.EasyReportsException;
import equilibrium.commons.report.chart.KrysalisChartAdapter;
import equilibrium.commons.report.chart.SpiderChartDataset;
import equilibrium.commons.report.chart.SpiderChartProperties;

public class SpiderChartFactory extends EasyReportsChartFactory {

    private int scaleMaxValue = 100;

    private int scaleIncrement = 10;

    public EasyReportsChart createChart(EasyReportsChartDataset dataset, EasyReportsChartProperties chartProperties) {
        BeansUtil.checkInstanceOf(chartProperties, SpiderChartProperties.class);
        BeansUtil.checkInstanceOf(dataset, SpiderChartDataset.class);
        SpiderChartDataset spiderDataset = (SpiderChartDataset) dataset;
        RadarChart chart = createRadarChart(spiderDataset.getValuesLabels(), spiderDataset.getValues(), chartProperties.getOutputWidht(), chartProperties.getOutputHeight());
        KrysalisChartAdapter adapter = new KrysalisChartAdapter(chart, ChartType.SPIDER, chartProperties);
        return adapter;
    }

    private RadarChart createRadarChart(String[] labels, double[] dataTable, int width, int height) {
        double[][] data = new double[][] { dataTable };
        String[] legendLabels = { "" };
        Paint[] paints = { Color.GREEN };
        RadarChartProperties radarChartProperties = new RadarChartProperties();
        radarChartProperties.setFillRadar(true);
        radarChartProperties.setShowGridLines(true);
        radarChartProperties.setScaleMaxValue(scaleMaxValue);
        radarChartProperties.setScaleIncrement(scaleIncrement);
        Font font = new Font("Helvetica", Font.BOLD, 14);
        ChartFont chartFont = new ChartFont(font, Color.BLACK);
        radarChartProperties.setAxisLabelChartFont(chartFont);
        RadarChartDataSet dataset = null;
        try {
            dataset = new RadarChartDataSet("  ", data, legendLabels, paints, labels, radarChartProperties);
        } catch (ChartDataException e) {
            throw new EasyReportsException("Unable to create data set for radar chart. ", e);
        }
        ChartProperties chartProperties = new ChartProperties();
        chartProperties.setUseAntiAliasing(true);
        chartProperties.setEdgePadding(5);
        RadarChart chart = new RadarChart(dataset, null, chartProperties, width, height);
        return chart;
    }
}
