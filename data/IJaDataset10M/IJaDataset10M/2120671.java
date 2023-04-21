package cn.sduo.app.sftp.exception;

public class SFTPIOException extends SFTPException {

    private static final long serialVersionUID = 1L;

    public SFTPIOException(String code, String message, Throwable root) {
        super(code, message, root);
    }

    public SFTPIOException(String code, String message) {
        super(code, message);
    }
}
