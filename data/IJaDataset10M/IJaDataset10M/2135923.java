package se.mushroomwars.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import se.mushroomwars.gameplay.DrawingListener;
import se.mushroomwars.network.protocol.Protocol;

public class ClientInterface implements DataHandleListener {

    private final List<DrawingListener> drawingListeners;

    private final Runnable thread;

    private final Thread t1;

    private Client client;

    public ClientInterface() {
        drawingListeners = new ArrayList<DrawingListener>();
        thread = new Client("192.168.0.11", 50034);
        client = (Client) thread;
        client.addListener(this);
        t1 = new Thread(thread, "client");
        t1.start();
    }

    public void addDrawingListener(final DrawingListener listener) {
        if (!drawingListeners.contains(listener)) drawingListeners.add(listener);
    }

    public void notifyDrawingListener() {
        for (Iterator<DrawingListener> iterator = drawingListeners.iterator(); iterator.hasNext(); ) {
            iterator.next().notifyDrawingChanged();
        }
    }

    public static void main(String argv[]) throws IOException {
        String input;
        final ClientInterface network = new ClientInterface();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            input = in.readLine();
            if (input.length() > 6 && input.substring(0, 6).equals("/kick ")) network.client.sendToServer(input.substring(6), Protocol.KICK_PLAYER); else if (input.length() > 6 && input.substring(0, 6).equals("/send ")) network.client.sendToServer(input.substring(6), Integer.parseInt(in.readLine())); else network.client.sendToServer(input, Protocol.LOBBY_MESSAGE);
        }
    }

    public void notifyDataToHandle(SocketChannel socket, Object dataPackage, int packageID) {
        switch(packageID) {
            case Protocol.LOBBY_MESSAGE:
                {
                    String data = (String) dataPackage;
                    System.out.println(data);
                    break;
                }
            case Protocol.LOBBY:
                {
                    ArrayList<String> data = (ArrayList<String>) dataPackage;
                    System.out.println("===LOBBY===");
                    for (Iterator<String> iterator = data.iterator(); iterator.hasNext(); ) System.out.println(iterator.next());
                    break;
                }
            case Protocol.PLAYER_DISCONNECTED:
                {
                    Integer data = (Integer) dataPackage;
                    break;
                }
            default:
                System.out.println("Unknown package received. (PackageID: " + packageID + " from server");
        }
    }

    @Override
    public void notifyPlayerDisconnected(SocketChannel socket) {
    }

    @Override
    public void notifyPlayerConnected(SocketChannel socket, int playerID) {
    }
}
