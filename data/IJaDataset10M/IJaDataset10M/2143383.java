package org.zend.webapi.test.server.response;

public class FileResponse extends ServerResponse {

    private String fileName;

    private long fileSize;

    private byte[] content;

    public FileResponse(int code, String fileName, long fileSize, byte[] content) {
        super(code);
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public byte[] getContent() {
        return content;
    }
}
