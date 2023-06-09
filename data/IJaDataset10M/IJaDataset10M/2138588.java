package test.sort;

import static org.junit.Assert.assertFalse;
import jam.data.AbstractHistogram;
import jam.data.HistInt1D;
import java.util.List;

final class Utility {

    private Utility() {
    }

    protected static HistInt1D getOneDHistogramFromSortGroup(final String name) {
        final List<AbstractHistogram> oneDimHistograms = AbstractHistogram.getHistogramList(1);
        assertFalse("Expected 1D histograms.", oneDimHistograms.isEmpty());
        HistInt1D result = null;
        for (AbstractHistogram histogram : oneDimHistograms) {
            if (histogram.getName().contains(name)) {
                result = (HistInt1D) histogram;
                break;
            }
        }
        return result;
    }
}
