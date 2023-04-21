package com.googlecode.gchart.gcharttestapp.client;

import com.googlecode.gchart.client.GChart;

public class TestGChart17b extends GChart {

    TestGChart17b() {
        super(300, 300);
        setChartTitle(GChartTestApp.getTitle(this));
        setLegendVisible(false);
        setChartFootnotes("Check: point labels/hover annotation at x=1, 2, 3, 4 are in <br>" + "normal, bold, italic, and bold-italic fonts, <br>" + "are in colors: default, red, blue, and green<br>" + "and are in font sizes: default, default, 20px, 20px<bb>" + "hover annotations' upper left corners at upper left corners of symbols.");
        getXAxis().setTickCount(6);
        getXAxis().setAxisMin(0);
        getXAxis().setAxisMax(5);
        getYAxis().setTickCount(6);
        getYAxis().setAxisMin(0);
        getYAxis().setAxisMax(5);
        addCurve();
        getCurve().addPoint(1, 1);
        getCurve().getPoint().setAnnotationText("not bold");
        getCurve().getSymbol().setHovertextTemplate("default");
        getCurve().getSymbol().setHoverWidget(null, 1, 1);
        getCurve().getSymbol().setHeight(30);
        getCurve().getSymbol().setWidth(30);
        addCurve();
        getCurve().addPoint(2, 2);
        getCurve().getPoint().setAnnotationText("bold");
        getCurve().getPoint().setAnnotationFontWeight("bold");
        getCurve().getPoint().setAnnotationFontStyle("normal");
        getCurve().getPoint().setAnnotationFontColor("red");
        getCurve().getSymbol().setHovertextTemplate("bold-normal-red");
        getCurve().getSymbol().setHoverWidget(null, 1, 1);
        getCurve().getSymbol().setHoverFontWeight("bold");
        getCurve().getSymbol().setHoverFontStyle("normal");
        getCurve().getSymbol().setHoverFontColor("red");
        getCurve().getSymbol().setHeight(30);
        getCurve().getSymbol().setWidth(30);
        addCurve();
        getCurve().addPoint(3, 3);
        getCurve().getPoint().setAnnotationText("italic");
        getCurve().getPoint().setAnnotationFontStyle("italic");
        getCurve().getPoint().setAnnotationFontWeight("normal");
        getCurve().getPoint().setAnnotationFontColor("blue");
        getCurve().getPoint().setAnnotationFontSize(20);
        getCurve().getSymbol().setHovertextTemplate("italic-normal-blue-20px");
        getCurve().getSymbol().setHoverWidget(null, 1, 1);
        getCurve().getSymbol().setHoverFontStyle("italic");
        getCurve().getSymbol().setHoverFontWeight("normal");
        getCurve().getSymbol().setHoverFontColor("blue");
        getCurve().getSymbol().setHoverFontSize(20);
        getCurve().getSymbol().setHeight(30);
        getCurve().getSymbol().setWidth(30);
        addCurve();
        getCurve().addPoint(4, 4);
        getCurve().getPoint().setAnnotationText("bold-italic");
        getCurve().getPoint().setAnnotationFontStyle("italic");
        getCurve().getPoint().setAnnotationFontWeight("bold");
        getCurve().getPoint().setAnnotationFontColor("green");
        getCurve().getPoint().setAnnotationFontSize(20);
        getCurve().getSymbol().setHovertextTemplate("italic-bold-green-20px");
        getCurve().getSymbol().setHoverWidget(null, 1, 1);
        getCurve().getSymbol().setHoverFontStyle("italic");
        getCurve().getSymbol().setHoverFontWeight("bold");
        getCurve().getSymbol().setHoverFontColor("green");
        getCurve().getSymbol().setHoverFontSize(20);
        getCurve().getSymbol().setHeight(30);
        getCurve().getSymbol().setWidth(30);
    }
}
