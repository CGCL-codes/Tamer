package gphoto.util;

public class Systeme {

    private static int os;

    public static final int WINDOWS_OS = 1;

    public static final int MAC_OS = 2;

    static {
        if (System.getProperty("os.name").equals("Windows XP")) {
            os = WINDOWS_OS;
        } else {
            os = MAC_OS;
        }
    }

    public static int getOS() {
        return os;
    }
}
