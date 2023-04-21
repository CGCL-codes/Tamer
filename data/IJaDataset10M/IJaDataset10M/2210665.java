package uk.ac.ncl.neresc.dynasoar.utils;

import org.apache.log4j.Logger;
import java.util.Vector;
import java.util.Properties;
import java.io.*;
import uk.ac.ncl.neresc.dynasoar.messages.ServiceListItem;
import uk.ac.ncl.neresc.dynasoar.fault.ConfigurationException;

/**
 * A Utility class for handling the config files and registries of DynaSOAr.
 * @author Arijit Mukherjee
 */
public class FileUtils {

    private static Logger mLog = Logger.getLogger(FileUtils.class.getName());

    /**
     * Gets all the entries in each line of the file and returns them in a vector
     * @param fileName - name of the file to be read
     * @return a vector containing all the entries in form of Strings
     * @throws IOException
     */
    public static Vector getLineEntries(String fileName) throws IOException {
        Vector list = new Vector();
        try {
            BufferedReader csRead = new BufferedReader(new FileReader(fileName));
            String in = csRead.readLine();
            while (in != null) {
                mLog.debug("Entry: " + in);
                list.add(in);
                in = csRead.readLine();
            }
            csRead.close();
        } catch (IOException ix) {
            mLog.error("IOException: " + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
        return list;
    }

    /**
     * Gets each name value pair (delimited by "=") from the file, and creates a list
     * of items identified by the className passed as input
     * @param fileName - Name of the file to read
     * @param className - Name of the class whose list is to be created
     * @return A list of objects of the type identified by className
     * @throws IOException
     */
    public static Vector getSplittedEntries(String fileName, Class className) throws IOException {
        Vector list = new Vector();
        try {
            BufferedReader csRead = new BufferedReader(new FileReader(fileName));
            String in = csRead.readLine();
            while (in != null) {
                mLog.debug("Service entry: " + in);
                String[] split = in.split("=");
                mLog.debug("Name: " + split[0]);
                mLog.debug("URI: " + split[1]);
                if (className.getName().equals("uk.ac.ncl.neresc.dynasoar.messages.ServiceListItem")) {
                    ServiceListItem item = new ServiceListItem();
                    item.setServiceName(split[0]);
                    item.setServiceURI(split[1]);
                    list.add(item);
                }
                in = csRead.readLine();
            }
            csRead.close();
        } catch (IOException ix) {
            mLog.error("IOException:" + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
        return list;
    }

    /**
     * Adds a name-value pair to a file
     * @param fileName - Name of the file
     * @param argument - The name to be added
     * @param value - The value identified by the name
     * @throws IOException
     */
    public static void addNameValuePair(String fileName, String argument, String value) throws IOException {
        mLog.debug("Name: " + argument);
        mLog.debug("Value: " + value);
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                if (exists(argument, fileName)) {
                    return;
                }
            }
            String outputString = argument + "=" + value;
            mLog.debug("Ready to write");
            BufferedWriter csWrite = new BufferedWriter(new FileWriter(file, true));
            csWrite.write(outputString, 0, outputString.length());
            csWrite.newLine();
            mLog.debug("Finished writing file");
            csWrite.flush();
            csWrite.close();
        } catch (IOException ix) {
            mLog.error("IOException:" + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
    }

    /**
     * Adds a string entry to a file
     * @param fileName - Name of the file
     * @param value - entry to be added
     * @return - success or failure
     * @throws IOException
     */
    public static boolean addEntry(String fileName, String value) throws IOException {
        mLog.debug("Adding " + value + " to " + fileName);
        boolean found = false;
        boolean valueAdded = false;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                found = exists(value, fileName);
                valueAdded = true;
            }
            if (!found) {
                mLog.debug("Ready to write");
                BufferedWriter csWrite = new BufferedWriter(new FileWriter(file, true));
                csWrite.write(value, 0, value.length());
                csWrite.newLine();
                mLog.debug("Finished writing file");
                csWrite.flush();
                csWrite.close();
                valueAdded = true;
            } else {
                mLog.debug(value + " is already in " + fileName);
            }
        } catch (IOException ix) {
            mLog.error("IOException:" + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
        return valueAdded;
    }

    public static String getValueForName(String name, String fileName) throws IOException {
        boolean found = false;
        String location = null;
        try {
            BufferedReader csRead = new BufferedReader(new FileReader(fileName));
            String in = csRead.readLine();
            while (in != null && !found) {
                if (in.startsWith(name)) {
                    mLog.debug("Found file in the Code Store");
                    String[] split = in.split("=");
                    String filepath = split[1];
                    mLog.debug("location: " + filepath);
                    location = filepath;
                    found = true;
                }
                in = csRead.readLine();
            }
            csRead.close();
        } catch (IOException ix) {
            mLog.error("IOException:" + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
        return location;
    }

    public static String getValueForNameAsStream(String name, String fileName) throws IOException {
        String value = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                mLog.debug(fileName + " does not exist");
            } else {
                InputStream inpStream = null;
                Properties returnProperty = new Properties();
                inpStream = new FileInputStream(file);
                mLog.debug("Loading data from file...");
                if (inpStream != null) {
                    returnProperty.load(inpStream);
                    value = returnProperty.getProperty(name);
                }
            }
        } catch (IOException ix) {
            mLog.error("IOException:" + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
        return value;
    }

    public static boolean exists(String name, String fileName) throws IOException {
        boolean found = false;
        try {
            BufferedReader csRead = new BufferedReader(new FileReader(fileName));
            String in = csRead.readLine();
            while (in != null && !found) {
                if (in.startsWith(name)) {
                    mLog.debug("Found " + name + " in file " + fileName);
                    found = true;
                }
                in = csRead.readLine();
            }
            csRead.close();
        } catch (IOException ix) {
            mLog.error("IOException:" + ix.getMessage() + ", cause: " + ix.getCause());
            throw ix;
        }
        return found;
    }
}
