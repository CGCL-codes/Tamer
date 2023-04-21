package sts.gui.actions;

import sts.gui.*;
import javax.swing.*;
import sts.gui.prefs.PreferencesDialog;

/**
 *
 * @author ken
 */
public class EditPreferencesAction extends AbstractAction {

    /** Singleton instance. */
    private static EditPreferencesAction me;

    /** Private constructor enforces singleton pattern. */
    private EditPreferencesAction() {
        this.putValue(Action.NAME, "Edit Preferences...");
        this.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("icons/preferences.png")));
    }

    /** Returns a reference to the only isntance of this class. */
    public static EditPreferencesAction onlyInstance() {
        if (me == null) me = new EditPreferencesAction();
        return me;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        AbstractEntityDialog aed = new PreferencesDialog(Main.onlyInstance());
        kellinwood.meshi.form.FormUtils.setModalDialogVisible(aed);
    }
}
