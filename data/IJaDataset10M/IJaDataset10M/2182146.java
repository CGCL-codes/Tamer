package ghm.followgui;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
Action which displays information about the Follow application.
@author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
*/
class About extends FollowAppAction {

    About(final FollowApp app) {
        super(app, app.resBundle_.getString("action.About.name"), app.resBundle_.getString("action.About.mnemonic"), app.resBundle_.getString("action.About.accelerator"));
    }

    public void actionPerformed(final ActionEvent e) {
        JOptionPane.showMessageDialog(app_.frame_, app_.resBundle_.getString("dialog.About.text"), app_.resBundle_.getString("dialog.About.title"), JOptionPane.INFORMATION_MESSAGE);
    }
}
