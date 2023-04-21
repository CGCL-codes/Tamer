package org.yehongyu.fckeditor.uploader;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.apache.commons.fileupload.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.yehongyu.websale.common.secure.CommonBean;
import org.yehongyu.websale.common.secure.UserSession;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Servlet to upload files.<br>
 *
 * This servlet accepts just file uploads, eventually with a parameter specifying file type
 *
 * @author Simone Chiaretta (simo@users.sourceforge.net)
 */
public class SimpleUploaderServlet extends HttpServlet {

    private static String baseDir;

    private static boolean debug = false;

    private static boolean enabled = false;

    private static Hashtable allowedExtensions;

    private static Hashtable deniedExtensions;

    /**
	 * Initialize the servlet.<br>
	 * Retrieve from the servlet configuration the "baseDir" which is the root of the file repository:<br>
	 * If not specified the value of "/UserFiles/" will be used.<br>
	 * Also it retrieve all allowed and denied extensions to be handled.
	 *
	 */
    public void init() throws ServletException {
        debug = (new Boolean(getInitParameter("debug"))).booleanValue();
        if (debug) System.out.println("\r\n---- SimpleUploaderServlet initialization started ----");
        baseDir = getInitParameter("baseDir");
        enabled = (new Boolean(getInitParameter("enabled"))).booleanValue();
        if (baseDir == null) baseDir = "/UserFiles/";
        String realBaseDir = getServletContext().getRealPath(baseDir);
        File baseFile = new File(realBaseDir);
        if (!baseFile.exists()) {
            baseFile.mkdir();
        }
        allowedExtensions = new Hashtable(3);
        deniedExtensions = new Hashtable(3);
        allowedExtensions.put("File", stringToArrayList(getInitParameter("AllowedExtensionsFile")));
        deniedExtensions.put("File", stringToArrayList(getInitParameter("DeniedExtensionsFile")));
        allowedExtensions.put("Image", stringToArrayList(getInitParameter("AllowedExtensionsImage")));
        deniedExtensions.put("Image", stringToArrayList(getInitParameter("DeniedExtensionsImage")));
        allowedExtensions.put("Flash", stringToArrayList(getInitParameter("AllowedExtensionsFlash")));
        deniedExtensions.put("Flash", stringToArrayList(getInitParameter("DeniedExtensionsFlash")));
        if (debug) System.out.println("---- SimpleUploaderServlet initialization completed ----\r\n");
    }

    /**
	 * Manage the Upload requests.<br>
	 *
	 * The servlet accepts commands sent in the following format:<br>
	 * simpleUploader?Type=ResourceType<br><br>
	 * It store the file (renaming it in case a file with the same name exists) and then return an HTML file
	 * with a javascript command in it.
	 *
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (debug) System.out.println("--- BEGIN DOPOST ---");
        response.setContentType("text/html; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();
        String retVal = "0";
        String newName = "";
        String fileUrl = "";
        String errorMessage = "";
        UserSession us = CommonBean.getUserSession(request);
        if (us == null) {
            errorMessage = "��δ��¼��û��Ȩ���ϴ��ļ���";
            retVal = "405";
        } else {
            String myDir = baseDir + us.getUserName() + "/";
            String realBaseDir = getServletContext().getRealPath(myDir);
            File baseFile = new File(realBaseDir);
            if (!baseFile.exists()) {
                baseFile.mkdir();
            }
            String typeStr = request.getParameter("Type");
            String currentPath = myDir + typeStr;
            String currentDirPath = getServletContext().getRealPath(currentPath);
            if (!new File(currentDirPath + "/").exists()) {
                new File(currentDirPath + "/").mkdir();
            }
            currentPath = request.getContextPath() + currentPath;
            if (debug) System.out.println(currentDirPath);
            if (enabled) {
                DiskFileUpload upload = new DiskFileUpload();
                try {
                    List items = upload.parseRequest(request);
                    Map fields = new HashMap();
                    Iterator iter = items.iterator();
                    while (iter.hasNext()) {
                        FileItem item = (FileItem) iter.next();
                        if (item.isFormField()) fields.put(item.getFieldName(), item.getString()); else fields.put(item.getFieldName(), item);
                    }
                    FileItem uplFile = (FileItem) fields.get("NewFile");
                    String fileNameLong = uplFile.getName();
                    fileNameLong = fileNameLong.replace('\\', '/');
                    String[] pathParts = fileNameLong.split("/");
                    String fileName = pathParts[pathParts.length - 1];
                    String nameWithoutExt = getNameWithoutExtension(fileName);
                    String ext = getExtension(fileName);
                    File pathToSave = new File(currentDirPath, fileName);
                    fileUrl = currentPath + "/" + fileName;
                    if (extIsAllowed(typeStr, ext)) {
                        int counter = 1;
                        while (pathToSave.exists()) {
                            newName = nameWithoutExt + "(" + counter + ")" + "." + ext;
                            fileUrl = currentPath + "/" + newName;
                            retVal = "201";
                            pathToSave = new File(currentDirPath, newName);
                            counter++;
                        }
                        uplFile.write(pathToSave);
                    } else {
                        retVal = "202";
                        errorMessage = "";
                        if (debug) System.out.println("Invalid file type: " + ext);
                    }
                } catch (Exception ex) {
                    if (debug) ex.printStackTrace();
                    retVal = "203";
                }
            } else {
                retVal = "1";
                errorMessage = "This file uploader is disabled. Please check the WEB-INF/web.xml file";
            }
        }
        out.println("<script type=\"text/javascript\">");
        out.println("window.parent.OnUploadCompleted(" + retVal + ",'" + fileUrl + "','" + newName + "','" + errorMessage + "');");
        out.println("</script>");
        out.flush();
        out.close();
        if (debug) System.out.println("--- END DOPOST ---");
    }

    private static String getNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
	 * Helper function to convert the configuration string to an ArrayList.
	 */
    private ArrayList stringToArrayList(String str) {
        if (debug) System.out.println(str);
        String[] strArr = str.split("\\|");
        ArrayList tmp = new ArrayList();
        if (str.length() > 0) {
            for (int i = 0; i < strArr.length; ++i) {
                if (debug) System.out.println(i + " - " + strArr[i]);
                tmp.add(strArr[i].toLowerCase());
            }
        }
        return tmp;
    }

    /**
	 * Helper function to verify if a file extension is allowed or not allowed.
	 */
    private boolean extIsAllowed(String fileType, String ext) {
        ext = ext.toLowerCase();
        ArrayList allowList = (ArrayList) allowedExtensions.get(fileType);
        ArrayList denyList = (ArrayList) deniedExtensions.get(fileType);
        if (allowList.size() == 0) if (denyList.contains(ext)) return false; else return true;
        if (denyList.size() == 0) if (allowList.contains(ext)) return true; else return false;
        return false;
    }
}
