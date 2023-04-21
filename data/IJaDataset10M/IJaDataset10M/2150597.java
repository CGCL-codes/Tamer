package net.sf.microinstall;

/**
 * This interface
 * 
 * @author Johan Karlsson (johan.karlsson@jayway.se)
 */
public interface InstallListener {

    /**
     * This is called then the installation object has been sent.
     */
    public void installationFinished();

    /**
     * This is called if the installation has failed.
     * 
     * @param message
     *            the specified message that should indicate what happened.
     * @param cause
     *            the exception that was thrown when the installation failed.
     *            This could be <code>null</code> if no exception casued the
     *            problem.
     */
    public void installationFailed(String message, Exception cause);
}
