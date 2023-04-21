package com.extentech.luminet.jsp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.extentech.luminet.*;
import com.extentech.security.User;
import com.extentech.toolkit.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * A helper class to manage meme attachments.
 * 
    @author John McMahon -- Copyright &copy;2009 <a href = "http://www.extentech.com">Extentech Inc.</a>
    @version 4.1
    @since 1.3
 	@author Sam Hanes 2009-06-03 
 */
public class FileUploadWidget extends FileUploadBean {

    /** HttpSession object for this request */
    private HttpSession sess = null;

    /** Allowed upload mime-types */
    Map<String, String> alloweds = new Hashtable<String, String>();

    String caption = "";

    boolean debug = false;

    protected int DEBUGLEVEL = 0, MAXINPUTSIZE = 500000, MININPUTSIZE = 12000;

    protected String ERRPAGE = "/error.jsp?error=";

    /**
     * Default constructor.
     * 
     * @param session the HttpSession object for this request
     */
    public FileUploadWidget(HttpSession session) {
        this.sess = session;
        alloweds.put("pdf", "Adobe PDF");
        alloweds.put("txt", "Text File");
        alloweds.put("zip", "Compressed ZIP File");
        alloweds.put("gzip", "Compressed GZIP File");
        alloweds.put("tar", "Tar Archive");
        alloweds.put("js", "JavaScript File");
        alloweds.put("gif", "GIF Image");
        alloweds.put("eps", "Encapsulated Postscript");
        alloweds.put("jpg", "JPEG Image");
        alloweds.put("jpeg", "JPEG Image");
        alloweds.put("csv", "CSV Text File");
        alloweds.put("png", "PNG Image");
        alloweds.put("swf", "Flash Vide File");
        alloweds.put("psd", "Photoshop Source File");
        alloweds.put("fla", "Flash Video File");
        alloweds.put("swf", "Flash Video File");
        alloweds.put("ppt", "Powerpoint Presentation");
        alloweds.put("ppsx", "Powerpoint Slideshow");
        alloweds.put("xls", "Excel File");
        alloweds.put("plain", "Plain text File");
        alloweds.put("exml", "ExtenXLS XML File");
        alloweds.put("xlsx", "Excel 2007 File");
        alloweds.put("xlsm", "Excel 2007 Macro-Enabled File");
        alloweds.put("doc", "Word Document");
        alloweds.put("docx", "Word 2007 Document");
        alloweds.put("ppt", "Powerpoint Document");
        alloweds.put("pptx", "Powerpoint 2007 Document");
        alloweds.put("wma", "WMA Media File");
        alloweds.put("wmv", "WMA Media File");
        alloweds.put("mp3", "MP3 Sound File");
        alloweds.put("mv4", "Mv4 Video File");
        alloweds.put("mp4", "MP4 Video File");
        alloweds.put("wav", "Wave Sound File");
        alloweds.put("bmp", "Bitmap Image");
        alloweds.put("mpeg", "MPEG Video");
        alloweds.put("mpg", "MPEG Video");
        alloweds.put("ogg", "OGG Sound File");
        alloweds.put("qt", "Quicktime Video");
        alloweds.put("mov", "Quicktime Video");
        alloweds.put("quicktime", "Quicktime Video");
        alloweds.put("qt", "Quicktime Video");
        alloweds.put("html", "HTML File");
        alloweds.put("xml", "XML File");
    }

