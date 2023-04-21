package org.sf.cocosc.util;

import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PopupDialogUtility {

    private Component parent;

    private JFileChooser fileChooser;

    public PopupDialogUtility(Component parent) {
        this.parent = parent;
    }

    public int showOpenConfigurationDialog() {
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(parent);
        return returnVal;
    }

    public int showSaveConfigurationDialog() {
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(parent);
        return returnVal;
    }

    public String getAbsolutePath() {
        if (fileChooser == null || fileChooser.getSelectedFile() == null) {
            throw new IllegalStateException("No file has been selected. API is not being used correctly.");
        }
        return fileChooser.getSelectedFile().getAbsolutePath();
    }
}
