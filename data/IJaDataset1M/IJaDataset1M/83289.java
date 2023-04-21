package net.solarnetwork.node.control.jf2.lata;

import net.solarnetwork.node.ConversationalDataCollector;
import net.solarnetwork.node.control.jf2.lata.command.CommandInterface;

/**
 * ConversationalDataCollector.Moderator for LATA switch.
 * 
 * @author matt
 * @version $Revision: 2085 $
 */
public class Converser implements ConversationalDataCollector.Moderator<String> {

    private final CommandInterface command;

    /**
	 * Constructor.
	 * 
	 * @param command the command to execute.
	 */
    public Converser(CommandInterface command) {
        this.command = command;
    }

    @Override
    public String conductConversation(ConversationalDataCollector dataCollector) {
        dataCollector.speakAndListen(command.getCommandData());
        return dataCollector.getCollectedDataAsString();
    }

    public CommandInterface getCommand() {
        return command;
    }
}
