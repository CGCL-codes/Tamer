package de.shandschuh.jaolt.gui.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.gui.dialogs.about.AboutFormManager;

public class AboutJDialog extends JDialog {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    public AboutJDialog(JFrame owner) {
        super(owner, Language.translateStatic("DIALOG_ABOUT_TITLE"), true);
        getContentPane().add(new AboutFormManager().getJComponent(false));
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
