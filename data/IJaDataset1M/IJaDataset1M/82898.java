package jp.ac.shizuoka.cs.panda.mmm.mr3.actions;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import jp.ac.shizuoka.cs.panda.mmm.mr3.*;

/**
 * @author takeshi morita
 *
 */
public class SaveProject extends AbstractActionFile {

    public SaveProject(MR3 mr3, String name) {
        super(mr3, name);
        setValues(name);
    }

    public SaveProject(MR3 mr3, String name, ImageIcon icon) {
        super(mr3, name, icon);
        setValues(name);
    }

    private void setValues(String shortDescription) {
        putValue(SHORT_DESCRIPTION, shortDescription);
        if (shortDescription.equals("Save Project")) {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        } else if (shortDescription.equals("Save Project As")) {
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK + KeyEvent.SHIFT_MASK));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (getName().equals("Save Project")) {
            File currentProject = mr3.getCurrentProject();
            if (currentProject == null) {
                saveProjectAs();
            } else {
                saveProject(currentProject);
            }
        } else {
            saveProjectAs();
        }
    }
}
