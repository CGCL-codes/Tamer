package org.gudy.azureus2.core3.util;

import java.io.File;
import java.io.IOException;

/**
 * @author parg
 *
 */
public class AETemporaryFileHandler {

    private static final String PREFIX = "AZU";

    private static final String SUFFIX = ".tmp";

    private static boolean started_up;

    private static File tmp_dir;

    public static synchronized void startup() {
        if (started_up) {
            return;
        }
        started_up = true;
        try {
            tmp_dir = FileUtil.getUserFile("tmp");
            if (tmp_dir.exists()) {
                File[] files = tmp_dir.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        if (file.getName().startsWith(PREFIX) && file.getName().endsWith(SUFFIX)) {
                            if (file.isDirectory()) {
                                FileUtil.recursiveDelete(file);
                            } else {
                                file.delete();
                            }
                        }
                    }
                }
            } else {
                tmp_dir.mkdir();
            }
        } catch (Throwable e) {
            try {
                tmp_dir = File.createTempFile(PREFIX, SUFFIX).getParentFile();
            } catch (Throwable f) {
                tmp_dir = new File("");
            }
            if (!(e instanceof NoClassDefFoundError)) {
                Debug.printStackTrace(e);
            }
        }
    }

    public static File getTempDirectory() {
        startup();
        return (tmp_dir);
    }

    public static boolean isTempFile(File file) {
        if (!file.exists()) {
            return (false);
        }
        try {
            String s1 = file.getParentFile().getCanonicalPath();
            String s2 = tmp_dir.getCanonicalPath();
            if (!s1.equals(s2)) {
                return (false);
            }
            String name = file.getName();
            if (!name.startsWith(PREFIX)) {
                return (false);
            }
            if (!name.endsWith(SUFFIX)) {
                return (false);
            }
            return (true);
        } catch (Throwable e) {
            Debug.out(e);
            return (false);
        }
    }

    public static File createTempFile() throws IOException {
        startup();
        return (File.createTempFile(PREFIX, SUFFIX, tmp_dir));
    }

    public static File createTempDir() throws IOException {
        startup();
        for (int i = 0; i < 16; i++) {
            File f = File.createTempFile(PREFIX, SUFFIX, tmp_dir);
            f.delete();
            if (f.mkdirs()) {
                return (f);
            }
        }
        throw (new IOException("Failed to create temporary directory in " + tmp_dir));
    }
}
