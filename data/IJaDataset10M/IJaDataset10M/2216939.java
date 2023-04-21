package tool.dtf4j.master;

import tool.dtf4j.common.CommandEvent;

/**
 * The command event listener interface.
 */
public interface CommandEventListener {

    /**
     * Returns the pattern, i.e. substring, which when present in the stdout
     * or stderr indicates that the running command is present.
     * @return the pattern, i.e. substring, which when present in the stdout
     * or stderr indicates that the running command is present.
     */
    public String getPattern();

    /**
     * Invoked if the provided pattern was found in the stdout or stderr
     * from the execution on the client.
     */
    public void onPatternFound(CommandEvent event);

    /**
     * Invoked if the command completed.
     */
    public void onCommandCompleted(CommandEvent event);

    /**
     * Invoked if the command was killed by the client on which it is
     * running.
     */
    public void onCommandKilled(CommandEvent event);

    /**
     * Invoked if the command timed out before the provided pattern was 
     * found in the stdout or stderr.
     */
    public void onCommandTimedOut(CommandEvent event);

    /**
     * Notification from the client that the jvm has crashed
     * @param event the event - doesn't contain the output!
     */
    public void onVMCrash(CommandEvent event);
}
