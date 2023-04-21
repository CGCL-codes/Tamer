package org.easyway.editor.commands;

import org.easyway.debug.DebugManager;
import org.easyway.editor.commands.intrefaces.ICommand;

public abstract class Command implements ICommand {

    public Command() {
        CommandList.objlistToCreate.add(this);
        DebugManager.debug = true;
    }

    boolean isDestroyed = false;

    public void destroy() {
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
