package org.nextmock.utils;

import java.io.File;

/**
 * Basedir allow to access file relatively to the root of the project 
 * When using maven (or ant) for your unit test, it is sometimes difficult to acces
 * files using path relativly to the root of the project.  Just using
 * new File(...) will returns a file relatively to the execution directory.
 * This class provide methos to obtain File relativly to the roo project directory.
 * This class look for a system property named "basedir" set by the surefire maven
 * plugin.  If basedir is not set, the current execution directory is used by default.
 *  
 * @author ScokartG
 */
public class Basedir {

    public static String getRelativePath(String relativePath) {
        return new File(System.getProperty("basedir"), relativePath).getAbsolutePath();
    }

    public static File getRelativeFile(String relativePath) {
        return new File(new File(System.getProperty("basedir")), relativePath);
    }
}
