package org.semanticweb.mmm.mr3.util;

import java.io.*;

/**
 * @author takeshi morita
 */
public class OWLFileFilter extends MR3FileFilter implements java.io.FileFilter {

    public String getExtension() {
        return "owl";
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null && extension.equals("owl")) {
            return true;
        }
        return false;
    }

    public String getDescription() {
        return "OWL File (*.owl)";
    }
}
