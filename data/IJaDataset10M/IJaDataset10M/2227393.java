package com.rooster.action.ready_for_submission;

import javax.print.DocPrintJob;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.form.boarding.DocumentForm;
import com.rooster.utils.CurrentDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
import java.io.File;

public class UploadFormattedResume extends Action {

    private static Logger logger = Logger.getLogger(UploadFormattedResume.class.getName());

    DataSource db;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        boolean bSession = true;
        String sFinalStr = "No Data Found";
        String sResult = "";
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            bSession = false;
            sFinalStr = "TIMEOUT";
        }
        if (bSession) {
            db = getDataSource(req);
            Vector vFiles = new Vector();
            Hashtable hOptions = new Hashtable();
            try {
                if (ServletFileUpload.isMultipartContent(req)) {
                    ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
                    List fileItemsList = servletFileUpload.parseRequest(req);
                    String sDescription = "";
                    FileItem fileItem = null;
                    int i = 0;
                    Iterator it = fileItemsList.iterator();
                    String fileNametoTest = "";
                    while (it.hasNext()) {
                        fileItem = (FileItem) it.next();
                        if (fileItem.isFormField()) {
                            hOptions.put(fileItem.getFieldName(), fileItem.getString());
                        } else {
                            if (fileItem.getSize() > 0) {
                                String fileName = fileItem.getName();
                                String fieldName = fileItem.getFieldName();
                                String sAppFolder = String.valueOf(session.getAttribute("APPLICATION_ROOT_PATH"));
                                String sTempFolder = String.valueOf(session.getAttribute("TEMPORARY_FOLDER"));
                                createDir(sAppFolder, sTempFolder);
                                String sTime = CurrentDate.getDateTimeInMillis();
                                String sCache = sTempFolder + "/u_" + sTime;
                                createDir(sAppFolder, sCache);
                                sCache = sAppFolder + sCache;
                                File saveToFile = new File(sCache + "/" + fileName);
                                logger.info("Cache file path :" + saveToFile.getAbsolutePath());
                                try {
                                    fileItem.write(saveToFile);
                                } catch (Exception e) {
                                }
                                vFiles.add(saveToFile);
                            }
                        }
                    }
                    System.out.println(vFiles.size() + " " + hOptions.size());
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            try {
                String sAppFolder = String.valueOf(session.getAttribute("APPLICATION_ROOT_PATH"));
                String sResumeFolderPath = String.valueOf(session.getAttribute("DOCUMENTS_FOLDER"));
                String sFormattedResumeFolder = String.valueOf(session.getAttribute("FORMATTED_FOLDER"));
                String sCandidateEmail = String.valueOf(hOptions.get("email_id"));
                String sFirstName = String.valueOf(hOptions.get("conFname"));
                String sLastName = String.valueOf(hOptions.get("conLname"));
                String sType = String.valueOf(hOptions.get("selected_type"));
                Hashtable descTable = new Hashtable();
                if (sType.equals(new String("FORMATTED_RESUME_UPLOAD"))) {
                    createDir(sAppFolder, sResumeFolderPath + sCandidateEmail + "/");
                    String sCandiFormattedFolder = sResumeFolderPath + sCandidateEmail + "/" + sFormattedResumeFolder + "/";
                    createDir(sAppFolder, sCandiFormattedFolder);
                    for (int i = 0; i < vFiles.size(); i++) {
                        File fUploaded = (File) vFiles.get(i);
                        String sFileName = sFirstName + "_" + sLastName + ".doc";
                        String sFilePath = sCandiFormattedFolder + sFileName;
                        writeFile(sAppFolder, sFilePath, fUploaded);
                        descTable.put(sFilePath, i);
                    }
                    sResult = updateTable(req, sCandidateEmail, descTable);
                }
                if ((sResult == null) || (sResult.equals(new String(""))) || (sResult.equals(new String("OK")))) {
                    sFinalStr = getTableData(sCandidateEmail);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        try {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.write(sFinalStr);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    private void createDir(String sAppFolder, String dirName) {
        String sFolderPath = sAppFolder + dirName;
        File f = new File(sFolderPath);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    private void writeFile(String sAppFolder, String sFilePath, File uploadedFile) {
        try {
            sFilePath = sAppFolder + sFilePath;
            File fNewFile = new File(sFilePath);
            copy(uploadedFile, fNewFile);
            try {
                String sCacheFilePath = String.valueOf(uploadedFile);
                deletefile(sCacheFilePath);
                String sCacheFolder = "";
                if (sCacheFilePath.indexOf(new String("/")) > -1) {
                    sCacheFolder = sCacheFilePath.substring(0, sCacheFilePath.lastIndexOf("/"));
                } else {
                    sCacheFolder = sCacheFilePath.substring(0, sCacheFilePath.lastIndexOf("\\"));
                }
                deletefile(sCacheFolder);
            } catch (StringIndexOutOfBoundsException sioobe) {
                logger.debug(sioobe);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        int bufferSize = 4 * 1024;
        byte[] buf = new byte[bufferSize];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private String getTableData(String sCandidateEmail) {
        Connection myCon = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector docInfo = new Vector();
        String docPath = new String();
        try {
            String sSql = "select formatted_resume_link from rooster_candidate_info where email_id='" + sCandidateEmail + "' ;";
            myCon = db.getConnection();
            stmt = myCon.createStatement();
            rs = stmt.executeQuery(sSql);
            while (rs.next()) {
                DocumentForm df = new DocumentForm();
                docPath = rs.getString("formatted_resume_link");
                df.setDoc_path(docPath);
                docInfo.add(df);
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
            }
        }
        String sFinalStr = getHTML(docInfo, docPath);
        return sFinalStr;
    }

    private String getHTML(Vector dataInfo, String docPath) {
        String sFinalStr = "";
        try {
            if (dataInfo.size() == 0) {
                return "No Data Found";
            }
            for (int i = 0; i < dataInfo.size(); i++) {
                DocumentForm data7 = (DocumentForm) dataInfo.get(i);
                sFinalStr += "<a id='formatted_resume_uploaded' style='cursor:pointer' onclick='openLinkInNewWindow(\"" + docPath + "\")' >" + "FORMATTED RESUME : " + docPath.substring(docPath.lastIndexOf("/") + 1) + "</a>";
            }
        } catch (Exception e) {
            System.err.println(e);
            return "No Data Found";
        }
        return sFinalStr;
    }

    private String updateTable(HttpServletRequest req, String sCandidateEmail, Hashtable descTable) {
        HttpSession session = req.getSession(false);
        String sEmailId = String.valueOf(session.getAttribute("UserId"));
        Connection myCon = null;
        try {
            myCon = db.getConnection();
            PreparedStatement insertStmt = myCon.prepareStatement("update rooster_candidate_info set formatted_resume_link = ?, formatted_actual_path = ? where email_id = ?");
            Enumeration enums = descTable.keys();
            while (enums.hasMoreElements()) {
                String sKey = String.valueOf(enums.nextElement());
                String sDocPath = sKey;
                insertStmt.setString(1, sDocPath);
                insertStmt.setString(2, sDocPath);
                insertStmt.setString(3, sCandidateEmail);
                insertStmt.addBatch();
                insertStmt.executeBatch();
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
            }
        }
        return "OK";
    }

    private static void deletefile(String file) {
        File f1 = new File(file);
        boolean success = f1.delete();
    }
}
