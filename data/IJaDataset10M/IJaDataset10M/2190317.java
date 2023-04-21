package djudge.common;

@ImplementMe
public class Deployment {

    static final String os = System.getProperty("os.name").toLowerCase();

    public static boolean isOSWinNT() {
        return os.indexOf("win") >= 0;
    }

    public static boolean isOSLinux() {
        return os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
    }

    public static boolean isOSMac() {
        return os.indexOf("mac") >= 0;
    }

    public static boolean isOSSupported() {
        return isOSWinNT() || isOSLinux();
    }

    public static boolean useLinks() {
        return true;
    }
}
