package cn.aprilsoft.lang;

import java.io.File;

public class SystemUtil {

    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        if (osName != null) {
            return osName.startsWith("Windows");
        }
        return false;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = SystemUtil.class.getClassLoader();
        }
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        return cl;
    }

    public static File loadUserDirFile(String filename) {
        return new File(System.getProperty("user.dir") + File.separator + filename);
    }
}
