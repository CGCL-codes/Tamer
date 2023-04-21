package br.com.ggagliano.swingutil.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * <p>
 * This class provides an flexible FileFilter to be used as a
 * {@link javax.swing.filechooser.FileFilter} instance, where one can set
 * dynamically the file types to pass the filter. This avoids you the need to
 * create a lot of different classes serving as specific filters.
 * </p>
 * 
 * <p>
 * By default, just creating one instance of the class makes it to be a
 * directory-only FileFilter. To add a file type, use the
 * {@link #addAcceptableExtension(String)} method, passing the file extension
 * (with or without the "."). No matter the case you used to pass the
 * extensions, all files with those extensions will pass the filter.
 * </p>
 * 
 * <p>
 * Example: an image FileFilter could be configured as:
 * </p>
 * 
 * <pre>
 * FileFilterEXD imageFilter = new FileFilterEXD();
 * imageFilter.add(&quot;jpg&quot;);
 * imageFilter.add(&quot;jpeg&quot;);
 * imageFilter.add(&quot;gif&quot;);
 * imageFilter.add(&quot;png&quot;);
 * imageFilter.setDescription(&quot;Image Files&quot;);
 * </pre>
 * 
 * <p>
 * Those files will pass the filter above: directories, "image1.JPEG",
 * "image2.png", "IMAGE3.GiF", etc.
 * </p>
 * 
 * <p>
 * To filter directories, avoiding them to pass the filter, use the method
 * {@link #setIgnoreDirectory(boolean)}.
 * </p>
 * 
 * <p>
 * Do not forget to set a description, using the {@link #setDescription(String)}
 * method! This is specially useful if you are dealing with internationalization
 * and need to have different descriptions to the same file filter.
 * </p>
 * 
 * <p>
 * Note that, although it is unnecessary and memory consuming, you can add many
 * times the same extension String. For best performance, this class was
 * designed not to check if the String was previously added, so it will store
 * the duplicated extension Strings. This will not prevent the filter to work
 * properly, but you need to know that it represents a small increase of the
 * used memory and, of course, the more extensions you have, the slower it will
 * work.
 * </p>
 * 
 * @author Gabriel Gagliano
 * @since 0.1
 * @version 1.0 08/11/2009
 * 
 */
public class FileFilterEXD extends FileFilter {

    private String description;

    private List<String> extensions;

    private boolean ignoreDirectory;

    /**
	 * Generic constructor. Just creates the class with default values: allowing
	 * no files but directories and having an empty description.
	 */
    public FileFilterEXD() {
        super();
    }

    /**
	 * A constructor with custom description. Creates the class allowing no
	 * files but directories.
	 * 
	 * @param description
	 *            The desired description of the filter.
	 */
    public FileFilterEXD(String description) {
        super();
        this.setDescription(description);
    }

    /**
	 * A constructor that automatically sets the filter description and whether
	 * it should accept directories or not. Remember that the default is to
	 * accept directories.
	 * 
	 * @param description
	 *            The desired description of the filter.
	 * @param ignoreDirectory
	 *            "true" causes the filter to avoid directories. "false" cause
	 *            it to accept them.
	 */
    public FileFilterEXD(String description, boolean ignoreDirectory) {
        super();
        this.setDescription(description);
        this.ignoreDirectory = ignoreDirectory;
    }

    /**
	 * A complete constructor to automatically create a fully configured single
	 * file type filter that also accepts directories. Of course, more file
	 * types can be added later.
	 * 
	 * @param description
	 *            The desired description of the filter.
	 * @param aSingleFileType
	 *            A String representing the file extension.
	 */
    public FileFilterEXD(String description, String aSingleFileType) {
        this(description, false, aSingleFileType);
    }

    /**
	 * A complete constructor to automatically create a fully configured single
	 * file type filter. Of course, more file types can be added later.
	 * 
	 * @param description
	 *            The desired description of the filter.
	 * @param ignoreDirectory
	 *            "true" causes the filter to avoid directories. "false" cause
	 *            it to accept them.
	 * @param aSingleFileType
	 *            A String representing the file extension.
	 */
    public FileFilterEXD(String description, boolean ignoreDirectory, String aSingleFileType) {
        super();
        this.setDescription(description);
        this.ignoreDirectory = ignoreDirectory;
        this.addAcceptableExtension(aSingleFileType);
    }

    @Override
    public boolean accept(File file) {
        if (file == null || !file.canRead()) {
            return false;
        }
        if (file.isDirectory() && !this.ignoreDirectory) {
            return true;
        }
        if (this.extensions != null) {
            String filename = file.getName().toLowerCase();
            for (String extension : this.extensions) {
                if (filename.endsWith(extension.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    /**
	 * Defines the desired String for the description of the filter.
	 * 
	 * @param description
	 *            The description text.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Adds an acceptable file extension. Only accepted files passes the filter.
	 * The extension may begin with "." or not. Remember that the check engine
	 * of this class is not case-sensitive.
	 * 
	 * @param extension
	 *            A String representing the file extension.
	 */
    public void addAcceptableExtension(String extension) {
        if (extension == null || extension.length() == 0) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new ArrayList<String>();
        }
        if (extension.startsWith(".")) {
            this.extensions.add(extension.substring(1));
        } else {
            this.extensions.add(extension);
        }
    }

    /**
	 * Tells if this instance is configures to accept or deny directories.
	 * 
	 * @return "true" if directories is being denied, "false" otherwise.
	 */
    public boolean isIgnoreDirectory() {
        return ignoreDirectory;
    }

    /**
	 * Defines if directories should be omitted by the filer.
	 * 
	 * @param ignoreDirectory
	 *            "true" causes the filter to avoid directories. "false" cause
	 *            it to accept them.
	 */
    public void setIgnoreDirectory(boolean ignoreDirectory) {
        this.ignoreDirectory = ignoreDirectory;
    }
}
