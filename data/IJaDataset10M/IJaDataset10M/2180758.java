package judge.server.commands;

import judge.server.*;
import judge.object.*;
import judge.object.alive.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class admin extends BasicCommand {

    public void invokeMethod() {
        if (mArgc == 0) {
            String s = "What did you want to set?";
            send(s);
        } else {
            String s = (String) mArgv.firstElement();
            if (s.equals("Admin")) {
                Player temp = World.getPlayer(mPlayer.getId());
                if (temp != null) {
                    temp.mCharacterClass.setLevel(1000);
                    s = temp.getName() + "'s level Set to " + temp.mCharacterClass.getLevel() + "\n";
                    temp.writeCharacter();
                } else s = "I couldn't find that player.  Sorry\n";
            } else {
                s = "I don't understand that\n";
            }
            send(s);
        }
    }
}
