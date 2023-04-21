package org.puremvc.java.multicore.patterns.command;

/**
 * A MacroCommand subclass used by MacroCommandTest.
 *
	 * @see org.puremvc.java.multicore.patterns.command.MacroCommandTest MacroCommandTest
	 * @see org.puremvc.java.multicore.patterns.command.MacroCommandTestSub1Command MacroCommandTestSub1Command
	 * @see org.puremvc.java.multicore.patterns.command.MacroCommandTestSub2Command MacroCommandTestSub2Command
	 * @see org.puremvc.java.multicore.patterns.command.MacroCommandTestVO MacroCommandTestVO
 */
public class MacroCommandTestCommand extends MacroCommand {

    /**
	 * Constructor.
	 */
    public MacroCommandTestCommand() {
        super();
    }

    /**
	 * Initialize the MacroCommandTestCommand by adding
	 * its 2 SubCommands.
	 */
    @Override
    protected void initializeMacroCommand() {
        addSubCommand(new MacroCommandTestSub1Command());
        addSubCommand(new MacroCommandTestSub2Command());
    }
}
