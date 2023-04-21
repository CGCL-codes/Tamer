package com.googlecode.progobots.ui;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.filechooser.FileFilter;

/**
 * Filter for world files.
 */
public class WorldFileFilter extends FileFilter {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("progobots/ProgobotsUI");

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            return f.getAbsolutePath().toLowerCase().endsWith(".pw");
        }
    }

    @Override
    public String getDescription() {
        return bundle.getString("Progobots_World_(*.pw)");
    }
}
