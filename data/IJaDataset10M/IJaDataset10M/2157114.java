package daria;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fars
 */
public class cDBDriver {

    private static final String dvrPath = "drivers";

    /**
     * gets the absolute path for the drivers directory
     * if the diretory does not exist, it creates it 
     * 
     * @return String
     */
    private String getAppPath() {
        String abspath = new File(dvrPath).getAbsolutePath();
        boolean exists = new File(abspath).exists();
        if (!exists) {
            File x = new File(abspath);
            x.mkdir();
        }
        return abspath;
    }

    /**
     * Proceeds to scan the driver directory for any jar or zip files
     * when any is found, it is added to a list
     * 
     * @throws FileNotFoundException
     * @return String[]
     */
    private String[] ScanDriverFiles() throws FileNotFoundException {
        String dirPath = getAppPath();
        File dvrdir = new File(dirPath);
        File lstFiles[] = dvrdir.listFiles(new FilenameFilter() {

            public boolean accept(File f, String name) {
                return name.endsWith(".jar");
            }
        });
        String[] drivers = new String[lstFiles.length];
        for (int i = 0; i < lstFiles.length; i++) drivers[i] = lstFiles[i].getAbsolutePath();
        return drivers;
    }

    /**
     * Given a valid Jar or Zip filename, said file is scanned to determine
     * if it has a Driver Class within. If found the driver class is returned
     * 
     * @param filename
     * @throws java.io.IOException
     * @return String
     */
    private String JarDir(String filename) throws IOException {
        JarFile jfile = new JarFile(filename);
        Enumeration enm = jfile.entries();
        String driver = null;
        while (enm.hasMoreElements() && driver == null) driver = processjar(enm.nextElement());
        return driver;
    }

    /**
     * Given a Jar Entry object, said entry is scanned to determine if it
     * is or is not a Driver Class
     * 
     * Driver Classes are assumed to end with "Driver.class"
     * 
     * When the driver class is found, it's path is returned, for example
     * the MySQL driver class would return:
     *      "com.mysql.jdbc.Driver"    
     * 
     * If no *Driver.class is found, the return is a null value
     * 
     * @param obj
     * @return String
     */
    private String processjar(Object obj) {
        String driver = null;
        JarEntry entry = (JarEntry) obj;
        String name = entry.getName();
        if (name.endsWith("Driver.class")) driver = name.replaceAll("/", ".").substring(0, name.length() - ".class".length());
        return driver;
    }

    /**
     * Proceeds to scan the drivers directory for any possible JDBC driver
     * when such drivers are found, it attempts to load them so that they may be
     * utilized by the DriverManager
     */
    public void LoadDrivers() {
        String[] drivers = null;
        try {
            drivers = ScanDriverFiles();
        } catch (FileNotFoundException ex) {
            drivers = null;
            Logger.getLogger(cDBDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (drivers == null) return;
        for (int i = 0; i < drivers.length; i++) {
            try {
                String dvrclass = JarDir(drivers[i]);
                if (dvrclass != null) {
                    cClasspath.addFile(drivers[i]);
                    Class.forName(dvrclass);
                }
            } catch (IOException eio) {
                Logger.getLogger(frmDBSet.class.getName()).log(Level.SEVERE, null, eio);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(frmDBSet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
