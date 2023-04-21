package com.orientechnologies.tools.oexplorer.filters;

import java.io.File;

public class ClassPathFileFilter extends BaseFileFilter {

    public boolean accept(File iFile) {
        try {
            if (iFile.isDirectory()) return true;
            String ext = iFile.getName().substring(iFile.getName().lastIndexOf('.'));
            if (ext.equals(".zip") || ext.equals(".jar") || ext.equals(".class")) return true;
        } catch (Exception e) {
        }
        return false;
    }

    public String getDescription() {
        return "JAR,ZIP,CLASS and directory";
    }

    public String getLastPathId() {
        return "oexplorer.paths.lastDbClassPath";
    }

    public static synchronized ClassPathFileFilter getInstance() {
        if (_instance == null) _instance = new ClassPathFileFilter();
        return _instance;
    }

    private static ClassPathFileFilter _instance = null;
}
