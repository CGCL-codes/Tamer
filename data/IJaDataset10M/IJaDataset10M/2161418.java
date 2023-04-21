package com.landak.ipod;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseDB {

    static final String CONTROL_DIR = "iPod_Control";

    public static final String DEVICE_DIR = CONTROL_DIR + File.separator + "Device";

    public static final String MUSIC_DIR = CONTROL_DIR + File.separator + "Music";

    public static final String ITUNES_DIR = CONTROL_DIR + File.separator + "iTunes";

    static final String MUSIC_SUBDIR[] = { "F00", "F01", "F02", "F03", "F04", "F05", "F06", "F07", "F08", "F09", "F10", "F11", "F12", "F13", "F14", "F15", "F16", "F17", "F18", "F19" };

    protected String iPodPath;

    protected Logger log = Logger.getAnonymousLogger();

    protected RandomAccessFile di;

    /**
     * @return Returns the iPodPath.
     */
    public String getIPodPath() {
        return iPodPath;
    }

    /**
     * @param podPath The iPodPath to set.
     */
    public void setIPodPath(String podPath) {
        log.setLevel(Level.ALL);
        iPodPath = podPath;
    }

    public BaseDB(String mpt) {
        setIPodPath(mpt);
    }

    protected void close() {
        try {
            if (di != null) di.close();
        } catch (IOException e) {
        }
    }

    protected String getMusicDesc(FileMeta fm) {
        return "(" + fm.id + ") " + fm.path.replace(':', File.separatorChar);
    }
}
