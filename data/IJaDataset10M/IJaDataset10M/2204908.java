package com.vlee.servlet.ecommerce;

import java.io.File;

class UploadedFile {

    private String filename;

    private String type;

    UploadedFile(String filename, String type) {
        this.filename = filename;
        this.type = type;
    }

    public String getContentType() {
        return type;
    }

    public String getFilesystemName() {
        return filename;
    }

    public File getFile() {
        if (filename == null) {
            return null;
        } else {
            return new File(filename);
        }
    }
}
