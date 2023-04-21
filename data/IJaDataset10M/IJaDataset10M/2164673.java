package chatteroll.server;

import chatteroll.CRServerCommand;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Vector;
import java.util.LinkedList;
import chatteroll.CRMessage;
import chatteroll.CRRoomDesc;

/**
 *
 * @author Dan Lazzari Jr
 */
public class CRClientManager implements Runnable {

    private Socket client;

    private BufferedReader in;

    private BufferedWriter out;

    private boolean is_alive = true;

    private CRServer server;

    private CRRoom room;

    /**
     * The username of the client being managed, needs to be set before
     * other operations are allowed.
     */
    private String username = "";

    /**
     * Creates a new CRClientManager that manages the client on the given socket
     *
     * @param s The socket to the client to be managed
     * @param server The server that contains room info, etc
     */
    public CRClientManager(Socket s, CRServer server) {
        client = s;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (Exception e) {
            System.out.println("Server: Error creating client socket streams.");
            System.out.println(e.toString());
            return;
        }
        Thread thread = new Thread(this);
        thread.start();
        System.out.println("Server: Client connected successfully");
    }

    public void run() {
        while (is_alive) {
            try {
                CRMessage mesg = getNextMessage();
                if (mesg != null) {
                    if (username == null || username.equals(mesg.origin)) {
                        processMessage(mesg);
                    }
                }
            } catch (Exception e) {
                System.out.println("Server: " + e.toString());
                e.printStackTrace();
            }
        }
        try {
            client.close();
        } catch (Exception e) {
        }
        server.removeClient(this);
    }

