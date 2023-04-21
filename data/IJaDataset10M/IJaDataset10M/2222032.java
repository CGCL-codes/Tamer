package ast.common.error;

/**
 * Class for a Backup error related to the filesystem like no write permission.
 *
 * @author bvollmer
 */
public class BackupIOError extends BackupError {

    /**
     * Creates an exception with a custom log message.
     *
     * @param aLogMessage Custom log message
     */
    public BackupIOError(final String aLogMessage) {
        super(aLogMessage);
    }

    /**
     * Creates an exception with a custom log message and original error.
     *
     * @param aLogMessage Custom log message
     * @param e Original exception
     */
    public BackupIOError(final String aLogMessage, final Throwable e) {
        super(aLogMessage, e);
    }

    /**
     * Creates an exception with the original error.
     *
     * @param e Original exception
     */
    public BackupIOError(final Throwable e) {
        super(e);
    }
}
