package plot;

import org.jfree.chart.ChartPanel;
import estructuras.Cartera;
import estructuras.Estrategia;

public class TimeSimplePlotPanel extends ChartPanel {

    public TimeSimplePlotPanel() {
        this(new Cartera());
    }

    public TimeSimplePlotPanel(Cartera cart) {
        super(cart.chart);
        this.setPreferredSize(new java.awt.Dimension(500, 270));
        this.setVerticalAxisTrace(true);
        this.setHorizontalAxisTrace(true);
    }
}
