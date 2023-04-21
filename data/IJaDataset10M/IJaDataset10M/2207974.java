package server.response;

import java.util.List;
import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.SystemColor;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to various social commands, i.e., wink, giggle, etc. Constructor
 * takes in a string that represents the verb of the social interaction.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class SocialResponse implements ServerResponse {

    private String verb;

    /**
	 * Constructs the social interaction with the given verb.
	 * The verb is assumed to be in 3rd person singular form, ending with an s.
	 * For example: giggles, not giggle.
	 * 
	 * @param verb
	 *            type of social interaction
	 */
    public SocialResponse(String verb) {
        this.verb = verb;
    }

    /**
	 * Send a social command to the room using the verb that this SocialResponse was constructed with.
	 */
    public ClientMessage respond(ServerThread serverThread, List<String> arguments) {
        final Player player = serverThread.getPlayer();
        final Room room = player.getRoom();
        final String withoutFinalS = this.verb.substring(0, this.verb.length() - 1);
        if (arguments.isEmpty()) {
            ClientMessage message = new ClientMessage(player.getName() + " " + this.verb);
            Server.sendMessageToAllClientsInRoom(room, message);
            return new ClientMessage("You " + withoutFinalS + "ed at the room!");
        } else {
            String targetName = arguments.get(0);
            Player target = Server.getUniverse().getPlayer(targetName);
            if (target == null) return new ClientMessage("That player, " + targetName + ", could not be found.", SystemColor.ERROR);
            ClientMessage message = new ClientMessage(player.getName() + " " + this.verb + " at " + targetName);
            Server.sendMessageToAllClientsInRoom(room, message);
            return new ClientMessage("You " + withoutFinalS + "ed at " + targetName + " to the whole room!");
        }
    }
}
