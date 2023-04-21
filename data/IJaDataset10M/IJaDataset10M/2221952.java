package musicTests;

import junit.framework.Assert;
import music.Durations;
import org.junit.Before;
import org.junit.Test;

public class DurationsTest {

    private Durations durationFL;

    private Durations durationHF;

    private Durations durationQ;

    private Durations durationE;

    @Before
    public void setUp() throws Exception {
        durationFL = Durations.FL;
        durationHF = Durations.HF;
        durationQ = Durations.Q;
        durationE = Durations.E;
    }

    @Test
    public void testGetDuration() {
        Assert.assertEquals("GetDuration() failed", 4.0, durationFL.getDuration());
        Assert.assertEquals("GetDuration() failed", 2.0, durationHF.getDuration());
        Assert.assertEquals("GetDuration() failed", 1.0, durationQ.getDuration());
        Assert.assertEquals("GetDuration() failed", 0.5, durationE.getDuration());
    }
}
