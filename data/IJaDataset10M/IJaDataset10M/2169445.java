package net.sourceforge.fsync.gui;

import javax.swing.JFileChooser;
import net.sourceforge.fsync.filesystem.LocalFolder;

public class LocalFolderChooser extends JFileChooser {

    private static final long serialVersionUID = -5426484931300552610L;

    private LocalFolderChooser() {
        super();
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public static LocalFolder getFolder() {
        LocalFolderChooser temp = new LocalFolderChooser();
        int erg = temp.showOpenDialog(null);
        if (erg == JFileChooser.APPROVE_OPTION) {
            return new LocalFolder(temp.getSelectedFile());
        } else return null;
    }
}
