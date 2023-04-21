package org.cofax.module;

import org.cofax.*;
import org.cofax.cms.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 *  CofaxToolsExtUpload
 *  This module allows to upload files on the server
 * 
 *  @author - Badr Chentouf
 *    
 */
public class CofaxToolsExtUpload extends CofaxToolsExtModule {

    /**
	* navigate throught the different modes for polls
	*
	**/
    public String navigate(DataStore db, CofaxPage page, HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        String mode = (String) req.getParameter("mode");
        String contentType = (String) req.getHeader("content-type");
        if (contentType != null) {
            if (contentType.indexOf("multipart/form-data") > -1) {
                mode = (String) req.getAttribute("mode");
            }
        }
        HashMap ht = new HashMap();
        CofaxToolsUser user = (CofaxToolsUser) (session.getAttribute("user"));
        String workingPubName = (String) user.workingPubName;
        String newFilePath = CofaxToolsServlet.staticFolder;
        if (!(newFilePath.endsWith(File.separator))) newFilePath += File.separator;
        newFilePath += user.workingPubName + File.separator;
        newFilePath += "images" + File.separator;
        newFilePath += (String) user.userInfoHash.get("USERNAME") + File.separator;
        page.putGlossaryValue("system:message", getI18NMessage("message_welcome"));
        page.putGlossaryValue("system:highLightTab", "admin");
        page.putGlossaryValue("request:pubName", workingPubName);
        if (mode.equals("imageupload_edit_imageupload")) {
            CofaxToolsNavigation.includeResource(page, "/toolstemplates/module/editImageupload.jsp", req, res, session);
            return "";
        }
        if (mode.equals("imageupload_upload_imageupload")) {
            String errorMessage = "";
            boolean uploaded = false;
            String uploadfile = (String) req.getAttribute("uploadfile");
            if (uploadfile == null) uploadfile = "";
            if (uploadfile.length() >= 5) {
                String fileName = "";
                if (uploadfile.lastIndexOf("/") > -1) {
                    fileName = uploadfile.substring(uploadfile.lastIndexOf("/") + 1);
                }
                if (uploadfile.lastIndexOf("\\") > -1) {
                    fileName = uploadfile.substring(uploadfile.lastIndexOf("\\") + 1);
                }
                fileName = CofaxToolsUtil.replace(fileName, " ", "_");
                fileName = CofaxToolsUtil.replace(fileName, "�", "e");
                fileName = CofaxToolsUtil.replace(fileName, "�", "e");
                fileName = CofaxToolsUtil.replace(fileName, "�", "a");
                fileName = CofaxToolsUtil.replace(fileName, "�", "u");
                fileName = CofaxToolsUtil.replace(fileName, "�", "i");
                fileName = CofaxToolsUtil.replace(fileName, "�", "u");
                fileName = CofaxToolsUtil.replace(fileName, "'", "_");
                File createPath = new File(newFilePath);
                if (!createPath.exists()) {
                    CofaxToolsUtil.log("CofaxToolsExtUpload: creating directory " + newFilePath);
                    boolean createdDir = false;
                    try {
                        createdDir = createPath.mkdirs();
                    } catch (Exception e) {
                        CofaxToolsUtil.log("CofaxToolsExtUpload creating " + newFilePath + "threw an exception: " + e);
                        errorMessage += "<br>ERROR creating " + newFilePath + "threw an exception: " + e;
                    }
                    if (!createdDir) {
                        CofaxToolsUtil.log("CofaxToolsExtUpload ERROR creating dir:" + newFilePath);
                        errorMessage += "<br>ERROR creating dir:" + newFilePath;
                    }
                }
                String orgFilePath = CofaxToolsServlet.fileTransferFolder;
                if (!(orgFilePath.endsWith(File.separator))) orgFilePath += File.separator;
                orgFilePath += CofaxToolsFTP.stripPath(uploadfile);
                File orgFile = new File(orgFilePath);
                File newFile = new File(newFilePath + File.separator + fileName);
                if (newFile.exists()) {
                    newFile.delete();
                }
                try {
                    uploaded = orgFile.renameTo(newFile);
                } catch (Exception e) {
                    CofaxToolsUtil.log("CofaxToolsExtUpload: failed remaning fileName." + e.toString());
                    errorMessage += "<br>ERROR failed remaning fileName." + e.toString();
                }
                page.putGlossaryValue("system:errorMessage", errorMessage);
                page.putGlossaryValue("system:uploaded", uploaded + "");
            }
            CofaxToolsNavigation.includeResource(page, "/toolstemplates/module/editImageupload_step2.jsp", req, res, session);
            return "";
        }
        if (mode.equals("imageupload_view_imageupload")) {
            page.putGlossaryValue("request:localImportDir", newFilePath);
            page.putGlossaryValue("request:userName", (String) user.userInfoHash.get("USERNAME"));
            CofaxToolsNavigation.includeResource(page, "/toolstemplates/module/editImageupload_step3.jsp", req, res, session);
            return "";
        }
        if (mode.equals("imageupload_delete_imageupload")) {
            File localDir = new File(newFilePath);
            String[] localFileList = localDir.list();
            if ((localFileList != null) && (localFileList.length != 0)) {
                for (int i = 0; i < localFileList.length; i++) {
                    String filename = localFileList[i];
                    String delete = (String) req.getParameter("" + i);
                    if ((delete != null) && (delete.equals("on"))) {
                        File newFile = new File(newFilePath + File.separator + filename);
                        if (newFile.exists()) {
                            newFile.delete();
                        }
                    }
                }
            }
            page.putGlossaryValue("request:localImportDir", newFilePath);
            page.putGlossaryValue("request:userName", (String) user.userInfoHash.get("USERNAME"));
            CofaxToolsNavigation.includeResource(page, "/toolstemplates/module/editImageupload_step3.jsp", req, res, session);
            return "";
        }
        return "";
    }

    public String formatString(String input) {
        String retVal = CofaxToolsUtil.replace(input, "'", "''");
        retVal = CofaxToolsUtil.replace(retVal, "\\", "\\\\");
        return retVal;
    }

    public String getI18NMessage(String message) {
        ResourceBundle messages;
        String returnMessage = "";
        Locale lcl = CofaxToolsServlet.lcl;
        try {
            messages = ResourceBundle.getBundle("org.cofax.module.upload", lcl);
            returnMessage = messages.getString(message);
        } catch (Exception e) {
            CofaxToolsUtil.log("CofaxToolsExtUpload : getI18NMessage : error while reading " + message);
        }
        return (returnMessage);
    }
}
