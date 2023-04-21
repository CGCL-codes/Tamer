package javax.swing.plaf.multi;

import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import junit.framework.TestCase;

public class MultiLookAndFeelTest extends TestCase {

    private MultiLookAndFeel mlaf = new MultiLookAndFeel();

    public void testIsNativeLookAndFeel() {
        assertFalse(mlaf.isNativeLookAndFeel());
    }

    public void testIsSupportedLookAndFeel() {
        assertTrue(mlaf.isSupportedLookAndFeel());
    }

    public void testGetName() {
        assertEquals(mlaf.getName(), "Multiplexing Look and Feel");
    }

    public void testGetID() {
        assertEquals(mlaf.getID(), "Multiplex");
    }

    public void testCreateUIs() {
        JButton button = new JButton();
        JLabel label = new JLabel();
        ComponentUI buttonUI = UIManager.getUI(button);
        ComponentUI labelUI = UIManager.getUI(label);
        MultiButtonUI multiButtonUI = new MultiButtonUI();
        MultiLabelUI multiLabelUI = new MultiLabelUI();
        LookAndFeel auxLaf = new SyserrLookAndFeel();
        assertEquals(buttonUI, MultiLookAndFeel.createUIs(multiButtonUI, multiButtonUI.uis, button));
        UIManager.addAuxiliaryLookAndFeel(auxLaf);
        assertEquals(multiButtonUI, MultiLookAndFeel.createUIs(multiButtonUI, multiButtonUI.uis, button));
        assertEquals(labelUI, MultiLookAndFeel.createUIs(multiLabelUI, multiLabelUI.uis, label));
        UIManager.removeAuxiliaryLookAndFeel(auxLaf);
    }

    public void testGetDefaults() {
        assertEquals(mlaf.getDefaults().get("ButtonUI"), "javax.swing.plaf.multi.MultiButtonUI");
        assertNull(mlaf.getDefaults().get("Button.background"));
    }

    @SuppressWarnings("unchecked")
    public void testUisToArray() {
        assertEquals(0, MultiLookAndFeel.uisToArray(null).length);
        assertNull(MultiLookAndFeel.uisToArray(new Vector()));
        Vector v = new Vector();
        ComponentUI content = new SyserrButtonUI();
        v.add(content);
        assertSame(content, MultiLookAndFeel.uisToArray(v)[0]);
        v.add(new Object());
        try {
            MultiLookAndFeel.uisToArray(v);
            fail();
        } catch (Exception e) {
        }
    }
}
