package org.nargila.xviewer;

import java.io.File;
import java.util.*;

class XFileFilter extends javax.swing.filechooser.FileFilter {

    Set suffixes;

    public XFileFilter(Set suffixes) {
        this.suffixes = suffixes;
    }

    private String getSuffix(File f) {
        String s = f.getPath(), suf = null;
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            suf = s.substring(i + 1).toLowerCase();
        }
        return suf;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String suf = this.getSuffix(f);
        if (null != suf && suffixes.contains(suf)) {
            return true;
        }
        return false;
    }

    public String getDescription() {
        String s = "XML Files(";
        Iterator i = suffixes.iterator();
        while (i.hasNext()) {
            String suf = (String) i.next();
            s += " *." + suf;
        }
        s += ")";
        return s;
    }
}
