package com.air.plugin.vo;

public class BasicFileVO {

    private String fileName;

    private long fileSize;

    private String rawFileUri;

    private String previewUri;

    private boolean hasPreview;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getRawFileUri() {
        return rawFileUri;
    }

    public void setRawFileUri(String rawFileUri) {
        this.rawFileUri = rawFileUri;
    }

    public String getPreviewUri() {
        return previewUri;
    }

    public void setPreviewUri(String previewUri) {
        this.previewUri = previewUri;
    }

    public boolean isHasPreview() {
        return hasPreview;
    }

    public void setHasPreview(boolean hasPreview) {
        this.hasPreview = hasPreview;
    }
}
