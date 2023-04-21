package com.pyrphoros.erddb.gui.common.filters;

import com.pyrphoros.erddb.Designer;
import com.pyrphoros.erddb.gui.util.FileExtension;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 */
public class ZPyrFilter extends FileFilter implements IFileFilter {

    @Override
    public String getDescription() {
        return Designer.getResource("gui.filter.zpyr");
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String ext = FileExtension.getExtension(f);
        return (ext != null && ext.equals(FileExtension.ZPYR));
    }

    public ArrayList<String> getExtensions() {
        ArrayList<String> al = new ArrayList<String>();
        al.add(FileExtension.ZPYR);
        return al;
    }
}
