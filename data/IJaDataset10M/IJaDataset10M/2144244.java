package uk.ac.shef.wit.saxon.util;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides a number of utility methods that are needed across the Saxon library.
 * @author Mark A. Greenwood
 * @version $Id: Utils.java 521 2008-09-26 11:38:44Z greenwoodma $
 */
public class Utils {

    /**
	 * All the methods of this class will be static so ensure that no one creates
	 * an instance of this class.
	 */
    private Utils() {
    }

    /**
	 * Returns a single boolean parameter value by combining the value from two sets.
	 * @param key the parameter value to return
	 * @param map1 the 1st map from which to get parameter values
	 * @param map2 the 2nd map from which to get parameter values
	 * @return a boolean parameter value
	 */
    public static boolean getBooleanValue(String key, Map map1, Map map2) {
        Boolean b1 = (Boolean) map1.get(key);
        Boolean b2 = (Boolean) map2.get(key);
        return (b1 != null && b1.booleanValue()) || (b2 != null && b2.booleanValue());
    }

    /**
	 * Return a Set parameter value by combining the sets found in two other
	 * config maps.
	 * @param key the parameter value to return
	 * @param map1 the 1st map from which to get parameter values
	 * @param map2 the 2nd map from which to get parameter values
	 * @return a Set parameter value
	 */
    public static Set getSetValue(String key, Map<String, Object> map1, Map<String, Object> map2) {
        Set<Object> combined = new HashSet<Object>();
        @SuppressWarnings("unchecked") Set<Object> s1 = (Set<Object>) map1.get(key);
        @SuppressWarnings("unchecked") Set<Object> s2 = (Set<Object>) map2.get(key);
        if (s1 != null) combined.addAll(s1);
        if (s2 != null) combined.addAll(s2);
        return combined;
    }

    /**
	 * Creates a temporary directory, something not supported by
	 * the standrad java.io.File class.
	 * @param prefix the prefix of the directory name to create
	 * @return a File instance representing the temp directory
	 */
    public static final File createTempDir(String prefix) {
        String os_tmpdir = System.getProperty("java.io.tmpdir");
        File tmpdir = new File(os_tmpdir, prefix + "-" + System.currentTimeMillis());
        while (tmpdir.exists()) {
            tmpdir = new File(os_tmpdir, prefix + "-" + System.currentTimeMillis());
        }
        if (!tmpdir.mkdirs()) throw new RuntimeException("Unable to create temporary directory!");
        return tmpdir;
    }

    /**
	 * Recurisvely delete the directory and all it's contents.
	 * NOTE: This hasn't been fully tested but can't delete anything
	 * other than the content of the specified directory.
	 * @param dir the directory to delete
	 */
    public static void deleteDir(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) deleteDir(f);
            if (!f.delete()) f.deleteOnExit();
        }
        if (!dir.delete()) dir.deleteOnExit();
    }
}
