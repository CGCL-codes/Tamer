package com.rstr.resume.action;

import java.io.File;
import java.io.FileOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.upload.FormFile;

public class ResumeAttachFunction {

    public java.util.Vector resumeAttach(String newFileName, HttpServletRequest req, FormFile fileName, String mailId) {
        String sWebSite = "http://www.bonsaiinc.net/";
        String sFolder = "ResumeFiles/";
        String folderPath = "/home/virtual/bonsaiinc.net/var/www/html/";
        String str = fileName.getFileName();
        String sAttached_link = "";
        String sUploadLink = "";
        String contextPath = sWebSite;
        folderPath = folderPath + sFolder + mailId;
        String filePath = contextPath + sFolder + mailId;
        try {
            File uploadedFolder = new File(folderPath);
            if (!uploadedFolder.exists()) {
                uploadedFolder.mkdir();
            }
            sAttached_link = filePath + "/" + str;
            sUploadLink = folderPath + "/" + newFileName;
            File uploadFile = new File(sUploadLink);
            if (!uploadFile.exists()) {
                FileOutputStream fos = new FileOutputStream(uploadFile);
                fos.write(fileName.getFileData());
                fos.flush();
                fos.close();
            } else if (uploadFile.delete()) {
                FileOutputStream fos = new FileOutputStream(uploadFile);
                fos.write(fileName.getFileData());
                fos.flush();
                fos.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (sAttached_link.length() != 0) {
            int sLast = sAttached_link.lastIndexOf("/");
            sAttached_link = sAttached_link.replace(sAttached_link.substring((sLast + 1), sAttached_link.length()), newFileName);
        }
        if (sUploadLink.length() != 0) {
            int sLast = sUploadLink.lastIndexOf("/");
            sUploadLink = sUploadLink.replace(sUploadLink.substring((sLast + 1), sUploadLink.length()), newFileName);
        }
        java.util.Vector vec = new java.util.Vector();
        vec.add(sAttached_link);
        vec.add(sUploadLink);
        return vec;
    }
}
