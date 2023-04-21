package org.sdrinovsky.sdsvn.files;

import java.io.File;
import static org.sdrinovsky.sdsvn.files.FileTableRow.*;

/**
 * Class Location
 *
 *
 * @author
 * @version $Revision: 86 $
 */
public class Location {

    private final String server;

    private final String branch;

    private final String path;

    private final File workingCopyBase;

    /**
   * Constructor Location
   *
   *
   *
   * @param file
   * @param url
   *
   */
    public Location(File file, String url) {
        this(file, parseURL(url, PARSE_URL_SERVER), parseURL(url, PARSE_URL_BRANCH), parseURL(url, PARSE_URL_PATH));
    }

    /**
   * Constructor Location
   *
   *
   *
   * @param file
   * @param server
   * @param branch
   * @param path
   *
   */
    public Location(File file, String server, String branch, String path) {
        this.server = server;
        this.branch = branch;
        this.path = path;
        String tmpwc = file.toString().replace('\\', '/');
        if (tmpwc.length() > path.length() + 1) {
            this.workingCopyBase = new File(tmpwc.substring(0, tmpwc.length() - path.length()));
        } else {
            this.workingCopyBase = null;
        }
    }

    /**
   * Method getServer
   *
   *
   * @return
   *
   */
    public String getServer() {
        return server;
    }

    /**
   * Method getBranch
   *
   *
   * @return
   *
   */
    public String getBranch() {
        return branch;
    }

    /**
   * Method getPath
   *
   *
   * @return
   *
   */
    public String getPath() {
        return path;
    }

    /**
   * Method getURL
   *
   *
   * @return
   *
   */
    public String getURL() {
        return server + "/" + branch + "/" + path;
    }

    /**
   * Method getURL
   *
   *
   * @param newPath
   *
   * @return
   *
   */
    public String getURL(String newPath) {
        return server + "/" + branch + "/" + newPath;
    }

    /**
   * Method getWorkingCopyBase
   *
   *
   * @return
   *
   */
    public File getWorkingCopyBase() {
        return workingCopyBase;
    }

    /**
   * Method toString
   *
   *
   * @return
   *
   */
    @Override
    public String toString() {
        return branch;
    }
}
