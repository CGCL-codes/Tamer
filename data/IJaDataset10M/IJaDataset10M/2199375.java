package net.sourceforge.liftoff.installer;

import java.io.IOException;

public interface InstallMonitor {

    public final int ERR_MKDIR = 0;

    public final int ERR_COPY = 1;

    public void showCopyOp(String from, String to);

    public void showPercentage(int percent);

    public void sync();

    /**
     * This will be called before the installation starts
     * to tell the monitor how many bytes will be copied
     * for all targets.
     */
    public void installTotal(long bytes);

    /**
     * This will be called from the copy loop to
     * tell the monitor how many bytes have been copied
     * to the system since the last call.
     */
    public void addInstalledBytes(long bytes);

    /**
     * this will be called for each new target.
     */
    public void showInstallOp(String from, String to);

    /**
     * this method will be called when the installation
     * process is finished.
     */
    public void installDone();

    /**
     * this method will be called when the installation
     * process was aborted.
     */
    public void installAborted();

    /**
     * Ask the user if he/she wants to overwrite a file.
     * 
     * @param file name of the file.
     *
     * @return true if the file should be overwritten, false if
     *         the file should be saved.
     */
    public boolean canOverwrite(String file) throws AbortInstallException;

    /**
     * Tell about errors.
     */
    public void showError(int reason, String[] args);

    /**
     * Show an error dialog for IOExceptions.
     *
     * @return true if the operation should be retried.
     */
    public boolean showIOException(IOException e) throws AbortInstallException;

    /**
     * Display a dialog when there was a checksum error.
     *
     * @param file name of the file with the error.
     * @return true to retry this file, false to ignore the error.
     */
    public boolean showChecksumError(String file) throws AbortInstallException;
}
