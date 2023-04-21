package de.creepsmash.common.messages.client;

import java.util.regex.Pattern;

/**
 * Request to delete player from database.
 * 
 * @author andreas
 *
 */
public class DeleteRequestMessage extends ClientMessage {

    /**
	* regular expression for message parsing.
	*/
    private static final String REGEXP_DELETE_REQUEST = "DELETE_REQUEST";

    /**
	 * pattern for regular expression.
	 */
    public static final Pattern PATTERN = Pattern.compile(REGEXP_DELETE_REQUEST);

    /**
	 * @return the message as String
	 */
    @Override
    public String getMessageString() {
        return "DELETE_REQUEST";
    }

    /**
	 * @param messageString the message as String
	 */
    @Override
    public void initWithMessage(String messageString) {
    }
}
