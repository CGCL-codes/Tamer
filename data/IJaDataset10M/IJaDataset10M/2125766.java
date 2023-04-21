package org.jtmb.crafty;

import java.io.File;
import java.io.FilenameFilter;

public class GraphicsFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        File file = new File(dir.getAbsolutePath() + "/" + name);
        if (file.isDirectory()) return true;
        name = name.toLowerCase();
        if (!name.startsWith(".")) {
            if (name.endsWith(".jpg")) return true;
            if (name.endsWith(".gif")) return true;
            if (name.endsWith(".mov")) return true;
            if (name.endsWith(".png")) return true;
            if (name.endsWith(".gid")) return true;
        }
        System.out.println(file.getAbsolutePath() + " did not meet upload criteria!");
        return false;
    }
}
