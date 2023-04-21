package com.google.code.sagetvaddons.sjq.server.commands;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.io.FileUtils;
import com.google.code.sagetvaddons.sjq.listener.Command;
import com.google.code.sagetvaddons.sjq.listener.NetworkAck;

/**
 * <p>Provides the ability to edit the contents of the server's crontab file
 * <p><pre>
 *    R: String (crontab contents)
 *    W: ACK
 * </pre></p>
 * @author dbattams
 * @version $Id: Putcron.java 1265 2010-11-10 15:10:58Z derek@battams.ca $
 */
public final class Putcron extends Command {

    public static final File CRONTAB_FILE = new File("plugins/sjq/crontab");

    /**
	 * @param in
	 * @param out
	 */
    public Putcron(ObjectInputStream in, ObjectOutputStream out) {
        super(in, out);
    }

    @Override
    public void execute() throws IOException {
        String crontab = getIn().readUTF();
        synchronized (Getcron.CRONTAB_FILE) {
            if (CRONTAB_FILE.exists()) CRONTAB_FILE.delete();
            FileUtils.writeStringToFile(CRONTAB_FILE, crontab, "UTF-8");
        }
        getOut().writeObject(NetworkAck.get(NetworkAck.OK));
        getOut().flush();
    }
}
