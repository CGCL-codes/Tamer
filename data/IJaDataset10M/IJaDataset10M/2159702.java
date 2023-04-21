package batserver;

import batcommon.BattleCommands;
import batcommon.BattleShipShip;
import java.io.*;
import java.net.*;
import java.util.regex.*;

public class BattleCommandProcessor {

    private static final BattleCommands bc = new BattleCommands();

    private static final Pattern pat = Pattern.compile("[:]+");

    public static String processCommand(int sessionID, String command) {
        String reply = "ERROR: Unknown Command: " + command;
        if (command.startsWith(bc.BC_LOGIN)) {
            reply = PlayerLogin.attemptLogin(sessionID, command);
        } else if (command.startsWith(bc.BC_SHOT)) {
            BattleGameList bgl = new BattleGameList();
            BattleGame bg = bgl.getBattleGameBySessionID(sessionID);
            PlayerList pl = new PlayerList();
            Player p = pl.getPlayerBySession(sessionID);
            String[] result = pat.split(command);
            Player p2 = null;
            ;
            int opponentID;
            int row, col;
            int status = BattleShipShip.BSS_NO_HIT;
            String sunk_message = null;
            try {
                opponentID = Integer.parseInt(result[1]);
                row = Integer.parseInt(result[2]);
                col = Integer.parseInt(result[3]);
                p2 = pl.getPlayer(opponentID);
                status = bg.fireAtPlayer(p2, row, col);
                if (status == BattleShipShip.BSS_NO_HIT) {
                    reply = bc.BC_MISS + p2.getPlayerID() + ":" + row + ":" + col;
                } else if (status == BattleShipShip.BSS_HIT) {
                    reply = bc.BC_HIT + p2.getPlayerID() + ":" + row + ":" + col;
                } else if (status == BattleShipShip.BSS_SUNK) {
                    if (bg.isPlayerSunk(p2)) reply = bc.BC_ALL_SUNK + p2.getPlayerID() + ":" + row + ":" + col; else reply = bc.BC_SUNK + p2.getPlayerID() + ":" + row + ":" + col;
                    sunk_message = bc.BC_SUNK_SHIP_INFO + p2.getPlayerID() + ":" + bg.getPlayerSunkShip(p2, row, col);
                    sendUnsolicitedMessage(sessionID, sunk_message);
                }
                sendUnsolicitedMessage(p2.getSessionID(), reply + ":" + p.getHandle());
            } catch (NumberFormatException e) {
            }
            String message = bc.BC_SHOT_FIRED + p.getHandle() + ":" + p2.getHandle() + ":" + reply;
            reply = reply + ":" + p2.getHandle();
            int[] session_list = bg.getSessionIDs();
            for (int i = 0; i < session_list.length; i++) {
                if (session_list[i] != sessionID && session_list[i] != p2.getSessionID()) {
                    sendUnsolicitedMessage(session_list[i], message);
                    if (status == BattleShipShip.BSS_SUNK) sendUnsolicitedMessage(session_list[i], sunk_message);
                }
            }
            int next_player = bg.getNextPlayer();
            if (next_player > 0) {
                p2 = pl.getPlayer(next_player);
                if (!bg.isPlayerSunk(p2)) {
                    sendUnsolicitedMessage(p2.getSessionID(), bc.BC_YOUR_TURN + bg.getOpponent());
                }
            } else {
                message = bc.BC_UNHIT_COORDS + p.getPlayerID() + ":" + bg.getPlayerUnhitCoords(p);
                for (int i = 0; i < session_list.length; i++) {
                    if (session_list[i] != sessionID) {
                        sendUnsolicitedMessage(session_list[i], message);
                    }
                }
            }
        } else if (command.startsWith(bc.BC_SALVO)) {
            reply = "Nice Salvo! Salvo logic goes here";
        } else if (command.startsWith(bc.BC_SEND_FLEET)) {
            String[] result = pat.split(command);
            int fleet_size;
            int status;
            reply = "Fleet Grid start attempted";
            try {
                fleet_size = Integer.parseInt(result[1]);
                BattleGameList bgl = new BattleGameList();
                BattleGame bg = bgl.getBattleGameBySessionID(sessionID);
                PlayerList pl = new PlayerList();
                Player p = pl.getPlayerBySession(sessionID);
                status = bg.addGridToPlayer(p, fleet_size);
                if (status == bg.BG_STATUS_GRID_ADDED) reply = "Fleet Grid started";
            } catch (NumberFormatException e) {
            }
        } else if (command.startsWith(bc.BC_SEND_SHIP)) {
            String[] result = pat.split(command);
            int status;
            reply = "Ship add to Fleet attempted";
            BattleGameList bgl = new BattleGameList();
            BattleGame bg = bgl.getBattleGameBySessionID(sessionID);
            PlayerList pl = new PlayerList();
            Player p = pl.getPlayerBySession(sessionID);
            status = bg.addShipToPlayer(p, result[2]);
            if (status == BattleGrid.BGRD_SHIP_ADDED) reply = "Ship added."; else if (status == BattleGrid.BGRD_FLEET_FULL) reply = "Ship added. Fleet Full";
        } else if (command.startsWith(bc.BC_DONE_FLEET)) {
            BattleGameList bgl = new BattleGameList();
            BattleGame bg = bgl.getBattleGameBySessionID(sessionID);
            PlayerList pl = new PlayerList();
            Player p = pl.getPlayerBySession(sessionID);
            if (bg.isPlayerGridFilled(p)) reply = "Got the fleet"; else reply = "ERROR: fleet check says incomplete";
            if (bg.areAllPlayerGridsFull()) {
                int[] session_list = bg.getSessionIDs();
                for (int i = 0; i < session_list.length; i++) {
                    sendUnsolicitedMessage(session_list[i], bc.BC_ALL_FLEETS);
                }
                sendUnsolicitedMessage(session_list[0], bc.BC_YOUR_TURN + bg.getOpponent());
            }
        } else if (command.startsWith(bc.BC_FIND_OPPONENTS)) {
            String[] result = pat.split(command);
            int game_type = 0;
            int status = -1;
            if (result.length != 2) {
                System.out.println("FIND_OPPONENTS: Error Invalid parameter count: " + result.length);
                reply = "FIND_OPPONENTS: Error Invalid parameter count: " + result.length;
            } else {
                try {
                    game_type = Integer.parseInt(result[1]);
                    BattleGameList bgl = new BattleGameList();
                    BattleGame bg = null;
                    status = bgl.requestGame(sessionID, game_type);
                    if (status == BattleGame.BG_STATUS_NOADD) {
                        reply = "Error: unable to add you wait list or game";
                    } else if (status == BattleGame.BG_STATUS_AWAIT) {
                        bg = bgl.getBattleGameBySessionID(sessionID);
                        reply = bc.BC_AWAITING + " For more players for game #" + bg.getGameID() + " need " + bg.getAwaiting() + " more players";
                        int[] session_list = bg.getSessionIDs();
                        for (int i = 0; i < session_list.length; i++) {
                            sendUnsolicitedMessage(session_list[i], reply);
                        }
                    } else if (status == BattleGame.BG_STATUS_STARTED) {
                        bg = bgl.getBattleGameBySessionID(sessionID);
                        if (bg == null) {
                            System.out.println("ERROR: GAME_STARTED but bg=null for sessionID" + sessionID);
                            reply = "ERROR: GAME_STARTED but bg=null for sessionID" + sessionID;
                        } else {
                            reply = "GAME_STARTED: For game #" + bg.getGameID();
                        }
                        String message = bc.BC_START_GAME_TYPE + bg.getGameType();
                        int[] session_list = bg.getSessionIDs();
                        for (int i = 0; i < session_list.length; i++) {
                            sendUnsolicitedMessage(session_list[i], message);
                        }
                        message = bc.BC_OPPONENT_LIST + bg.getPlayerIDsString();
                        for (int i = 0; i < session_list.length; i++) {
                            sendUnsolicitedMessage(session_list[i], message);
                        }
                        message = bc.BC_OPPONENT_NAME_LIST + bg.getPlayerHandlesString();
                        for (int i = 0; i < session_list.length; i++) {
                            sendUnsolicitedMessage(session_list[i], message);
                        }
                    } else {
                        reply = "ERROR: in FIND_OPPONENTS status=" + status;
                    }
                } catch (NumberFormatException e) {
                    reply = "ERROR: bad number for game type";
                }
            }
        } else if (command.startsWith(bc.BC_CHAT_ALL)) {
            String[] result = pat.split(command);
            if (result.length != 2) reply = "CHAT_ALL: Error Invalid parameter count on Chat Command: " + result.length; else {
                BattleGameList bgl = new BattleGameList();
                BattleGame bg = null;
                PlayerList pl = new PlayerList();
                Player p = pl.getPlayerBySession(sessionID);
                bg = bgl.getBattleGameBySessionID(sessionID);
                if (bg != null) {
                    int[] session_list = bg.getSessionIDs();
                    for (int i = 0; i < session_list.length; i++) {
                        if (session_list[i] != sessionID) sendUnsolicitedMessage(session_list[i], bc.BC_CHAT + p.getHandle() + ":" + result[1]);
                    }
                    reply = "CHAT_ALL: sent...";
                } else {
                    reply = "CHAT_ALL: Error bg = null";
                }
            }
        } else if (command.startsWith(bc.BC_CHAT)) {
            int recipient_session = 0;
            PlayerList pl = new PlayerList();
            Player p = pl.getPlayerBySession(sessionID);
            String[] result = pat.split(command);
            if (result.length != 3) reply = "CHAT: Error Invalid parameter count on Chat Command: " + result.length; else {
                try {
                    recipient_session = Integer.parseInt(result[1]);
                    reply = sendUnsolicitedMessage(recipient_session, bc.BC_CHAT + p.getHandle() + ":" + result[2]);
                } catch (NumberFormatException e) {
                }
            }
        } else if (command.startsWith("UNSOLICITED")) {
            String message;
            message = "Message to session(" + sessionID + ") on channel 2";
            reply = sendUnsolicitedMessage(sessionID, message);
            System.out.println(message + " ...reply=" + reply);
        }
        return reply;
    }

    protected static String sendUnsolicitedMessage(int session_id, String message) {
        PrintWriter out;
        BufferedReader in;
        String reply = "ERROR:";
        Socket temp_socket = ServeOnePlayer.getSocket2(session_id);
        if (temp_socket == null) {
            System.out.println("Socket2 is null");
            reply = "Error: Socket2 is null";
        } else {
            try {
                in = new BufferedReader(new InputStreamReader(temp_socket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(temp_socket.getOutputStream())), true);
                out.println(message);
                reply = in.readLine();
            } catch (IOException e) {
                reply = "Error: IOException";
            }
        }
        return reply;
    }

    public static void main(String[] args) {
        PlayerList pl = new PlayerList();
        Player p;
        int dummy_session = 105;
        String reply = processCommand(dummy_session, "LOGIN:dave@diano.com:battle:diano");
        System.out.println(reply);
        reply = processCommand(dummy_session, "LOGIN:dave@diano.com:battleBAD:diano");
        System.out.println(reply);
        reply = processCommand(dummy_session, "LOGIN:dave@diano.com:battle:dianoHAND");
        System.out.println(reply);
        p = pl.getPlayer("dave@diano.com");
        System.out.println(p.toString());
    }
}
