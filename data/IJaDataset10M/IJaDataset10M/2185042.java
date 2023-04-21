package vm;

import consts.Const;

/**
 * Tracks the generic configuration settings for the target VM.
 */
public class VMConfig {

    static String targetJDKVersion;

    static int minSupportedClassfileVersion;

    static int maxSupportedClassfileVersion;

    static int maxSupportedClassfileMinorVersion;

    static {
        minSupportedClassfileVersion = Const.JAVA_MIN_SUPPORTED_VERSION;
        maxSupportedClassfileVersion = Const.JAVA_MAX_SUPPORTED_VERSION;
        maxSupportedClassfileMinorVersion = Const.JAVA_MAX_SUPPORTED_MINOR_VERSION;
        targetJDKVersion = "1." + (maxSupportedClassfileVersion - 44);
    }

    public static boolean setJDKVersion(String jdkVersion) {
        int newMaxVersion;
        String temp;
        if (jdkVersion.charAt(0) != '1') {
            return false;
        }
        int idxOfFirstDot = jdkVersion.indexOf('.');
        if (idxOfFirstDot < 0) {
            return false;
        }
        temp = jdkVersion.substring(idxOfFirstDot + 1);
        if (temp.equals("")) {
            return false;
        }
        int idxOfSecondDot = temp.indexOf('.');
        if (idxOfSecondDot > 0) {
            temp = temp.substring(0, idxOfSecondDot);
        }
        try {
            newMaxVersion = Integer.parseInt(temp) + 44;
        } catch (NumberFormatException ex) {
            return false;
        }
        if ((newMaxVersion < Const.JAVA_MIN_SUPPORTED_VERSION) || (newMaxVersion > Const.JAVA_MAX_SUPPORTED_VERSION)) {
            return false;
        }
        maxSupportedClassfileVersion = newMaxVersion;
        maxSupportedClassfileMinorVersion = 0;
        targetJDKVersion = jdkVersion;
        return true;
    }

    public static String getJDKVersion() {
        return targetJDKVersion;
    }

    public static int getMinSupportedClassfileVersion() {
        return minSupportedClassfileVersion;
    }

    public static int getMaxSupportedClassfileVersion() {
        return maxSupportedClassfileVersion;
    }

    public static int getMaxSupportedClassfileMinorVersion() {
        return maxSupportedClassfileMinorVersion;
    }
}
