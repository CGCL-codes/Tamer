package com.clumsybird.nb.osgi.platform;

import org.openide.filesystems.FileObject;

/**
 *
 * @author XiaoManjun
 */
public class Utils {

    public enum FRAMEWORK {

        FELIX {

            public String frameworkName() {
                return "Felix";
            }
        }
        , KNOPFLERFISH {

            public String frameworkName() {
                return "Knopflerfish";
            }
        }
        , EQUINOX {

            public String frameworkName() {
                return "Equinox";
            }
        }
        , NONE {

            public String frameworkName() {
                return "NONE";
            }
        }
        ;

        public abstract String frameworkName();
    }

    public static FRAMEWORK getFramework(FileObject dir) {
        if (dir != null) {
            FileObject[] files = dir.getChildren();
            for (int i = 0; i < files.length; i++) {
                FileObject file = files[i];
                if (file.isFolder()) {
                    String name = file.getName();
                    if (name.equals("bin")) {
                        if (isFelix(file)) {
                            return FRAMEWORK.FELIX;
                        }
                    } else if (name.equals("osgi")) {
                        if (isKnopflerfish(file)) {
                            return FRAMEWORK.KNOPFLERFISH;
                        }
                    }
                }
            }
        }
        return FRAMEWORK.NONE;
    }

    private static boolean isFelix(FileObject file) {
        FileObject files[] = file.getChildren();
        for (int i = 0; i < files.length; i++) {
            FileObject f = files[i];
            if (!f.isFolder()) {
                String name = f.getNameExt();
                if (name.equals("felix.jar")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isKnopflerfish(FileObject file) {
        FileObject[] files = file.getChildren();
        for (int i = 0; i < files.length; i++) {
            FileObject f = files[i];
            if (!f.isFolder()) {
                String name = f.getNameExt();
                if (name.equals("framework.jar")) {
                    return true;
                }
            }
        }
        return false;
    }
}
