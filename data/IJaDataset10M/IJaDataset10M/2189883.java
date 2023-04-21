package server;

import server.response.AttackResponse;
import server.response.CommandsResponse;
import server.response.DropResponse;
import server.response.EmoteResponse;
import server.response.GetResponse;
import server.response.GiveResponse;
import server.response.InventoryResponse;
import server.response.LookResponse;
import server.response.MoveResponse;
import server.response.OocResponse;
import server.response.QuitResponse;
import server.response.SayResponse;
import server.response.ScoreResponse;
import server.response.ServerResponse;
import server.response.ShutdownResponse;
import server.response.SocialResponse;
import server.response.TellResponse;
import server.response.UnknownResponse;
import server.response.UseResponse;
import server.response.WhoResponse;
import server.universe.Direction;

/**
 * A parser!
 */
public class ResponseFactory {

    /**
	 * From the given text, get an appropriate server response.
	 * 
	 * @param text
	 *            The text of the command.
	 * @return An appropriate ServerResponse.
	 */
    public static ServerResponse getResponse(String text) {
        Command command = getCommand(text);
        switch(command) {
            case EXIT:
            case QUIT:
                return new QuitResponse();
            case HELP:
            case COMMANDS:
                return new CommandsResponse();
            case WHO:
                return new WhoResponse();
            case TELL:
                return new TellResponse();
            case OOC:
                return new OocResponse();
            case SAY:
                return new SayResponse();
            case DROP:
                return new DropResponse();
            case USE:
                return new UseResponse();
            case L:
            case LS:
            case LOOK:
                return new LookResponse();
            case INVENTORY:
                return new InventoryResponse();
            case SC:
            case SCORE:
                return new ScoreResponse();
            case ATTACK:
                return new AttackResponse();
            case CD:
            case MOVE:
                return new MoveResponse();
            case N:
            case NORTH:
                return new MoveResponse(Direction.NORTH);
            case S:
            case SOUTH:
                return new MoveResponse(Direction.SOUTH);
            case E:
            case EAST:
                return new MoveResponse(Direction.EAST);
            case W:
            case WEST:
                return new MoveResponse(Direction.WEST);
            case SHUTDOWN:
                return new ShutdownResponse();
            case GIVE:
                return new GiveResponse();
            case GET:
                return new GetResponse();
            case EMOTE:
                return new EmoteResponse();
            case LAUGH:
                return new SocialResponse("laughs");
            case GIGGLE:
                return new SocialResponse("giggles");
            case SLAP:
                return new SocialResponse("slaps");
            case WINK:
                return new SocialResponse("winks");
            default:
                return new UnknownResponse();
        }
    }

    /**
	 * Get the command of the input.
	 */
    private static Command getCommand(String input) {
        String[] words = input.split(" ");
        Command command = null;
        try {
            command = Command.valueOf(words[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            command = Command.UNKNOWN;
        }
        return command;
    }

    /**
	 * An enumeration of possible commands.
	 */
    private enum Command {

        EXIT, QUIT, SC, SCORE, ATTACK, OOC, COMMANDS, WHO, TELL, SAY, L, LS, LOOK, INVENTORY, DROP, USE, MOVE, SHUTDOWN, GIVE, GET, UNKNOWN, CD, N, NORTH, S, SOUTH, E, EAST, W, WEST, HELP, EMOTE, LAUGH, GIGGLE, SLAP, WINK
    }
}
