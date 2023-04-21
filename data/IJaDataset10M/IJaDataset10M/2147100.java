package ursus.server.plugins.core;

import ursus.common.Communicator;
import ursus.server.plugins.core.persistence.*;

/**
 * Basic TextCommand instance that takes the String arguments passed by execute and 
 * returns them to the client. 
 *
 * @see TextCommand
 * @author Anthony
 */
public class EchoCommand extends BasicTextCommand {

    public static final String NAME = "echo";

    public static final String RETURN_TYPE = TextListener.TYPE;

    public EchoCommand() {
        super(NAME);
    }

    public void execute(Communicator com, String args) {
        com.write(RETURN_TYPE, args);
    }
}
