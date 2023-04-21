package org.rrd4j.graph;

import java.awt.Paint;

class HRule extends Rule {

    final double value;

    HRule(double value, Paint color, LegendText legend, float width) {
        super(color, legend, width);
        this.value = value;
    }

    void setLegendVisibility(double minval, double maxval, boolean forceLegend) {
        legend.enabled &= (forceLegend || (value >= minval && value <= maxval));
    }
}
