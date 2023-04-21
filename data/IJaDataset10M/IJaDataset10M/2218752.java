package uk.co.marcoratto.apache.ssh;

import java.util.Locale;

/**
 * Condition that tests the OS type.
 *
 * @since Ant 1.4
 */
public class Os implements Condition {

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

    private static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);

    private static final String PATH_SEP = System.getProperty("path.separator");

    /**
     * OS family to look for
     */
    private String family;

    /**
     * Name of OS
     */
    private String name;

    /**
     * version of OS
     */
    private String version;

    /**
     * OS architecture
     */
    private String arch;

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_WINDOWS = "windows";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_9X = "win9x";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_NT = "winnt";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_OS2 = "os/2";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_NETWARE = "netware";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_DOS = "dos";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_MAC = "mac";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_TANDEM = "tandem";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_UNIX = "unix";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_VMS = "openvms";

    /**
     * OS family that can be tested for. {@value}
     */
    public static final String FAMILY_ZOS = "z/os";

    /** OS family that can be tested for. {@value} */
    public static final String FAMILY_OS400 = "os/400";

    /**
     * OpenJDK is reported to call MacOS X "Darwin"
     * @see https://issues.apache.org/bugzilla/show_bug.cgi?id=44889
     * @see https://issues.apache.org/jira/browse/HADOOP-3318
     */
    private static final String DARWIN = "darwin";

    /**
     * Default constructor
     *
     */
    public Os() {
    }

    /**
     * Constructor that sets the family attribute
     * @param family a String value
     */
    public Os(String family) {
        setFamily(family);
    }

    /**
     * Sets the desired OS family type
     *
     * @param f      The OS family type desired<br />
     *               Possible values:<br />
     *               <ul>
     *               <li>dos</li>
     *               <li>mac</li>
     *               <li>netware</li>
     *               <li>os/2</li>
     *               <li>tandem</li>
     *               <li>unix</li>
     *               <li>windows</li>
     *               <li>win9x</li>
     *               <li>z/os</li>
     *               <li>os/400</li>
     *               </ul>
     */
    public void setFamily(String f) {
        family = f.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Sets the desired OS name
     *
     * @param name   The OS name
     */
    public void setName(String name) {
        this.name = name.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Sets the desired OS architecture
     *
     * @param arch   The OS architecture
     */
    public void setArch(String arch) {
        this.arch = arch.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Sets the desired OS version
     *
     * @param version   The OS version
     */
    public void setVersion(String version) {
        this.version = version.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Determines if the OS on which Ant is executing matches the type of
     * that set in setFamily.
     * @return true if the os matches.
     * @throws BuildException if there is an error.
     * @see Os#setFamily(String)
     */
    public boolean eval() throws ConditionException {
        return isOs(family, name, arch, version);
    }

    /**
     * Determines if the OS on which Ant is executing matches the
     * given OS family.
     * @param family the family to check for
     * @return true if the OS matches
     * @throws ConditionException 
     * @since 1.5
     */
    public static boolean isFamily(String family) throws ConditionException {
        return isOs(family, null, null, null);
    }

    /**
     * Determines if the OS on which Ant is executing matches the
     * given OS name.
     *
     * @param name the OS name to check for
     * @return true if the OS matches
     * @throws ConditionException 
     * @since 1.7
     */
    public static boolean isName(String name) throws ConditionException {
        return isOs(null, name, null, null);
    }

    /**
     * Determines if the OS on which Ant is executing matches the
     * given OS architecture.
     *
     * @param arch the OS architecture to check for
     * @return true if the OS matches
     * @throws ConditionException 
     * @since 1.7
     */
    public static boolean isArch(String arch) throws ConditionException {
        return isOs(null, null, arch, null);
    }

    /**
     * Determines if the OS on which Ant is executing matches the
     * given OS version.
     *
     * @param version the OS version to check for
     * @return true if the OS matches
     * @throws ConditionException 
     * @since 1.7
     */
    public static boolean isVersion(String version) throws ConditionException {
        return isOs(null, null, null, version);
    }

    /**
     * Determines if the OS on which Ant is executing matches the
     * given OS family, name, architecture and version
     *
     * @param family   The OS family
     * @param name   The OS name
     * @param arch   The OS architecture
     * @param version   The OS version
     * @return true if the OS matches
     * @throws ConditionException 
     * @since 1.7
     */
    public static boolean isOs(String family, String name, String arch, String version) throws ConditionException {
        boolean retValue = false;
        if (family != null || name != null || arch != null || version != null) {
            boolean isFamily = true;
            boolean isName = true;
            boolean isArch = true;
            boolean isVersion = true;
            if (family != null) {
                boolean isWindows = OS_NAME.indexOf(FAMILY_WINDOWS) > -1;
                boolean is9x = false;
                boolean isNT = false;
                if (isWindows) {
                    is9x = (OS_NAME.indexOf("95") >= 0 || OS_NAME.indexOf("98") >= 0 || OS_NAME.indexOf("me") >= 0 || OS_NAME.indexOf("ce") >= 0);
                    isNT = !is9x;
                }
                if (family.equals(FAMILY_WINDOWS)) {
                    isFamily = isWindows;
                } else if (family.equals(FAMILY_9X)) {
                    isFamily = isWindows && is9x;
                } else if (family.equals(FAMILY_NT)) {
                    isFamily = isWindows && isNT;
                } else if (family.equals(FAMILY_OS2)) {
                    isFamily = OS_NAME.indexOf(FAMILY_OS2) > -1;
                } else if (family.equals(FAMILY_NETWARE)) {
                    isFamily = OS_NAME.indexOf(FAMILY_NETWARE) > -1;
                } else if (family.equals(FAMILY_DOS)) {
                    isFamily = PATH_SEP.equals(";") && !isFamily(FAMILY_NETWARE);
                } else if (family.equals(FAMILY_MAC)) {
                    isFamily = OS_NAME.indexOf(FAMILY_MAC) > -1 || OS_NAME.indexOf(DARWIN) > -1;
                } else if (family.equals(FAMILY_TANDEM)) {
                    isFamily = OS_NAME.indexOf("nonstop_kernel") > -1;
                } else if (family.equals(FAMILY_UNIX)) {
                    isFamily = PATH_SEP.equals(":") && !isFamily(FAMILY_VMS) && (!isFamily(FAMILY_MAC) || OS_NAME.endsWith("x") || OS_NAME.indexOf(DARWIN) > -1);
                } else if (family.equals(FAMILY_ZOS)) {
                    isFamily = OS_NAME.indexOf(FAMILY_ZOS) > -1 || OS_NAME.indexOf("os/390") > -1;
                } else if (family.equals(FAMILY_OS400)) {
                    isFamily = OS_NAME.indexOf(FAMILY_OS400) > -1;
                } else if (family.equals(FAMILY_VMS)) {
                    isFamily = OS_NAME.indexOf(FAMILY_VMS) > -1;
                } else {
                    throw new ConditionException("Don\'t know how to detect os family \"" + family + "\"");
                }
            }
            if (name != null) {
                isName = name.equals(OS_NAME);
            }
            if (arch != null) {
                isArch = arch.equals(OS_ARCH);
            }
            if (version != null) {
                isVersion = version.equals(OS_VERSION);
            }
            retValue = isFamily && isName && isArch && isVersion;
        }
        return retValue;
    }
}
