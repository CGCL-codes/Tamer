package com.coladoro.core.events;

import com.coladoro.core.shell.Command;
import com.coladoro.core.sources.Source;

/**
 * @author tanis
 */
public class CoreCommandEvent extends Event {

    /**
     * The command to treat.
     */
    private Command commandToTreat;

    /**
     * Builds a command event.
     * 
     * @param commandToTreat
     *                the command to treat.
     */
    public CoreCommandEvent(Command commandToTreat, Source source) {
        this.commandToTreat = commandToTreat;
        this.sourceOfEvent = source;
    }

    @Override
    public boolean isToStartAsThread() {
        return false;
    }

    /**
     * @return the commandToTreat
     */
    public Command getCommandToTreat() {
        return commandToTreat;
    }

    /**
     * @param commandToTreat
     *                the commandToTreat to set
     */
    public void setCommandToTreat(Command commandToTreat) {
        this.commandToTreat = commandToTreat;
    }
}
