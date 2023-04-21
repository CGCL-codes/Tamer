package com.crappycomic.solarquest.net;

import java.io.*;
import java.net.*;
import java.util.Set;
import com.crappycomic.solarquest.model.*;
import com.crappycomic.solarquest.view.*;

/** Handles communication between the connection and the {@link ClientModel}. */
public class Client {

    public static interface HandshakeObserver {

        void availablePlayers(Set<Integer> availablePlayers);

        void starting();
    }

    private HandshakeObserver observer;

    private View view;

    private ClientModel model;

    private ClientSideConnection connection;

    private boolean started;

    public Client(HandshakeObserver observer) {
        this.observer = observer;
    }

    public void setView(View view) {
        this.view = view;
    }

    void setModel(ClientModel model) {
        model.fixOwnedNodes();
        this.model = model;
        view.setModel(model);
        model.setView(view);
    }

    public void setConnection(ClientSideConnection connection) {
        this.connection = connection;
        new Thread(connection).start();
    }

    void receiveMessage(ViewMessage message) {
        model.forwardMessage(message);
    }

    public void sendObject(Object object) {
        try {
            connection.sendClientObject(object);
        } catch (IOException ioe) {
            System.err.println("Connection dropped: " + ioe.toString());
        }
    }

    public void connectDirect(String host, int port) throws IOException {
        System.out.println("Connecting...");
        setConnection(new DirectClientSideConnection(this, new Socket(host, port)));
        System.out.println("Connected...");
    }

    void receivePlayers(Set<Integer> players) {
        if (started) {
            System.out.println(view);
            System.out.println(players);
            view.setLocalPlayers(players);
        } else observer.availablePlayers(players);
    }

    void starting() {
        if (observer != null) observer.starting();
        started = true;
    }
}
