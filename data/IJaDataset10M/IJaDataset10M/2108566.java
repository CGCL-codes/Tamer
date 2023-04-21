package org.cobertura4j2me.reporting;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import org.cobertura4j2me.runtime.Factory;

/**
 * Because the source files can be dispersed in different
 * folders, we need to recursively search through the given
 * source folder
 */
public class SourceFileLocator {

    /**
     * The root directory
     */
    private File sourceDir;

    /**
     * Our cache
     */
    private ArrayList cache;

    /**
     * The list of directories we don't want to search
     */
    private static final String[] BLACK_LIST = new String[] { "cldc1_0", "midp1" };

    /**
     * The FileFilter we use
     */
    private static FileFilter filter = new FileFilter() {

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                String path = pathname.getAbsolutePath();
                for (int i = 0; i < BLACK_LIST.length; i++) {
                    if (path.indexOf(BLACK_LIST[i]) != -1) return false;
                }
                return true;
            }
            if (pathname.getName().endsWith(".java")) return true;
            return false;
        }
    };

    /**
     * Constructs a new FileSourceLocator
     * 
     * @param sourceDir the root directory
     */
    public SourceFileLocator(File sourceDir) {
        this.sourceDir = sourceDir;
        cache = new ArrayList(100);
    }

    /**
     * Locates the given source file
     * 
     * @param className the name of the source file (pkg1/pkg2/../MyClass.java)
     * 
     * @return the source file
     */
    public File locateSourceFile(String className) {
        if (sourceDir == null || !sourceDir.exists()) return null;
        int index = className.indexOf('$');
        if (index != -1) className = className.substring(0, index) + ".java";
        Factory.trace(Factory.TRACE_REPORTING, "Locating " + className + "...");
        File file = searchCache(className);
        if (file == null) {
            file = searchFileSystem(sourceDir, className);
        }
        return file;
    }

    /**
     * Searches the cache for the given class name
     * 
     * @param className the source file name
     * 
     * @return the source file or null if not found
     */
    private File searchCache(String className) {
        Factory.trace(Factory.TRACE_REPORTING, "Searching cache...");
        for (int i = 0; i < cache.size(); i++) {
            String file = (String) cache.get(i);
            if (file.endsWith(className)) {
                Factory.trace(Factory.TRACE_REPORTING, "Found in cache");
                return new File(file);
            }
        }
        Factory.trace(Factory.TRACE_REPORTING, "Nothing found");
        return null;
    }

    /**
     * We recursively search the file system starting from
     * the given directory and cache all found java source
     * files that don't match <code>className</code>
     * 
     * @param dir the directory to start searching from
     * @param className the source file we are looking for
     * @return the source file or null if not found
     */
    private File searchFileSystem(File dir, String className) {
        Factory.trace(Factory.TRACE_REPORTING, "Searching dir " + dir.getPath() + "...");
        File sourceFile = null;
        File[] children = dir.listFiles(filter);
        for (int i = 0; i < children.length; i++) {
            File child = children[i];
            if (child.isDirectory()) {
                sourceFile = searchFileSystem(child, className);
                if (sourceFile != null) break;
            } else {
                String path = child.getPath().replace('\\', '/');
                if (path.endsWith(className)) {
                    Factory.trace(Factory.TRACE_REPORTING, "Found in file system");
                    sourceFile = child;
                    break;
                } else if (!cache.contains(path)) {
                    Factory.trace(Factory.TRACE_REPORTING, "Caching " + path + "...");
                    cache.add(path);
                }
            }
        }
        return sourceFile;
    }
}