    /** Save an uploaded file.
     * 
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public boolean saveFile(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (this.checkAccess()) {
            Serve serve = ((ServeConnection) req).getServe();
            HttpSession sesh = req.getSession();
            Logger.logInfo("FileUploadWidget.saveFile Called...");
            try {
                byte[] barray = super.doUpload(req);
                if (barray.length > MAXINPUTSIZE) {
                    res.sendError(500, "Upload File size: " + barray.length + " exceeds Maximum upload file size of:" + MAXINPUTSIZE);
                    return false;
                }
                if (barray.length < MININPUTSIZE) {
                    res.sendError(500, "Upload File size: " + barray.length + " smaller than Minimum upload file size of:" + MININPUTSIZE);
                    return false;
                }
                String outFileName = this.directory + filename;
                if (DEBUGLEVEL > 0) Logger.logInfo("FileUploadWidget about to Save File: " + filename + " SUCCESS!");
                if (DEBUGLEVEL > 0) Logger.logInfo("FileUploadWidget Streaming the bytes to file: " + outFileName);
                FileOutputStream fos = new FileOutputStream(new File(outFileName));
                fos.write(barray);
                fos.flush();
                fos.close();
                Logger.logInfo("FileUploadWidget Saved File: " + filename + " SUCCESS!");
            } catch (Exception e) {
                ((ServeConnection) res).sendError(500, "FileUploadWidget Exception: " + e.toString(), (Throwable) e);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Moves the given file to its canonical location.
     * 
     * If this method returns true you must assume the file no longer exists
     * in its original location.
     * 
     * @param serve the Serve instance this is running under
     * @param file the file to move
     * @param path the web path of the file
     * 
     * @return whether the move succeeded
     */
    public boolean saveFile(Serve serve, File file, String path) {
        try {
            com.extentech.toolkit.JFileWriter.copyFile(file, path);
            file.delete();
        } catch (IOException ex) {
            Logger.logErr("FileUploadWidget.saveFile: Couldn't move file '" + file.getPath() + "' to '" + path + "'.");
            return false;
        }
        return true;
    }

