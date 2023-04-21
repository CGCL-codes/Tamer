package net.sourceforge.yagurashogi.server;

import java.net.*;
import java.util.*;

/**
 * Mechanism to process queries in Extended Protocol and CSA format.
 */
public class ExtendedShogiProtocol {

    ShogiServer server;

    CSAProtocol csap;

    /**
	 * Creates new instance of ExtendedShogiProtocol that deals with the server given.
	 *
	 * @param shogiServer	server instance to associate this instance with
	 */
    public ExtendedShogiProtocol(ShogiServer shogiServer) {
        server = shogiServer;
        csap = new CSAProtocol(server);
    }

    /**
	 * Processes queries.
	 *
	 * @param sock	socket that this query came from
	 * @param input	query to interpret
	 * @return	reply to send back
	 */
    String processInput(Socket sock, String input) {
        String output = new String();
        if (server.playersBySocket.containsKey(sock.toString())) {
            Player player = (Player) server.playersBySocket.get(sock.toString());
            if ((player.getState() == Player.GAME_WAITING) && (input.equals("%%IDLE ON\n"))) {
                player.setState(Player.IDLE);
                output = "##[IDLE] ON\n";
            } else if ((player.getState() == Player.IDLE) && (input.equals("%%IDLE OFF\n"))) {
                player.setState(Player.GAME_WAITING);
                output = "##[IDLE] OFF\n";
            } else if (input.equals("%%STATE\n")) {
                switch(player.getState()) {
                    case Player.IDLE:
                        output = "##[STATE] IDLE\n";
                        break;
                    case Player.GAME_WAITING:
                        output = "##[STATE] GAME_WAITING\n";
                        break;
                    case Player.GAME:
                        output = "##[STATE] GAME\n";
                        break;
                }
            } else if (input.matches("%%CHAT .*\\n")) {
                player.chat(input.split(" ", 2)[1]);
            } else if (input.equals("%%WHO\n")) {
                for (Enumeration p = server.playersByName.elements(); p.hasMoreElements(); ) {
                    Player current = (Player) p.nextElement();
                    output += "##[WHO] " + current.getName() + " : ";
                    switch(current.getState()) {
                        case Player.IDLE:
                            output += "IDLE\n";
                            break;
                        case Player.GAME_WAITING:
                            output += "GAME_WAITING\n";
                            break;
                        case Player.GAME:
                            output += "GAME\n";
                            break;
                    }
                }
                output += "##[WHO] +OK\n";
            } else output = csap.processInput(sock, input, true);
        } else {
            if (input.equals("%%STATE\n")) {
                output = "Your state is: CONNECTED\n";
            } else output = csap.processInput(sock, input, false);
        }
        if (output == null) {
            if (input.equals("%%PING\n")) {
                output = "##[PONG]\n";
            } else {
                output = "##[ERROR] Bad syntax: " + input;
            }
        }
        return output;
    }
}
