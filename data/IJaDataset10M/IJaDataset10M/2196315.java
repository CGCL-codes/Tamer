package com.googlecode.gchart.gcharttestapp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.googlecode.gchart.client.GChart;

/**
 * Tests that, if the same Widget is used in two different
 * annotations, GChart behaves in a manner reasonably consistent
 * with how GWT behaves if the same Widget is added to two
 * different panels on the same page.
 * <p>
 *
 * It's still not entirely consistent with how GWT does it, but if
 * developer is careful to follow instructions in
 * setAnnotationWidget, there should be no surprises.
 *
 * 
 */
public class TestGChart53 extends GChart {

    final HTML theX = new HTML("<b>X</b>");

    TestGChart53() {
        setChartFootnotes(new Button("Check: Clicking me drops 2nd point, moves X to 1st point, or visa-versa", new ClickHandler() {

            public void onClick(ClickEvent event) {
                getCurve().getPoint(0).setAnnotationWidget(theX);
                if (getCurve().getNPoints() > 1) getCurve().removePoint(1); else {
                    getCurve().addPoint(1, 1);
                    getCurve().getPoint().setAnnotationWidget(theX);
                }
                update();
            }
        }));
        setChartSize(150, 150);
        addCurve();
        for (int i = 0; i < 2; i++) {
            getCurve().addPoint(i, i);
            getCurve().getPoint().setAnnotationWidget(theX);
        }
        getXAxis().setAxisLabel("x");
        getYAxis().setAxisLabel("y");
    }
}
