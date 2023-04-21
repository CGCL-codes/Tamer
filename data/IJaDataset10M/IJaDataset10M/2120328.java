package com.ctext.ite.gui.main;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.ctext.ite.gui.dialogs.AboutDialog;
import com.ctext.ite.utils.StringHandler;
import javax.swing.JOptionPane;
import org.omegat.gui.main.ProjectUICommands;

/**
 * Handles the About, Preferences and Quit menu items on the Mac
 * @author W. Fourie
 */
public class MacHandler extends ApplicationAdapter {

    private MainFrame mainframe;

    public MacHandler(MainFrame mf) {
        mainframe = mf;
    }

    @Override
    public void handleAbout(ApplicationEvent e) {
        e.setHandled(true);
        new AboutDialog(mainframe, false).setVisible(true);
    }

    @Override
    public void handleQuit(ApplicationEvent e) {
        mainframe.close();
    }

    @Override
    public void handlePreferences(ApplicationEvent e) {
        int choice = JOptionPane.showConfirmDialog(mainframe, StringHandler.getString("DM_PS"), StringHandler.getString("DT_PS"), JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            mainframe.project.alwaysDisplayPSD = true;
            mainframe.PH.saveProject(mainframe.PH.convertProjectToSettings(mainframe.project));
            mainframe.restart();
            ProjectUICommands.projectSave();
        }
    }
}
