package com.orientechnologies.tools.oexplorer;

import java.util.*;
import javax.swing.*;

public class IconManager {

    protected IconManager() {
        instance = this;
        icons = new HashMap();
    }

    private ImageIcon registerIcon(String iName, boolean iRawName) {
        ImageIcon icon = null;
        String fileName = iRawName ? START_PATH + iName : START_PATH + iName + SIZE + EXTENSION;
        try {
            icon = new ImageIcon(com.orientechnologies.tools.oexplorer.IconManager.class.getResource(fileName));
            icons.put(iName, icon);
        } catch (Exception e) {
            System.out.println("ERROR: Cannot load graphical resource: " + fileName);
        }
        return icon;
    }

    public ImageIcon getIcon(String iName) {
        ImageIcon icon = (ImageIcon) icons.get(iName);
        if (icon != null) return icon;
        return registerIcon(iName, false);
    }

    public ImageIcon getImage(String iName) {
        ImageIcon icon = (ImageIcon) icons.get(iName);
        if (icon != null) return icon;
        return registerIcon(iName, true);
    }

    public static void createInstance() {
        instance = new IconManager();
    }

    public static IconManager getInstance() {
        if (instance == null) createInstance();
        return instance;
    }

    private HashMap icons;

    private static final String START_PATH = "images/";

    private static final String SIZE = "16";

    private static final String EXTENSION = ".gif";

    private static IconManager instance = null;
}
