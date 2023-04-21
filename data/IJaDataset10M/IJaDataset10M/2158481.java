package vpm.server.controller;

import vpm.server.model.NetworkThread;
import vpm.server.view.ViewEvent;
import vpm.server.view.ViewEventListener;

public class NetworkController implements ViewEventListener {

    NetworkThread networkThread;

    public NetworkController(NetworkThread network) {
        this.networkThread = network;
    }

    @Override
    public void advertisement(ViewEvent event) {
        switch(event.getType()) {
            case STARTSERVER:
                if (!networkThread.isAlive()) networkThread.start();
                System.out.println("Server starts ..");
                break;
            default:
                break;
        }
    }
}
