package netxrv.jnlp;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The JAR element.
 *
 * @author <a href="mailto:jmaxwell@users.sourceforge.net">Jon A. Maxwell (JAM)</a> - initial author
 * @version $Revision: 1.2 $
 */
public class JARDesc {

    /** the location of the JAR file */
    private URL location;

    /** the required JAR versions, or null */
    private Version version;

    /** the part name */
    private String part;

    /** whether to load the JAR on demand */
    private boolean lazy;

    /** whether the JAR contains the main class */
    private boolean main;

    /** whether the JAR contains native libraries */
    private boolean nativeJar;

    /**
     * Create a JAR descriptor.
     *
     * @param location the location of the JAR file
     * @param version the required JAR versions, or null
     * @param part the part name, or null
     * @param lazy whether to load the JAR on demand
     * @param main whether the JAR contains the main class
     * @param nativeJam whether the JAR contains native libraries
     */
    public JARDesc(URL location, Version version, String part, boolean lazy, boolean main, boolean nativeJar) {
        this.location = location;
        this.version = version;
        this.part = part;
        this.lazy = lazy;
        this.main = main;
        this.nativeJar = nativeJar;
    }

    /**
     * Returns the URL of the JAR file.
     */
    public URL getLocation() {
        return location;
    }

    /**
     * Returns the required version of the JAR file.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Returns the part name, or null if not specified in the JNLP
     * file.
     */
    public String getPart() {
        return part;
    }

    /**
     * Returns true if the JAR file contains native code
     * libraries.
     */
    public boolean isNative() {
        return nativeJar;
    }

    /**
     * Returns true if the JAR file should be downloaded before
     * starting the application.
     */
    public boolean isEager() {
        return !lazy;
    }

    /**
     * Returns true if the JAR file should be downloaded on demand.
     */
    public boolean isLazy() {
        return lazy;
    }

    /**
     * Returns true if the JNLP file defined this JAR as containing
     * the main class.  If no JARs were defined as the main JAR then
     * the first JAR should be used to locate the main class.
     *
     * @see ResourcesDesc#getMainJAR
     */
    public boolean isMain() {
        return main;
    }
}
