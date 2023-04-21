package org.apache.struts2.showcase.fileupload;

import java.io.File;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Show case File Upload example's action. <code>FileUploadAction</code>
 *
 */
public class FileUploadAction extends ActionSupport {

    private static final long serialVersionUID = 5156288255337069381L;

    private String contentType;

    private File upload;

    private String fileName;

    private String caption;

    public String getUploadFileName() {
        return fileName;
    }

    public void setUploadFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadContentType() {
        return contentType;
    }

    public void setUploadContentType(String contentType) {
        this.contentType = contentType;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String input() throws Exception {
        return SUCCESS;
    }

    public String upload() throws Exception {
        return SUCCESS;
    }
}
