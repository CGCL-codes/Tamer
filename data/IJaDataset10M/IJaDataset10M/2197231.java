package edu.upenn.law.updates;

import java.io.File;
import java.sql.SQLException;
import com.hojothum.common.DataCheck;
import com.hojothum.common.Debug;
import com.hojothum.common.Constants;
import edu.upenn.law.Database;
import edu.upenn.law.SourceFiles;

/**
 * @author  Eric Pancoast
 */
public class UpdateProcedures extends Object {

    /** Tells the Manual class to output all debug messages if true.
     * If false the class will only output error messages. */
    private static final boolean DEBUG = true;

    /** Tells the Manual class to output the date with all debug messages if true.
     * If false the class will not output dated debug messages. */
    private static final boolean DEBUG_DATE = false;

    /** Tells the Manual class what type of debug message redirect to use.
     * See Debug class. */
    private static final int DEBUG_TYPE = Debug.TYPE_SYSOUT;

    /** If a type of file redirect is used, this indicates what the output files
     * name should be. */
    private static final String DEBUG_FILENAME = "";

    /** Redirects debug output messages using the Debug class. */
    private static void dmsg(String message) {
        if (DEBUG) Debug.dmsg(message, DEBUG_TYPE, DEBUG_DATE, DEBUG_FILENAME);
    }

    /** Redirects debug error messages using the Debug class. */
    private static void derr(String message) {
        Debug.derr(message, DEBUG_TYPE, DEBUG_DATE, DEBUG_FILENAME);
    }

    public UpdateProcedures() {
        dmsg("Initializing " + this.getClass() + " Servlet...");
        init();
        dmsg("Initialization complete.");
    }

    public void init() {
        dmsg("Initializing All " + this.getClass() + " Servlet Parameters...");
        dmsg(this.getClass() + " Servlet Parameters Initialized.");
    }

    public void destroy() {
        dmsg("Destroying " + this.getClass() + "...");
        dmsg("Destroyed.");
    }

    public static boolean isWorkflowRootUser(String login, String pwd) {
        return isWorkflowRootUser(login, pwd, false);
    }

    public static boolean isWorkflowRootUser(String login, String pwd, boolean useSAC) {
        java.sql.PreparedStatement pst = null;
        java.sql.ResultSet rs = null;
        java.sql.Connection conn = null;
        boolean root = false;
        try {
            dmsg("isWorkflowRootUser: Checking root workflow user...");
            conn = (useSAC) ? Database.newConnection() : Database.connect(conn);
            pst = conn.prepareStatement("SELECT wp.root, u.password, u.id, u.firstname, u.lastname FROM users u, workflow_permissions wp WHERE wp.user_id = u.id AND u.login = ?");
            pst.setString(1, login);
            rs = pst.executeQuery();
            if (rs.next()) {
                dmsg("isWorkflowRootUser: User: " + login + " " + rs.getString(3) + " - " + rs.getString(4) + " " + rs.getString(5) + " trying to authenticate as root for update procedures.");
                if (DataCheck.matchPassword(pwd, rs.getBytes(2)) && rs.getInt(1) == 1) {
                    root = true;
                }
            }
            dmsg("isWorkflowRootUser: Workflow root access : " + ((root) ? "GRANTED" : "DENIED") + ".");
            Database.close(conn);
            return root;
        } catch (SQLException e) {
            e.printStackTrace();
            derr("---===[ isWorkflowRootUser: Root auth failed! ]===---");
            derr("SQLException: " + e.getMessage());
            derr("SQLState:     " + e.getSQLState());
            derr("VendorError:  " + e.getErrorCode());
            Database.close(conn);
            return root;
        }
    }

    public static int UpdateFileStoragePaths(String login, String pwd) {
        return UpdateFileStoragePaths(login, pwd, false);
    }

