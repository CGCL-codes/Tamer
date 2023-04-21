package com.google.api.chart;

/**
 * 
 * @author sorenad
 *
 */
public class ChartLabelsTest extends ChartMakerTestCase {

    /**
	 * @see http://code.google.com/apis/chart/labels.html#chart_title
	 */
    public void testChartTitle_01() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().title("Site visitors by month", "January to July"));
        assertURL(m, "&chtt=Site+visitors+by+month|January+to+July");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#chart_title
	 */
    public void testChartTitle_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().title(ColorMaker.rrggbb("FF0000"), 20, "Site visitors"));
        assertURL(m, "&chtt=Site+visitors");
        assertURL(m, "&chts=FF0000,20");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#chart_legend
	 */
    public void testChartLegend_01() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().legend("NASDAQ", "FTSE100", "DOW"));
        assertURL(m, "&chdl=NASDAQ|FTSE100|DOW");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#chart_legend
	 */
    public void testChartDataLabel_WithPositions() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().legend(ChartMaker.legend("First", "Second", "Third").top()));
        assertURL(m, "&chdl=First|Second|Third");
        assertURL(m, "&chdlp=t");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#pie_labels
	 */
    public void testChartDataLabel_WithPositions_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.pie().label("May", "Jun", "Jul", "Aug", "Sep", "Oct"));
        assertURL(m, "&chl=May|Jun|Jul|Aug|Sep|Oct");
        assertURL(m, "?cht=p");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#gom_labels
	 */
    public void testChartLabel_GoogleOMeter() {
        ChartMaker m = new ChartMaker().type(ChartMaker.googleometer().label("Hello"));
        assertURL(m, "&chl=Hello");
        assertURL(m, "?cht=gom");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_01() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX(), ChartMaker.axisLeftY(), ChartMaker.axisRightY(), ChartMaker.axisTopX()));
        assertURL(m, "&chxt=x,y,r,t");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_Labels() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX().labels("Jan", "July", "Jan", "July", "Jan"), ChartMaker.axisLeftY().labels("0", "50", "100"), ChartMaker.axisRightY().labels("A", "B", "C"), ChartMaker.axisBottomX().labels("2005", "2006", "2007")));
        assertURL(m, "&chxt=x,y,r,x");
        assertURL(m, "&chxl=0:|Jan|July|Jan|July|Jan|1:|0|50|100|2:|A|B|C|3:|2005|2006|2007");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_Labels_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX().labels("Jan", "July", "Jan", "July", "Jan"), ChartMaker.axisLeftY(), ChartMaker.axisRightY().labels("A", "B", "C"), ChartMaker.axisBottomX().labels("2005", "", "2006", "", "2007")));
        assertURL(m, "&chxt=x,y,r,x");
        assertURL(m, "&chxl=0:|Jan|July|Jan|July|Jan|2:|A|B|C|3:|2005||2006||2007");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_Styles() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX(), ChartMaker.axisLeftY(), ChartMaker.axisRightY().labels(new double[] { 10, 35, 75 }, "min", "average", "max")));
        assertURL(m, "&chxt=x,y,r");
        assertParam(m, "chxl", "2:|min|average|max");
        assertParam(m, "chxp", "2,10,35,75");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_Styles_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX(), ChartMaker.axisLeftY().positions(10, 35, 75)));
        assertURL(m, "&chxt=x,y");
        assertParam(m, "chxp", "1,10,35,75");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_Range() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX().range(100, 500), ChartMaker.axisLeftY().range(0, 200), ChartMaker.axisRightY().range(1000, 0)));
        assertParam(m, "chxt", "x,y,r");
        assertParam(m, "chxr", "0,100,500|1,0,200|2,1000,0");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_Range_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX().range(10, 50, 5)));
        assertParam(m, "chxt", "x");
        assertParam(m, "chxr", "0,10,50,5");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_StyleAxis_01() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX(), ChartMaker.axisLeftY().labels("min", "average", "max").positions(10, 35, 75), ChartMaker.axisRightY().range(0, 4), ChartMaker.axisBottomX().labels("Jan", "Feb", "Mar").style(ChartMaker.axisStyle().color(ColorMaker.rrggbb("0000DD")).fontSize(13))));
        assertParam(m, "chxt", "x,y,r,x");
        assertParam(m, "chxr", "2,0,4");
        assertParam(m, "chxl", "1:|min|average|max|3:|Jan|Feb|Mar");
        assertParam(m, "chxp", "1,10,35,75");
        assertParam(m, "chxs", "3,0000DD,13");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_StyleAxis_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX(), ChartMaker.axisLeftY(), ChartMaker.axisRightY().labels("min", "average", "max").positions(10, 35, 95).style(ChartMaker.axisStyle().color(ColorMaker.rrggbb("0000DD")).fontSize(13).alignLeft().drawTickmarksOnly().tickmarkColor(ColorMaker.rrggbb("FF0000"))), ChartMaker.axisBottomX().labels("Jan", "Feb", "Mar")));
        assertParam(m, "chxt", "x,y,r,x");
        assertParam(m, "chxl", "2:|min|average|max|3:|Jan|Feb|Mar");
        assertParam(m, "chxp", "2,10,35,95");
        assertParam(m, "chxs", "2,0000DD,13,-1,t,FF0000");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#axis_styles_labels
	 */
    public void testChartAxisStyleAndLabel_StyleAxis_03() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().axes(ChartMaker.axisBottomX(), ChartMaker.axisLeftY().tickmarkLength(10), ChartMaker.axisRightY().style(ChartMaker.axisStyle().color(ColorMaker.rrggbb("0000DD")).fontSize(13)).tickmarkLength(-180).positions(10, 35, 95), ChartMaker.axisBottomX()));
        assertParam(m, "chxt", "x,y,r,x");
        assertParam(m, "chxp", "2,10,35,95");
        assertParam(m, "chxs", "2,0000DD,13");
        assertParam(m, "chxtc", "1,10|2,-180");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#data_point_labels
	 */
    public void testChartDataPointLabels_01() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().dataLabels(ChartMaker.datalabel().text("Min").color("0000FF").dataSetIndex(0).dataPoint(1).fontSize(10), ChartMaker.datalabel().flag("Max").color("FF0000").dataSetIndex(0).dataPoint(3).fontSize(15)));
        assertParam(m, "chm", "tMin,0000FF,0,1,10|fMax,FF0000,0,3,15");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#data_point_labels
	 */
    public void testChartDataPointLabels_02() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().dataLabels(ChartMaker.datalabel().number(ChartMaker.numberlabel().percentage().precision(0)).color("000000").dataSetIndex(0).dataPointAll().fontSize(11)));
        assertParam(m, "chm", "N*p0*,000000,0,-1,11");
    }

    /**
	 * @see http://code.google.com/apis/chart/labels.html#data_point_labels
	 */
    public void testChartDataPointLabels_03() {
        ChartMaker m = new ChartMaker().type(ChartMaker.bar().dataLabels(ChartMaker.datalabel().number(ChartMaker.numberlabel().currency("EUR").precision(1)).color("000000").dataSetIndex(0).dataPointAll().fontSize(11)));
        assertParam(m, "chm", "N*cEUR1*,000000,0,-1,11");
    }
}
