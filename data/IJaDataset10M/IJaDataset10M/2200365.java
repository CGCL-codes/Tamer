package com.centraview.mail;

import org.apache.struts.upload.FormFile;

/**
 * This class is used for getting file from Struts
 * to webserver
 */
public class FileForm extends org.apache.struts.action.ActionForm {

    private FormFile file;

    private String[] fileList = null;

    private String fileNameFromCent;

    public FormFile getFile() {
        return this.file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public String[] getFileList() {
        return this.fileList;
    }

    public void setFileList(String[] fileList) {
        this.fileList = fileList;
    }

    public String getFileNameFromCent() {
        return (this.fileNameFromCent);
    }

    public void setfileNameFromCent(String fileNameFromCent) {
        this.fileNameFromCent = fileNameFromCent;
    }
}
