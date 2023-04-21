package com.googlecode.jazure.sdk.spi.classloader;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AppClassLoaders {

    private static ConcurrentMap<String, AppClassLoader> classloaders = new ConcurrentHashMap<String, AppClassLoader>();

    public static ClassLoader lookup(String projectName) {
        return classloaders.get(projectName);
    }

    public static void initialize() {
        List<String> apps = getApps();
        for (String app : apps) {
            refresh(app);
        }
    }

    public static void refresh(String projectName) {
        classloaders.put(projectName, new AppClassLoader(getApproot() + projectName));
    }

    private static List<String> getApps() {
        List<String> apps = new ArrayList<String>();
        File[] subfolders = listDirectories(getApproot());
        for (File folder : subfolders) {
            String app = folder.getPath().substring(folder.getPath().lastIndexOf("/") + 1);
            apps.add(app);
        }
        return apps;
    }

    /**
	 * Consider alternative folders
	 */
    public static String getApproot() {
        return System.getProperty("user.dir") + "/apps/";
    }

    private static File[] listDirectories(String folder) {
        return new File(folder).listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
    }
}
