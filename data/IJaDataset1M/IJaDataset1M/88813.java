package org.jmlspecs.openjml.eclipse;

public class Options {

    /**
     * When true, debugging output is enabled
     */
    public boolean debug;

    /**
     * When 0, only error messages are emitted; 
     * When 1, warning and error messages are emitted;
     * When 2, a small amount of informational messages are emitted as well as warning/error messages;
     * When 3 or higher, verbose progress output is produced.
     */
    public int verbosity;

    /** Verbosity of the plugin itself:
     * When 0, quiet; 
     * When 1, timing and limited progress messages;
     * When 2, action information and replication of ESC warnings;
     * When 3 or higher, debug output.
     */
    public int uiverbosity = 1;

    public String classpath;

    /** The directory in which to place output .class files.
     * FIXME - is there reasonable default behavior when null?
     * (eclipse does not do the Java behavior of placing class files with
     * the java files, if there is more than one root directory)
     */
    public String destination;

    /** The level of Java source that is recognized; should be either
     * 1.4, 1.5, 1.6, 1.7.
     */
    public String source;

    /** Verbose output about JML */
    public boolean jmlverbose;

    /** If true, then annotations beginning with +@ are processed as well
     * as those beginning with @ or -@.
     */
    public boolean parsePlus;

    /** If true, checks for proper use of purity are performed. */
    public boolean checkPurity;

    /** If true, the project is NonNull by default; if false is Nullable by default. */
    public boolean nonnullByDefault;

    /** If true, warnings are issued for features used but not implemented. */
    public boolean showNotImplemented;

    /** If true, warnings are issued for features used in RAC but not executable. */
    public boolean showNotExecutable;

    /** If true, the internal library specifications are not automatically used. */
    public boolean noInternalSpecs;

    /** If true, the internal runtime library is not automatically used. */
    public boolean noInternalRuntime;

    /** If true (the default), the internal runtime library is automatically added
     * to the project classpath (so that the project files have the annotations defined).
     */
    public boolean autoAddRuntimeToProject;

    /** If true, warn if elements of the specs path do not exist.  If false, ignore
     * any non-existent directories or non-directories on the path.
     */
    public boolean checkSpecsPath;

    /** The directory (subdirectory of the project) in which RAC-compiled
     * class files are placed.
     */
    public String racbin;

    /** Creates an option structure with default settings. */
    public Options() {
        setDefaults();
    }

    /**
     * Sets each field to its default.  Note that there may be
     * non-constant (e.g. environment dependent) options.
     * @return a reference to this, for convenience
     */
    public Options setDefaults() {
        debug = false;
        verbosity = 2;
        uiverbosity = 1;
        jmlverbose = false;
        classpath = System.getProperty("java.classpath", "");
        destination = "";
        source = "1.5";
        autoAddRuntimeToProject = true;
        parsePlus = false;
        checkPurity = false;
        nonnullByDefault = true;
        checkSpecsPath = true;
        showNotImplemented = true;
        showNotExecutable = true;
        noInternalSpecs = false;
        noInternalRuntime = false;
        racbin = "bin";
        return this;
    }
}
