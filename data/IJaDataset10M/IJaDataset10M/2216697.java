package edu.yale.csgp.vitapad.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.DefaultButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.UIManager;
import edu.yale.csgp.vitapad.OperatingSystem;
import edu.yale.csgp.vitapad.VitaPad;
import edu.yale.csgp.vitapad.action.ActionContext;
import edu.yale.csgp.vitapad.action.VPAction;
import edu.yale.csgp.vitapad.util.logging.ILogger;
import edu.yale.csgp.vitapad.util.logging.LoggerController;

/**
 * 
 * Extends JCheckBoxMenuItem to allow for shortcuts to be shown next
 * to the label. Basically the same as VPMenuItem except it has a
 * custom ButtonModel. Also is copied from jEdit.
 * 
 * @author Matt Holford
 */
public class VPCheckBoxMenuItem extends JCheckBoxMenuItem {

    private static ILogger _log = LoggerController.createLogger(VPCheckBoxMenuItem.class);

    public class Model extends DefaultButtonModel {

        /**
         * Overriden to get the selection status from whether or not
         * the action is selected.
         */
        @Override
        public boolean isSelected() {
            if (!isShowing()) return false;
            VPAction a = context.getAction(action);
            if (a == null) {
                _log.warn("Unknown action: " + action);
                return false;
            }
            try {
                return a.isSelected(VPCheckBoxMenuItem.this);
            } catch (Throwable t) {
                _log.error("Exception thrown in reading check box menu item", t);
                return false;
            }
        }

        /**
         * Overriden to do nothing.
         */
        @Override
        public void setSelected(boolean b) {
        }
    }

    private static Font acceleratorFont;

    private static Color acceleratorForeground;

    private static Color acceleratorSelectionForeground;

    private String action;

    private String shortcut;

    private ActionContext context;

    static {
        String shortcutFont;
        if (OperatingSystem.isMacOSLF()) shortcutFont = "Lucida Grande"; else shortcutFont = "Monospaced";
        acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");
        if (acceleratorFont == null) acceleratorFont = new Font(shortcutFont, Font.PLAIN, 12); else {
            acceleratorFont = new Font(shortcutFont, acceleratorFont.getStyle(), acceleratorFont.getSize());
        }
        acceleratorForeground = UIManager.getColor("MenuItem.acceleratorForeground");
        if (acceleratorForeground == null) acceleratorForeground = Color.BLACK;
        acceleratorSelectionForeground = UIManager.getColor("MenuItem.acceleratorSelectionForeground");
        if (acceleratorSelectionForeground == null) acceleratorSelectionForeground = Color.BLACK;
    }

    /**
     * Create a menu item using the given label and action. An
     * actionListener is generated using VPAction.Wrapper.
     * 
     * @param label
     *            The string to show in the menu
     * @param action
     *            The property key for the action that is performed on
     *            user selection
     * @param context
     *            The ActionContext that maps this menu item to an
     *            action.
     */
    public VPCheckBoxMenuItem(String label, String action, ActionContext context) {
        this.context = context;
        this.action = action;
        shortcut = getShortcut();
        if (OperatingSystem.hasScreenMenuBar() && shortcut != null) {
            setText(label + " (" + shortcut + ")");
            shortcut = null;
        } else setText(label);
        if (action != null) {
            setEnabled(true);
            addActionListener(new VPAction.Wrapper(context, action));
        } else setEnabled(false);
        setModel(new Model());
    }

    /**
     * Overriden to allow extra room for the shortcut String.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        if (shortcut != null) {
            d.width += (getFontMetrics(acceleratorFont).stringWidth(shortcut) + 15);
        }
        return d;
    }

    /**
     * Overriden to paint the shortcut String.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (shortcut != null) {
            g.setFont(acceleratorFont);
            g.setColor(getModel().isArmed() ? acceleratorSelectionForeground : acceleratorForeground);
            FontMetrics fm = g.getFontMetrics();
            Insets insets = getInsets();
            g.drawString(shortcut, getWidth() - (fm.stringWidth(shortcut) + insets.right + insets.left + 5), getFont().getSize() + (insets.top - (OperatingSystem.isMacOSLF() ? 0 : 1)));
        }
    }

    /**
     * Gets a String representing the shortcut from the properties
     * file. There may be two shortcuts. In which case the menu will
     * have a legend 'shorcut1 or shortcut2'.
     * 
     */
    private String getShortcut() {
        if (action == null) return null; else {
            String sc1 = VitaPad.getProperty(action + ".shortcut");
            String sc2 = VitaPad.getProperty(action + ".shortcut2");
            if (sc1 == null || sc1.length() == 0) {
                if (sc2 == null || sc2.length() == 0) return null; else return sc2;
            } else {
                if (sc2 == null || sc2.length() == 0) return sc1; else return sc1 + " or " + sc2;
            }
        }
    }
}
