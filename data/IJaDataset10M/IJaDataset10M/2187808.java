package javax.swing.plaf.metal;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.SwingTestCase;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

public class MetalDesktopIconUITest extends SwingTestCase {

    private TestMetalDesktopIconUI ui;

    private JInternalFrame.JDesktopIcon icon;

    private JInternalFrame frame;

    private class TestMetalDesktopIconUI extends MetalDesktopIconUI {

        public JComponent getIconPane() {
            return iconPane;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        frame = new JInternalFrame();
        icon = frame.getDesktopIcon();
        ui = new TestMetalDesktopIconUI();
        ui.installUI(icon);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for MetalDesktopIconUITest.
     * @param name
     */
    public MetalDesktopIconUITest(final String name) {
        super(name);
    }

    public void testCreateUI() {
        ComponentUI ui1 = MetalDesktopIconUI.createUI(frame);
        ComponentUI ui2 = MetalDesktopIconUI.createUI(frame);
        assertTrue("not null", ui1 != null);
        assertTrue("statefull", ui1 != ui2);
    }

    public void testGetMaximumSize() {
        assertEquals("== minimumSize (JRockit fails)", ui.getMinimumSize(icon), ui.getMaximumSize(icon));
    }

    public void testGetMinimumSize() {
        assertEquals("== preferredSize (JRockit fails)", ui.getPreferredSize(icon), ui.getMinimumSize(icon));
    }

    public void testGetPreferredSize() {
        Dimension size = ui.getPreferredSize(icon);
        int desktopIconWidth = UIManager.getInt("DesktopIcon.width");
        assertEquals("width ok", desktopIconWidth, size.width);
        icon.setSize(ui.getPreferredSize(icon));
        icon.doLayout();
        if (isHarmony()) {
            assertEquals("height ok", ui.getIconPane().getPreferredSize().height, ui.getIconPane().getSize().height);
        }
    }

    public void testInstallComponents() {
        int count = icon.getComponentCount();
        ui.uninstallComponents();
        assertEquals("uninstalled", count - 2, icon.getComponentCount());
        ui.installComponents();
        assertEquals("added 2 component", count, icon.getComponentCount());
        if (isHarmony()) {
            assertTrue("added iconPane", icon.isAncestorOf(ui.getIconPane()));
        }
    }

    public void testUninstallComponents() {
        int count = icon.getComponentCount();
        if (isHarmony()) {
            assertTrue("added iconPane", icon.isAncestorOf(ui.getIconPane()));
        }
        ui.uninstallComponents();
        assertEquals("uninstalled", count - 2, icon.getComponentCount());
        if (isHarmony()) {
            assertFalse("removed iconPane", icon.isAncestorOf(ui.getIconPane()));
        }
    }

    public void testInstallDefaults() {
        icon.setBackground(null);
        icon.setForeground(null);
        icon.setFont(null);
        ui.installDefaults();
        assertTrue("opaque", icon.isOpaque());
        assertNotNull(icon.getBackground());
        assertNotNull(icon.getForeground());
        assertNotNull(icon.getFont());
    }

    public void testInstallListeners() {
    }

    public void testUninstallListeners() {
    }

    public void testMetalDesktopIconUI() {
    }
}
