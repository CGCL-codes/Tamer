package mosi.logic.simulation.statistics.histogram;

import java.util.EventListener;

public interface HistogramListener extends EventListener {

    public void breaksChanged(HistogramEvent e);

    public void dataChanged(HistogramEvent e);
}
