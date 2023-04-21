package org.sysolar.fileupload.ex;

/**
 * 当文件类型（可以通过文件扩展名判断）不合法时抛出。
 */
public class FileExtendException extends FileUploadException {

    private static final long serialVersionUID = 4489759361471363452L;

    public FileExtendException() {
        this(null, null);
    }

    public FileExtendException(String msg, String fileName) {
        super(msg, fileName);
    }
}
