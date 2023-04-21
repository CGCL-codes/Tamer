package edu.unibi.agbi.dawismd.config;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;

/**
 * @author Benjamin Kormeier
 * @version 1.1 27.02.2008
 */
public class PackageList implements FileFilter {

    private final String SUFFIX = new String(".class");

    private String binDir = new String("bin/");

    private LinkedList<Class<?>> class_list = new LinkedList<Class<?>>();

    /**
	 * 
	 * @param packageName
	 * @throws ClassNotFoundException
	 */
    public PackageList(String packageName) throws ClassNotFoundException {
        this(packageName, null);
    }

    /**
	 * 	
	 * @param binDir
	 * @param packageName
	 * @throws ClassNotFoundException
	 */
    public PackageList(String binDir, String packageName) throws ClassNotFoundException {
        if (binDir != null) this.binDir = binDir;
        String path = packageName.replaceAll("\\.", "/");
        File direcrory = new File(path);
        File bin_dir = new File(this.binDir + path);
        if (direcrory.exists()) {
            if (direcrory.list().length == 0) throw new ClassNotFoundException("No resource for " + path); else {
                File[] class_files = direcrory.listFiles(this);
                for (File file : class_files) {
                    class_list.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - SUFFIX.length())));
                }
            }
        } else if (bin_dir.exists()) {
            if (bin_dir.list().length == 0) throw new ClassNotFoundException("No resource for " + path); else {
                File[] class_files = bin_dir.listFiles(this);
                for (File file : class_files) {
                    class_list.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - SUFFIX.length())));
                }
            }
        } else throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
    }

    public LinkedList<Class<?>> getClasses() {
        return class_list;
    }

    @SuppressWarnings("rawtypes")
    public String toString() {
        String string = new String();
        for (Class clazz : class_list) {
            string = string.concat(clazz.toString() + "\n");
        }
        return string;
    }

    public boolean accept(File pathname) {
        if (pathname.getName().endsWith(SUFFIX)) return true; else return false;
    }
}
