package org.openi.project;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author paullucas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileUploadServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(FileUploadServlet.class);

    /**
     *
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("beging file upload servlet");
        List fileItems = null;
        try {
            DiskFileUpload fu = new DiskFileUpload();
            fu.setSizeMax(1000000);
            fu.setSizeThreshold(4096);
            fu.setRepositoryPath(System.getProperty("java.io.tmpdir"));
            fileItems = fu.parseRequest(request);
        } catch (FileUploadException e) {
            logger.error(e);
        }
        FileItem uploadedFile = null;
        String targetDirectory = null;
        logger.debug("found " + fileItems.size() + " items in request");
        Iterator items = fileItems.iterator();
        while (items.hasNext()) {
            FileItem fileItem = (FileItem) items.next();
            if ("uploadFile".equals(fileItem.getFieldName())) {
                uploadedFile = fileItem;
            } else if ("targetDirectory".equals(fileItem.getFieldName())) {
                targetDirectory = fileItem.getString();
            }
        }
        try {
            String fileName = parseFilename(uploadedFile.getName());
            logger.debug("fileName: " + fileName);
            logger.debug("saving uploadFile to directory: " + targetDirectory);
            logger.debug("size: " + uploadedFile.getSize());
            File newFile = new File(targetDirectory + "/" + fileName);
            logger.info("saving file: " + newFile.getCanonicalPath());
            uploadedFile.write(newFile);
        } catch (Exception e) {
            logger.error(e);
        }
        try {
            response.sendRedirect("splash.jsp");
        } catch (java.io.IOException e) {
            logger.error(e);
        }
    }

    /**
     * needed to support IE problems - when uploading a file in IE,
     * the entire filename including full canonical path is used.
     */
    private String parseFilename(String inputName) {
        String cleanFilename = inputName;
        int idx = cleanFilename.lastIndexOf('/');
        if ((idx >= 0) && (idx < cleanFilename.length())) {
            cleanFilename = cleanFilename.substring(idx + 1);
        }
        idx = cleanFilename.lastIndexOf('\\');
        if ((idx >= 0) && (idx < cleanFilename.length())) {
            cleanFilename = cleanFilename.substring(idx + 1);
        }
        return cleanFilename;
    }
}
