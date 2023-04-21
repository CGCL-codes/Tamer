package screencompare;

import java.io.File;

public class Screencompare {

    public static String getDataDir() {
        return getCwd() + File.separator + "data" + File.separator;
    }

    public static String getDataPath(String append) {
        String path = getDataDir() + append;
        return path;
    }

    public static String getLibDir() {
        return getCwd() + File.separator + "lib" + File.separator;
    }

    public static String getLibPath(String append) {
        String path = getLibDir() + append;
        return path;
    }

    public static void mkDataDirs(String append) {
        String dir = getDataPath(append);
        new File(dir).mkdirs();
    }

    private static File getCwd() {
        return new File(System.getProperty("user.dir"));
    }

    public static void _exec(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void _exec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
