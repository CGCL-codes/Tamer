package net.community.chest.net.proto.text;

/**
 * Copyright 2007 as per GPLv2
 * @author Lyor G.
 * @since Jun 28, 2007 2:08:44 PM
 */
public interface TextProtocolCommandInfo {

    /**
 	 * @return command name string (null/empty if bad/not set)
 	 */
    String getCmdName();

    /**
 	 * @param cmdName sets the current command name
 	 */
    void setCmdName(String cmdName);

    /**
 	 * @param cmdName the current command name to be set
 	 * @return FALSE if command already set (in which case it remains unchanged)
 	 */
    boolean setCmdNameOnce(String cmdName);

    /**
	 * Updates the command argument(s)
	 * @param argIndex argument index (0..)
	 * @param argVal argument value string
	 * @return 0 if successful
	 */
    int setCmdArg(int argIndex, String argVal);

    /**
	 * @param argIndex command argument index (starting from zero)
	 * @return command argument string - null/empty if none available
	 * for specified index (including if index is out of range)
	 */
    public String getCmdArg(int argIndex);

    /**
	 * Restores the object to an "empty" state
	 */
    public void reset();
}
