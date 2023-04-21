package org.oclc.da.ndiipp.ingester.lookup;

import java.io.File;
import java.util.Arrays;

/**
 * Utility methods for naming/renaming batch files.
 *
 * @author Leah Houser  December 5, 2002
 */
public class BatchNameUtilities {

    private static final String me = "BatchNameUtilities ";

    /** inprocess extention */
    public static final String INPROCESS = ".inprocess";

    /** invalid_data extention */
    public static final String INVALID_DATA = ".invalid_data";

    /** system_error extention */
    public static final String SYSTEM_ERROR = ".system_error";

    /** retry extention */
    public static final String RETRY = ".retry";

    /** complete extention */
    public static final String COMPLETE = ".complete";

    /** notready extention */
    public static final String NOTREADY = ".notready";

    /** ready extention */
    public static final String READY = ".ready";

    /** shipped extention */
    public static final String SHIPPED = ".shipped";

    /** 
     *  Initialize
     * @throws Exception 
     */
    public BatchNameUtilities() throws Exception {
    }

    /** Rename the batch file based on status
      * @param oldName 
      * @param newStatus 
      * @return the new filename
      * @throws Exception 
      */
    public static String renameFileToStatus(String oldName, String newStatus) throws Exception {
        if (!matchesAStatus(newStatus)) throw new Exception("Invalid status.  Use list of statuses from BatchNameUtilities.java");
        File theFile = new File(oldName);
        String newName = changeStatus(oldName, newStatus);
        File newFile = new File(newName);
        boolean ok;
        try {
            ok = theFile.renameTo(newFile);
        } catch (Exception e) {
            theFile = null;
            newFile = null;
            throw new Exception(me + "error renaming batch file from " + oldName + " to " + newName + ".  Error was: +" + e.getMessage());
        }
        theFile = null;
        newFile = null;
        if (ok) return (newName); else throw new Exception(me + "error renaming batch file from " + oldName + " to " + newName + ".  No exception from the rename code");
    }

    /** delete this file, & everything under it if a directory
      * @param fileName 
      * @return boolean true if successful; false otherwise
      */
    public boolean delete(String fileName) {
        File theFile = new File(fileName);
        boolean status = theFile.delete();
        theFile = null;
        return (status);
    }

    /** Should this batch be processed?
     * 
     * @param path path
     * @return is ignorable batch?
     */
    public boolean isIgnorableBatch(String path) {
        int dot = path.lastIndexOf(".");
        if (dot <= 0) return (false);
        String status = path.substring(dot);
        if (matchesAStatus(status)) return (true);
        return (false);
    }

    /**
       * Modifies the pathname string to change the status portion to newStatus.
       * Handles both Hub&Spoke and DA pattern pathnames.
       * @param path path
       * @param newStatus new statuss
       * @return channge path status
       */
    public static String changePathStatus(String path, String newStatus) {
        if (path.endsWith(".zip")) return changeHSStatus(path, newStatus); else return changeStatus(path, newStatus);
    }

    /**
       * Modifies the pathname string to change the status portion to newStatus.
       * Handles DA pattern pathnames.
       * @param path new path
       * @param newStatus new status
       * @return change hub and spoke status
       */
    public static String changeHSStatus(String path, String newStatus) {
        int ziploc;
        String ziplessPath = null;
        ziploc = path.lastIndexOf(".zip");
        if (ziploc < 0) return (path + newStatus + ".zip");
        ziplessPath = path.substring(0, ziploc);
        return changeStatus(ziplessPath, newStatus) + ".zip";
    }

    /**
       * Modifies the pathname string to change the status portion to newStatus.
       * Handles both Hub&Spoke pattern pathnames, meaning it accomodates files ending
       * with .zip
       * @param path
       * @param newStatus new status
       * @return change status
       */
    public static String changeStatus(String path, String newStatus) {
        int dot;
        dot = path.lastIndexOf(".");
        if (dot < 0) return (path + newStatus);
        return (path.substring(0, dot) + newStatus);
    }

    /**
       * Retrieves the .suffix part of a pathname, which should
       * be the batch status
       * @param path
       * @return  batch status
       */
    public String getStatus(String path) {
        int dot;
        dot = path.lastIndexOf(".");
        if (dot < 0) return null;
        return (path.substring(dot));
    }

    /**
       * Removes the .suffix part of the pathname, which should be
       * the path status.
       * @param path
       * @return  pathname sans .status
       */
    public static String removeStatus(String path) {
        int dot;
        dot = path.lastIndexOf(".");
        if (dot < 0) return null;
        return (path.substring(0, dot));
    }

    private static boolean matchesAStatus(String status) {
        if ((status.equalsIgnoreCase(BatchNameUtilities.INPROCESS)) || (status.equalsIgnoreCase(BatchNameUtilities.INVALID_DATA)) || (status.equalsIgnoreCase(BatchNameUtilities.RETRY)) || (status.equalsIgnoreCase(BatchNameUtilities.READY)) || (status.equalsIgnoreCase(BatchNameUtilities.COMPLETE)) || (status.equalsIgnoreCase(BatchNameUtilities.NOTREADY))) return true;
        return false;
    }

    /**
      * sorts an array of filenames by last modified date
      * @param files 
      */
    @SuppressWarnings("unchecked")
    public void sortFilesByDateModified(String[] files) {
        Arrays.sort((Object[]) files, new BatchComparator());
    }

    /**
      * Pre-pend the directory pathname & separator to each filename
      * @param fileNames 
      * @param batchDirectoryPath 
      * @return fully qualified paths
      */
    public String[] fullyQualifyPaths(String[] fileNames, String batchDirectoryPath) {
        if (fileNames.length <= 0) return (null);
        String[] quals = new String[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) quals[i] = batchDirectoryPath + fileNames[i];
        return quals;
    }

    /** Test routines for this class
     * @param args 
     * @throws Exception 
     */
    public static void main(String args[]) throws Exception {
        System.out.println(me + "test main\n");
        BatchNameUtilities bnu = null;
        try {
            bnu = new BatchNameUtilities();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        String fileName;
        try {
            System.out.println("this should fail due to a bad status value\n");
            fileName = BatchNameUtilities.renameFileToStatus("abc", "xyz");
            System.out.println("rename succeeded but shouldn't have");
        } catch (Exception e) {
            System.out.println("rename failed as expected\n");
            System.out.println(e.toString());
        }
        fileName = "R:/dev1/test/batch2";
        try {
            System.out.println("this should succeed\n");
            fileName = BatchNameUtilities.renameFileToStatus(fileName, BatchNameUtilities.INPROCESS);
            System.out.println("rename succeeded");
        } catch (Exception e) {
            System.out.println("rename failed\n");
            System.out.println(e.toString());
        }
        try {
            System.out.println("this should succeed\n");
            fileName = BatchNameUtilities.renameFileToStatus(fileName, BatchNameUtilities.COMPLETE);
            System.out.println("rename succeeded");
        } catch (Exception e) {
            System.out.println("rename failed\n");
            System.out.println(e.toString());
        }
        System.out.println("test completed");
    }
}
