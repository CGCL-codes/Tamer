package sourceforge.shinigami.io;

public class FileInexistentException extends RuntimeException {

    /** SERIAL MUMBO-JUMBO */
    private static final long serialVersionUID = -3863130709131322063L;

    public FileInexistentException() {
    }

    public FileInexistentException(String message) {
        super(message);
    }

    public FileInexistentException(Throwable t) {
        super(t);
    }

    public FileInexistentException(String message, Throwable t) {
        super(message, t);
    }
}
