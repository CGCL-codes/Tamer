package com.xohm.base.testtool.gui;

import java.io.*;
import java.util.*;

/**
 * <B>This class represents a command of either a delay or a Common
 * API call.</B><br><br>
 *
 * <font size=-1>Open source WiMAX connection manager<br>
 * � Copyright Sprint Nextel Corp. 2008</font><br><br>
 *
 * @author Robin Katzer
 */
@SuppressWarnings("serial")
public class Command implements Serializable {

    public String commandName = null;

    public Hashtable<String, Object> attributes = new Hashtable<String, Object>();

    /**
	 * <B>Constructor for creating an object that contains a base command to the
	 * C common API.  This command can be serialized to the file system for
	 * later retrieval and execution.</B><br><br>
	 * 
	 * @param command java.lang.String
	 */
    public Command(String command) {
        this.commandName = command;
    }

    /**
	 * If the command is a delay, then return the delay value in milli-seconds.
	 * 
	 * @return long
	 */
    public long getDelayTime() {
        long delay = 0;
        if (attributes.containsKey("delay")) {
            delay = Long.valueOf((String) attributes.get("delay")).longValue();
        }
        return delay;
    }

    /**
	 * Return a string representation of the command.
	 */
    public String toString() {
        String show = commandName;
        if (commandName.equals("delay")) show = show + " (" + attributes.get("delay") + "ms)";
        return show;
    }
}
