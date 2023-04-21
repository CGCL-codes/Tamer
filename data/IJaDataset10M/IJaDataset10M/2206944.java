package com.strangebreeds.shelf;

public class FileList {

    private int fileID;

    private String fileName;

    public FileList(int fileID, String fileName) {
        this.fileID = fileID;
        this.fileName = fileName;
    }

    public int getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }
}
