package net.sf.eclibatis.java.editor.hyperlink.tools.filefilters;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Benny
 *
 */
public class XMLFileFilter implements FileFilter {

    public boolean accept(File file) {
        final String filename = file.getName();
        final int extensionPos = filename.lastIndexOf('.') + 1;
        if (extensionPos >= 0) if (filename.substring(extensionPos).equals("xml")) return true;
        return false;
    }
}
