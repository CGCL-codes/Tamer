package repast.simphony.chart;

import org.jfree.data.statistics.SimpleHistogramBin;

/**
 * Dynamic histogram whose bin limits change as new data is added.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class DynamicHistogramDataset extends AbstractHistogramDataset {

    private int numBins;

    private double min, max;

    public DynamicHistogramDataset(Comparable<?> seriesKey, int numBins) {
        super(seriesKey);
        this.numBins = numBins;
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
    }

    @Override
    public void addValue(double val) {
        min = Math.min(min, val);
        max = Math.max(max, val);
        super.addValue(val);
    }

    @Override
    protected void doUpdate() {
        if (buffer.size() > 0) {
            double curMin = 0, curMax = 0;
            int itemCount = getItemCount(0);
            if (itemCount > 0) {
                curMin = getStartXValue(0, 0);
                curMax = getEndXValue(0, itemCount - 1);
            }
            if (curMin != min || curMax != max) {
                removeAllBins();
                double interval = (max - min) / numBins;
                double start = min;
                for (int i = 0, n = numBins - 1; i < n; i++) {
                    double end = start + interval;
                    addBin(new SimpleHistogramBin(start, end, true, false));
                    start = end;
                }
                addBin(new SimpleHistogramBin(start, max, true, true));
            }
            clearObservations();
            for (int i = 0, n = buffer.size(); i < n; i++) {
                addObservation(buffer.getQuick(i), false);
            }
            min = Double.POSITIVE_INFINITY;
            max = Double.NEGATIVE_INFINITY;
        }
    }
}
