package net.sf.mailand.pop3.server;

/**
 *
 */
public interface MailAccess {

    /**
	 * Authenticate the user and locks the maildrop.
	 */
    boolean authenticate(String user, String password);

    /**
	 * Number of messages waiting for download.
	 * 
	 * @return
	 */
    int getNumberOfMessages();

    /**
	 * Retrieves the message with the given index.
	 * 
	 * @param index
	 */
    String get(int index);

    /**
	 * Marks a message for deletion.
	 * 
	 * @param message
	 * 
	 * @return
	 */
    boolean delete(int message);

    /**
	 * Unmarks all deleted messages.
	 */
    void reset();

    /**
	 * Ends the session.
	 * 
	 * All messages which are marked for deletion are deleted.
	 */
    boolean quit();

    /**
	 * Ends the session. 
	 * 
	 * All messages which are marked for deletion must not be deleted.
	 */
    void terminate();
}
