package org.glossitope.container.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import org.glossitope.container.Core;
import org.glossitope.container.PreferencesPanel;

/**
 *
 * @author joshy
 */
public class ShowPreferencesAction extends AbstractAction {

    Core main;

    JDialog prefsDialog;

    /** Creates a new instance of ShowPreferencesAction */
    public ShowPreferencesAction(Core main) {
        this.main = main;
    }

    public void actionPerformed(ActionEvent e) {
        if (main.prefs == null) {
            prefsDialog = new JDialog(main.getFrame());
            main.prefs = new PreferencesPanel(main);
            prefsDialog.add(main.prefs);
            prefsDialog.pack();
        }
        prefsDialog.setVisible(true);
    }
}
