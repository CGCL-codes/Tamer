package swym.client;

import java.util.*;
import java.io.*;

/**
 *
 * @author henryja
 */
public class VersionChecker {

    private static VersionChecker instance = new VersionChecker();

    private Vector<String> locks;

    private File versionFile;

    private VersionChecker() {
        locks = new Vector<String>();
        versionFile = new File("watchVersionFile");
        try {
            versionFile.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static VersionChecker getInstance() {
        return instance;
    }

    public String[] getVersionInformation(String filePath) {
        String[] out = new String[2];
        String line;
        boolean found = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(versionFile));
            while ((line = reader.readLine()) != null && !found) {
                if (line.startsWith(filePath)) {
                    found = true;
                    out[1] = line.substring(line.lastIndexOf("|"));
                    line = line.replace("|" + out[1], "");
                    out[0] = line.substring(line.lastIndexOf("|"));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public void setVersionInformation(String filePath, String version, String modDateTime) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(versionFile, true));
            writer.append(filePath + "|" + version + "|" + modDateTime + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String getFileLock(String filePath) {
        if (!locks.contains(filePath)) locks.add(filePath);
        return locks.get(locks.indexOf(filePath));
    }
}
