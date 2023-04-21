package org.openstreetmap.travelingsalesman.nativetools;

import java.io.File;

/**
 * Class for gets system information.
 * It needs by system dependent code.
 * Create to reduce System... getProperties calls.
 * Singleton.
 *
 * @author <a href="mailto:oleg_chubaryov@mail.ru">Oleg Chubaryov </a>
 */
public final class SystemInformation {

    /**
     * Operation system type.
     */
    private static OsType os = null;

    /**
     * The singleton-instance.
     */
    private static SystemInformation instance;

    /**
     * @return The singleton-instance.
     */
    public static SystemInformation getInstance() {
        if (instance == null) {
            instance = new SystemInformation();
        }
        return instance;
    }

    /**
     * utility-classes have no public constructor.
     */
    private SystemInformation() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            os = OsType.WINDOWS;
        } else if (System.getProperty("os.name").startsWith("Linux")) {
            os = OsType.LINUX;
        } else {
            os = OsType.UNKNOW;
        }
    }

    /**
     * @return Operation system type at this computer.
     */
    public OsType os() {
        return os;
    }

    /**
     * It don't depends from JAVA_HOME variable.
     * JAVA_HOME environment variable may be point to JDK or JRE directory.
     * @return JRE directory always
     */
    public static String jrePath() {
        SystemInformation sysinfo = SystemInformation.getInstance();
        String javaHome = System.getProperty("java.home");
        if (javaHome == null) {
            javaHome = System.getenv("java.home");
        }
        String ext = null;
        if (sysinfo.os() == OsType.WINDOWS) {
            ext = ".exe";
        } else {
            ext = "";
        }
        String javacPath = File.separator + "bin" + File.separator + "javac" + ext;
        File javacFile = new File(javaHome + javacPath);
        if (javacFile.exists()) {
            return javaHome + File.separator + "jre";
        } else {
            return javaHome;
        }
    }
}
