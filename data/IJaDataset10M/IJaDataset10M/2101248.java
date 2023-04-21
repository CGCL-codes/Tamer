package org.jcvi.common.core.symbol.qual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jcvi.common.core.symbol.qual.PhredQuality;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

/**
 * @author dkatzel
 *
 *
 */
@RunWith(Parameterized.class)
public class TestPhredQuality {

    private final PhredQuality quality;

    private final double errorProbability;

    @Parameters
    public static Collection<?> data() {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] { PhredQuality.valueOf(10), 0.1F });
        data.add(new Object[] { PhredQuality.valueOf(20), 0.01F });
        data.add(new Object[] { PhredQuality.valueOf(30), 0.001F });
        data.add(new Object[] { PhredQuality.valueOf(40), 0.0001F });
        data.add(new Object[] { PhredQuality.valueOf(50), 0.00001F });
        data.add(new Object[] { PhredQuality.valueOf(15), 0.031622777F });
        data.add(new Object[] { PhredQuality.valueOf(18), 0.015848932F });
        data.add(new Object[] { PhredQuality.valueOf(7), 0.199526231F });
        data.add(new Object[] { PhredQuality.valueOf(27), 0.001995262F });
        data.add(new Object[] { PhredQuality.valueOf(32), 0.000630957F });
        data.add(new Object[] { PhredQuality.valueOf(43), 0.000050119 });
        return data;
    }

    /**
     * @param quality
     * @param errorProbability
     */
    public TestPhredQuality(PhredQuality quality, double errorProbability) {
        this.quality = quality;
        this.errorProbability = errorProbability;
    }

    @Test
    public void errorRate() {
        assertEquals(quality.getErrorProbability(), errorProbability, 0.0000001f);
    }

    @Test
    public void convertToQuality() {
        assertEquals(quality, PhredQuality.withErrorProbability(errorProbability));
    }
}
