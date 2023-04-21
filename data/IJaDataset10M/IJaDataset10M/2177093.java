package ch.bbv.explorer.actions;

import java.io.File;
import java.util.Arrays;
import javax.swing.filechooser.FileFilter;

/**
 * The simple file filter selects files with extension exists in a list of
 * expected ones.
 * @author MarcelBaumann
 * @version $Revision: 1.5 $
 */
public class SimpleFileFilter extends FileFilter {

    /**
   * List of extensions which should be filtered.
   */
    private String[] extensions;

    /**
   * The default extensions of the file filter. This extension is used to infer
   * the file extension if the user did not provide one.
   */
    private String defaultExtension;

    /**
   * Description of the simple file filter.
   */
    private String description;

    /**
   * Constructor of the class. The default extension is the first extension in
   * the extensions list.
   * @param extensions list of extensions which should be filtered
   * @param description description of the simple filter
   * @pre (extensions != null) && (extensions.length > 0) && (description !=
   *      null)
   */
    public SimpleFileFilter(String[] extensions, String description) {
        this(extensions, extensions[0], description);
    }

    /**
   * Constructor of the class.
   * @param extensions list of extensions which should be filtered
   * @param defaultExtension default extension of the file filter
   * @param description description of the simple filter
   * @pre (extensions != null) && (extensions.length > 0) && (description !=
   *      null)
   * @pre defaultExtension == null ? true :
   *      Arrays.asList(extensions).contains(defaultExtension)
   */
    public SimpleFileFilter(String[] extensions, String defaultExtension, String description) {
        assert (extensions != null) && (extensions.length > 0) && (description != null);
        assert defaultExtension == null ? true : Arrays.asList(extensions).contains(defaultExtension);
        this.extensions = extensions;
        this.defaultExtension = defaultExtension;
        this.description = description;
    }

    @Override
    public boolean accept(File file) {
        boolean shouldAccept = false;
        if (file.isDirectory()) {
            shouldAccept = true;
        } else {
            String name = file.getName().toLowerCase();
            for (int i = 0; i < extensions.length; i++) {
                if (name.endsWith(extensions[i])) {
                    shouldAccept = true;
                    break;
                }
            }
        }
        return shouldAccept;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
   * Gets the default extension.
   * @return the default extension of the application
   */
    public String getDefaultExtension() {
        return defaultExtension;
    }

    /**
   * Checks if the given file has a legal extension defined in the file filter.
   * @param file file which extension should be checked
   * @return true if the extension is defined in file filter otherwise false
   * @pre file != null
   */
    public boolean hasLegalExtension(File file) {
        assert file != null;
        String name = file.getName().toLowerCase();
        for (int i = 0; i < extensions.length; i++) {
            if (name.endsWith(extensions[i])) {
                return true;
            }
        }
        return false;
    }
}
