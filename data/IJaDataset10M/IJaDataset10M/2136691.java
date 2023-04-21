package net.sf.logdistiller.gui;

import java.io.File;

public class XmlFileFilter extends javax.swing.filechooser.FileFilter {

    public XmlFileFilter() {
    }

    public boolean accept(File f) {
        return (f.isDirectory() || f.getName().endsWith(".xml"));
    }

    public String getDescription() {
        return "XML files";
    }
}
