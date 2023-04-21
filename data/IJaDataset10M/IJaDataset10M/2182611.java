package edu.upenn.law;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import com.hojothum.common.DataCheck;
import com.hojothum.common.Debug;
import com.hojothum.common.Debugger;
import com.hojothum.common.StopWatch;
import edu.upenn.law.exception.CheckinException;
import edu.upenn.law.exception.CheckoutException;
import edu.upenn.law.exception.GetCheckoutInfoException;
import edu.upenn.law.io.AnnotationFile;
import edu.upenn.law.io.AnnotationFileList;
import edu.upenn.law.io.exception.AnnotationFileCheckinException;
import edu.upenn.law.io.exception.BadAnnotationFileException;
import edu.upenn.law.io.exception.BadConvertedAnnotationFileException;
import edu.upenn.law.io.exception.InvalidAnnotationFileTypeException;
import edu.upenn.law.io.exception.MissingValidationClassException;
import com.hojothum.common.Constants;

/**
 * <code>Checkio</code> handles the information and processes associated with checking out and checking in files
 * using the checkio.jsp interface in LAW.ear/LAW.war/checkio/checkio.jsp.
 * 
 * @author Eric Pancoast
 */
public class Checkio implements Debugger {

    /** Redirects debug output messages using the Debug class. */
    public void dmsg(String message) {
        Debug.dmsg(this.getClass().getName() + ": " + message);
    }

    /** Redirects debug error messages using the Debug class. */
    public void derr(String message) {
        Debug.derr(this.getClass().getName() + ": " + message);
    }

    /**
     * Where the checked in files will be uploaded temporarily, until the system
     * extracts the archive and registers the files in the database. At which
     * point this archive will be deleted.
     */
    public static final String TEMP_PATH = edu.upenn.law.Constants.ROOT_PATH + "/.tmp/ci";

    /**
     * Class-wide prepared statement. 
     */
    private java.sql.PreparedStatement pst;

    /**
     * Primary class-wide result set.
     */
    private java.sql.ResultSet rs;

    /**
     * Secondary class-wide result set.
     */
    private java.sql.ResultSet rs2;

    /**
     * Class-wide connection.
     */
    private java.sql.Connection conn;

    /**
     * <code>Login</code> instance sent from the JSP to identify the user.
     */
    private Login login;

    /**
     * The information of checked out files for this user sent back to the JSP by @see #initCheckioFiles(int)
     */
    private CheckoutInfo coinfo;

    /**
     * A list of workareas which this user can checkout files from.  This is restricted to the workareas which have files available for this user.
     */
    private Vector workareas;

    /**
     * Class-wide <code>DataCheck</code> instance for manipulating, validating, processing data in common ways.
     */
    private com.hojothum.common.DataCheck dc = new com.hojothum.common.DataCheck();

    /**
     * This prevents more than one user from simultaneously checking out files from the same area.  There was an unlikely, but detrimental, occurrence of two users having the same set of files checked out to them.  This caused one annotator's work on the files to be wasted. 
     */
    private static HashMap workareaCheckoutLock = new HashMap();

    /**
     * Reference to the <code>CheckinProcessor</code> so that user requests to checkin can be handed off to it.
     */
    private static CheckinProcessor cp = new CheckinProcessor();

    public Checkio() {
        dmsg("Initializing " + this.getClass() + " Servlet...");
        init();
        dmsg("Initialization complete.");
    }

    public void init() {
        dmsg("Initializing All " + this.getClass() + " Servlet Parameters...");
        pst = null;
        rs = null;
        rs2 = null;
        conn = null;
        coinfo = new CheckoutInfo();
        workareas = new Vector();
        dmsg(this.getClass() + " Servlet Parameters Initialized.");
    }

