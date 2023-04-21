package com.coldcore.coloradoftp.command.impl.ftp;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.command.impl.AbstractCommand;
import com.coldcore.coloradoftp.connection.DataConnection;
import com.coldcore.coloradoftp.connection.DataPortListener;
import com.coldcore.coloradoftp.connection.DataPortListenerSet;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import org.apache.log4j.Logger;

public class AborCommand extends AbstractCommand {

    private static Logger log = Logger.getLogger(AborCommand.class);

    public Reply execute() {
        Reply reply = getReply();
        if (!testLogin()) return reply;
        controlConnection.getDataConnectionInitiator().abort();
        DataPortListenerSet listeners = (DataPortListenerSet) ObjectFactory.getObject(ObjectName.DATA_PORT_LISTENER_SET);
        for (DataPortListener listener : listeners.list()) listener.removeConnection(controlConnection);
        DataConnection dataConnection = controlConnection.getDataConnection();
        if (dataConnection != null) {
            log.debug("Aborting data connection, it will send reply to ABOR command");
            dataConnection.abort();
            return null;
        } else {
            reply.setCode("226");
            reply.setText("Abort command successful.");
            return reply;
        }
    }

    public boolean processInInterruptState() {
        return true;
    }

    public boolean canClearInterruptState() {
        return true;
    }
}
