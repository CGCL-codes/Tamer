package ru.nsu.ccfit.pm.econ.net;

import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.ccfit.pm.econ.common.IGameEventHandler;
import ru.nsu.ccfit.pm.econ.common.controller.clientnet.INetworkEvents;
import ru.nsu.ccfit.pm.econ.common.engine.events.IUGameEvent;
import ru.nsu.ccfit.pm.econ.common.engine.roles.IUPersonDescription;
import ru.nsu.ccfit.pm.econ.common.net.INetworkClient;
import ru.nsu.ccfit.pm.econ.modules.names.InNetworking;
import ru.nsu.ccfit.pm.econ.net.ClientNetworkThread.ClientNetworkJobType;
import ru.nsu.ccfit.pm.econ.net.engine.events.IUKickBanEvent;
import ru.nsu.ccfit.pm.econ.net.engine.events.KickBanEvent;
import com.google.inject.Inject;

/**
 * @author orfest
 * 
 */
public class NetworkClient implements INetworkClient, IGameEventHandler, IClientCoordinator {

    private static Logger logger = LoggerFactory.getLogger(NetworkClient.class);

    private INetworkEvents networkEvents = null;

    private ServerLookuper serverLookuper = null;

    private ClientNetworkThread networkThread = null;

    private IGameEventHandler eventHandler = null;

    public NetworkClient() {
        serverLookuper = new ServerLookuper();
        Thread lookupThread = new Thread(serverLookuper, "ServerLookuper");
        lookupThread.setDaemon(true);
        lookupThread.start();
        networkThread = new ClientNetworkThread(this);
        Thread thread = new Thread(networkThread, "Client Network Thread");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void connect(InetSocketAddress serverAddress, IUPersonDescription playerData) {
        try {
            networkThread.addJob(networkThread.new ClientNetworkJob(ClientNetworkJobType.CONNECT, serverAddress, playerData));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            networkThread.addJob(networkThread.new ClientNetworkJob(ClientNetworkJobType.DISCONNECT));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return networkThread.isConnected();
    }

    @Override
    public void reconnect() {
        try {
            networkThread.addJob(networkThread.new ClientNetworkJob(ClientNetworkJobType.RECONNECT));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(IUGameEvent event) {
        try {
            networkThread.addJob(networkThread.new ClientNetworkJob(ClientNetworkJobType.SEND_EVENT, event));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startServerLookup() {
        serverLookuper.setActive(true);
    }

    @Override
    public void stopServerLookup() {
        serverLookuper.setActive(false);
    }

    @Inject
    public void setNetworkEvents(@InNetworking INetworkEvents events) {
        logger.info("network events set");
        networkEvents = events;
        serverLookuper.setNetworkEvents(networkEvents);
        networkThread.setNetworkEvents(networkEvents);
    }

    @Override
    public void eventArrived(IUGameEvent event) {
        if (event.getClass().equals(KickBanEvent.class)) {
            doBeKickedBanned((IUKickBanEvent) event);
            return;
        }
        eventHandler.handleEvent(event);
    }

    private void doBeKickedBanned(IUKickBanEvent event) {
        boolean isBanned = event.getBanned();
        String reason = event.getReason();
        networkEvents.onKick(reason, isBanned);
    }

    @Inject
    public void setEventHandler(@InNetworking IGameEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
}
