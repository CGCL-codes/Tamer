package com.googlecode.gchart.gcharttestapp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.googlecode.gchart.client.GChart;

public class TestGChart07a extends GChart {

    TestGChart07a() {
        super(9 * 50, 9 * 50);
        setChartTitle(GChartTestApp.getTitle(this));
        setChartFootnotes("Check: Each relative button position consistent with its label and buttons disable when clicked, 2x2 Grid on x-axis");
        AnnotationLocation[] locations = { AnnotationLocation.CENTER, AnnotationLocation.EAST, AnnotationLocation.NORTH, AnnotationLocation.NORTHEAST, AnnotationLocation.NORTHWEST, AnnotationLocation.SOUTH, AnnotationLocation.SOUTHEAST, AnnotationLocation.SOUTHWEST, AnnotationLocation.WEST };
        String[] locationNames = { "Center____", "East______", "Northg____", "Northeastg", "Northwestg", "South_____", "Southeast_", "Southwest_", "West______" };
        addCurve();
        getXAxis().setAxisMin(-1);
        Grid g = new Grid(2, 2);
        g.setHTML(0, 0, "<b>(0,0)</b>");
        g.setHTML(0, 1, "<b>(0,1)</b>");
        g.setHTML(1, 0, "<b>(1,0)</b>");
        g.setHTML(1, 1, "<b>(1,1)</b>");
        getXAxis().addTick(0.0, g, GChart.NAI, GChart.NAI);
        getYAxis().setAxisMin(-1);
        Button b = new Button("y=0");
        getYAxis().addTick(0.0, b);
        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ((Button) event.getSource()).setEnabled(false);
            }
        });
        getYAxis().setHasGridlines(false);
        this.setGridColor("silver");
        for (int i = 0; i < locations.length; i++) {
            getCurve().addPoint(i, i);
            getCurve().getPoint().setAnnotationLocation(locations[i]);
            if (i % 3 == 0) getCurve().getPoint().setAnnotationWidget(new Button("Error!"));
            if (i % 2 == 0) getCurve().getPoint().setAnnotationText("junk");
            getCurve().getPoint(i).setAnnotationWidget(new Button(locationNames[i]));
            if (!(getCurve().getPoint(i).getAnnotationWidget() instanceof Button)) throw new IllegalStateException("getAnnotationWidget test failed");
            ((Button) getCurve().getPoint(i).getAnnotationWidget()).addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    ((Button) event.getSource()).setEnabled(false);
                }
            });
        }
        setLegendVisible(false);
    }
}
