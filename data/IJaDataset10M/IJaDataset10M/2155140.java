package com.continuent.tungsten.commons.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Methods to help loading manipulating a propertlies file
 * 
 * @author <a href="mailto:joe.daly@continuent.com">Joe Daly</a>
 * @version 1.0
 */
public class PropertyCommands {

    static Logger logger = Logger.getLogger(PropertyCommands.class);

    /**
     * Loads a property file that in name=value. This is used rather then using
     * the Properties.load(InputStream inStream) to allow paths on windows to be
     * loaded without having to add \\ for paths.
     * 
     * @param propertyFile the property file to read in.
     * @return a listing of properties for the given property file
     * @throws Exception if the property file can not be loaded
     */
    public static Properties readPropertyFile(String propertyFile) throws Exception {
        Properties properties = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(propertyFile));
            properties = new Properties();
            String str;
            while ((str = br.readLine()) != null) {
                str = str.trim();
                if ((!str.startsWith("#")) && (str.length() > 0)) {
                    int indexOf = str.indexOf("=");
                    int length = str.length();
                    String propertyName = str.substring(0, indexOf);
                    String value = "";
                    int valueStart = indexOf + 1;
                    if (valueStart == length) {
                        value = "";
                    } else {
                        value = str.substring(valueStart, length);
                    }
                    properties.setProperty(propertyName, value);
                }
            }
        } catch (Throwable t) {
            throw new Exception("Unable to load property file" + propertyFile, t);
        }
        return properties;
    }

    /**
     * Updates a property in a file.
     * 
     * @param property the property to update
     * @param value the value to update to
     * @param propertyFile the file path
     * @param keepBackup if an update was done, keep the old copy around
     * @param add if the property does not exist append to the end of the file
     * @return true if the file was update, false if the property did not exist and no update
     * was done
     * @throws Exception
     */
    public static boolean updatePropertyInPropertyFile(String property, String value, String propertyFile, boolean keepBackup, boolean add) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(propertyFile));
        String tmpPropertiesFile = propertyFile + ".tmp";
        File tmpFile = new File(tmpPropertiesFile);
        FileOutputStream out = new FileOutputStream(tmpFile);
        PrintStream printStream = new PrintStream(out);
        String str;
        boolean didUpdate = false;
        while ((str = br.readLine()) != null) {
            str = str.trim();
            if ((str.startsWith("#"))) {
                printStream.println(str);
            } else {
                if (str.indexOf(property) > -1) {
                    StringBuffer buffer = new StringBuffer(50);
                    buffer.append(property).append("=").append(value);
                    printStream.println(buffer.toString());
                    didUpdate = true;
                } else {
                    printStream.println(str);
                }
            }
        }
        if (!didUpdate) {
            if (add) {
                StringBuffer buffer = new StringBuffer(50);
                buffer.append(property).append("=").append(value);
                printStream.println(buffer.toString());
                didUpdate = true;
            }
        }
        printStream.close();
        if (didUpdate) {
            if (keepBackup) {
                File oldFile = new File(propertyFile);
                String parentDirectory = oldFile.getParent();
                String fileName = oldFile.getName();
                String nextBackupName = getNextBackupName(parentDirectory, fileName, ".bak");
                String nextBackupPath = parentDirectory + File.separator + nextBackupName;
                DirectoryCommands.copyFile(propertyFile, nextBackupPath);
            }
            DirectoryCommands.copyFile(tmpPropertiesFile, propertyFile);
            tmpFile.delete();
            return true;
        } else {
            tmpFile.delete();
            return false;
        }
    }

    private static String getNextBackupName(String directoryPath, String fileName, String extension) {
        String startFileName = fileName + extension;
        File directory = new File(directoryPath);
        int value = -1;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isFile()) {
                    String tmpFileName = f.getName();
                    if (tmpFileName.startsWith(startFileName)) {
                        logger.debug("fileName is: " + tmpFileName);
                        String intString = tmpFileName.substring(startFileName.length() + 1, tmpFileName.length());
                        try {
                            int tmpInt = Integer.parseInt(intString);
                            if (tmpInt > value) {
                                value = tmpInt;
                            }
                        } catch (Throwable t) {
                            logger.debug("file name not recognized ignoring=" + tmpFileName);
                        }
                    }
                }
            }
        }
        int newValue = value + 1;
        return fileName + extension + "." + newValue;
    }
}
