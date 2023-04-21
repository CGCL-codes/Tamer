package judge.server.commands;

import judge.server.*;
import judge.server.net.io.*;
import judge.object.*;
import judge.object.alive.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class drop extends BasicCommand {

    public void invokeMethod() {
        if (mArgc == 0) {
            String s = "What did you want to drop?";
            send(s);
        } else dropItem();
    }

    public void dropItem() {
        boolean success = false;
        boolean found = false;
        int numItems;
        Entity thing;
        int errno = 0;
        int CantBeDropped = 1;
        int NoSuchItem = 2;
        String s = (String) mArgv.firstElement();
        numItems = mPlayer.getInv().size();
        Room r = mPlayer.mRoom;
        try {
            thing = mPlayer.getInv().get(s, 0);
            IDropable id = (IDropable) thing;
            id.drop();
            success = true;
            r.getInv().add(thing);
            mPlayer.getInv().remove(thing);
            r.broadcast(mPlayer.getName() + " drops a " + s, mPlayer);
            s = "You drop the " + s;
            ClientStringWriter.send(mPlayer, s);
        } catch (ClassCastException cce) {
            errno = CantBeDropped;
        } catch (NullPointerException npe) {
            errno = NoSuchItem;
        }
        if (!success) {
            if (errno == NoSuchItem) {
                s = "You don't seem to see that here";
            } else if (errno == CantBeDropped) {
                s = "You can't drop that.";
            }
            ClientStringWriter.send(mPlayer, s);
        }
    }
}
