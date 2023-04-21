package org.exmaralda.folker.actions.fileactions;

import org.exmaralda.folker.actions.AbstractApplicationAction;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;
import org.exmaralda.folker.application.ApplicationControl;
import org.exmaralda.folker.utilities.FOLKERInternationalizer;
import org.exmaralda.folker.utilities.PreferencesUtilities;

/**
 *
 * @author thomas
 */
public class SaveAsAction extends AbstractApplicationAction {

    /** Creates a new instance of OpenAction */
    public SaveAsAction(ApplicationControl ac, String name, Icon icon) {
        super(ac, name, icon);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("[*** SaveAsAction ***]");
        ApplicationControl ac = (ApplicationControl) applicationControl;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(FOLKERInternationalizer.getString("dialog.saveAs"));
        fileChooser.setFileFilter(new org.exmaralda.folker.utilities.FolkerFileFilter());
        fileChooser.setCurrentDirectory(new File(PreferencesUtilities.getProperty("workingDirectory", "")));
        if (ac.currentFilePath == null) {
            int index = Math.max(0, ac.currentMediaPath.lastIndexOf(".wav"));
            File f = new File(ac.currentMediaPath.substring(0, index) + ".flk");
            fileChooser.setSelectedFile(f);
        }
        int retValue = fileChooser.showSaveDialog(ac.getFrame());
        if (retValue == JFileChooser.CANCEL_OPTION) return;
        File f = fileChooser.getSelectedFile();
        if (!(f.getName().contains("."))) {
            f = new File(f.getAbsolutePath() + ".flk");
        }
        PreferencesUtilities.setProperty("workingDirectory", f.getParent());
        ac.saveTranscriptionFileAs(f, true);
    }
}
