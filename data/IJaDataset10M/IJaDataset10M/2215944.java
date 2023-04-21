package net.sf.japi.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import static javax.swing.SwingUtilities.updateComponentTreeUI;
import javax.swing.UIManager;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.getLookAndFeel;
import static javax.swing.UIManager.installLookAndFeel;
import static javax.swing.UIManager.setLookAndFeel;
import net.sf.japi.swing.action.ActionBuilderFactory;

/** A class that manages look and feel and provides a corresponding menu.
 * If you want your frames and dialogs to be default look and feel decorated, you currently must invoke the corresponding method {@link
 * #setDefaultLookAndFeelDecorated(boolean)} before creating any instances of JFrame or JDialog.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 * @todo find a method to update the isDefaultLookAndFeelDecorated state of Frames.
 * @todo perhaps this class should be more a component manager than just a LookAndFeelManager?
 */
public final class LookAndFeelManager {

    /** The Root Component(s) for which to update the look and feel. */
    private final List<Component> roots = new ArrayList<Component>();

    /** Action for look and feel decoration change. */
    private final LAFDecoAction lafDecoAction = new LAFDecoAction();

    static {
        try {
            installLookAndFeel("Windows", "net.sf.japi.swing.WindowsLookAndFeel");
        } catch (final Exception e) {
            Logger.getLogger("net.sf.japi").log(Level.INFO, "lafUnavailable", e);
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    /** Create a LookAndFeelManager. */
    public LookAndFeelManager() {
    }

    /** Add a Component to the roots.
     * @param comp Component to add to the roots
     */
    public void add(final Component comp) {
        if (comp != null && !roots.contains(comp)) {
            roots.add(comp);
        }
    }

    /** Provide a menu which allows the user to choose from installed look and feels.
     * @return menu with selectable look and feels
     */
    public JMenu createMenu() {
        return fillMenu(new JMenu(ActionBuilderFactory.getInstance().getActionBuilder("net.sf.japi.swing").createAction(true, "laf")));
    }

    /** Fill a menu with look and feel selection items.
     * @param menu Menu to fill
     * @return menu for convenience
     */
    public JMenu fillMenu(final JMenu menu) {
        final String active = getLookAndFeel().getClass().getName();
        final ButtonGroup bg = new ButtonGroup();
        for (final UIManager.LookAndFeelInfo lafInfo : getInstalledLookAndFeels()) {
            final JRadioButtonMenuItem mi = new JRadioButtonMenuItem(new LAFAction(lafInfo));
            if (lafInfo.getClassName().equals(active)) {
                mi.setSelected(true);
            }
            bg.add(mi);
            menu.add(mi);
        }
        menu.add(lafDecoAction.createJCheckBoxMenuItem());
        return menu;
    }

    /** Remove a Component to the roots.
     * @param comp Component to remove from the roots
     */
    public void remove(final Component comp) {
        roots.remove(comp);
    }

    /** Set whether JFrames and JDialogs should use the default look and feel decoration.
     * Also updates all JFrames and JDialogs managed.
     * @param defaultLookAndFeelDecorated <code>true</code> for decoration from default look and feel, <code>false</code> for decoration from os
     * @see JFrame#setDefaultLookAndFeelDecorated(boolean)
     * @see JDialog#setDefaultLookAndFeelDecorated(boolean)
     */
    public void setDefaultLookAndFeelDecorated(final boolean defaultLookAndFeelDecorated) {
        JFrame.setDefaultLookAndFeelDecorated(defaultLookAndFeelDecorated);
        JDialog.setDefaultLookAndFeelDecorated(defaultLookAndFeelDecorated);
        for (final Component comp : roots) {
            if (comp instanceof JFrame || comp instanceof JDialog) {
                updateComponentTreeUI(comp);
            }
        }
    }

    /** LookAndFeel Change Action.
     * @author $Author: christianhujer $
     * @version $Id: LookAndFeelManager.java,v 1.8 2006/03/13 00:34:51 christianhujer Exp $
     */
    private final class LAFAction extends AbstractAction {

        /** Serial Version. */
        @SuppressWarnings({ "AnalyzingVariableNaming" })
        private static final long serialVersionUID = 1L;

        /** Class name of look and feel.
         * @serial include
         */
        private final String className;

        /** Create a LAFAction.
         * @param lafInfo LookAndFeelInfo
         */
        LAFAction(final UIManager.LookAndFeelInfo lafInfo) {
            putValue(NAME, lafInfo.getName());
            className = lafInfo.getClassName();
        }

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent e) {
            try {
                setLookAndFeel(className);
                for (final Component comp : roots) {
                    updateComponentTreeUI(comp);
                }
            } catch (final Exception ex) {
                System.err.println(ex);
            }
        }

        /** {@inheritDoc} */
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /** LookAndFeelDecoration Action. */
    private final class LAFDecoAction extends AbstractAction {

        /** Serial Version. */
        @SuppressWarnings({ "AnalyzingVariableNaming" })
        private static final long serialVersionUID = 1L;

        /** The list of JMenuItems created for this Action. */
        private final List<JCheckBoxMenuItem> menuItems = new ArrayList<JCheckBoxMenuItem>();

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent e) {
            setDefaultLookAndFeelDecorated(!JFrame.isDefaultLookAndFeelDecorated());
        }

        /** Set the default look and feel decoration state.
         * @param defaultLookAndFeelDecorated <code>true</code> if default look and feel decorated, otherwise <code>false</code>
         */
        private void setDefaultLookAndFeelDecorated(final boolean defaultLookAndFeelDecorated) {
            LookAndFeelManager.this.setDefaultLookAndFeelDecorated(defaultLookAndFeelDecorated);
            for (final JCheckBoxMenuItem menuItem : menuItems) {
                menuItem.setSelected(defaultLookAndFeelDecorated);
            }
        }

        /** Create a JCheckBoxMenuItem.
         * @return JCheckBoxMenuItem for the associated look and feel
         */
        public JCheckBoxMenuItem createJCheckBoxMenuItem() {
            final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(this);
            menuItems.add(menuItem);
            return menuItem;
        }

        /** {@inheritDoc} */
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
