package net.jfellow.tail.util;

/**
 * @author @author@
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JDKCheck {

    public static String getJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        return javaVersion;
    }

    public static boolean isMin1_4() {
        String javaVersion = System.getProperty("java.version");
        if ((javaVersion != null) && !"".equals(javaVersion)) {
            if (javaVersion.startsWith("2")) {
                return true;
            }
            if (javaVersion.startsWith("1.4")) {
                return true;
            }
            if (javaVersion.startsWith("1.5")) {
                return true;
            }
            if (javaVersion.startsWith("1.6")) {
                return true;
            }
            if (javaVersion.startsWith("1.7")) {
                return true;
            }
            if (javaVersion.startsWith("1.8")) {
                return true;
            }
            if (javaVersion.startsWith("1.9")) {
                return true;
            }
            if (javaVersion.startsWith("1.10")) {
                return true;
            }
        }
        return false;
    }
}
