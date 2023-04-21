package networksecuritygame;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class GameServer extends JFrame implements Runnable {

    protected final int DEFAULT_PORT = 6789;

    protected ServerSocket ListenSocket;

    protected ClientThread Attacker, Defender;

    /** Creates new form GameServer */
    public GameServer() {
        try {
            ListenSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        new Thread(this).start();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void run() {
        BufferedReader ClientInput;
        String ClientMessage;
        try {
            while (true) {
                Socket Client = ListenSocket.accept();
                new ClientThread(Client);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void processMessage(NetworkMessage msg) {
        if (msg.Destination.equals("Attacker") && Attacker != null) {
            Attacker.sendMessage(msg);
        } else if (msg.Destination.equals("Defender") && Defender != null) {
            Defender.sendMessage(msg);
        }
    }

    protected void registerAttacker(ClientThread ct) {
        Attacker = ct;
    }

    protected void registerDefender(ClientThread ct) {
        Defender = ct;
    }

    /**
   * @param args the command line arguments
   */
    public static void main(String args[]) {
        new GameServer();
    }

    protected class ClientThread extends Thread {

        protected Socket Client;

        protected ObjectOutputStream out;

        protected ObjectInputStream in;

        public ClientThread(Socket client) {
            Client = client;
            try {
                out = new ObjectOutputStream(Client.getOutputStream());
                in = new ObjectInputStream(Client.getInputStream());
                NetworkMessage CheckIn = (NetworkMessage) in.readObject();
                if (CheckIn.getSource().equals("Attacker")) {
                    registerAttacker(this);
                } else {
                    registerDefender(this);
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            start();
        }

        public void sendMessage(NetworkMessage msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            NetworkMessage ClientMessage;
            while (true) {
                try {
                    ClientMessage = (NetworkMessage) in.readObject();
                    System.out.println("msg received");
                    processMessage(ClientMessage);
                } catch (EOFException ex) {
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
