package com.ununbium.Util;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ScriptFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String ext = getExt(f);
        if (ext == null) return false;
        if (!ext.equals("vls")) return false;
        if (FileUtil.jarContains(f, "script.xml")) return true;
        return false;
    }

    private String getExt(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) ext = s.substring(i + 1).toLowerCase();
        return ext;
    }

    public String getDescription() {
        return "Script Files: *.vls";
    }
}
