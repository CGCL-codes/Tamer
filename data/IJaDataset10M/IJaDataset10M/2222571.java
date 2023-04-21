package com.entelience.objects.probes;

public class FileProbeHistory extends ProbeHistory implements java.io.Serializable {

    public FileProbeHistory() {
    }

    private String fileName;

    private String fileSize;

    private Long countElements;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Long getCountElements() {
        return countElements;
    }

    public void setCountElements(Long countElements) {
        this.countElements = countElements;
    }
}
