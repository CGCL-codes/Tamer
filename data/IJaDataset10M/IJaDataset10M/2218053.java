package com.wgo.precise.client.common.session;

/**
 * Interface for types that holds a PreciseSession with a custom sub-session.
 * 
 * @author petterei
 *
 * @param <T> Custom subsession
 */
public interface ISessionHolder<T> {

    /**
	 * Sets the session.
	 * 
	 * @param newSession
	 * @return
	 */
    public boolean setSession(PreciseSession domainSession);

    /**
	 * If session is dirty, the user is asked if changes should be saved.
	 * 
	 * @return
	 */
    public boolean disposeSession();

    /**
	 * Disposes the session with no user interaction.
	 * 
	 * @param saveChanges If changes should be saved or not.
	 * @return
	 */
    public boolean disposeSession(boolean saveChanges);

    /**
	 * If session is dirty, the user is asked the cusom message if changes 
	 * should be saved.
	 * 
	 * @param whySaveExplanation Custom message.
	 * @return
	 */
    public boolean disposeSession(String whySaveExplanation);

    /**
	 * @return the ClientSession.
	 */
    public T getSession();

    /**
	 * @return Whether the session is set or not.
	 */
    public boolean isSessionSet();
}
