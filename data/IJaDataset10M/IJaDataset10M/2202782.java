package org.mitre.mrald.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *  Filename filter for returning files that start with the current user's id
 *
 *@author     Jeffrey Hoyt
 *@created    November 8, 2002
 */
public class NonPublicFilenameFilter implements FilenameFilter {

    /**
     *  Constructor for the UserFilenameFilter object
     *
     *@param  userid  Description of the Parameter
     */
    public NonPublicFilenameFilter(String userid) {
    }

    /**
     *  Constructor for the UserFilenameFilter object
     *
     *@param  userid  Description of the Parameter
     */
    public NonPublicFilenameFilter(int userid) {
    }

    /**
     *  Constructor for the UserFilenameFilter object
     */
    public NonPublicFilenameFilter() {
    }

    /**
     *  main for testing and showing usage
     *
     *@param  args  Description of the Parameter
     */
    public static void main(String args[]) {
        File dir = new File(args[0]);
        UserFilenameFilter filter = new UserFilenameFilter();
        String[] list = dir.list(filter);
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }

    /**
     *  main for testing and showing usage
     *
     *@param  args  Description of the Parameter
     */
    public String[] filter(String dirName) {
        File dir = new File(dirName);
        return dir.list(this);
    }

    /**
     *  main for testing and showing usage
     *
     *@param  args  Description of the Parameter
     */
    public String[] filter(File dir) {
        return dir.list(this);
    }

    /**
     *  Tests if a specified file should be included in a file list.
     *
     *@param  dir   the directory in which the file was found
     *@param  name  the name of the file
     *@return       true if and only if the name should be included in the file
     *      list; false otherwise
     */
    public boolean accept(File dir, String name) {
        boolean toRet = name.endsWith("jsp") || name.endsWith("xml");
        return toRet && !(name.startsWith("public")) && (!name.startsWith("build.xml"));
    }
}
