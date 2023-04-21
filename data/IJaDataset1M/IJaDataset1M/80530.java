package jstella.j2me;

/**
 * Indicates a saved snapshot does not contain valid bytes
 * @author Williams
 */
public final class InvalidSnapshotException extends Exception {

    public InvalidSnapshotException() {
        super();
    }

    public InvalidSnapshotException(String message) {
        super(message);
    }
}
