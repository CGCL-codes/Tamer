package com.mentalray.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class ChartGenerator {

    public JFreeChart createPieChart(PieDataset dataset, String name, Boolean legend, Boolean tooltip) {
        JFreeChart chart = ChartFactory.createPieChart3D(name, dataset, legend, tooltip, false);
        chart.setAntiAlias(true);
        chart.setBackgroundImageAlpha((float) 0.1);
        chart.setBackgroundPaint(null);
        chart.setTextAntiAlias(true);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setNoDataMessage("There is no data to display");
        plot.setSimpleLabels(true);
        plot.setLabelFont(new Font("Tahoma", Font.PLAIN, 16));
        chart.setBorderVisible(false);
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }

    public JFreeChart createBarChart(CategoryDataset dataset, String name, Boolean legend, Boolean tooltip) {
        JFreeChart chart = ChartFactory.createBarChart3D(name, "Grade", "Students", dataset, PlotOrientation.VERTICAL, legend, tooltip, false);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundAlpha((float) 0.5);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAxisLinePaint(Color.BLACK);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setShadowPaint(Color.red);
        renderer.setBaseOutlinePaint(Color.BLACK);
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setAxisLinePaint(Color.BLACK);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        return chart;
    }
}
