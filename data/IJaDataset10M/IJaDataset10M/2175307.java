package free.jin.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import free.jin.Jin;
import free.jin.Session;
import free.jin.SessionEvent;
import free.jin.SessionListener;
import free.jin.plugin.Plugin;
import free.util.AWTUtilities;
import free.util.models.BooleanModel;
import free.util.models.Model;
import free.util.models.ModelUtils;

/**
 * The preferences menu.
 */
public class PrefsMenu extends JMenu implements SessionListener {

    /**
   * The "Look and Feel" menu item.
   */
    private final JMenuItem lnfMenu;

    /**
   * Are we currently in the "connected" state?
   */
    private boolean isConnected = false;

    /**
   * The index of the separator between global preferences and plugin
   * preferences. <code>-1</code> when none.
   */
    private int separatorIndex = -1;

    /**
   * The plugins in the current session, <code>null</code> when none.
   */
    private Plugin[] plugins = null;

    /**
   * Creates a new <code>PreferencesMenu</code>.
   */
    public PrefsMenu() {
        super("Preferences");
        setMnemonic('P');
        add(lnfMenu = new JMenuItem("User Interface", 'L'));
        lnfMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Frame parentFrame = AWTUtilities.frameForComponent(PrefsMenu.this);
                JDialog dialog = new PrefsDialog(parentFrame, "User Interface Preferences", new UiPrefsPanel());
                AWTUtilities.centerWindow(dialog, parentFrame);
                dialog.setVisible(true);
            }
        });
    }

    /**
   * Registers us as session listener.
   */
    public void addNotify() {
        super.addNotify();
        Jin.getInstance().getConnManager().addSessionListener(this);
        Session session = Jin.getInstance().getConnManager().getSession();
        setConnected(session != null, session);
    }

    /**
   * Unregisters us as a session listener.
   */
    public void removeNotify() {
        super.removeNotify();
        Jin.getInstance().getConnManager().removeSessionListener(this);
    }

    /**
   * SessionListener implementation. Simply delegates to
   * <code>setConnected</code>.
   */
    public void sessionEstablished(SessionEvent evt) {
        setConnected(true, evt.getSession());
    }

    /**
   * SessionListener implementation. Simply delegates to
   * <code>setConnected</code>.
   */
    public void sessionClosed(SessionEvent evt) {
        setConnected(false, evt.getSession());
    }

    /**
   * Modifies the state of the menu to match the specified state.
   */
    public void setConnected(boolean isConnected, Session session) {
        if (this.isConnected == isConnected) return;
        this.isConnected = isConnected;
        this.plugins = session.getPlugins();
        if (isConnected) addPluginPreferenceMenuItems(); else removePluginPreferenceMenuItems();
    }

    /**
   * Adds the menu items for opening preference dialogs for the specified list
   * of plugins.
   */
    private void addPluginPreferenceMenuItems() {
        separatorIndex = getItemCount();
        addSeparator();
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            if (!plugin.hasPreferencesUI()) continue;
            JMenuItem menuItem = new JMenuItem(plugins[i].getName());
            menuItem.setActionCommand(String.valueOf(i));
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int pluginIndex = Integer.parseInt(evt.getActionCommand());
                    Plugin plugin = plugins[pluginIndex];
                    Frame parentFrame = AWTUtilities.frameForComponent(PrefsMenu.this);
                    JDialog dialog = new PrefsDialog(parentFrame, plugin.getName() + " Preferences", plugin.getPreferencesUI());
                    AWTUtilities.centerWindow(dialog, parentFrame);
                    dialog.setVisible(true);
                }
            });
            add(menuItem);
        }
        addSeparator();
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            Model[] models = plugin.getHotPrefs();
            if (models == null) continue;
            for (int j = 0; j < models.length; j++) {
                Model model = models[j];
                if (model instanceof BooleanModel) {
                    addBooleanPref((BooleanModel) model);
                } else throw new IllegalArgumentException("Unsupported model: " + model);
            }
        }
    }

    /**
   * Adds a checkbox menu item to toggle the state of the specified boolean
   * model.
   */
    private void addBooleanPref(BooleanModel model) {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(model.getName(), model.get());
        ModelUtils.link(model, menuItem.getModel());
        add(menuItem);
    }

    /**
   * Removes the menu items associated with displaying the preference panels
   * of the various plugins.
   */
    private void removePluginPreferenceMenuItems() {
        while (separatorIndex < getItemCount()) remove(separatorIndex);
        separatorIndex = -1;
    }
}
