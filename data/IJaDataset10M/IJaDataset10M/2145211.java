package com.ail.core.persistence;

import com.ail.core.Type;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/UpdateCommand.java,v $
 * @stereotype command
 */
public class UpdateCommand extends Command implements UpdateArg {

    private UpdateArg args = null;

    public UpdateCommand() {
        super();
        args = new UpdateArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (UpdateArg) arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Type getObjectArg() {
        return args.getObjectArg();
    }

    /**
     * {@inheritDoc}
     * @param objectArg @{inheritDoc}
     */
    public void setObjectArg(Type objectArg) {
        args.setObjectArg(objectArg);
    }
}
