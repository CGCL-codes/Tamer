package net.sf.fc.io;

import java.io.File;

/**
 *
 * Exception that is thrown by {@link FileCopier} when a copy operation fails.
 * <p>
 * This exception contains the source file and the destination file for the copy
 * operation.
 *
 * @author David Armstrong
 */
@SuppressWarnings("serial")
public class FileCopyException extends RuntimeException {

    private final File source;

    private final File dest;

    /**
     * Constructor that takes a reason for the exception and the source file
     * and destination file for the copy operation.
     * @param message The reason for the copy failure
     * @param source The source file
     * @param dest The destination file
     */
    public FileCopyException(String message, File source, File dest) {
        super(message);
        this.source = new File(source.getPath());
        this.dest = new File(dest.getPath());
    }

    /**
     * Constructor that takes a reason for the exception, the cause,
     * and the source file and destination file for the copy operation.
     * @param message The reason for the copy failure
     * @param cause The cause for the exception
     * @param source The source file
     * @param dest The destination file
     */
    public FileCopyException(String message, Throwable cause, File source, File dest) {
        super(message, cause);
        this.source = source;
        this.dest = dest;
    }

    /**
     * Constructor that takes the causeand the source file and destination file
     * for the copy operation.
     * @param cause The cause for the exception
     * @param source The source file
     * @param dest The destination file
     */
    public FileCopyException(Throwable cause, File source, File dest) {
        super(cause);
        this.source = source;
        this.dest = dest;
    }

    /**
     * Constructor that takes a the source file and destination file for the copy
     * operation that failed.
     * @param source The source file
     * @param dest The destination file
     */
    public FileCopyException(File source, File dest) {
        super();
        this.source = source;
        this.dest = dest;
    }

    /**
     * Method that returns the source file for the copy operation that failed.
     * @return the source File object
     */
    public File getSource() {
        return new File(source.getPath());
    }

    /**
     * Method that returns the destination file for the copy operation that failed.
     * @return the destination File object
     */
    public File getDest() {
        return new File(dest.getPath());
    }
}
