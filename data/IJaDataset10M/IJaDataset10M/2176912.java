package general;

import java.io.File;

/**
 * Beim Aufrufen der Klasse die gewünschte Erweiterung mitgeben und gut ist.
 * 
 * @author ingo
 * 
 */
public class FileFilter extends javax.swing.filechooser.FileFilter {

    public String ext = "";

    public FileFilter(String ext) {
        this.ext = "." + ext;
    }

    public boolean accept(File file) {
        String filename = file.getName();
        return (filename.endsWith(this.ext) || file.isDirectory());
    }

    public String getDescription() {
        return "*" + this.ext;
    }
}
