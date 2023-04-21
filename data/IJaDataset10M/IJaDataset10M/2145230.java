package javax.swing;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.IOException;
import javax.accessibility.AccessibleRole;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class JPanelTest extends SwingTestCase {

    /**
     * @param arg0
     */
    public JPanelTest(final String arg0) {
        super(arg0);
    }

    protected JPanel panel = null;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(JPanelTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        panel = new JPanel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testUpdateUI() {
        panel.setUI(null);
        panel.updateUI();
        assertTrue(panel.getUI() != null);
    }

    public void testJPanelLayoutManagerboolean() {
        final LayoutManager layout1 = new GridLayout(2, 2);
        panel = new JPanel(layout1, true);
        assertTrue(panel.isOpaque());
        assertSame(layout1, panel.getLayout());
        assertTrue(panel.isDoubleBuffered());
        final LayoutManager layout2 = new FlowLayout();
        panel = new JPanel(layout2, false);
        assertTrue(panel.isOpaque());
        assertSame(layout2, panel.getLayout());
        assertFalse(panel.isDoubleBuffered());
    }

    public void testJPanelLayoutManager() {
        final LayoutManager layout1 = new GridLayout(2, 2);
        panel = new JPanel(layout1);
        assertTrue(panel.isOpaque());
        assertSame(layout1, panel.getLayout());
        assertTrue(panel.isDoubleBuffered());
        final LayoutManager layout2 = new FlowLayout();
        panel = new JPanel(layout2);
        assertTrue(panel.isOpaque());
        assertSame(layout2, panel.getLayout());
        assertTrue(panel.isDoubleBuffered());
    }

    public void testJPanelboolean() {
        panel = new JPanel(true);
        assertTrue(panel.isOpaque());
        assertTrue(panel.getLayout().getClass() == FlowLayout.class);
        assertTrue(panel.isDoubleBuffered());
        panel = new JPanel(false);
        assertTrue(panel.isOpaque());
        assertTrue(panel.getLayout().getClass() == FlowLayout.class);
        assertFalse(panel.isDoubleBuffered());
    }

    public void testJPanel() {
        assertTrue(panel.isOpaque());
        assertTrue(panel.getLayout().getClass() == FlowLayout.class);
        assertTrue(panel.isDoubleBuffered());
    }

    public void testSetUIPanelUI() {
        PanelUI panelUI = null;
        panel.setUI(panelUI);
        assertEquals(panelUI, panel.getUI());
        panelUI = new BasicPanelUI();
        panel.setUI(panelUI);
        assertEquals(panelUI, panel.getUI());
    }

    public void testGetUI() {
        testSetUIPanelUI();
    }

    public void testGetAccessibleContext() {
        boolean assertedValue = (panel.getAccessibleContext() != null && panel.getAccessibleContext().getClass().getName().equals("javax.swing.JPanel$AccessibleJPanel"));
        assertTrue(assertedValue);
        assertTrue(panel.getAccessibleContext().getAccessibleRole().equals(AccessibleRole.PANEL));
    }

    public void testParamString() {
        class TestingPanel extends JPanel {

            private static final long serialVersionUID = 1L;

            public String getParamString() {
                return paramString();
            }
        }
        TestingPanel testingPanel = new TestingPanel();
        assertTrue(testingPanel.getParamString() != null);
        assertTrue(testingPanel.getParamString() != "");
    }

    public void testGetUIClassID() {
        assertEquals("PanelUI", panel.getUIClassID());
    }

    public void testWriteObject() throws IOException {
    }

    public void testReadObject() throws IOException, ClassNotFoundException {
    }
}
