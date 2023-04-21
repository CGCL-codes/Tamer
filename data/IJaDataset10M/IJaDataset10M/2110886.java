package com.isfasiel.util.file;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadedFile {

    private String title;

    private CommonsMultipartFile fileData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CommonsMultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }
}
