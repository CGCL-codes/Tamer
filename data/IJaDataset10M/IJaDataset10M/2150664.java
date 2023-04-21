package net.sf.compositor;

import javax.swing.JSlider;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.compositor.util.StackProbe;

public class SliderGeneratorTest extends TestCase {

    public static void main(final String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(StackProbe.getMyClass());
    }

    private abstract static class TestApp extends AbstractTestApp {

        private boolean started;

        protected String getDescriptor() {
            return "<ui name='$$$TestDummy'>" + "<windows>" + "<frame name='main' title='$$$TestDummy'>" + "<panel>" + "<slider name='sldr' info='" + getSldrInfo() + "'>" + getSldrContent() + "</slider>" + "</panel>" + "</frame>" + "</windows>" + "</ui>";
        }

        protected abstract String getSldrInfo();

        protected abstract String getSldrContent();

        public JSlider x_main_sldr;

        public void run() {
            started = true;
            super.run();
        }
    }

    private TestApp makeApp(final String info, final String content) {
        final TestApp result = new TestApp() {

            protected String getSldrInfo() {
                return info;
            }

            protected String getSldrContent() {
                return content;
            }
        };
        while (!result.started) {
            Thread.yield();
        }
        return result;
    }

    public void testSetNothing() {
        final TestApp app = makeApp("", "");
        final JSlider sldr = app.x_main_sldr;
        assertNotNull("Missing slider.", sldr);
        assertEquals("Wrong min.", 0, sldr.getMinimum());
        assertEquals("Wrong max.", 100, sldr.getMaximum());
        assertEquals("Wrong major.", 0, sldr.getMajorTickSpacing());
        assertEquals("Wrong minor.", 0, sldr.getMinorTickSpacing());
        assertFalse("Wrong ticks.", sldr.getPaintTicks());
        assertEquals("Wrong track.", true, sldr.getPaintTrack());
        assertEquals("Wrong labels.", false, sldr.getPaintLabels());
        assertEquals("Wrong value.", 50, sldr.getValue());
        GeneratorTestUtils.hideApp(app);
    }

    public void testSetEverything() {
        final TestApp app = makeApp("min: 20; max: 50; major: 10; minor: 5; track: false; labels: true", "37");
        final JSlider sldr = app.x_main_sldr;
        assertNotNull("Missing slider.", sldr);
        assertEquals("Wrong min.", 20, sldr.getMinimum());
        assertEquals("Wrong max.", 50, sldr.getMaximum());
        assertEquals("Wrong major.", 10, sldr.getMajorTickSpacing());
        assertEquals("Wrong minor.", 5, sldr.getMinorTickSpacing());
        assertTrue("Wrong ticks.", sldr.getPaintTicks());
        assertEquals("Wrong track.", false, sldr.getPaintTrack());
        assertEquals("Wrong labels.", true, sldr.getPaintLabels());
        assertEquals("Wrong value.", 37, sldr.getValue());
        GeneratorTestUtils.hideApp(app);
    }

    public void testSetEverythingDifferently() {
        final TestApp app = makeApp("min: -200; max: 200; minor: 10; track: true; labels: false", "0");
        final JSlider sldr = app.x_main_sldr;
        assertNotNull("Missing slider.", sldr);
        assertEquals("Wrong min.", -200, sldr.getMinimum());
        assertEquals("Wrong max.", 200, sldr.getMaximum());
        assertEquals("Wrong major.", 0, sldr.getMajorTickSpacing());
        assertEquals("Wrong minor.", 10, sldr.getMinorTickSpacing());
        assertTrue("Wrong ticks.", sldr.getPaintTicks());
        assertEquals("Wrong track.", true, sldr.getPaintTrack());
        assertEquals("Wrong labels.", false, sldr.getPaintLabels());
        assertEquals("Wrong value.", 0, sldr.getValue());
        GeneratorTestUtils.hideApp(app);
    }

    public void testSetEverythingBad() {
        final TestApp app = makeApp("min: cat; max: dog; major: fish; minor: lizard; track: gerbil; labels: hamster", "horde");
        final JSlider sldr = app.x_main_sldr;
        assertNotNull("Missing slider.", sldr);
        assertEquals("Wrong min.", 0, sldr.getMinimum());
        assertEquals("Wrong max.", 100, sldr.getMaximum());
        assertEquals("Wrong major.", 0, sldr.getMajorTickSpacing());
        assertEquals("Wrong minor.", 0, sldr.getMinorTickSpacing());
        assertTrue("Wrong ticks.", sldr.getPaintTicks());
        assertEquals("Wrong track.", false, sldr.getPaintTrack());
        assertEquals("Wrong labels.", false, sldr.getPaintLabels());
        assertEquals("Wrong value.", 50, sldr.getValue());
        GeneratorTestUtils.hideApp(app);
    }
}