    public void destroy() {
        dmsg("Destroying " + this.getClass() + "...");
        dmsg("--Releasing DB Connection...");
        Database.close(conn);
        dmsg("--Connection Released.");
        dmsg("Destroyed.");
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public boolean hasFilesToCheckout() {
        return (workareas.size() > 0);
    }

    public int getNumWorkareas() {
        return workareas.size();
    }

    public String getWorkareaID(int i) {
        return ((String[]) workareas.get(i))[0];
    }

    public String getWorkareaName(int i) {
        return ((String[]) workareas.get(i))[1];
    }

    public String getWorkareaWorkflowName(int i) {
        return ((String[]) workareas.get(i))[2];
    }

    public CheckoutInfo initCheckioFiles(int user_id) throws GetCheckoutInfoException {
        pst = null;
        rs = null;
        rs2 = null;
        coinfo = new CheckoutInfo();
        try {
            StopWatch sw = new StopWatch();
            sw.start();
            dmsg("***Timing initCheckioFiles:");
            conn = Database.connect(conn);
            dmsg("initCheckioFiles: Getting file info...");
            pst = conn.prepareStatement("SELECT wh.ID, wl.workflow_id, wh.workarea_id, sf.filename, wh.source_file_id, wh.state_id, wh.last_ul_date, wh.last_dl_date, wh.date_checked_out FROM workflow_history wh, source_file sf, workarea_lu wl WHERE wh.workarea_id = wl.workarea_id AND wh.source_file_id = sf.id AND wh.user_id = ? AND (wh.state_id = " + AnnotationFile.STATE_CHECKED_OUT + " OR wh.state_id = " + AnnotationFile.STATE_INVALID + " OR wh.state_id = " + AnnotationFile.STATE_SAVED_PROGRESS + ") ORDER BY wl.workflow_id, wl.ordernum, wh.date_checked_out");
            pst.setInt(1, user_id);
            rs = pst.executeQuery();
            dmsg("***Time after query: " + sw.getCurrentElapsedTimeReadable());
            while (rs.next()) {
                int status = CheckinProcessor.getStatus(rs.getInt(1));
                Date last_ul_date = rs.getDate(7);
                Date last_dl_date = rs.getDate(8);
                Date date_checked_out = rs.getDate(9);
                coinfo.addFile(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getInt(5), ((status != -1) ? status : rs.getInt(6)), (last_ul_date == null) ? -1 : last_ul_date.getTime(), (last_dl_date == null) ? -1 : last_dl_date.getTime(), (date_checked_out == null) ? -1 : date_checked_out.getTime());
            }
            sw.stop();
            dmsg("***Time to get checkio file info: " + sw.getCurrentElapsedTimeReadable());
            dmsg("initCheckioFiles: Complete. (User " + user_id + " has " + coinfo.getNumFilesCO() + " files checked out)");
            Database.close(conn);
            return coinfo;
        } catch (SQLException e) {
            e.printStackTrace();
            derr("---===[ initCheckioFiles: File selection failed! ]===---");
            derr("SQLException: " + e.getMessage());
            derr("SQLState:     " + e.getSQLState());
            derr("VendorError:  " + e.getErrorCode());
            Database.close(conn);
            throw new GetCheckoutInfoException("Database Error");
        }
    }

    public int initCheckioWorkareas(int user_id) {
        pst = null;
        rs = null;
        rs2 = null;
        try {
            StopWatch sw = new StopWatch();
            sw.start();
            conn = Database.connect(conn);
            dmsg("initCheckioWorkareas: Getting workarea info...");
            pst = conn.prepareStatement("SELECT wa.id, wa.name, w.name, wl.ordernum, wa.pass_2 FROM workarea wa, annotator_lu al, workflow w, workarea_lu wl WHERE w.id = wl.workflow_id AND wa.id = wl.workarea_id AND wl.workarea_id = al.workarea_id AND al.user_id = ? ORDER BY w.name, wl.ordernum");
            pst.setInt(1, user_id);
            rs = pst.executeQuery();
            dmsg("***Time after query for workareas user(" + user_id + ") is a member: " + sw.getCurrentElapsedTimeReadable());
            int workarea_id = -1;
            while (rs.next()) {
                workarea_id = rs.getInt(1);
                pst = conn.prepareStatement("SELECT COUNT(wh.id) FROM workflow_history wh, workarea wa WHERE wh.user_id IS NULL AND date_checked_out is null AND (wa.pass_2 = '0' OR (wa.pass_2 = '1' AND ? != (SELECT wh2.user_id FROM workflow_history wh2, prev_wh_lu p WHERE wh2.id = p.prev_wh_id AND p.wh_id = wh.id)) ) AND wh.workarea_id = wa.id and wa.id = ? LIMIT 1");
                pst.setInt(1, user_id);
                pst.setInt(2, workarea_id);
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    dmsg("  ***Time after query for available files in wa(" + workarea_id + "): " + sw.getCurrentElapsedTimeReadable());
                    if (rs2.getInt(1) > 0) {
                        String tmp[] = { rs.getString(1), rs.getString(2), rs.getString(3) };
                        workareas.add(tmp);
                    }
                } else {
                    derr("No query result for " + user_id + ") workarea: " + workarea_id);
                }
            }
            sw.stop();
            dmsg("***Time after result processing: " + sw.getCurrentElapsedTimeReadable());
            dmsg("initCheckioWorkareas: Complete. (" + workareas.size() + " workareas available)");
            Database.close(conn);
            return Constants.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            derr("---===[ initCheckioFiles: Get workarea info failed! ]===---");
            derr("SQLException: " + e.getMessage());
            derr("SQLState:     " + e.getSQLState());
            derr("VendorError:  " + e.getErrorCode());
            Database.close(conn);
            return Constants.ERROR_DB;
        }
    }

