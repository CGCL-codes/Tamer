package jaxlib.system;

import java.security.AccessController;
import jaxlib.security.action.GetPropertyAction;

/**
 * Provides constants and tools to deal with system properties.
 *
 * @see java.lang.System#getProperty(String)
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: SystemProperties.java,v 1.2 2004/09/01 00:18:21 joerg_wassmer Exp $
 */
public class SystemProperties extends Object {

    protected SystemProperties() throws InstantiationException {
        throw new InstantiationException();
    }

    /**
   * Name of system property "file.encoding".
   * The system's default encoding for text files.
   * First java version provided this property: 1.1 (?)
   * <p>
   * Example value: <tt>ISO-8859-1</tt>.
   * </p>
   *
   * @see java.nio.charset.Charset
   *
   * @since JaXLib 1.0
   */
    public static final String FILE_ENCODING = "file.encoding";

    /**
   * Name of system property "file.separator".
   * The separator of the operating system to seperate names in strings describing a path to a file or a directory.<br>
   * First java version provided this property: 1.1
   * <p>
   * For operating system Microsoft Windows the value of the property is <tt>"\"</tt>; for Unix/Linux the value is <tt>"/"</tt>.
   * </p>
   *
   * @see java.io.File#separator
   *
   * @since JaXLib 1.0
   */
    public static final String FILE_SEPARATOR = "file.separator";

