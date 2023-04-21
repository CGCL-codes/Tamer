package org.jnf.application.services.upload;

import java.io.Serializable;

public class FileItemDefinition implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String fieldName;

    private String ieFileName;

    private String fileName;

    private String contentType;

    private Long sizeInBytes;

    private String errorDetail = "";

    private boolean error = false;

    private String path;

    private String action;

    private String fileId;

    public FileItemDefinition(String fieldName, String fileName, String contentType, Long sizeInBytes) {
        super();
        this.fieldName = fieldName;
        setFileName(fileName);
        this.contentType = contentType;
        this.sizeInBytes = sizeInBytes;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (fileName.lastIndexOf("\\") > -1) {
            this.fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            this.ieFileName = fileName;
        } else {
            this.fileName = fileName;
        }
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String theErrorDetail) {
        this.error = true;
        this.errorDetail = theErrorDetail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public boolean isError() {
        return error;
    }

    public String getIeFileName() {
        return ieFileName;
    }

    public void setIeFileName(String ieFileName) {
        this.ieFileName = ieFileName;
    }
}
