package edu.fit.it.blue;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

class blueBrowseForFilename {

    private String file;

    private JFileChooser fileChooser;

    public blueBrowseForFilename() {
        fileChooser = new JFileChooser();
    }

    public String getName() {
        return file;
    }

    public int showDialog(Component parent, String title, String dir, int mode) {
        if (title == null) title = "Select file.";
        fileChooser.setDialogTitle(title);
        if (dir == null) fileChooser.setCurrentDirectory(null); else fileChooser.setCurrentDirectory(new File(dir));
        fileChooser.setFileSelectionMode(mode);
        fileChooser.rescanCurrentDirectory();
        int status = fileChooser.showDialog(parent, "Select");
        if (status == JFileChooser.APPROVE_OPTION) file = fileChooser.getCurrentDirectory() + File.separator + fileChooser.getSelectedFile().getName();
        return status;
    }

    public int showDialog(Component parent, String title, String dir) {
        return showDialog(parent, title, dir, JFileChooser.FILES_ONLY);
    }

    public int showDialog(Component parent, String title) {
        return showDialog(parent, title, ".", JFileChooser.FILES_ONLY);
    }

    public int showDialog(String title) {
        return showDialog(null, title, ".", JFileChooser.FILES_ONLY);
    }

    public int showDialog() {
        return showDialog(null, "Select a file.", ".", JFileChooser.FILES_ONLY);
    }
}
