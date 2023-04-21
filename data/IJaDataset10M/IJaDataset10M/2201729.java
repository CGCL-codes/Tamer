package com.googlecode.gchart.gcharttestapp.client;

import com.googlecode.gchart.client.GChart;

/** Basic pie chart
 * <p>
 * 
 * This chart uses the built-in HTML-element based pie chart
 * rendering.  As of GChart 2.5, faster drawn, better
 * looking, solid-filled pie slices can be produced by
 * plugging an external canvas library into GChart.  See the
 * <tt>setCanvasFactory</tt> method's javadocs for the
 * details, and the various pie charts on GChart's live demo
 * for complete working examples.
 * 
 */
public class GChartExample07 extends GChart {

    GChartExample07() {
        double[] pieMarketShare = { 0.65, 0.20, 0.10, 0.05 };
        String[] pieTypes = { "Apple", "Cherry", "Pecan", "Bannana" };
        String[] pieColors = { "green", "red", "maroon", "yellow" };
        this.setChartSize(300, 200);
        setChartTitle("<h3>2008 Sales by Pie Flavor" + "<br>(Puny Pies, Inc.) </h3>");
        this.setLegendVisible(false);
        getXAxis().setAxisVisible(false);
        getYAxis().setAxisVisible(false);
        getXAxis().setAxisMin(0);
        getXAxis().setAxisMax(10);
        getXAxis().setTickCount(0);
        getYAxis().setAxisMin(0);
        getYAxis().setAxisMax(10);
        getYAxis().setTickCount(0);
        setInitialPieSliceOrientation(0.75 - pieMarketShare[0] / 2);
        for (int i = 0; i < pieMarketShare.length; i++) {
            addCurve();
            getCurve().addPoint(5, 5);
            getCurve().getSymbol().setSymbolType(SymbolType.PIE_SLICE_OPTIMAL_SHADING);
            getCurve().getSymbol().setBorderColor("white");
            getCurve().getSymbol().setBackgroundColor(pieColors[i]);
            getCurve().getSymbol().setModelWidth(6);
            getCurve().getSymbol().setHeight(0);
            getCurve().getSymbol().setFillSpacing(3);
            getCurve().getSymbol().setFillThickness(3);
            getCurve().getSymbol().setHovertextTemplate(GChart.formatAsHovertext(pieTypes[i] + ", " + Math.round(100 * pieMarketShare[i]) + "%"));
            getCurve().getSymbol().setPieSliceSize(pieMarketShare[i]);
            getCurve().getPoint().setAnnotationText(pieTypes[i]);
            getCurve().getPoint().setAnnotationLocation(AnnotationLocation.OUTSIDE_PIE_ARC);
        }
    }
}
