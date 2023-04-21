package net.sf.compositor.util;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class DirectoryChooser extends JFileChooser {

    public DirectoryChooser(final File dir) {
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(new FileFilter() {

            public boolean accept(final File f) {
                return f.isDirectory();
            }

            public String getDescription() {
                return (Env.IS_WINDOWS ? "Folders" : "Directories") + " only";
            }
        });
        setCurrentDirectory(dir);
    }
}
