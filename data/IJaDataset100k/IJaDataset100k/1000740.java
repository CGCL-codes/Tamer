package org.junithelper.core;

import java.io.InputStream;
import org.junithelper.core.util.IOUtil;

public class Version {

    private Version() {
    }

    private static final Version SINGLETON = new Version();

    private static String version;

    public static final String get() {
        return SINGLETON._get();
    }

    private String _get() {
        if (version == null) {
            try {
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("version.txt");
                version = IOUtil.readAsString(is, null);
            } catch (Exception e) {
                e.printStackTrace();
                version = "unknown";
            }
        }
        return version;
    }
}
