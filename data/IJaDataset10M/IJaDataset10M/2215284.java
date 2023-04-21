package eu.medsea.filemanager.exceptions;

public class FileManagerException extends RuntimeException {

    private static final long serialVersionUID = 6115370909618328265L;

    public FileManagerException() {
        super();
    }

    public FileManagerException(String message) {
        super(message);
    }

    public FileManagerException(Throwable t) {
        super(t);
    }

    public FileManagerException(String message, Throwable t) {
        super(message, t);
    }
}
