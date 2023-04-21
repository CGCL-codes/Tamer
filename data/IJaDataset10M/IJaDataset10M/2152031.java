package com.jpatch.settings;

import com.jpatch.afw.settings.*;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DirectorySettings extends AbstractSettings {

    Icon icon = new ImageIcon(ClassLoader.getSystemResource("jpatch/images/prefs/directories.png"));

    public Icon getIcon() {
        return icon;
    }

    public boolean rememberLastDirectories = true;

    public File jpatchFiles = new File(System.getProperty("user.dir"));

    public File spatchFiles = new File(System.getProperty("user.dir"));

    public File animationmasterFiles = new File(System.getProperty("user.dir"));

    public File povrayFiles = new File(System.getProperty("user.dir"));

    public File rendermanFiles = new File(System.getProperty("user.dir"));

    public File objFiles = new File(System.getProperty("user.dir"));

    public File rotoscopeFiles = new File(System.getProperty("user.dir"));
}
