package org.apache.jetspeed.modules.actions.portlets.designer;

import java.io.File;
import org.apache.turbine.util.upload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * File Uploader helper
 * 
 * @author <a href="mailto:jlim@gluecode.com">Jonas Lim</a>
 * @version $Id: FileUploader.java,v 1.1 2004/03/10 22:53:59 taylor Exp $
 */
public class FileUploader {

    private static Log log = LogFactory.getLog(FileUploader.class);

    public boolean upload(FileItem fileItem, String location, String fileTypes[]) {
        boolean hasUpload = false;
        try {
            File file = new File(fileItem.getFileName());
            String filename = file.getName();
            String contentType = fileItem.getContentType();
            int index = filename.lastIndexOf("\\");
            int index2 = filename.lastIndexOf("//");
            if (index > 0) {
                filename = filename.substring(index + 1);
            }
            if (index2 > 0) {
                filename = filename.substring(index2 + 1);
            }
            fileItem.write(location + filename);
            hasUpload = true;
        } catch (Exception e) {
            log.info("error in FileUploader class");
            hasUpload = false;
            log.error(e);
        }
        return hasUpload;
    }

    public String getFilename(FileItem fileItem, String location, String fileTypes[]) {
        String filename = "no result";
        try {
            File file = new File(fileItem.getFileName());
            filename = fileItem.getName();
            int index = filename.lastIndexOf("\\");
            int index2 = filename.lastIndexOf("//");
            if (index > 0) {
                filename = filename.substring(index + 1);
            }
            if (index2 > 0) {
                filename = filename.substring(index2 + 1);
            }
            filename = location + filename;
        } catch (Exception e) {
            log.error(e);
        }
        return filename;
    }
}
