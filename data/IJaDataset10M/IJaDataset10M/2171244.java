package org.paccman.ui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.paccman.preferences.ui.MainPrefs;

/**
 *
 * @author joao
 */
public class PaccmanFileChooser extends JFileChooser {

    class PaccmanFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("paccman")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /**
         * Method to get the extension of the file, in lowercase
         */
        private String getExtension(File f) {
            String s = f.getName();
            int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) {
                return s.substring(i + 1).toLowerCase();
            }
            return "";
        }

        public String getDescription() {
            return "Paccman file";
        }
    }

    /** Creates a new instance of PaccmanFileChooser */
    public PaccmanFileChooser() {
        super(MainPrefs.getDataDirectory());
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        addChoosableFileFilter(new PaccmanFileFilter());
    }

    @Override
    public void approveSelection() {
        if (getDialogType() == JFileChooser.SAVE_DIALOG) {
            if (getSelectedFile().exists()) {
                int diag = JOptionPane.showConfirmDialog(this, "File already exists. Overwrite ?", "Overwrite file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (diag == JOptionPane.NO_OPTION) {
                    return;
                }
            }
        }
        super.approveSelection();
    }
}
