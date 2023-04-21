package ase.eleitweg.client.gui.utils;

import ase.eleitweg.common.*;
import java.io.*;
import org.apache.log4j.*;

public class FileHandler {

    private static Logger log = Logger.getLogger(FileHandler.class);

    public static File prepareDir(User u) {
        File homeDirectory = new File(System.getProperty("user.home"));
        File eleitwegDir = new File(homeDirectory, "eleitweg");
        if (!eleitwegDir.exists()) {
            if (!eleitwegDir.mkdir()) {
                log.error("Cannot create directory.");
                return null;
            }
        }
        File userDir = new File(eleitwegDir, Integer.toString(u.getId()));
        if (!userDir.exists()) {
            if (!userDir.mkdir()) {
                log.error("Cannot create directory.");
                return null;
            }
        }
        return userDir;
    }

    public static File getRunDir(File userDir, int id) {
        File runDir = new File(userDir, Integer.toString(id));
        if (!runDir.exists()) {
            if (!runDir.mkdir()) {
                log.error("Cannot create directory.");
                return null;
            }
        }
        return runDir;
    }

    public static void copy(File source, File dest) throws IOException {
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
