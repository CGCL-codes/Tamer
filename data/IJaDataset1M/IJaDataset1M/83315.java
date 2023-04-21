package org.telscenter.sail.webapp.presentation.google.charts.options;

import java.util.Map;
import org.telscenter.sail.webapp.presentation.google.charts.ChartOption;

/**
 * @author patrick lawler
 * @version $Id:$
 */
public class LinearStripes implements ChartOption {

    private String area;

    private int angle;

    private Map<String, Double> colorsWidths;

    public LinearStripes(String area, int angle, Map<String, Double> colorsWidths) throws Exception {
        if (area.equals("bg") || area.equals("c")) {
            this.area = area;
        } else {
            throw new Exception("Area specified must be bg (backgroud) or c (chart)");
        }
        if (angle < 0 || angle > 90) {
            throw new Exception("Angle specified must be between 0 and 90");
        } else {
            this.angle = angle;
        }
        if (colorsWidths.size() < 1) {
            throw new Exception("Colors and Widths must be specified");
        }
        for (Double width : colorsWidths.values()) {
            if (width < 0 || width > 1) {
                throw new Exception("Widths specified must between 0 and 1");
            }
        }
        this.colorsWidths = colorsWidths;
    }

    public String getOptionString() {
        String stripes = this.area + ",ls," + this.angle + ",";
        for (String color : this.colorsWidths.keySet()) {
            stripes = stripes + color + "," + this.colorsWidths.get(color) + ",";
        }
        return stripes.substring(0, stripes.length() - 1);
    }
}
