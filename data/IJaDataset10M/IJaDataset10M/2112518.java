package judge.server.commands;

import judge.server.*;
import judge.object.*;
import judge.object.alive.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ponder extends BasicCommand {

    public void invokeMethod() {
        String playermessage;
        String broadcastmessage;
        playermessage = "You ponder.  Hmm.  Tricky.";
        broadcastmessage = mPlayer.getName() + " ponders.";
        broadcast(broadcastmessage);
        send(playermessage);
    }
}
