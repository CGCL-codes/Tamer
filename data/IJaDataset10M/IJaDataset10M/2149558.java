package de.tuc.in.sse.weit.export.ootrans.main;

public interface TransformObserver {

    /**
     * Returns true if the user hits the Cancel button in the progress dialog.
     * 
     * @return true iff the user wants to cancel the operation
     */
    boolean isCancelled();

    /**
     * Sets the note that explains what the export is currently doing
     * 
     * @param note
     *            the note to be shown to the user
     */
    void setNote(String note);

    /**
     * Indicate the progress of the operation being monitored.
     * 
     * @param nv
     *            progress indicator
     */
    void setProgress(int nv);

    /**
     * Convenience method to inform about progress and provide a note
     * 
     * @param nv
     *            progress indicator
     * @param note
     *            progress note
     */
    void setProgressNote(int nv, String note);

    /**
     * Indicates that the progress of the operation cannot be shown
     */
    void setIndeterminate();

    /**
     * Indicates that the progress of the operation can be tracked and will end
     * at the given maximum
     * 
     * @param maximum
     *            indicates the end of the operation
     */
    void setDeterminate(int maximum);
}