    public AnnotationFileList coFile(int user_id, int workarea_id) throws CheckoutException {
        return coFile(user_id, workarea_id, 1);
    }

    private synchronized void lockWorkarea(int workarea_id) {
        while (workareaCheckoutLock.containsKey(workarea_id + "")) {
            try {
                dmsg("Waiting 1 second for unlock...");
                wait(Constants.ONE_SECOND);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                derr("INTERRUPTED IN LOCK: lockWorkarea(" + workarea_id + ")");
            }
        }
        workareaCheckoutLock.put(workarea_id + "", "");
    }

    public synchronized void unlockWorkarea(int workarea_id) {
        workareaCheckoutLock.remove(workarea_id + "");
    }

    public AnnotationFileList coFile(int user_id, int workarea_id, int numFiles) throws CheckoutException {
        AnnotationFileList afList = new AnnotationFileList();
        StopWatch sw = new StopWatch();
        StopWatch sw2 = new StopWatch();
        sw2.start();
        pst = null;
        rs = null;
        rs2 = null;
        try {
            conn = Database.connect(conn);
            dmsg("coFile: Retrieving number of files user " + user_id + " has checked out and workarea information for workarea " + workarea_id + "...");
            pst = conn.prepareStatement("SELECT COUNT(wh.id), (SELECT co_limit FROM workarea WHERE id = ?), (SELECT disabled FROM users WHERE id = ?) FROM workflow_history wh WHERE wh.workarea_id = ? AND wh.user_id = ? AND wh.state_id = " + AnnotationFile.STATE_CHECKED_OUT);
            pst.setInt(1, workarea_id);
            pst.setInt(2, user_id);
            pst.setInt(3, workarea_id);
            pst.setInt(4, user_id);
            dmsg("***TIMING QUERY:");
            sw.start();
            rs = pst.executeQuery();
            sw.stop();
            dmsg("***GET COUNT TO CHECK MAX FILE CO COMPLETED IN: " + sw.getCurrentElapsedTimeReadable());
            if (!rs.next()) {
                dmsg("coFile: (*) User or Workarea doesn't exist?");
                Database.close(conn);
                throw new CheckoutException("Unknown user or workarea.");
            }
            if (rs.getInt(3) == 1) {
                dmsg("coFile: (*) User disabled, discontinuing checkout.");
                Database.close(conn);
                throw new CheckoutException("You're account has been disabled.");
            }
            if (rs.getInt(1) >= rs.getInt(2)) {
                dmsg("coFile: (*) User has maximum number of files checked out. co:" + rs.getInt(1) + " max:" + rs.getInt(2));
                Database.close(conn);
                throw new CheckoutException("You have checked out the maximum number of files allowed.");
            }
            int max = rs.getInt(2) - rs.getInt(1);
            dmsg("coFile: User " + user_id + " has " + rs.getInt(1) + " files from workarea " + workarea_id + " : " + max + " more allowed : " + numFiles + " requested.");
            if (max < numFiles) {
                numFiles = max;
            }
            dmsg("Grabing source file info for file to checkout...");
            pst = conn.prepareStatement("SELECT sf.source_file_info_id, sf.id, u.disabled, wh.ID, wal.ordernum, p.format_id, f.ext, wal.workflow_id, wa.id, p.id, wh.version, wh.state_id, wa.circular_active FROM workflow_history wh, workarea wa, workarea_lu wal, annotator_lu al, users u, source_file sf, process p, formats f WHERE u.id = al.user_id AND p.id = wh.process_id AND f.id = p.format_id AND wal.workarea_id = wh.workarea_id AND sf.id = wh.source_file_id AND wh.workarea_id = wa.id AND wh.workarea_id = al.workarea_id AND wa.id = ? AND al.user_id = ? AND wh.user_id IS NULL AND date_checked_out IS NULL AND date_checked_in IS NULL AND ((wa.pass_2 = '1' AND ? != (SELECT user_id FROM workflow_history WHERE id = prev_wh_lu.prev_wh_id AND prev_wh_lu.wh_id = wh.id)) OR pass_2 = '0') ORDER BY wa.name, wh.id LIMIT ?");
            pst.setInt(1, workarea_id);
            pst.setInt(2, user_id);
            pst.setInt(3, user_id);
            pst.setInt(4, numFiles);
            dmsg("***TIMING QUERY:");
            sw.start();
            lockWorkarea(workarea_id);
            rs = pst.executeQuery();
            unlockWorkarea(workarea_id);
            sw.stop();
            dmsg("***GET FILE INFO FOR CHECKOUT OF " + numFiles + " FILES COMPLETED IN: " + sw.getCurrentElapsedTimeReadable());
            AnnotationFile af = null;
            while (rs.next()) {
                try {
                    af = new AnnotationFile(AnnotationFile.ACTION_CHECKOUT, rs.getInt(1), rs.getInt(2), rs.getInt(4), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(6), rs.getString(7), rs.getInt(11), rs.getInt(12), rs.getInt(13) == 1);
                } catch (InvalidAnnotationFileTypeException ite) {
                    ite.printStackTrace();
                    derr("coFile: " + ite.getMessage());
                } catch (BadAnnotationFileException bafe) {
                    bafe.printStackTrace();
                    derr("coFile: " + bafe.getMessage());
                } catch (MissingValidationClassException mvce) {
                    mvce.printStackTrace();
                    derr("coFile: " + mvce.getMessage());
                } catch (BadConvertedAnnotationFileException bcafe) {
                    bcafe.printStackTrace();
                    derr("coFile: " + bcafe.getMessage());
                }
                dmsg("coFile: Marking file " + af.getSourceFileZipName() + " as checked out and searching for previous annotation file...");
                pst = conn.prepareStatement("UPDATE workflow_history SET date_checked_out = ?, last_dl_date = ?, user_id = ?, state_id = " + AnnotationFile.STATE_CHECKED_OUT + " WHERE ID = ? AND date_checked_out IS NULL; SELECT pwl.prev_wh_id, wal.workflow_id, wh.workarea_id, p.format_id, f.ext, (SELECT converter_class FROM converters WHERE initial_file_format_id = f.id AND final_file_format_id = ?), wh.version FROM prev_wh_lu pwl, workarea_lu wal, workflow_history wh, process p, formats f WHERE p.id = wh.process_id AND f.id = p.format_id AND wal.workarea_id = wh.workarea_id AND wh.id = pwl.prev_wh_id AND pwl.wh_id = ?");
                pst.setTimestamp(1, new java.sql.Timestamp((new Date()).getTime()));
                pst.setTimestamp(2, new java.sql.Timestamp((new Date()).getTime()));
                pst.setInt(3, user_id);
                pst.setInt(4, af.getWorkflowHistoryID());
                pst.setInt(5, af.getFileFormatID());
                pst.setInt(6, af.getWorkflowHistoryID());
                dmsg("***TIMING QUERY:");
                sw.start();
                rs2 = pst.executeQuery();
                sw.stop();
                dmsg("***UPDATE FILE STATE & GET HISTORY COMPLETED IN: " + sw.getCurrentElapsedTimeReadable());
                dmsg("coFile: File " + af.getSourceFileZipName() + " marked for checkout by user " + user_id + ", saving file location...");
                if (rs2.next()) {
                    try {
                        af.setHistory(rs2.getInt(2), rs2.getInt(3), rs2.getInt(1), rs2.getInt(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7));
                    } catch (BadAnnotationFileException bafe) {
                        bafe.printStackTrace();
                        derr("coFile: Set Annotation File History FAILED! : " + bafe.getMessage());
                    }
                }
                afList.add(af);
            }
            if (afList.size() <= 0) {
                dmsg("coFile: (*) No files available for checkout.");
                Database.close(conn);
                throw new CheckoutException("No files available for checkout.");
            }
            sw2.stop();
            dmsg("***FULL CHECKOUT PROCEDURE COMPLETED IN: " + sw2.getCurrentElapsedTimeReadable());
            dmsg("coFile: (*) File Checkout Info Retrieval Complete. " + afList.size() + " Files Checked Out.");
            Database.close(conn);
            return afList;
        } catch (SQLException e) {
            unlockWorkarea(workarea_id);
            e.printStackTrace();
            derr("---===[ coFile: File checkout failed! ]===---");
            derr("SQLException: " + e.getMessage());
            derr("SQLState:     " + e.getSQLState());
            derr("VendorError:  " + e.getErrorCode());
            Database.close(conn);
            throw new CheckoutException("Database Error.");
        }
    }

