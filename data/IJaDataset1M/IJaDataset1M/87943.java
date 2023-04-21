package emtigi.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * @author Thomas Kamps
 *
 */
class ClientCommunicator extends Thread {

    String answer = null;

    public ClientCommunicator() {
    }

    public void run() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(9999, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket connection = socket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = input.readLine();
                if (line == null) continue;
                String[] infos = line.split(":");
                Player p = new Player(infos[0], Integer.parseInt(infos[1]), connection, Game.getInGame(), Game.getStack());
                Game.addPlayer(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Asks a Player to call a ManaColor
	 * @param p The Player to ask
	 * @param allowedColors The ManaColors that are allowed to answer
	 * @return The answered ManaColor
	 */
    public synchronized ManaColor askPlayerForManaColor(Player p, HashSet<ManaColor> allowedColors) {
        String request = "QST:ManaColor:";
        for (ManaColor c : ManaColor.values()) if (allowedColors.contains(c)) request = request + ManaColor.getChar(c);
        p.send(request);
        try {
            wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert (answer != null);
        String ret = answer;
        answer = null;
        return ManaColor.getFromChar(ret);
    }

    /**
	 * Gets an answer and notify the current waiting thread;
	 * @param answer
	 */
    public synchronized void answer(String answer) {
        this.answer = answer;
        notify();
    }
}
