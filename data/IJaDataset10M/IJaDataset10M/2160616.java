package net.scharlie.lumberjack4logs.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A convenience implementation of FileFilter that filters out all files except
 * for those type extensions that it knows about.
 */
public class SaveFileFilter extends FileFilter {

    private final String extension;

    private final String description;

    /**
     * Create a file extension that accepts the given file type.
     */
    public SaveFileFilter(final String extension, final String description) {
        this.extension = extension == null ? "*" : extension;
        this.description = description == null ? "" : description;
    }

    /**
     * Return true if this file should be shown in the directory pane, false if
     * it shouldn't.
     */
    @Override
    public boolean accept(final File file) {
        return file != null && (file.isDirectory() || extension == "*" || extension.equals(getExtension(file)));
    }

    /**
     * Return the extension portion of the file's name.
     */
    private String getExtension(final File file) {
        if (file != null) {
            final String filename = file.getName();
            final int i = filename.lastIndexOf('.');
            return i > 0 && i < filename.length() - 1 ? filename.substring(i + 1).toLowerCase() : null;
        }
        return null;
    }

    /**
     * Return the file's name with an adjusted extension portion.
     */
    public File adjustExtension(final File file) {
        if (file == null || extension.equals(getExtension(file))) {
            return file;
        }
        String path = file.getPath();
        path = path + (path.endsWith(".") ? "" : ".") + extension;
        return new File(path);
    }

    /**
     * Return the human readable description of this extension.
     */
    @Override
    public String getDescription() {
        return description + (description.length() == 0 ? "" : " ") + "(." + extension + ")";
    }
}
