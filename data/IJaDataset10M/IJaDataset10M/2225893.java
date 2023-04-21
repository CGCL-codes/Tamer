package protocol;

import client.client.Network;
import db.Database;
import db.SynchroData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import server.EndLoopOnLogout;

/**
 *
 * @author Guldan
 */
public class RemoveBuddy extends ArcticProtocol {

    String senderMail;

    Contact buddy;

    public RemoveBuddy(String senderMail, Contact buddy) {
        this.senderMail = senderMail;
        this.buddy = buddy;
    }

    @Override
    public void process() throws EndLoopOnLogout {
        try {
            Database db = Database.getInstance();
            SynchroData sd = SynchroData.getInstance();
            String buddyName = db.getBuddyName(buddy.getBuddyMail());
            if ((buddyName.equals(buddy.getBuddyName()))) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("contacts/" + senderMail + ".apc")));
                LinkedList<Contact> cl = (LinkedList<Contact>) ois.readObject();
                ois.close();
                cl.remove(new Contact(null, buddy.getBuddyMail(), buddy.getBuddyName(), false));
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("contacts/" + senderMail + ".apc")));
                oos.writeObject(cl);
                oos.close();
                sd.writeObjectToOS(senderMail, this);
            } else {
                sd.writeObjectToOS(senderMail, new Error(Error.USER_DOES_NOT_EXIST));
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    @Override
    public void execute(Network net) {
        net.getClientGuiThread().getContactArray().removeContact(buddy);
        net.getClientGuiThread().getMainFrame().removeContact(buddy);
    }
}
