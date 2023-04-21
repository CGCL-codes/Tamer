package net.sf.jgamelibrary.i18n;

/**
 * Defines pair bundle path and base name.
 * 
 * @author Taras Kostiak
 * 
 */
public class Namespace {

    /**
     * Bundle path in classpath.
     */
    protected String bundlePath = null;

    /**
     * Base name of bundle.
     */
    protected String baseName = null;

    /**
     * Creates new bundle path and base name pair.<br>
     * Arguments can't be null.
     * 
     * @param bundlePath
     *            Bundle path in classpath.
     * @param baseName
     *            Base name of bundle.
     * @throws IllegalArgumentException
     *             When one of arguments is null.
     */
    public Namespace(String bundlePath, String baseName) {
        if (bundlePath == null || bundlePath.isEmpty() || baseName == null || baseName.isEmpty()) throw new IllegalArgumentException("Arguments can't be null or empty!");
        this.bundlePath = bundlePath;
        this.baseName = baseName;
    }

    /**
     * @see #bundlePath
     */
    public String getBundlePath() {
        return bundlePath;
    }

    /**
     * @see #baseName
     */
    public String getBaseName() {
        return baseName;
    }
}