    public static int UpdateFileStoragePaths(String login, String pwd, boolean useSAC) {
        java.sql.PreparedStatement pst = null;
        java.sql.ResultSet rs = null;
        java.sql.Connection conn = null;
        try {
            if (isWorkflowRootUser(login, pwd, useSAC)) {
                conn = (useSAC) ? Database.newConnection() : Database.connect(conn);
                dmsg("UpdateFileStoragePaths: Getting workflow_history info...");
                pst = conn.prepareStatement("SELECT wal.workflow_id, wh.workarea_id, sf.source_file_info_id, wh.source_file_id, wh.id, wh.state_id, wh.version FROM workflow_history wh, source_file sf, workarea_lu wal WHERE wal.workarea_id = wh.workarea_id AND sf.id = wh.source_file_id AND (wh.date_checked_in is not null OR wh.last_ul_date is not null) ORDER BY wal.workflow_id, wh.workarea_id, wh.id");
                rs = pst.executeQuery();
                while (rs.next()) {
                    String from_directory = SourceFiles.ANNOTATION_FILE_PATH + "/workflow_" + rs.getInt(1) + "/workarea_" + rs.getInt(2) + "/sfi_" + rs.getInt(3) + "/sf_" + rs.getInt(4) + "/wh_" + rs.getInt(5);
                    String to_directory = SourceFiles.ANNOTATION_FILE_PATH + "/workflow_" + rs.getInt(1) + "/workarea_" + rs.getInt(2) + "/sfi_" + rs.getInt(3) + "/sf_" + rs.getInt(4) + "/wh_" + rs.getInt(5) + "/state_" + rs.getInt(6) + "/version_" + rs.getInt(7);
                    String source_file = "source_file_" + rs.getInt(4) + ".src";
                    File fd = new File(from_directory);
                    File td = new File(to_directory);
                    File files_in_fd[] = fd.listFiles();
                    File tsf = null;
                    if (files_in_fd == null) {
                        derr("*UPDATE PROC:: ERROR-" + fd.getAbsolutePath() + " is empty!");
                    } else {
                        for (int i = 0; i < files_in_fd.length; i++) {
                            dmsg("***UPDATE PROC:: Processing: " + files_in_fd[i].getAbsolutePath());
                            if ((files_in_fd[i].isDirectory() && files_in_fd[i].getAbsolutePath().substring(files_in_fd[i].getAbsolutePath().lastIndexOf(File.separator) + 1).startsWith("converted_")) || (files_in_fd[i].isFile() && files_in_fd[i].getAbsolutePath().substring(files_in_fd[i].getAbsolutePath().lastIndexOf(File.separator) + 1).startsWith("source_file"))) {
                                tsf = new File(td.getAbsolutePath() + File.separator + files_in_fd[i].getName());
                                dmsg("***UPDATE PROC:: " + files_in_fd[i].getName() + " exists = " + files_in_fd[i].exists() + "\n" + files_in_fd[i].getAbsolutePath() + " ->\n" + tsf.getAbsolutePath());
                                if (!td.exists()) {
                                    td.mkdirs();
                                }
                                if (!files_in_fd[i].renameTo(tsf)) {
                                    derr("*UPDATE PROC:: ERROR- RENAME FAILED!!");
                                    return Constants.ERROR_UNKNOWN;
                                }
                            }
                        }
                    }
                }
            }
            dmsg("UpdateFileStoragePaths: Complete.");
            Database.close(conn);
            return Constants.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            derr("---===[ UpdateFileStoragePaths: Update file locations failed! ]===---");
            derr("SQLException: " + e.getMessage());
            derr("SQLState:     " + e.getSQLState());
            derr("VendorError:  " + e.getErrorCode());
            Database.close(conn);
            return Constants.ERROR_DB;
        }
    }

    public static void main(String args[]) {
        if (args.length < 2) {
            String usage = "\nusage: UpdateProcedures login password [-u update_id]\n" + "   1 - Print user status (default)\n" + "   2 - Update File Storage Paths : to state_id based from original paths\n";
            System.out.println(usage);
        } else {
            String login = (args.length >= 1) ? args[0] : "anonymous";
            String pwd = (args.length >= 2) ? args[1] : "";
            int update_id = (args.length >= 4 && args[2].compareTo("-u") == 0) ? Integer.parseInt(args[3]) : 0;
            switch(update_id) {
                case 2:
                    UpdateProcedures.UpdateFileStoragePaths(login, pwd, true);
                    break;
                default:
                    UpdateProcedures.isWorkflowRootUser(login, pwd, true);
            }
        }
    }
}
