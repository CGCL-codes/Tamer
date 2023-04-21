package net.sf.compositor;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.compositor.util.StackProbe;

public class PanelGeneratorTest extends TestCase {

    public static void main(final String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(StackProbe.getMyClass());
    }

    private abstract static class TestApp extends AbstractTestApp {

        private boolean started;

        private boolean selected;

        protected String getDescriptor() {
            return "<ui name='$$$TestDummy'>" + "<windows>" + "<frame name='main' title='$$$TestDummy'>" + "<panel>" + "<panel name='pnl' info='" + getPanelInfo() + "'>" + getPanelContent() + "</panel>" + "</panel>" + "</frame>" + "</windows>" + "</ui>";
        }

        protected abstract String getPanelInfo();

        protected abstract String getPanelContent();

        public JPanel x_main_pnl;

        public void run() {
            started = true;
            super.run();
        }
    }

    private TestApp makeApp(final String info, final String content) {
        final TestApp result = new TestApp() {

            protected String getPanelInfo() {
                return info;
            }

            protected String getPanelContent() {
                return content;
            }
        };
        while (!result.started) {
            Thread.yield();
        }
        return result;
    }

    public void testNothing() {
        final TestApp app = makeApp("", "");
        final JPanel pnl = app.x_main_pnl;
        assertNotNull("Missing panel.", pnl);
        GeneratorTestUtils.hideApp(app);
    }

    public void testSetEverything() {
        final TestApp app = makeApp("title: I am a panel; dropTarget: true", "<label>Hello</label>");
        final JPanel pnl = app.x_main_pnl;
        assertNotNull("Missing panel.", pnl);
        assertEquals("Wrong title.", "I am a panel", ((TitledBorder) pnl.getBorder()).getTitle());
        assertEquals("Wrong drop target.", "I am the drop target for main.pnl", pnl.getDropTarget().toString());
        GeneratorTestUtils.hideApp(app);
    }
}
