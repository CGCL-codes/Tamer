package net.sf.myra.datamining;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import junit.framework.TestCase;

public class ErrorBasedMetricTest extends TestCase {

    public void testEstimate() {
        NumberFormat formatter = new DecimalFormat("#.##");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        double rate = ErrorBasedMetric.estimate(1.0, 3.0);
        assertEquals("0.69", formatter.format(rate));
        rate = ErrorBasedMetric.estimate(1.0, 4.0);
        assertEquals("0.55", formatter.format(rate));
        rate = ErrorBasedMetric.estimate(1.0, 7.0);
        assertEquals("0.34", formatter.format(rate));
        rate = ErrorBasedMetric.estimate(59.0, 62.0);
        assertEquals("0.97", formatter.format(rate));
    }
}
