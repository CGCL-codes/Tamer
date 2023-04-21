package de.internnetz.eaf.pluginupdate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import de.internnetz.eaf.core.AbstractEAFPlugin;
import de.internnetz.eaf.core.EAFMain;
import de.internnetz.eaf.pluginupdate.i18n.Messages;
import de.internnetz.eaf.pluginupdate.utils.UpdateThread;

public class PluginUpdateTool extends AbstractEAFPlugin {

    private static PluginUpdatePreferences preferences;

    public static final String LOGPREFIX = Messages.getString("PluginUpdateTool.0");

    static {
        preferences = new PluginUpdatePreferences();
    }

    public static PluginUpdatePreferences getPreferences() {
        return preferences;
    }

    private EAFMain main;

    public PluginUpdateTool() {
        super();
    }

    @Override
    public JMenuItem[] getAddonMenuItems() {
        JMenuItem updater = new JMenuItem();
        updater.setText(Messages.getString("PluginUpdateTool.1"));
        updater.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                pluginUpdaterActionPerformed(evt);
            }
        });
        return new JMenuItem[] { updater };
    }

    public JPanel getPreferencesPanel() {
        return null;
    }

    public boolean isEnabled() {
        return preferences.isEnabled();
    }

    private void pluginUpdaterActionPerformed(ActionEvent evt) {
        UpdateThread updater = new UpdateThread(this.main.getMainFrame(), false);
        updater.start();
    }

    public void setEnabled(boolean b) {
        preferences.setEnabled(b);
    }

    public void startupHook(EAFMain main) {
        this.main = main;
        if (preferences.isAutoUpdate()) {
            UpdateThread updater = new UpdateThread(this.main.getMainFrame(), true);
            updater.start();
        }
    }
}
