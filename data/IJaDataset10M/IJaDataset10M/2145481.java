package sourceforge.shinigami.io;

public class FileExtensionUnsupportedException extends RuntimeException {

    /** SERIAL MUMBO-JUMBO */
    private static final long serialVersionUID = -4685008764008897410L;

    public FileExtensionUnsupportedException() {
        super();
    }

    public FileExtensionUnsupportedException(String msg) {
        super(msg);
    }

    public FileExtensionUnsupportedException(Throwable t) {
        super(t);
    }

    public FileExtensionUnsupportedException(String msg, Throwable t) {
        super(msg, t);
    }
}
