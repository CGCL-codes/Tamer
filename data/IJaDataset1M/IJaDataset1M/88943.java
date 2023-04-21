package fr.soleil.mambo.containers.sub.dialogs.profile;

import java.awt.Dimension;
import javax.swing.JDialog;
import fr.soleil.mambo.containers.profile.ProfileNewPanel;
import fr.soleil.mambo.tools.Messages;

/**
 * JDialog for new profile editing
 *
 * @author SOLEIL
 */
public class ProfileNewDialog extends JDialog {

    private static ProfileNewDialog instance;

    public static ProfileNewDialog getInstance() {
        if (instance == null) {
            instance = new ProfileNewDialog();
        }
        return instance;
    }

    private ProfileNewDialog() {
        super(ProfileSelectionDialog.getInstance(), Messages.getMessage("PROFILE_TITLE") + " : " + Messages.getMessage("PROFILE_NEW"), true);
        this.setContentPane(ProfileNewPanel.getInstance());
        this.setSize(new Dimension(300, 120));
    }
}
