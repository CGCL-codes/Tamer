package fr.soleil.mambo.components;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ConfigurationFileFilter extends FileFilter {

    protected String description;

    protected String extension;

    /**
     * 
     */
    public ConfigurationFileFilter(String _extension) {
        super();
        this.extension = _extension;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String _extension = getExtension(f);
        if (_extension != null && _extension.equals(extension)) {
            return true;
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
	 * @return Returns the extension.
	 */
    public String getExtension() {
        return extension;
    }
}
