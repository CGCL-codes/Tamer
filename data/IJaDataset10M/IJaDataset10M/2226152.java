package server.response;

import java.util.List;
import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.SystemColor;
import server.universe.Player;
import server.universe.Room;
import util.ArrayUtil;

/**
 * Responds to the emote command as input by the user. This allows users to
 * speak about themselves in the third person, which may or may not be creepy.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class EmoteResponse implements ServerResponse {

    /**
	 * Send an emote message to all players in the room.
	 */
    public ClientMessage respond(ServerThread serverThread, List<String> arguments) {
        if (arguments.size() < 1) {
            return new ClientMessage("The proper syntax is: emote <phrase>", SystemColor.ERROR);
        } else {
            Player player = serverThread.getPlayer();
            Room room = player.getRoom();
            String phrase = ArrayUtil.joinArguments(arguments, " ").trim();
            ClientMessage message = new ClientMessage("*" + player.getName() + " " + phrase + "*");
            Server.sendMessageToAllClientsInRoom(room, message);
            return new ClientMessage("You emoted, \"" + phrase + "\"");
        }
    }
}
