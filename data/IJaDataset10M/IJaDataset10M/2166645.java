package prajna.gui;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * This class implements a FileFilter that handles any extensions passed in. It
 * extends the FileFilter class from the Swing package, and implements the
 * FileFilter interface from the java.io package, so it can be used anywhere
 * either FileFilter is needed. The filter is case-insensitive.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class ExtensionFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

    private HashSet<String> extensionList = new HashSet<String>();

    /**
     * Creates a new ExtensionFilter object.
     * 
     * @param extensions A list of extensions, separated by whitespaces or
     *            commas
     */
    public ExtensionFileFilter(String extensions) {
        setExtension(extensions);
    }

    /**
     * Determines whether the given file is accepted by this filter. All
     * Directories are automatically accepted.
     * 
     * @param file the file to be tested
     * @return Whether the given file is accepted by this filter.
     */
    @Override
    public boolean accept(File file) {
        boolean accepted = false;
        if (file.isDirectory()) {
            accepted = true;
        } else {
            String name = file.getName().toLowerCase();
            int periodIndex = name.lastIndexOf(".");
            String extension = name.substring(periodIndex + 1);
            accepted = extensionList.contains(extension);
        }
        return accepted;
    }

    /**
     * Get the description of this filter
     * 
     * @return the description of this filter
     */
    @Override
    public String getDescription() {
        return "File Filter for " + extensionList + " files.";
    }

    /**
     * Sets the extensions handled by this Filter
     * 
     * @param extensions A list of extensions, separated by whitespaces or
     *            commas
     */
    public void setExtension(String extensions) {
        extensionList.clear();
        StringTokenizer tok = new StringTokenizer(extensions, " \t\n\r\f,.");
        while (tok.hasMoreTokens()) {
            extensionList.add(tok.nextToken().toLowerCase());
        }
    }
}
