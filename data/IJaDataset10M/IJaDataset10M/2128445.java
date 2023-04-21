package com.ivis.xprocess.web.commands;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ivis.xprocess.web.framework.IWebDataSource;
import com.ivis.xprocess.web.queue.AbstractQueuedCommand;
import com.ivis.xprocess.web.queue.CommandMessage;

/**
 * The command from the session to update the files from VCS.
 *
 * This command is called from the quartz timer defined in
 * applicationContext.xml, the bean called UpdateTrigger.
 *
 * How frequently the Quartz task is triggered is defined in
 * web.properties
 */
public class UpdateMasterDSCommand extends AbstractQueuedCommand {

    private static final Logger logger = Logger.getLogger(UpdateMasterDSCommand.class.getName());

    private IWebDataSource myMasterDS_WS;

    public void setMasterDS(IWebDataSource masterDS) {
        myMasterDS_WS = masterDS;
    }

    public void init() throws Exception {
        if (myMasterDS_WS == null) {
            throw new Exception(this.getClass().getName() + " is not setup correctly: MasterDS must be set");
        }
        super.init();
    }

    public CommandMessage getCommand() throws Exception {
        logger.log(Level.FINEST, "UpdateMasterDSCommand - getCommand()");
        Class<?>[] paramTypes = new Class[] {};
        Serializable[] paramValues = new Serializable[] {};
        commandMessage = new CommandMessage(myMasterDS_WS, "update", paramTypes, getPriority(), paramValues);
        fillMessageData(commandMessage);
        return commandMessage;
    }
}
