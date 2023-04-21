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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.ListIterator;

/** 
    This is the stream that sends files.  It also does some special handling for
    doing data verify.
    @author Fred Gylys-Colwell
    @version $Name:  $, $Revision: 1.13 $
*/
public class Sender extends ObjectOutputStream {

    public Sender(OutputStream out) throws IOException {
        super(out);
    }

    /** The order should match Receiver.run() */
    void run(Chooser verify) throws IOException {
        Bug.profile("sending....");
        Debug.println(Bug.TRANSFER, "Sending.");
        Main.data.setLastAction(Main.SEND);
        writeUTF(Main.table.getProperty("local"));
        writeUTF(Main.table.getProperty("remote"));
        Version.transfer = Version.min(Version.local, Main.data.remoteVersion);
        Debug.println(Bug.VERSION, "transfer version = " + Version.transfer);
        writeObject(Version.transfer);
        Debug.println(Bug.VERSION, "local version = " + Version.local);
        writeObject(Version.local);
        Date sent = new Date();
        Bug.pause("Send date set.");
        Main.data.setLastSendAttempt(sent);
        writeLong(sent.getTime());
        writeLong(Main.data.getLastReceive().getTime());
        boolean boot = Main.boot();
        Debug.println(Bug.TIMESTAMP, "~~Stream created: " + sent);
        Debug.println(Bug.TIMESTAMP, "~~lastStream = " + Main.data.getLastReceive());
        Main.data.debugDates();
        Debug.println(Bug.TRANSFER, "cutoff=" + Main.cutoff + ", today=" + (new Date()).getTime());
        writeBoolean(boot);
        Main.update(sent);
        Debug.println(Bug.TRANSFER, "Sending roots, boot = " + Main.boot());
        Debug.println(Bug.TIMESTAMP, "Sending boot = " + Main.boot());
        Main.data.roots.send(this, verify);
        Message done = new Done();
        done.send(this, verify);
        Bug.pause("send done.");
        Bug.profile("done with send....");
        close();
        verify.warn("Transfer Complete.", Verify.COMMENT);
        verify.pause();
        Main.updateSendDate();
    }

    private int count = 0;

    public static int countMax = 1000;

    /** This periodically flushes and resets the data stream, so that it
     doesn't get too full. */
    public void writeFile(FilePair p) throws IOException {
        writeObject(p);
        if (count++ < countMax) return;
        count = 0;
        flush();
        reset();
    }
}