    /** delete the file associated with this record
     *  only files in the upload directory are eligible for deletion
     * 
     *  an attempt to delete a file not contained in the upload directory will fail
     * 
     * @return whether the file deletion succeeded
     */
    public boolean deleteFile(String fn) {
        if (!checkAccess()) return false;
        String drx = this.getDirectory();
        if (drx != null) if (fn.indexOf(drx) < 0) return false;
        File fnx = new File(fn);
        if (!fnx.exists()) {
            return false;
        }
        try {
            fnx.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 
     * Deletes a file from the upload directory.
     * 
     * @param serve the Serve instance this is running under
     * @param fname the web path to the target file
     * 
     * @return whether the file deletion succeeded
     */
    public boolean deleteFile(Serve serve, String fname) {
        if (!checkAccess()) return false;
        String baseDir = serve.getRealPath("/");
        File file = new File(baseDir + fname);
        if (!file.exists()) {
            return false;
        }
        try {
            file.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * utility method to handle file uploads, deletes and the corresponding
     * meme records
     * 
     * If target_id is null, will return the temporary upload file instead of
     * moving it to its canonical media location.
     * 
     * @param target_id ?
     * @param user the User making this request
     * @param serve the Serve instance this is running under
     * @param target_table ?
     * @param connMySheets a JDBC Connection
     * @param request the HttpServletRequest object for this request
     * @param response the HttpServletResponse object for this request
     * 
     * @return
     * 
     * @throws Exception
     */
    public File handleFileRequest(String target_id, User user, Serve serve, String target_table, Connection connMySheets, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File fout = null;
        filename = "UploadedFile";
        String site_root_dir = serve.getRealPath("/");
        if (directory == null) directory = site_root_dir + "media/" + user.getId() + "/";
        if (target_id != null && !target_id.equals("null")) {
            directory += target_id + "/";
        }
        if (request.getMethod().equals("POST")) {
            try {
                try {
                    File tempdir = new File(directory);
                    if (!tempdir.exists()) tempdir.mkdirs();
                    fout = File.createTempFile("upload", ".tmp", tempdir);
                } catch (Exception e) {
                    Logger.logErr("FileUploadWidget Exception saving file: " + e.toString());
                }
                this.setMININPUTSIZE(10);
                this.setMAXINPUTSIZE(150000000);
                if (!this.doUploadToFile(fout, request).exists()) {
                    response.sendError(500, "Upload File Failed: Problem uploading file.");
                    return null;
                }
                filename = this.getFileName();
                String mt = filename.substring(filename.lastIndexOf(".") + 1);
                mt = mt.toLowerCase();
                if (mt.equals("jpg")) {
                    mt = "jpeg";
                    int pos = filename.lastIndexOf(".");
                    filename = filename.substring(0, pos) + "." + mt;
                }
                if (alloweds.get(mt) == null) {
                    response.sendError(500, "Upload File Failed: Disallowed file type:" + mt);
                    fout.delete();
                    return null;
                }
                if (serve.mimemap.get(mt) != null) mt = serve.mimemap.get(mt).toString();
                String webname = directory + filename;
                if (!this.saveFile(serve, fout, webname)) {
                    if (!new File(webname).exists()) {
                        response.sendError(500, "Upload File Failed: could not move file" + " to canonical location:" + webname);
                        fout.delete();
                        return null;
                    } else {
                        Logger.logWarn("FileUploadWidget.handleFileRequest() could not overwrite existing file with same name: " + webname);
                    }
                }
                fout = new File(webname);
                webname = "/media/" + user.getId() + "/" + target_id + "/" + filename;
                if (target_id != null) try {
                    caption = this.getField("caption");
                    if (caption == null) caption = "Imported File";
                    final PreparedStatement stmt = connMySheets.prepareStatement("INSERT INTO kb_medialink_idx (table_name,meme_id," + "table_id,link_description,resource_URL,mime_type) " + "VALUES (?,?,?,?,?,?)");
                    stmt.setString(1, target_table);
                    stmt.setInt(2, Integer.parseInt(target_id));
                    stmt.setInt(3, Integer.parseInt(target_id));
                    stmt.setString(4, caption);
                    stmt.setString(5, webname);
                    stmt.setString(6, mt);
                    stmt.executeUpdate();
                } catch (Exception ex) {
                    response.sendError(500, "Upload File Failed: Problem inserting file details in database:" + ex.toString());
                    this.deleteFile(serve, webname);
                    return null;
                }
            } catch (Throwable tx) {
                response.sendError(500, "Upload File Failed:" + tx.toString());
            }
        } else if (request.getParameter("delete_rec") != null) {
            int mdi = Integer.parseInt(request.getParameter("delete_rec"));
            String fnx;
            final PreparedStatement stmtFetch = connMySheets.prepareStatement("SELECT * FROM kb_medialink_idx WHERE id=?");
            stmtFetch.setInt(1, mdi);
            ResultSet Recordset0 = stmtFetch.executeQuery();
            if (Recordset0.next()) {
                fnx = Recordset0.getString("resource_URL");
            } else {
                response.sendError(500, "Delete File Failed: resource URL not found in database");
                return null;
            }
            final PreparedStatement stmt = connMySheets.prepareStatement("DELETE FROM kb_medialink_idx WHERE id=?");
            stmt.setInt(1, mdi);
            stmt.executeUpdate();
            if (!this.deleteFile(serve, fnx)) {
                Logger.logWarn("FileUploadWidget.Delete File Failed: file not found in correct media directory.");
                return null;
            }
        }
        return fout;
    }

    public boolean checkAccess() {
        if (sess == null) return false;
        return true;
    }

    /**
     * Gets version information for this class. 
     */
    public String getVersion() {
        return "2.0";
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getDEBUGLEVEL() {
        return DEBUGLEVEL;
    }

    public void setDEBUGLEVEL(int debuglevel) {
        DEBUGLEVEL = debuglevel;
    }

    public String getERRPAGE() {
        return ERRPAGE;
    }

    public void setERRPAGE(String errpage) {
        ERRPAGE = errpage;
    }

    public int getMAXINPUTSIZE() {
        return MAXINPUTSIZE;
    }

    public void setMAXINPUTSIZE(int maxinputsize) {
        MAXINPUTSIZE = maxinputsize;
    }

    public int getMININPUTSIZE() {
        return MININPUTSIZE;
    }

    public void setMININPUTSIZE(int mininputsize) {
        MININPUTSIZE = mininputsize;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String d) {
        this.directory = d;
    }
}
