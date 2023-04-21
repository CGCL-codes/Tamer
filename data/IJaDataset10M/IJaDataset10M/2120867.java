package toolkit.levelEditor.tools;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

    private final String[] extensions;

    private final String description;

    public FileExtensionFilter(final String extensions) {
        this(extensions, "Files of type: " + extensions);
    }

    /**
	 * @param extensions
	 *            A set of extensions, comma seperated.
	 */
    public FileExtensionFilter(final String extensions, final String description) {
        this.extensions = extensions.split(",");
        this.description = description;
    }

    @Override
    public boolean accept(final File pathname) {
        if (pathname.isDirectory()) return true;
        for (final String ext : extensions) {
            if (pathname.getName().toLowerCase().endsWith(ext)) return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
