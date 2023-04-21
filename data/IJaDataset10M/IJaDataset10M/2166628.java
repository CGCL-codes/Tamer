package com.google.code.sagetvaddons.sjq.agent.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.google.code.sagetvaddons.sjq.agent.ProcessRunner;
import com.google.code.sagetvaddons.sjq.listener.Command;
import com.google.code.sagetvaddons.sjq.listener.Handler;
import com.google.code.sagetvaddons.sjq.listener.NetworkAck;
import com.google.code.sagetvaddons.sjq.shared.QueuedTask;

/**
 * @author dbattams
 *
 */
public class Isactive extends Command {

    /**
	 * @param in
	 * @param out
	 */
    public Isactive(ObjectInputStream in, ObjectOutputStream out) {
        super(in, out);
    }

    @Override
    public void execute() throws IOException {
        try {
            QueuedTask qt = (QueuedTask) getIn().readObject();
            qt.setServerHost(Handler.SOCKET_DETAILS.get().getRemoteAddress());
            getOut().writeObject(NetworkAck.get(NetworkAck.OK + String.valueOf(ProcessRunner.isActive(qt))));
            getOut().flush();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}
