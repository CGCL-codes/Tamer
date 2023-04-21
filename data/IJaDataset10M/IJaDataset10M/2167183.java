package javax.swing.plaf.basic;

import java.beans.PropertyChangeListener;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingTestCase;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import java.util.EventListener;

public class BasicRootPaneUITest extends SwingTestCase {

    private JRootPane rootPane;

    private BasicRootPaneUI ui;

    public BasicRootPaneUITest(final String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        rootPane = new JRootPane();
        ui = (BasicRootPaneUI) BasicRootPaneUI.createUI(rootPane);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBasicRootPaneUI() {
        ui = new BasicRootPaneUI();
        assertTrue(ui != null);
    }

    protected boolean isListenerInstalled(final JComponent c, final PropertyChangeListener listener) {
        EventListener[] listeners = rootPane.getPropertyChangeListeners();
        boolean result = false;
        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == listener) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void testInstallUninstallUI() {
        ui.installUI(rootPane);
        assertTrue(isListenerInstalled(rootPane, ui));
        int inputMapType = isHarmony() ? JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT : JComponent.WHEN_IN_FOCUSED_WINDOW;
        assertTrue("inputMap installed", SwingUtilities.getUIInputMap(rootPane, inputMapType) != null);
        int inputMapLength = SwingUtilities.getUIInputMap(rootPane, inputMapType).size();
        assertTrue(SwingUtilities.getUIActionMap(rootPane) != null);
        int actionMapLength = SwingUtilities.getUIActionMap(rootPane).size();
        ui.uninstallUI(rootPane);
        InputMap inputMap = SwingUtilities.getUIInputMap(rootPane, inputMapType);
        if (inputMap != null) {
            assertTrue("keys were uninstalled", inputMap.size() < inputMapLength);
        }
        ActionMap actionMap = SwingUtilities.getUIActionMap(rootPane);
        if (actionMap != null) {
            assertTrue("actions were uninstalled", actionMap.size() < actionMapLength);
        }
        assertFalse(isListenerInstalled(rootPane, ui));
    }

    public void testPropertyChange() {
        JFrame frame = new JFrame();
        rootPane = frame.getRootPane();
        rootPane.setUI(ui);
        int inputMapType = isHarmony() ? JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT : JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = SwingUtilities.getUIInputMap(rootPane, inputMapType);
        assertTrue("inputMap != null", inputMap != null);
        Object[] keys = SwingUtilities.getUIInputMap(rootPane, inputMapType).keys();
        int keysLength = (keys == null) ? 0 : keys.length;
        rootPane.setDefaultButton(new JButton());
        keys = SwingUtilities.getUIInputMap(rootPane, inputMapType).keys();
        assertTrue("keys were added", keys.length > keysLength);
        rootPane.setDefaultButton(null);
        keys = SwingUtilities.getUIInputMap(rootPane, inputMapType).keys();
        assertTrue("keys were removed", keysLength == 0 && keys == null || keys.length == keysLength);
    }

    public void testCreateUI() {
        ComponentUI ui = BasicRootPaneUI.createUI(rootPane);
        assertTrue(ui != null);
        assertTrue(ui instanceof BasicRootPaneUI);
        ComponentUI ui2 = BasicRootPaneUI.createUI(rootPane);
        assertTrue("stateless", ui == ui2);
    }
}
