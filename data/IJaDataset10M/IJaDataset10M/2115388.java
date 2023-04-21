package com.sts.webmeet.upload.common;

import java.io.Serializable;
import java.util.Vector;

public class UploadInfo implements Serializable {

    private String name;

    private Integer scriptId;

    private Vector fileInfos = new Vector();

    public UploadInfo(String name, Integer scriptId) {
        this.name = name;
        this.scriptId = scriptId;
    }

    public void addFileInfo(FileInfo fileInfo) {
        fileInfos.add(fileInfo);
    }

    public FileInfo[] getFileInfos() {
        return (FileInfo[]) this.fileInfos.toArray(new FileInfo[0]);
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return super.toString() + "{ name: " + this.getName() + ", fileInfos: " + this.fileInfos.toString() + " }";
    }

    public Integer getScriptId() {
        return scriptId;
    }
}
