package com.explosion.expf.preferences;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import com.explosion.expf.Application;
import com.explosion.expf.ExpModuleManager;
import com.explosion.utilities.preferences.dialogs.PreferencesTable;
import com.explosion.utilities.preferences.dialogs.TableBasedPreferenceEditorDialog;

public class StandardConfigurationDialog extends TableBasedPreferenceEditorDialog {

    private JButton button = new JButton("Dump preferences");

    public StandardConfigurationDialog(Dialog owner, String title) {
        super(owner, title, true, true);
        super.setLocationRelativeTo(owner);
    }

    public StandardConfigurationDialog(Frame owner, String title) {
        super(owner, title, true, true);
        super.setLocationRelativeTo(owner);
    }

    Vector dataTables;

    public void loadPreferences(Object preferences) {
        try {
            dataTables = new Vector();
            populateTable("General", (Vector) preferences);
            Vector v = Application.getModules();
            if (v != null) {
                for (int i = 0; i < v.size(); i++) {
                    ExpModuleManager descrip = (ExpModuleManager) v.elementAt(i);
                    Vector prefs = descrip.getPreferences();
                    populateTable(descrip.getName(), prefs);
                }
            }
        } catch (Exception e) {
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while saving properties");
        }
    }

    /**
     * @param descrip
     * @param prefs
     */
    private void populateTable(String name, Vector prefs) {
        if (prefs != null) {
            JScrollPane scrollPane2 = new JScrollPane();
            configTabbedPane.add(scrollPane2, name);
            PreferencesTable table2 = new PreferencesTable(prefs, Application.getApplicationFrame(), this);
            scrollPane2.getViewport().add(table2);
            dataTables.addElement(table2);
            table2.setTableHeader(null);
        }
    }

    /**
     * This method saves the properties to persistent storage and calls the
     * application.applyPreferences method
     */
    public void apply() {
        Application.getInstance().applyPreferences();
        myUpdateLookAndFeel();
        try {
            Application.getInstance().savePreferences();
        } catch (Exception ex) {
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Unable to apply and save properties");
        }
    }

    /**
     * Runs through all of the editors and renderers updating their look and
     * feel
     */
    private void myUpdateLookAndFeel() {
        try {
            Application.getInstance().updateLookAndFeel(this);
        } catch (Exception e) {
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while updating lookandfeel of property editors and renderers");
        }
    }
}
