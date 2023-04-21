package net.sf.doolin.cli.command;

/**
 * Configuration for {@link HelpCommand}.
 * 
 * @author Damien Coraboeuf
 */
public class HelpCommandConfig extends CLIConfig {

    private String commandName;

    /**
	 * Gets the command name.
	 * 
	 * @return the command name
	 */
    public String getCommandName() {
        return commandName;
    }

    /**
	 * Sets the command name.
	 * 
	 * @param commandName
	 *            the new command name
	 */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}
