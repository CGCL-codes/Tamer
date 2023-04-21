package com.gargoylesoftware.htmlunit.javascript.host;

/**
 * A JavaScript object for a document.navigator.plugins.
 * @version $Revision: 6701 $
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
public class Plugin extends SimpleArray {

    private String description_;

    private String filename_;

    private String name_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Plugin() {
    }

    /**
     * C'tor initializing fields.
     * @param name the plugin name
     * @param description the plugin description
     * @param filename the plugin filename
     */
    public Plugin(final String name, final String description, final String filename) {
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Gets the name of the mime type.
     * @param element a {@link MimeType}
     * @return the name
     */
    @Override
    protected String getItemName(final Object element) {
        return ((MimeType) element).jsxGet_type();
    }

    /**
     * Gets the plugin's description.
     * @return the description
     */
    public String jsxGet_description() {
        return description_;
    }

    /**
     * Gets the plugin's file name.
     * @return the file name
     */
    public String jsxGet_filename() {
        return filename_;
    }

    /**
     * Gets the plugin's name.
     * @return the name
     */
    public String jsxGet_name() {
        return name_;
    }
}
