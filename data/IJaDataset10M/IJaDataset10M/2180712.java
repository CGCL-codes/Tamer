package fi.vtt.noen.mfw.probes.tester;

import fi.vtt.noen.mfw.bundle.probe.shared.BaseMeasure;

/**
 * Test probe to provide test data to the server-agent.
 *
* @author Teemu Kanstren
 */
public class TestProbe2 extends TestProbe {

    private int counter = 0;

    public static final String PROBE_DESCRIPTION = "Test Probe 2";

    public static final String TARGET_NAME = "Bob2";

    public static final String TARGET_TYPE = "Spam Filter";

    public static final String BM_CLASS = "configuration file";

    public static final String BM_NAME = "Bobby";

    public static final String BM_DESCRIPTION = "Provides the configuration file";

    public TestProbe2() {
        super(TARGET_NAME, TARGET_TYPE, BM_CLASS, BM_NAME, BM_DESCRIPTION, PROBE_DESCRIPTION, 1);
    }

    public BaseMeasure measure() {
        String result = Integer.toString(counter % 100);
        counter++;
        return new BaseMeasure(result);
    }
}
