package yati.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import yati.data.Config;
import yati.data.Message;
import yati.game.DummyGame;

public class SimpleServer {

    private ServerSocket server;

    private ExecutorService pool;

    private static SimpleServer instance;

    private static int nextid;

    private HashMap<Integer, DummyGame> games = new HashMap<Integer, DummyGame>();

    private ArrayList<Message> messages = new ArrayList<Message>();

    private HashMap<Integer, String> playernames = new HashMap<Integer, String>();

    private int readyCount;

    private ArrayList<Handler> handler = new ArrayList<Handler>();

    private boolean start = false;

    public static SimpleServer getInstance(int listen_port) {
        if (instance == null) {
            instance = new SimpleServer(listen_port);
        }
        return instance;
    }

    private SimpleServer(int listen_port) {
        try {
            server = new ServerSocket(listen_port);
            pool = Executors.newCachedThreadPool();
            for (; ; ) {
                Thread.sleep(100);
                Handler h = new Handler(server.accept());
                handler.add(h);
                pool.execute(h);
                retransmitNames();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void retransmitNames() {
        for (Handler han : handler) {
            han.setTransmitnames();
        }
    }

    private static int getNextId() {
        System.out.println("Server: Spieler connected. id=" + (nextid + 1));
        return ++nextid;
    }

    private class Handler implements Runnable {

        private int id;

        private NetworkUtility netUtil;

        private String playername;

        private boolean transmitnames = true, started = false;

        public Handler(Socket client) {
            this.netUtil = new NetworkUtility(client);
            this.id = SimpleServer.getNextId();
            this.netUtil.writeInt(id);
            this.playername = netUtil.readMessage();
            playernames.put(id, playername);
            games.put(this.id, new DummyGame());
        }

        public void setTransmitnames() {
            this.transmitnames = true;
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    Thread.sleep(Config.network_thread_delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList<Message> copy = new ArrayList<Message>(messages);
                for (Message m : copy) {
                    if (m.getClientsLength().equals(nextid)) messages.remove(m);
                    if (!m.checkId(id)) {
                        netUtil.writeInt(0);
                        netUtil.writeMessage(m.getMessage());
                        m.addClient(id);
                    }
                }
                if (transmitnames) {
                    netUtil.writeInt(1);
                    for (String p : playernames.values()) {
                        netUtil.writeInt(2);
                        netUtil.writeMessage(p);
                    }
                    netUtil.writeInt(3);
                    transmitnames = false;
                }
                if (!started && start) {
                    netUtil.writeInt(5);
                    started = true;
                }
                if (!netUtil.dataAvailable()) continue;
                System.out.println("Server: Daten vorhanden...");
                switch(netUtil.readInt()) {
                    case 0:
                        String nachricht = netUtil.readMessage();
                        messages.add(new Message(nachricht));
                        System.out.println("Server: Recieving message. message=\"" + nachricht + "\"");
                        break;
                    case 4:
                        ++readyCount;
                        playernames.remove(id);
                        playernames.put(id, playername + "(ready)");
                        retransmitNames();
                        if (readyCount == nextid) start = true;
                        break;
                }
            }
        }
    }
}