    public AnnotationFileList getCheckedOutFiles(int user_id, int workarea_id, boolean updateDownload) {
        return (getCheckedOutFiles(user_id, workarea_id, "-1", updateDownload));
    }

    public AnnotationFileList getCheckedOutFiles(int user_id, int workarea_id, String wh_ids, boolean updateDownload) {
        AnnotationFileList afList = new AnnotationFileList();
        AnnotationFile annotationFile = null;
        StopWatch sw = new StopWatch(), sw2 = new StopWatch();
        HashMap validationSchemes = new HashMap();
        dmsg("***TIMING PROCESS:");
        sw2.start();
        String final_ext = "EXT";
        String spec_wh_ids = "";
        if (wh_ids.compareTo("-1") != 0) {
            spec_wh_ids = " AND wh.id IN (" + wh_ids + ") ";
        }
        pst = null;
        rs = null;
        rs2 = null;
        try {
            conn = Database.connect(conn);
            dmsg("getCheckedOutFiles: Grabing source file info...");
            pst = conn.prepareStatement("SELECT sf.source_file_info_id, sf.id, u.password, u.disabled, wal.ordernum, wh.id, wal.workflow_id, wh.last_ul_date, " + "p.format_id, f.ext, wh.workarea_id, p.id, wa.validate, wh.state_id, wa.validation_blocking, wh.version, wa.save_validation_results, " + "wa.circular_active, wa.validation_class FROM users u, workflow_history wh, workarea_lu wal, source_file sf, process p, workarea wa, formats f " + "WHERE wa.id = wh.workarea_id AND p.id = wh.process_id AND f.id = p.format_id AND u.id = wh.user_id and wal.workarea_id = wh.workarea_id and " + "sf.id = wh.source_file_id and (wh.workarea_id = ? OR -1 = ?) and wh.user_id = ? AND wh.state_id = " + AnnotationFile.STATE_CHECKED_OUT + spec_wh_ids + " ORDER BY wh.date_checked_out DESC ");
            pst.setInt(1, workarea_id);
            pst.setInt(2, workarea_id);
            pst.setInt(3, user_id);
            dmsg("***TIMING QUERY:");
            sw.start();
            rs = pst.executeQuery();
            sw.stop();
            dmsg("***RETRIEVED SOURCE FILE INFO IN: " + sw.getCurrentElapsedTimeReadable());
            while (rs.next()) {
                if (rs.getInt(4) == 1) {
                    dmsg("getCheckedOutFiles: User disabled... Get files canceled.");
                    Database.close(conn);
                    return null;
                } else {
                    annotationFile = null;
                    try {
                        annotationFile = new AnnotationFile(AnnotationFile.ACTION_CHECKIN, rs.getInt(1), rs.getInt(2), rs.getInt(6), rs.getInt(7), rs.getInt(11), rs.getInt(12), rs.getInt(9), rs.getString(10), rs.getInt(16), rs.getInt(14), rs.getInt(18) == 1);
                    } catch (InvalidAnnotationFileTypeException ite) {
                        ite.printStackTrace();
                        derr("getCheckedOutFiles: " + ite.getMessage());
                    } catch (BadAnnotationFileException bafe) {
                        bafe.printStackTrace();
                        derr("getCheckedOutFiles: " + bafe.getMessage());
                    } catch (MissingValidationClassException mvce) {
                        mvce.printStackTrace();
                        derr("getCheckedOutFiles: " + mvce.getMessage());
                    } catch (BadConvertedAnnotationFileException bcafe) {
                        bcafe.printStackTrace();
                        derr("getCheckedOutFiles: " + bcafe.getMessage());
                    }
                    if (annotationFile != null) {
                        if (rs.getInt(13) == 1 && rs.getString(19) != null && rs.getString(19).compareTo("") != 0) {
                            String validation_class = rs.getString(19);
                            try {
                                annotationFile.setValidation(true, validation_class, rs.getInt(15) == 1, rs.getInt(17) == 1);
                            } catch (MissingValidationClassException mve) {
                                mve.printStackTrace();
                                derr(mve.getMessage());
                            }
                        }
                        dmsg("getCheckedOutFiles: Getting info for file:" + annotationFile.getAnnotationFileZipName() + "...");
                        if (updateDownload) {
                            pst = conn.prepareStatement("UPDATE workflow_history SET last_dl_date = ? WHERE ID = ?; SELECT pwl.prev_wh_id, wal.workflow_id, wh.workarea_id, p.format_id, f.ext, (SELECT converter_class FROM converters WHERE initial_file_format_id = p.format_id AND final_file_format_id = ?), wh.version FROM prev_wh_lu pwl, workarea_lu wal, workflow_history wh, process p, formats f WHERE p.id = wh.process_id AND f.id = p.format_id AND wal.workarea_id = wh.workarea_id AND wh.id = pwl.prev_wh_id AND pwl.wh_id = ?");
                            pst.setTimestamp(1, new java.sql.Timestamp((new Date()).getTime()));
                            pst.setInt(2, annotationFile.getWorkflowHistoryID());
                            pst.setInt(3, annotationFile.getFileFormatID());
                            pst.setInt(4, annotationFile.getWorkflowHistoryID());
                            dmsg("***TIMING QUERY:");
                            sw.start();
                            rs2 = pst.executeQuery();
                            sw.stop();
                            dmsg("***RETRIEVED HISTORY INFO IN: " + sw.getCurrentElapsedTimeReadable());
                            dmsg("getCheckedOutFiles: Source file download date updated for source_file_id:" + annotationFile.getAnnotationFileZipName() + "...");
                            if (rs2.next()) {
                                dmsg("getCheckedOutFiles: Saving file location for source_file_id:" + annotationFile.getAnnotationFileZipName() + "...");
                                try {
                                    annotationFile.setHistory(rs2.getInt(2), rs2.getInt(3), rs2.getInt(1), rs2.getInt(4), rs2.getString(5), rs2.getString(6), rs2.getInt(7));
                                } catch (BadAnnotationFileException bafe) {
                                    bafe.printStackTrace();
                                    derr("getCheckedOutFiles: Set Annotation File History FAILED! : " + bafe.getMessage());
                                }
                            }
                            afList.add(annotationFile);
                            dmsg("getCheckedOutFiles: Added Annotation File: " + annotationFile + " to return list.");
                        } else {
                            afList.add(annotationFile);
                        }
                    }
                }
            }
            if (afList.size() < 1) {
                dmsg("getCheckedOutFiles: No files found.");
                Database.close(conn);
                return null;
            }
            Database.close(conn);
            sw2.stop();
            dmsg("***GET CHECKED OUT FILES COMPLETED IN: " + sw2.getCurrentElapsedTimeReadable());
            dmsg("getCheckedOutFiles: Found " + afList.size() + " Files. Returning info..");
            return afList;
        } catch (SQLException e) {
            e.printStackTrace();
            derr("---===[ getCheckedOutFiles: File d/l failed! ]===---");
            derr("SQLException: " + e.getMessage());
            derr("SQLState:     " + e.getSQLState());
            derr("VendorError:  " + e.getErrorCode());
            Database.close(conn);
            return null;
        }
    }

