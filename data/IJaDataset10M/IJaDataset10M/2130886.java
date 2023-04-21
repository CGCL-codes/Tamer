package com.ynhenc.gis.file;

import java.io.*;

public class FileFilter_02_Folder extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {

    public FileFilter_02_Folder() {
        super();
    }

    public boolean accept(File file) {
        return file.isDirectory();
    }

    public String getDescription() {
        return "Folders Only";
    }
}
