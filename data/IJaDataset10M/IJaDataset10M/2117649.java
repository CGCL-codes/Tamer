package mipt.aaf.command;

import java.util.Iterator;

/**
 * A part of CommandBuilder responsible for creating command set (and each command)
 * @author Evdokimov
 */
public interface CommandFactory {

    /**
	 * Is needed only for calling builder.buildCommands() (once)
	 * @return collection of objects that will be sent to createCommand() 
	 */
    Iterator getCommandsInfo();

    /**
	 * Is responsible only for instantiation
	 */
    Command createCommand(Object commandInfo) throws IllegalArgumentException;
}
