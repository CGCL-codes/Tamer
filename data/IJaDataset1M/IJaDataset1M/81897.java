package com.kodcu.web;

import org.apache.wicket.markup.html.WebPage;
import org.objetdirect.wiqueryplugins.ui.flot.Flot;

/**
 *
 * @author Altug Bilgin ALTINTAS
 */
public class FlotPage extends WebPage {

    public FlotPage() {
        double[][] data = new double[4][2];
        data[0][0] = 0;
        data[0][1] = 3;
        data[1][0] = 4.1;
        data[1][1] = 8.2;
        data[2][0] = 8.0;
        data[2][1] = 5.0;
        data[3][0] = 9.0;
        data[3][1] = 13.4;
        Flot flot = new Flot("placeholder");
        flot.setChartData(data);
        add(flot);
    }
}
