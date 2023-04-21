package vkmc;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Exit93
 */
public class Settings {

    private static String home = System.getProperty("user.home");

    private static String dldir = home + "/VKMC";

    private static String cfgfile = "./vkmc.cfg";

    public static void Initialize() {
        if (pathExists(cfgfile)) {
            dldir = readDldirPath();
            return;
        }
        writeDldirPath(dldir);
    }

    private static String readDldirPath() {
        try {
            String path = dldir;
            BufferedReader in = new BufferedReader(new FileReader(cfgfile));
            path = in.readLine();
            in.close();
            return path;
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            return dldir;
        }
    }

    private static void writeDldirPath(String path) {
        try {
            if (pathExists(cfgfile)) {
                File file = new File(cfgfile);
                file.delete();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(cfgfile));
            out.write(path);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static boolean pathExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String getDldir() {
        return dldir;
    }

    public static void setDldir(String path) {
        dldir = path;
        writeDldirPath(path);
    }

    public static void createDldirIfNotExist() {
        if (pathExists(dldir)) {
            return;
        }
        File file = new File(dldir);
        file.mkdir();
    }
}
