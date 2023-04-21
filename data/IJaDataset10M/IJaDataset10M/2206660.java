package com.google.code.sagetvaddons.sjq.server.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.google.code.sagetvaddons.sjq.listener.Command;
import com.google.code.sagetvaddons.sjq.listener.NetworkAck;
import com.google.code.sagetvaddons.sjq.server.DataStore;
import com.google.code.sagetvaddons.sjq.shared.QueuedTask;

/**
 * <p>Provides the ability to log task output for an executed task</p>
 * <p><pre>
 *    R: QueuedTask
 *    R: int (num chunks)
 *    R: String (log data) x num chunks
 *    W: ACK
 * </pre></p>
 * @author dbattams
 * @version $Id: Logexe.java 1356 2011-01-29 22:26:14Z derek@battams.ca $
 */
public final class Logexe extends Command {

    /**
	 * @param in
	 * @param out
	 */
    public Logexe(ObjectInputStream in, ObjectOutputStream out) {
        super(in, out);
    }

    @Override
    public void execute() throws IOException {
        try {
            QueuedTask qt = (QueuedTask) getIn().readObject();
            int chunks = getIn().readInt();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chunks; ++i) sb.append(getIn().readUTF());
            NetworkAck ack = null;
            if (DataStore.get().logOutput(qt, QueuedTask.OutputType.TASK, sb.toString())) ack = NetworkAck.get(NetworkAck.OK); else ack = NetworkAck.get(NetworkAck.ERR + "Failed to write log to data store!");
            getOut().writeObject(ack);
            getOut().flush();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}
