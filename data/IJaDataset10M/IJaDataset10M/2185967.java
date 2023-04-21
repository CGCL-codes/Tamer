package mipt.aaf.command;

/**
 * Group of commands enabled simultaneously
 * Also execute commands (so that implementations can serve as Mediators in undo/redo support)
 * Note: not used yet (ActionEnabler is used in *.swing because user should view enables status)
 * @author Evdokimov
 */
interface Commander extends Enableable {

    /**
	 * Performs the given command if this object is not disabled
	 * @return boolean - true if isEnabled==true
	 */
    boolean doCommand(Command command);

    /**
	 * Performs the given command if possible
	 * @return boolean - true if command is accepted by this commander and isEnabled==true
	 */
    boolean doCommand(String name);

    Command getCommand(String name);

    void putCommand(Command command);
}