    public CheckinInfo checkin(HttpServletRequest request) throws CheckinException {
        StopWatch sw = new StopWatch();
        boolean force_circular = false;
        boolean ignore_validation_warnings = false;
        CheckinInfo cinfo = new CheckinInfo();
        java.io.File tempDir = new java.io.File(TEMP_PATH);
        tempDir.mkdirs();
        FileUpload upload = new FileUpload();
        upload.setSizeMax(edu.upenn.law.SourceFiles.MAX_UPLOAD_SIZE);
        upload.setSizeThreshold(edu.upenn.law.SourceFiles.MAX_MEMORY_SIZE);
        upload.setRepositoryPath(TEMP_PATH);
        boolean uploading = false;
        int checkin_type = AnnotationFile.ACTION_SAVE_CHECKIN;
        String bug_message = "-- Unspecified Bug --";
        int user_id = -1;
        try {
            java.util.List items = upload.parseRequest(request);
            java.util.Iterator iter = items.iterator();
            dmsg("checkin: Checking action field...");
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField() && item.getFieldName().compareTo("user_id") == 0) {
                    user_id = Integer.parseInt(item.getString());
                }
                if (item.isFormField() && item.getFieldName().compareTo("force_circular") == 0) {
                    if (item.getString().toLowerCase().compareTo("true") == 0) {
                        force_circular = true;
                    }
                }
                if (item.isFormField() && item.getFieldName().compareTo("bug_message") == 0) {
                    bug_message = DataCheck.filterForDBHTML(item.getString());
                }
                if (item.isFormField() && item.getFieldName().compareTo("action") == 0) {
                    if (item.getString().compareTo("checkin") == 0) {
                        dmsg("checkin: action 'checkin' found.");
                        uploading = true;
                        checkin_type = AnnotationFile.ACTION_CHECKIN;
                    } else if (item.getString().compareTo("save_file") == 0) {
                        dmsg("checkin: action 'save_file' found.");
                        uploading = true;
                        checkin_type = AnnotationFile.ACTION_SAVE_CHECKIN;
                    } else if (item.getString().compareTo("bug_file") == 0) {
                        dmsg("checkin: action 'bug_file' found.");
                        uploading = true;
                        checkin_type = AnnotationFile.ACTION_PROBLEM_CHECKIN;
                    }
                }
            }
            if (uploading && user_id != -1) {
                iter = items.iterator();
                int actionType = -1;
                dmsg("checkin: Checking for binary content in form post...");
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (!item.isFormField() && item.getFieldName().compareTo("zipfile") == 0) {
                        dmsg("checkin: File name: (" + item.getName() + " : " + item.getContentType() + ")");
                        if (item.getContentType() != null) {
                            if (item.getContentType().compareTo("application/x-compressed") == 0 || item.getContentType().compareTo("application/x-zip-compressed") == 0 || item.getContentType().compareTo("application/zip") == 0 || item.getContentType().compareTo("multipart/x-zip") == 0) {
                                dmsg("checkin: Zip file found.  Extracting...");
                                File zip = new File(TEMP_PATH + "/" + (new Date()).getTime() + "_" + user_id + ".zip");
                                ZipFile z = null;
                                File f = new File(TEMP_PATH);
                                if (!f.isDirectory()) {
                                    f.mkdirs();
                                }
                                try {
                                    item.write(zip.getAbsolutePath());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    z = new ZipFile(zip.getAbsolutePath());
                                } catch (java.io.IOException ioe) {
                                    dmsg("checkin: " + ioe.getMessage());
                                    ioe.printStackTrace();
                                    throw new CheckinException("Checkin Failed, the zip file: " + zip.getAbsolutePath() + " could not be found.");
                                }
                                Enumeration entries = z.entries();
                                AnnotationFileList checked_out_files = getCheckedOutFiles(user_id, -1, false);
                                if (checked_out_files == null || checked_out_files.size() == 0) {
                                    dmsg("checkin: (*) No files to check in!!! How did you get here?");
                                    cinfo.setStatus(CheckinInfo.FAILED);
                                    cinfo.setMessage("No files checked in.<br>You have no files to checkin to this workarea.");
                                    return (cinfo);
                                }
                                boolean accepted_for_processing;
                                while (entries.hasMoreElements()) {
                                    ZipEntry e = (ZipEntry) entries.nextElement();
                                    String name = (e).getName();
                                    cinfo.uploadedFile(name);
                                    if (name.indexOf("source_file_") > -1 && name.indexOf(".src.") > ("source_file_".length())) {
                                        accepted_for_processing = false;
                                        for (int i = 0; i < checked_out_files.size(); i++) {
                                            AnnotationFile af = checked_out_files.getAnnotationFile(i);
                                            String expectedID = af.getAnnotationFileZipName();
                                            if (expectedID.compareTo(name) == 0) {
                                                try {
                                                    af.setUserID(user_id);
                                                    if (bug_message != null) {
                                                        af.setProblemMessage(bug_message);
                                                    }
                                                    af.setCheckinType(checkin_type);
                                                    af.setForcingCircularCheckin(force_circular);
                                                    af.setIgnoringValidationWarnings(ignore_validation_warnings);
                                                    actionType = CheckinProcess.save(z, e, af);
                                                    cinfo.acceptedFile(name);
                                                    accepted_for_processing = true;
                                                    CheckinProcessor.addToQueue(af);
                                                    checked_out_files.remove(i);
                                                } catch (AnnotationFileCheckinException afcie) {
                                                    afcie.printStackTrace();
                                                    derr("checkin: Writing file failed. Ignoring file: " + name + " :" + afcie.getMessage());
                                                } catch (InvalidAnnotationFileTypeException iafte) {
                                                    iafte.printStackTrace();
                                                    derr("checkin: Writing file failed. Ignoring file: " + name + " :" + iafte.getMessage());
                                                }
                                            }
                                        }
                                        if (!accepted_for_processing) {
                                            cinfo.ignoredAnnotationFile(name);
                                        }
                                    } else {
                                        cinfo.ignoredFile(name);
                                    }
                                }
                                zip.delete();
                                dmsg("checkin: (*) File DB & write complete. " + cinfo.getNumUploadedFiles() + " Uploaded.  " + cinfo.getNumAcceptedFiles() + " Files Accepted For Checkin.  " + cinfo.getNumIgnoredAnnotationFiles() + " Annotation Files Ignored.");
                                cinfo.setStatus(CheckinInfo.SUCCEEDED);
                                cinfo.setZipFound(true);
                                cinfo.setMessage("Checkin Complete");
                            } else if (item.getContentType().compareTo("text/xml") == 0) {
                                dmsg("checkin: (*) Zip file not found. (XML found)");
                                cinfo.setStatus(CheckinInfo.FAILED);
                                cinfo.setZipFound(false);
                                cinfo.setMessage("No files checked in.<br>Please upload a zip file.");
                            } else if (item.getContentType().compareTo("text/plain") == 0) {
                                dmsg("checkin: (*) Zip file not found. (Text found)");
                                cinfo.setStatus(CheckinInfo.FAILED);
                                cinfo.setZipFound(false);
                                cinfo.setMessage("No files checked in.<br>Please upload a zip file.");
                            } else {
                                dmsg("checkin: (*) Zip file not found.");
                                cinfo.setStatus(CheckinInfo.FAILED);
                                cinfo.setZipFound(false);
                                cinfo.setMessage("No files checked in.<br>Please upload a zip file.");
                            }
                        } else {
                            dmsg("checkin: (*) Content Type was null!!!");
                            cinfo.setStatus(CheckinInfo.FAILED);
                            cinfo.setZipFound(false);
                            cinfo.setMessage("No files checked in.<br>Please upload a zip file.");
                        }
                    }
                }
            }
        } catch (org.apache.commons.fileupload.FileUploadException fe) {
            fe.printStackTrace();
        }
        dmsg("checkin: (*) File Checkin Complete.");
        Database.close(conn);
        return (cinfo);
    }
}
