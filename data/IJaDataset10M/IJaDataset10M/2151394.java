package sifter;

import sifter.*;
import sifter.rcfile.*;
import sifter.translation.*;
import sifter.ui.*;
import sifter.messages.*;
import net.sourceforge.jmisc.Debug;
import java.io.*;
import java.util.Properties;
import java.util.Date;

/** 
    Used to receive messages from the remote host.  This is used when the
    connection between the two hosts is VERY slow: i.e. a person carrying a floppy
    from one machine to another.

    @author Fred Gylys-Colwell
    @version $Name:  $, $Revision: 1.16 $
*/
public class Receiver extends ObjectInputStream {

    public Receiver(InputStream in) throws IOException, StreamCorruptedException {
        super(in);
    }

    /** Decide if there is a receive or not. */
    public Date lookfor() throws IOException {
        String remote = readUTF();
        Debug.println(Bug.TRANSFER, "Remote = " + remote);
        if (!remote.equals(Main.table.getProperty("remote"))) {
            throw new IOException("wrong remote host.");
        }
        String local = readUTF();
        Debug.println(Bug.TRANSFER, "Local = " + local);
        if (!local.equals(Main.table.getProperty("local"))) {
            throw new IOException("wrong remote host.");
        }
        try {
            readVersions(null, true);
        } catch (ClassNotFoundException ex) {
            throw new IOException("Bad Stream Format: " + ex);
        }
        return readDates(null, true);
    }

    /** Do a full receive.
	The data/message order should match Sender.run() 
    */
    void run(Chooser verify) throws IOException, ClassNotFoundException {
        Debug.println(Bug.TRANSFER, "Receiving.");
        String remote = readUTF();
        Debug.println(Bug.TRANSFER, "Remote = " + remote);
        if (!remote.equals(Main.table.getProperty("remote"))) {
            verify.warn("Remote host listed in stream as " + remote + "\n" + "Expected remote host: " + Main.table.getProperty("remote"), verify.RECKLESS);
        }
        String local = readUTF();
        Debug.println(Bug.TRANSFER, "Local = " + local);
        if (!local.equals(Main.table.getProperty("local"))) {
            verify.warn("Local host listed in stream as " + local + "\n" + "Expected local host: " + Main.table.getProperty("local"), verify.RECKLESS);
        }
        try {
            readVersions(verify, false);
        } catch (ClassNotFoundException ex) {
            throw new IOException("Bad Stream Format: " + ex);
        }
        Date dateReceived = readDates(verify, false);
        verify.pause();
        Main.data.save();
        while (!(Message.receive(this, Main.data.roots, null, verify) instanceof Done)) {
        }
        Debug.println(Bug.TRANSFER, "done.");
        close();
        verify.warn("Transfer Complete", verify.COMMENT);
        verify.pause();
        Main.data.setLastAction(Main.RECEIVE);
        Main.data.setLastReceive(dateReceived);
    }

    void readVersions(Chooser verify, boolean justChecking) throws IOException, ClassNotFoundException {
        Version.transfer = (Version) readObject();
        Debug.println(Bug.VERSION, "transfer version = " + Version.transfer);
        Main.data.remoteVersion = (Version) readObject();
        Debug.println(Bug.VERSION, "local version = " + Version.local);
        Debug.println(Bug.VERSION, "remote version = " + Main.data.remoteVersion);
        if (justChecking) return;
        if (!Main.data.remoteVersion.equals(Version.local)) {
            verify.warn("Local Version: " + Version.local + ".   Remote Version: " + Main.data.remoteVersion, verify.COMMENT);
        }
    }

    Date readDates(Chooser verify, boolean justChecking) throws IOException {
        Date dateReceived = new Date(readLong());
        Date lastSend = new Date(readLong());
        Main.data.setLastSend(lastSend);
        boolean boot = readBoolean();
        if (justChecking) {
            return dateReceived;
        }
        verify.oneDate("data stream created", dateReceived);
        verify.oneDate("acknowledged send", lastSend);
        Main.data.debugDates();
        if (lastSend.before(Main.data.getLastSendAttempt())) {
            verify.warn("Last send, on date " + Main.data.getLastSendAttempt() + " was not acknowledged.\n" + "Instead, send on " + lastSend + " was acknowledged.", verify.UNUSUAL);
        } else {
            Main.data.incrementUpdate();
            if (Main.data.getLastAction() == Main.RECEIVE) {
                verify.warn("Previous action was also a receive on " + Main.data.getLastReceive(), verify.UNUSUAL);
            } else if (Main.data.getLastReceive().after(lastSend)) {
                verify.warn("Last recorded receive was after last succesful send:\n" + "Receive: " + Main.data.getLastReceive() + ",\nAcknowledged Send: " + lastSend + ",\nSend Attempt: " + Main.data.getLastSendAttempt(), verify.SEVERE);
            }
        }
        if (!boot && (lastSend.after(Main.data.getLastSendAttempt()))) {
            verify.warn("Last send was on " + Main.data.getLastSendAttempt() + ".\n" + "However a later send on " + lastSend + " was acknowledged.", verify.SEVERE);
        }
        if (lastSend.after(dateReceived)) {
            verify.warn("Last successful send was after receive data:\n" + "Send: " + lastSend + ", Receive: " + Main.data.getLastReceive(), verify.SEVERE);
        }
        if (!boot) {
            Date stamp = new Date();
            Debug.println(Bug.TIMESTAMP, "~~Recieved on: " + stamp);
            Main.update(stamp);
        }
        return dateReceived;
    }
}