    /**
   * Name of system property "java.class.path".
   * The path the default classloader uses to search classes and resources.<br>
   * First java version provided this property: 1.1
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_CLASS_PATH = "java.class.path";

    /**
   * Name of system property "java.class.version".
   * Highest supported java class version number.<br>
   * First java version provided this property: 1.1
   * <p>
   * Example value: <tt>48.3</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_CLASS_VERSION = "java.class.version";

    /**
   * Name of system property "java.compiler".
   * Name of the JIT compiler the virtual machine is using.<br>
   * First java version provided this property: 1.4
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_COMPILER = "java.compiler";

    /**
   * Name of system property "java.ext.dirs".
   * First java version provided this property: 1.3
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_EXT_DIRS = "java.ext.dirs";

    /**
   * Name of system property "java.home".
   * The directory where java is installed.<br>
   * First java version provided this property: 1.1
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_HOME = "java.home";

    /**
   * Name of system property "java.io.tmpdir".
   * The default directory where to store temporary files.<br>
   * First java version provided this property: 1.4
   *
   * @see java.io.File#createTempFile(String,String)
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    /**
   * Name of system property "java.library.path".
   * The path the virtual machine uses to search native libraries.<br>
   * First java version provided this property: 1.4
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_LIBRARY_PATH = "java.library.path";

    /**
   * Name of system property "java.specification.name".
   * The name of the java specification the java installation is based on.<br>
   * First java version provided this property: 1.2
   * <p>
   * For java implementations provided by Sun Microsystems the value of this property is <tt>"Java Platform API Specification"</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_SPECIFICATION_NAME = "java.specification.name";

    /**
   * Name of system property "java.specification.vendor".
   * The name of the vendor of the java specification the java installation is based on.<br>
   * First java version provided this property: 1.2
   * <p>
   * For java implementations provided by Sun Microsystems the value of this property is <tt>"Sun Microsystems Inc."</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_SPECIFICATION_VENDOR = "java.specification.vendor";

    /**
   * Name of system property "java.specification.version".
   * The version of the java specification the java installation is based on.<br>
   * First java version provided this property: 1.2
   * <p>
   * Example value: <tt>"1.4"</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_SPECIFICATION_VERSION = "java.specification.version";

    /**
   * Name of system property "java.vendor".
   * The name of the vendor of the java installation.<br>
   * First java version provided this property: 1.1
   * <p>
   * For java implementations provided by Sun Microsystems the value of this property is <tt>"Sun Microsystems Inc."</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VENDOR = "java.vendor";

    /**
   * Name of system property "java.vendor.url".
   * The url of the homepage of the vendor of the java installation.<br>
   * First java version provided this property: 1.1
   * <p>
   * For java implementations provided by Sun Microsystems the value of this property is <tt>"http://java.sun.com"</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VENDOR_URL = "java.vendor.url";

    /**
   * Name of system property "java.version".
   * The version of the runtime class library of the java installation.<br>
   * First java version provided this property: 1.1
   * <p>
   * Example value: "1.4.1_01".
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VERSION = "java.version";

    /**
   * Name of system property "java.vm.name".
   * The name of the java virtual machine implementation.<br>
   * First java version provided this property: 1.2
   * <p>
   * Example value: "Java HotSpot(TM) Client VM".
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VM_NAME = "java.vm.name";

    /**
   * Name of system property "java.vm.specification.name".
   * The name of the specification the java virtual machine implementation is based on.<br>
   * First java version provided this property: 1.2
   * <p>
   * Example value: "Java Virtual Machine Specification".
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VM_SPECIFICATION_NAME = "java.vm.specification.name";

    /**
   * Name of system property "java.vm.specification.vendor".
   * The vendor of the specification the java virtual machine implementation is based on.<br>
   * First java version provided this property: 1.2
   * <p>
   * Example value: "Sun Microsystems Inc.".
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VM_SPECIFICATION_VENDOR = "java.vm.specification.vendor";

    /**
   * Name of system property "java.vm.specification.vendor".
   * The version of the specification the java virtual machine implementation is based on.<br>
   * First java version provided this property: 1.2
   * <p>
   * For java 1.4 distributions provided by Sun Microsystems the value of this property is <tt>"1.0"</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VM_SPECIFICATION_VERSION = "java.vm.specification.version";

    /**
   * Name of system property "java.vm.vendor".
   * The name of the vendor of the running the java virtual machine implementation.<br>
   * First java version provided this property: 1.2
   * <p>
   * For java implementations provided by Sun Microsystems the value of this property is <tt>"Sun Microsystems Inc."</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VM_VENDOR = "java.vm.vendor";

    /**
   * Name of system property "java.vm.version".
   * The version number of the running the java virtual machine implementation.<br>
   * First java version provided this property: 1.2
   * <p>
   * Example value: "1.4.1_01-b01".
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String JAVA_VM_VERSION = "java.vm.version";

    /**
   * Name of system property "line.separator".
   * The line separator of the operating system.<br>
   * First java version provided this property: 1.1
   * <p>
   * Example value: For Unix/Linux operating systems the value of the property is <tt>"\n"</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String LINE_SEPARATOR = "line.separator";

    /**
   * Name of system property "os.arch".
   * The architecture of the operating system.<br>
   * First java version provided this property: 1.1
   * <p>
   * Example value: <tt>"i386"</tt>.<br>
   * The value of the property may differ from the real architecture, e.g. the value may be "i386" on "i686" systems.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String OS_ARCH = "os.arch";

    /**
   * Name of system property "os.name".
   * The name of the operating system.<br> 
   * First java version provided this property: 1.1
   * <p>
   * Example value: <tt>"Linux"</tt>.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String OS_NAME = "os.name";

    /**
   * Name of system property "os.version".
   * The version string of the operating system.<br>
   * First java version provided this property: 1.1
   * <p>
   * Example value: <tt>"2.4.20-1mdk"</tt>.<br>
   * For Linux systems the value of this property seems to be the Linux kernel version.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String OS_VERSION = "os.version";

    /**
   * Name of system property "path.separator".
   * The separator of the operating system to seperate single entries in strings describing a list of paths.<br>
   * First java version provided this property: 1.1
   * <p>
   * Example value: For Unix/Linux systems the value of this property is <tt>":"</tt>, for Microsoft Windows the value is <tt>";"</tt>.
   * </p>
   *
   * @see java.io.File#pathSeparator
   *
   * @since JaXLib 1.0
   */
    public static final String PATH_SEPARATOR = "path.separator";

    /**
   * Name of system property "user.dir".
   * The path to the user's current working directory.<br>
   * First java version provided this property: 1.1
   *
   * @since JaXLib 1.0
   */
    public static final String USER_DIR = "user.dir";

    /**
   * Name of system property "user.home".
   * The path to the user's home directory.<br>
   * First java version provided this property: 1.1
   * <p>
   * Some single-user operating systems may not provide such directories. For Microsoft Windows95 for example the value of the property may point to the 
   * windows installation directory (e.g. "c:\windows").
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String USER_HOME = "user.home";

    /**
   * Name of system property "user.name".
   * The user's name.<br>
   * First java version provided this property: 1.1
   *
   * @since JaXLib 1.0
   */
    public static final String USER_NAME = "user.name";

    private static final boolean hotSpotClient = (getProperty(JAVA_VM_NAME).indexOf("HotSpot") >= 0) && (getProperty(JAVA_VM_NAME).indexOf("Client") >= 0);

