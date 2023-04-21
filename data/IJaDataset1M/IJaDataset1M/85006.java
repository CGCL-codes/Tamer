package com.nurflugel.util.antscriptvisualizer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.filechooser.FileFilter;

/**
 * A convenience implementation of FileFilter that filters out all files except for those type extensions that it knows about.
 *
 * <p>Extensions are of the type ".foo", which is typically found on Windows and Unix boxes, but not on Macinthosh. Case is ignored.</p>
 *
 * <p>Example - create a new filter that filerts out all files but gif and jpg image files:</p>
 *
 * <p>JFileChooser chooser = new JFileChooser(); ExampleFileFilter filter = new ExampleFileFilter( new String{"gif", "jpg" }, "JPEG & GIF Images")
 * chooser.addChoosableFileFilter(filter); chooser.showOpenDialog(this);</p>
 *
 * @author   Jeff Dinkins
 * @version  1.10 02/06/02
 */
public class ExampleFileFilter extends FileFilter {

    private static String HIDDEN_FILE = "Hidden File";

    private static String TYPE_UNKNOWN = "Type Unknown";

    private boolean useExtensionsInDescription = true;

    private Map<String, ExampleFileFilter> filters;

    private String description;

    private String fullDescription;

    /**
   * Creates a file filter. If no filters are added, then all files are accepted.
   *
   * @see  #addExtension
   */
    public ExampleFileFilter() {
        filters = new HashMap<String, ExampleFileFilter>();
    }

    /**
   * Creates a file filter from the given string array. Example: new ExampleFileFilter(String {"gif", "jpg" });
   *
   * <p>Note that the "." before the extension is not needed adn will be ignored.</p>
   *
   * @see  #addExtension
   */
    public ExampleFileFilter(String[] filters) {
        this(filters, null);
    }

    /**
   * Creates a file filter that accepts files with the given extension. Example: new ExampleFileFilter("jpg");
   *
   * @see  #addExtension
   */
    public ExampleFileFilter(String extension) {
        this(extension, null);
    }

    /**
   * Creates a file filter from the given string array and description. Example: new ExampleFileFilter(String {"gif", "jpg" }, "Gif and JPG Images");
   *
   * <p>Note that the "." before the extension is not needed and will be ignored.</p>
   *
   * @see  #addExtension
   */
    public ExampleFileFilter(String[] filters, String description) {
        this();
        for (String filter : filters) {
            addExtension(filter);
        }
        if (description != null) {
            setDescription(description);
        }
    }

    /**
   * Creates a file filter that accepts the given file type. Example: new ExampleFileFilter("jpg", "JPEG Image Images");
   *
   * <p>Note that the "." before the extension is not needed. If provided, it will be ignored.</p>
   *
   * @see  #addExtension
   */
    public ExampleFileFilter(String extension, String description) {
        this();
        if (extension != null) {
            addExtension(extension);
        }
        if (description != null) {
            setDescription(description);
        }
    }

    /**
   * Adds a filetype "dot" extension to filter against.
   *
   * <p>For example: the following code will create a filter that filters out all files except those that end in ".jpg" and ".tif":</p>
   *
   * <p>ExampleFileFilter filter = new ExampleFileFilter(); filter.addExtension("jpg"); filter.addExtension("tif");</p>
   *
   * <p>Note that the "." before the extension is not needed and will be ignored.</p>
   */
    public void addExtension(String extension) {
        if (filters == null) {
            filters = new HashMap<String, ExampleFileFilter>(5);
        }
        filters.put(extension.toLowerCase(), this);
        fullDescription = null;
    }

    /**
   * Sets the human readable description of this filter. For example: filter.setDescription("Gif and JPG Images");
   *
   * @see  #setDescription
   * @see  #setExtensionListInDescription
   * @see  #isExtensionListInDescription
   */
    public void setDescription(String description) {
        this.description = description;
        fullDescription = null;
    }

    /**
   * Return true if this file should be shown in the directory pane, false if it shouldn't.
   *
   * <p>Files that begin with "." are ignored.</p>
   *
   * @see  #getExtension
   * @see  FileFilter#accept(File)
   */
    @Override
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if ((extension != null) && (filters.get(getExtension(f)) != null)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Return the extension portion of the file's name .
   *
   * @see  #getExtension
   * @see  FileFilter#accept
   */
    public String getExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if ((i > 0) && (i < (filename.length() - 1))) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
   * Returns the human readable description of this filter. For example: "JPEG and GIF Image Files (*.jpg, *.gif)"
   *
   * @see  #setDescription
   * @see  #setExtensionListInDescription
   * @see  #isExtensionListInDescription
   * @see  FileFilter#getDescription
   */
    @Override
    public String getDescription() {
        if (fullDescription == null) {
            if ((description == null) || isExtensionListInDescription()) {
                fullDescription = (description == null) ? "(" : (description + " (");
                Set<String> keys = filters.keySet();
                Iterator<String> extensions = keys.iterator();
                if (extensions != null) {
                    fullDescription += ("." + extensions.next());
                    while (extensions.hasNext()) {
                        fullDescription += (", " + extensions.next());
                    }
                }
                fullDescription += ")";
            } else {
                fullDescription = description;
            }
        }
        return fullDescription;
    }

    /**
   * Returns whether the extension list (.jpg, .gif, etc) should show up in the human readable description.
   *
   * <p>Only relevent if a description was provided in the constructor or using setDescription();</p>
   *
   * @see  #getDescription
   * @see  #setDescription
   * @see  #setExtensionListInDescription
   */
    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }

    /**
   * Determines whether the extension list (.jpg, .gif, etc) should show up in the human readable description.
   *
   * <p>Only relevent if a description was provided in the constructor or using setDescription();</p>
   *
   * @see  #getDescription
   * @see  #setDescription
   * @see  #isExtensionListInDescription
   */
    public void setExtensionListInDescription(boolean b) {
        useExtensionsInDescription = b;
        fullDescription = null;
    }
}
