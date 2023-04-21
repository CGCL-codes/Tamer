package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.tools.Messages;

/**
 * Opens the application's "About" popup.
 * 
 * @author CLAISSE
 */
public class OpenAboutAction extends BensikinAction {

    /**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
    public OpenAboutAction(String name) {
        this.putValue(Action.NAME, name);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String ok = Messages.getMessage("ABOUT_OK");
        Object[] options = { ok };
        ImageIcon myIcon = new ImageIcon(Bensikin.class.getResource("icons/soleil_logo-150.gif"));
        String release = Messages.getAppMessage("project.version");
        String releaseDate = Messages.getAppMessage("build.date");
        String author = Messages.getMessage("ABOUT_AUTHOR_LABEL") + Messages.getMessage("ABOUT_AUTHOR");
        String facility = Messages.getMessage("ABOUT_FACILITY");
        String msg = Messages.getMessage("ABOUT_DESCRIPTION");
        String revision = Messages.getMessage("ABOUT_REVISION");
        String update = Messages.getMessage("ABOUT_UPDATE_LABEL") + Messages.getMessage("ABOUT_UPDATE");
        msg += GUIUtilities.CRLF;
        msg += revision;
        msg += release;
        msg += " (" + releaseDate + ")";
        msg += GUIUtilities.CRLF;
        msg += author;
        msg += GUIUtilities.CRLF;
        msg += update;
        msg += GUIUtilities.CRLF;
        msg += facility;
        String title = Messages.getMessage("MENU_ABOUT");
        JOptionPane.showOptionDialog(BensikinFrame.getInstance(), msg, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, myIcon, options, options[0]);
    }
}