    private static final boolean hotSpotServer = (getProperty(JAVA_VM_NAME).indexOf("HotSpot") >= 0) && (getProperty(JAVA_VM_NAME).indexOf("Server") >= 0);

    private static final float javaClassVersion = parseVersion(getProperty(JAVA_CLASS_VERSION));

    private static final float javaSpecificationVersion = parseVersion(getProperty(JAVA_SPECIFICATION_VERSION));

    private static final float javaVersion = parseVersion(getProperty(JAVA_VERSION));

    private static final float javaVMVersion = parseVersion(getProperty(JAVA_VM_VERSION));

    private static final String lineSeparator = getProperty(LINE_SEPARATOR);

    private static final String encoding = initEncoding();

    private static String initEncoding() {
        return new java.io.InputStreamReader(System.in).getEncoding();
    }

    /**
   * Retrieves a system property.
   */
    private static String getProperty(String name) {
        if (System.getSecurityManager() == null) {
            try {
                return System.getProperty(name);
            } catch (SecurityException ex) {
            }
        }
        return (String) AccessController.doPrivileged(new GetPropertyAction(name));
    }

    /**
   * Parses the numerical part of specified version string.
   *
   * @return the number the string starts with; <tt>NaN</tt> if the string does not start with a number.
   */
    private static float parseVersion(String s) {
        boolean p = false;
        for (int i = 0, hi = s.length(); i < hi; i++) {
            char c = s.charAt(i);
            if (c == '.') {
                if (p) {
                    s = s.substring(0, i);
                    break;
                } else p = true;
            } else if ((c < '0') || (c > '9')) {
                s = s.substring(0, i);
                break;
            }
        }
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException ex) {
            return Float.NaN;
        }
    }

    public static String getFileEncoding() {
        return encoding;
    }

    /**
   * Returns the numerical part of the system property "java.class.version".
   * If the system property does not start with a numerical value (should usually not happen), then this method returns "NaN".
   *
   * @see #JAVA_CLASS_VERSION
   * @see Float#parseFloat(String)
   *
   * @since JaXLib 1.0
   */
    public static float getJavaClassVersion() {
        return javaClassVersion;
    }

    /**
   * Returns <tt>true</tt> if and only if the system property "java.class.version" it not smaller than specified value.
   *
   * @see #getJavaClassVersion()
   *
   * @since JaXLib 1.0
   */
    public static boolean isJavaClassVersionAtLeast(float requiredVersion) {
        return (javaClassVersion >= requiredVersion);
    }

    /**
   * Returns the numerical part of the system property "java.version".
   * If the system property does not start with a numerical value (should usually not happen), then this method returns "NaN".
   *
   * @see #JAVA_VERSION
   * @see Float#parseFloat(String)
   *
   * @since JaXLib 1.0
   */
    public static float getJavaVersion() {
        return javaVersion;
    }

    public static String getLineSeparator() {
        return lineSeparator;
    }

    /**
   * Returns <tt>true</tt> if and only if the system property "java.version" it not smaller than specified value.
   *
   * @see #getJavaVersion()
   *
   * @since JaXLib 1.0
   */
    public static boolean isJavaVersionAtLeast(float requiredVersion) {
        return (javaVersion >= requiredVersion);
    }

    /**
   * Returns the numerical part of the system property "java.specification.version".
   * If the system property does not start with a numerical value (should usually not happen), then this method returns "NaN".
   *
   * @see #JAVA_SPECIFICATION_VERSION
   * @see Float#parseFloat(String)
   *
   * @since JaXLib 1.0
   */
    public static float getJavaSpecificationVersion() {
        return javaSpecificationVersion;
    }

    /**
   * Returns the numerical part of the system property "java.vm.version".
   * If the system property does not start with a numerical value (should usually not happen), then this method returns "NaN".
   *
   * @see #JAVA_VM_VERSION
   * @see Float#parseFloat(String)
   *
   * @since JaXLib 1.0
   */
    public static float getJavaVMVersion() {
        return javaVMVersion;
    }

    /**
   * Returns true if the virtual machine running is an implementation of Sun Microsystem's HotSpot Client VM.
   *
   * @since JaXLib 1.0
   */
    public static boolean isJavaVMHotSpotClient() {
        return hotSpotClient;
    }

    /**
   * Returns true if the virtual machine running is an implementation of Sun Microsystem's HotSpot Server VM.
   *
   * @since JaXLib 1.0
   */
    public static boolean isJavaVMHotSpotServer() {
        return hotSpotServer;
    }
}
