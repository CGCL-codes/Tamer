package de.miij;

public interface IExceptionHandler {

    /**
	 * Does the exception handling just like you defined it.
	 * 
	 * @param ex
	 */
    public void handleException(Exception ex);

    public void handleException(Exception ex, MiijExceptionHandlerModes actions);

    public void logException(Exception ex);

    public void informUser(Exception ex);
}
