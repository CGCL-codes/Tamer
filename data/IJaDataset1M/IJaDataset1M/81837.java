package com.bluemarsh.jswat.core.breakpoint;

/**
 * A LineBreakpoint stops at a specific line within a particular source file.
 *
 * @author Nathan Fiedler
 */
public interface LineBreakpoint extends ResolvableBreakpoint {

    /** Name of the 'lineNumber' property. */
    String PROP_LINENUMBER = "lineNumber";

    /** Name of the 'packageName' property. */
    String PROP_PACKAGENAME = "packageName";

    /** Name of the 'sourceName' property. */
    String PROP_SOURCENAME = "sourceName";

    /** Name of the 'URL' property. */
    String PROP_URL = "URL";

    /**
     * Retrieve the line number associated with this breakpoint.
     *
     * @return  line number of breakpoint.
     */
    int getLineNumber();

    /**
     * Returns the name of the package containing the class this breakpoint
     * is set within.
     *
     * @return  package name (may be null).
     */
    String getPackageName();

    /**
     * Returns the name of the source file containing the class this
     * breakpoint is set within.
     *
     * @return  the source file name.
     */
    String getSourceName();

    /**
     * Returns the URL of the file this breakpoint was set in.
     *
     * @return  unique file URL.
     */
    String getURL();

    /**
     * Set the line number at which this breakpoint is set. This method will
     * force the breakpoint to be unresolved. It must be resolved again before
     * it will be effective. The caller should notify breakpoint listeners of
     * the change.
     *
     * @param  line  line number at this this breakpoint is set.
     */
    void setLineNumber(int line);

    /**
     * Sets the name of the package containing the class this breakpoint
     * is set within.
     *
     * @param  pkg  package name (may be null).
     */
    void setPackageName(String pkg);

    /**
     * Sets the name of the source file containing the class this
     * breakpoint is set within.
     *
     * @param  name  the source file name.
     */
    void setSourceName(String name);

    /**
     * Sets the URL of the file this breakpoint is set in.
     *
     * @param  url  unique file URL.
     */
    void setURL(String url);
}
