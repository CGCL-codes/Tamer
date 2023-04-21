package threading;

/**
 * Used by utility classes called from a task's runLongTaks to report back to
 * the task that some progress has been made.
 * <p>
 * For example the xml parser may notify the FileImport task that another card
 * has been processed and the task can then notify the user about the progress.
 * @author jholy
 */
public interface IProgressNotificationCallback {

    /**
     * Notify that a (job-specific) step in the processing has finished.
     * Should not throw any exception.
     */
    void notifyStepFinished();
}
