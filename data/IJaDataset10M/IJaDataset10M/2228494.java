package net.kirke.mp3dj;

import net.kirke.mp3dj.resources.Msgs;
import java.io.*;
import java.sql.*;

/**
 * Refresh (recreate) mysql database reflecting tree of mp3 files.
 * 
 * We use MySQL Connector/J to access the database from java.
 * 	http://www.mysql.com/products/connector/j/
 * 	http://dev.mysql.com/doc/connector/j/en/index.html
 * 
 * JDBC (java.sql) docs:
 * 	http://java.sun.com/j2se/1.5.0/docs/api/
 *
 * @author Kirk Erickson (latest modification by $Author: kirke $)
 * @version $Revision: 1.3 $ $Date: 2008/06/21 04:46:35 $
 */
public class Refresh implements Runnable, Serializable {

    private boolean done;

    private boolean stop;

    private int totalFiles;

    private Msgs msgs;

    private RefreshFiles files;

    private String directory;

    private String url;

    /**
	 *	Refresh (recreate) database identified by '_url' by
	 *	reading and parsing id3 tag information from mp3 files
	 *  subordinate to 'directory'.
	 */
    public Refresh(String _directory, String _url) {
        directory = _directory;
        done = false;
        files = null;
        msgs = new Msgs();
        stop = false;
        totalFiles = 0;
        url = _url;
    }

    /**
	 *	Register driver, then make connection
	 */
    private Connection connect(String URL) {
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
        } catch (Exception x) {
            Object params[] = { driver, x.getMessage() };
            String msg = msgs.getMessage("UNABLE_TO_LOAD_DRIVER", params);
            System.out.println(msg);
        }
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException x) {
            Object params[] = { x.getMessage() };
            String msg = msgs.getMessage("UNABLE_TO_CONNECT", params);
            System.out.println(msg);
        }
        return (conn);
    }

    /**
	 *	Close the mysql connection.
	 */
    private void disconnect(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException x) {
            System.out.println(x.getMessage());
        }
    }

    /**
	 *	Process files under dir (recursively)
	 */
    private void setTotalFiles(File dir) {
        if (stop) return;
        if (dir.isDirectory()) {
            if ((dir.getName()).endsWith("[Incoming]") || (dir.getName()).endsWith("[Playlists]")) {
                return;
            }
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                setTotalFiles(new File(dir, children[i]));
            }
        } else if (dir.exists()) {
            totalFiles++;
        }
    }

    /**
	 *	Process files under dir (recursively)
	 */
    private void visitAllFiles(File dir, RefreshFiles files) {
        if (stop) {
            return;
        }
        if (dir.isDirectory()) {
            if ((dir.getName()).endsWith("[Incoming]") || (dir.getName()).endsWith("[Playlists]")) {
                return;
            }
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                visitAllFiles(new File(dir, children[i]), files);
            }
        } else if (!dir.exists()) {
            Object params[] = { dir.getAbsolutePath() };
            String msg = msgs.getMessage("SKIPPED_DOES_NOT_EXIST", params);
            System.out.println(msg);
        } else {
            files.update(dir);
        }
    }

    public int getPercentage() {
        int percentage = 0;
        if (done) {
            return 100;
        }
        if ((totalFiles > 0) && (files != null)) {
            percentage = (100 * files.getTotalFiles() / totalFiles);
        }
        return percentage;
    }

    public boolean getDone() {
        return done;
    }

    public void run() {
        File dir = new File(directory);
        Object params[] = { url };
        String msg = null;
        if (!dir.exists()) {
            Object params2[] = { directory };
            msg = msgs.getMessage("DOES_NOT_EXIST", params2);
            System.out.println(msg);
            return;
        }
        msg = msgs.getMessage("REFRESHING", params);
        System.out.println(msg);
        setTotalFiles(dir);
        if (stop) {
            msg = msgs.getMessage("REFRESH_CANCELLED", params);
            System.out.println(msg);
            return;
        }
        Object params3[] = { directory, totalFiles };
        msg = msgs.getMessage("SCANNING_DIRECTORY", params3);
        System.out.println(msg);
        Connection conn = connect(url);
        if (conn == null) {
            msg = msgs.getMessage("REFRESH_FAILED", params);
            System.out.println(msg);
            return;
        }
        RefreshFolders folders = new RefreshFolders(conn);
        if (stop) {
            msg = msgs.getMessage("REFRESH_CANCELLED", params);
            System.out.println(msg);
            disconnect(conn);
            return;
        }
        RefreshGenres genres = new RefreshGenres(conn);
        if (stop) {
            msg = msgs.getMessage("REFRESH_CANCELLED", params);
            System.out.println(msg);
            disconnect(conn);
            return;
        }
        files = new RefreshFiles(conn, directory, folders, genres);
        visitAllFiles(dir, files);
        if (stop) {
            msg = msgs.getMessage("REFRESH_CANCELLED", params);
            System.out.println(msg);
            disconnect(conn);
            return;
        }
        files.finalize();
        folders.finalize();
        genres.finalize();
        RefreshStats stats = new RefreshStats(conn);
        stats.update(directory);
        stats.finalize();
        disconnect(conn);
        done = true;
        msg = msgs.getMessage("REFRESHED_SUCCESSFULLY", params);
        System.out.println(msg);
    }

    public void stop() {
        stop = true;
    }
}