    /**
     * Sends a message to the client managed by this object
     *
     * @param crm The message to be sent
     */
    public synchronized void sendMessage(CRMessage crm) {
        if (chatteroll.Debug.DEBUG) {
            System.out.println("Server: Sending a message");
        }
        if (!client.isConnected()) {
            System.out.println("Can't send message. Not connected to a client!");
            return;
        }
        try {
            crm.writeToStream(out);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Checks the input stream for a complete message and returns it
     *
     * @return The next complete message in the input stream or null if there
     * isn't one
     */
    private CRMessage getNextMessage() throws java.io.IOException {
        synchronized (in) {
            try {
                return CRMessage.fromReader(in);
            } catch (Exception e) {
                try {
                    in.mark(1);
                    in.read();
                    in.reset();
                } catch (Exception socket_dead) {
                    this.dispose();
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Processes a message.
     * TODO This function is a mess. It needs to be cleaned up.
     */
    private void processMessage(CRMessage mesg) {
        if (chatteroll.Debug.DEBUG) {
            System.out.println("Server: Processing a message");
        }
        if (mesg.dest.get(0).equals("SERVER")) {
            Object tmp_obj = mesg.getMessageData("ChatteRoll", 1.0f).get(0);
            if (tmp_obj == null) {
                System.out.println("Recieved malformed server command.");
            }
            CRServerCommand smesg = (CRServerCommand) tmp_obj;
            if (smesg.command.equals("USERNAME")) {
                if (smesg.data.get(0).equals("SERVER") || smesg.data.get(0).equals("ALL")) {
                    sendErrorMessage(smesg.command, "Reserved Username");
                    return;
                }
                CRClientManager tmp = server.getUser(smesg.data.get(0));
                if (tmp != null) {
                    sendErrorMessage(smesg.command, "User already exists");
                    return;
                }
                username = smesg.data.get(0);
                CRServerCommand reply = new CRServerCommand("USERNAME", username);
                CRMessage reply_msg = new CRMessage();
                reply_msg.addMessageData("ChatteRoll", 1.0f, reply);
                reply_msg.origin = "SERVER";
                sendMessage(reply_msg);
                return;
            }
            if (smesg.command.equals("ROOMLIST")) {
                if (username.equals("")) {
                    sendErrorMessage(smesg.command, "Username not set");
                    return;
                }
                Vector<CRRoomDesc> rooms = server.getRooms();
                CRServerCommand reply = new CRServerCommand("ROOMLIST");
                CRMessage reply_msg = new CRMessage();
                reply_msg.addMessageData("ChatteRoll", 1.0f, reply);
                LinkedList<CRRoomDesc> tmp_rooms = new LinkedList(rooms);
                reply_msg.addMessageData("ChatteRoll", 1.0f, tmp_rooms, CRRoomDesc.class);
                reply_msg.origin = "SERVER";
                sendMessage(reply_msg);
                return;
            }
            if (smesg.command.equals("CREATEROOM")) {
                if (username.equals("")) {
                    sendErrorMessage(smesg.command, "Username not set");
                    return;
                }
                if (server.getRoom(smesg.data.get(0)) != null) {
                    sendErrorMessage(smesg.command, "Room already exists");
                    return;
                }
                if (smesg.data.size() > 1) room = server.createRoom(smesg.data.get(0), smesg.data.get(1), this); else room = server.createRoom(smesg.data.get(0), null, this);
                CRServerCommand reply = new CRServerCommand("CREATEROOM", smesg.data.get(0));
                CRMessage reply_msg = reply.createMessage();
                reply_msg.origin = "SERVER";
                sendMessage(reply_msg);
                return;
            }
            if (smesg.command.equals("JOINROOM")) {
                if (username.equals("")) {
                    sendErrorMessage(smesg.command, "Username not set");
                    return;
                }
                room = server.getRoom(smesg.data.get(0));
                if (room == null) {
                    sendErrorMessage(smesg.command, "Room does not exist");
                    return;
                }
                boolean result;
                if (smesg.data.size() > 1) result = room.addPlayer(this, smesg.data.get(1)); else result = room.addPlayer(this, null);
                if (!result) sendErrorMessage(smesg.command, "Incorrect Password");
                return;
            } else if (smesg.command.equals("ROOMINFO")) {
                if (username.equals("")) {
                    sendErrorMessage(smesg.command, "Username not set");
                    return;
                }
                if (room == null) {
                    sendErrorMessage(smesg.command, "Not in a room");
                    return;
                }
                CRServerCommand command = new CRServerCommand("ROOMINFO", room.getGM());
                command.data.add(room.getName());
                CRMessage reply = command.createMessage();
                reply.origin = "SERVER";
                sendMessage(reply);
                return;
            } else if (smesg.command.equals("PLAYERLIST")) {
                if (username.equals("")) {
                    sendErrorMessage(smesg.command, "Username not set");
                    return;
                }
                if (room == null) {
                    sendErrorMessage(smesg.command, "Not in a room");
                    return;
                }
                CRMessage reply = (new CRServerCommand("PLAYERLIST", new LinkedList<String>(room.getPlayers()))).createMessage();
                reply.origin = "SERVER";
                sendMessage(reply);
                return;
            } else if (smesg.command.equals("ROOMKICK")) {
            }
        }
        if (room != null) {
            room.processMessage(mesg);
        } else {
            sendErrorMessage("", "Could not process message, Not in a room.");
        }
    }

    public void sendErrorMessage(String command, String error) {
        CRServerCommand tmp = new CRServerCommand("ERROR", command);
        tmp.data.add(error);
        CRMessage msg = tmp.createMessage();
        msg.origin = "SERVER";
        msg.addMessageData("ChatteRoll", 1.0f, tmp);
        sendMessage(msg);
    }

    public CRRoom getRoom() {
        return room;
    }

    public String getUsername() {
        return username;
    }

    public void roomKick() {
        room = null;
        CRServerCommand command = new CRServerCommand("ROOMKICK", "GM Left the Room");
        CRMessage mesg = command.createMessage();
        mesg.origin = "SERVER";
        mesg.dest.add(username);
        sendMessage(mesg);
    }

    public void dispose() {
        is_alive = false;
    }
}
