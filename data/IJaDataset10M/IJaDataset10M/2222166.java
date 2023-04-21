package net.sf.wwusmart.helper;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * All operating system dependent stuff shall be in this class.
 *
 * @author Thilo
 * @author Armin
 * @version $Rev: 777 $
 */
public class OperatingSystem {

    private static void unknown() {
        String message = "Could not dertermine Operating System!\n" + "System Properties -->\n";
        StringWriter out = new StringWriter();
        PrintWriter print = new PrintWriter(out, false);
        System.getProperties().list(print);
        print.flush();
        out.flush();
        message += out.toString() + "\n<-- System Properties";
        Logger.getLogger(OperatingSystem.class.getName()).warning(message);
        throw new RuntimeException("Unknown Operating System: `" + System.getProperty("os.name") + "'");
    }

    public static String[] nativeLibFileExtensions() {
        if ("Linux;Unix;Solaris;SunOS".contains(System.getProperty("os.name"))) {
            return ".so".split(";");
        } else if (System.getProperty("os.name").contains("Windows")) {
            return ".dll".split(";");
        } else if ("Mac OS X".contains(System.getProperty("os.name"))) {
            return ".dylib".split(";");
        } else {
            unknown();
            return null;
        }
    }

    /**
     * @return user-friendly textual representation of the library filename extensions
     */
    public static String nativeLibFileExtensionsToString() {
        String str = "";
        for (String ext : nativeLibFileExtensions()) {
            str += "*" + ext + ", ";
        }
        str = str.substring(0, str.length() - 2);
        return str;
    }

    /**
     * Return a String in regex notation which donates all chracters NOT allowed
     * in filenames. The String is system independent and thus most of the time
     * includes more characters as are actually forbidden on the current system.
     * <br>
     * Also note that filenames an most file systems must not end with a
     * period nor be equal to system dependent reserved words. This cannot be
     * ensured by matching against the returned String, but has to be checked
     * seperately.
     *
     * @return RegEx String donating all characters not allowed in file names
     *               on a wide range of systems.
     */
    public static String getUnallowedFileCharacters() {
        String unallowed = "/\\><|:\"*?%\\x01-\\x1F\t\n\r\f";
        return unallowed;
    }

    private static final String smartDbPathSeparator = "/";

    public static String toSmartDbSeparator(String filename) {
        String[] path = filename.split(File.separator.equals("\\") ? "\\\\" : File.separator);
        String newFilename = path[0];
        for (int i = 1; i < path.length; i++) newFilename += smartDbPathSeparator + path[i];
        return newFilename;
    }

    public static String toOsSeparator(String filename) {
        String[] path = filename.split(smartDbPathSeparator);
        String newFilename = path[0];
        for (int i = 1; i < path.length; i++) newFilename += File.separator + path[i];
        return newFilename;
    }
}
