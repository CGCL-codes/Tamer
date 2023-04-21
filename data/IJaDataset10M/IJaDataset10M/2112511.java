package abricots.net;

import abricots.net.common.AckedFramesManager;
import abricots.net.common.ConnectionProvider;
import abricots.net.common.GameMessage;
import abricots.net.common.GameMessage.CommandsMessage;
import abricots.net.common.GameMessage.EntityMessage;
import abricots.net.common.MessageDispatcher;
import abricots.net.common.PaquetProcessor;
import com.esotericsoftware.kryonet.Connection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author charly
 */
public class ServerConnection extends Connection implements ConnectionProvider {

    private PaquetProcessor paquetProcessor;

    private AckedFramesManager ackedFramesManager;

    private MessageDispatcher messageDispatcher;

    private final Set<EntityMessage> newEntitiesMsg;

    private final Set<CommandsMessage> commandsMessages;

    private int lastValidatedEntityLocalId;

    private long lastReceivedCommandTime;

    private int entityId;

    public ServerConnection(MessageDispatcher dispatcher) {
        paquetProcessor = new PaquetProcessor(this);
        ackedFramesManager = new AckedFramesManager();
        addListener(paquetProcessor);
        this.messageDispatcher = dispatcher;
        lastValidatedEntityLocalId = -1;
        entityId = -1;
        newEntitiesMsg = Collections.synchronizedSet(new HashSet<EntityMessage>());
        commandsMessages = Collections.synchronizedSet(new HashSet<CommandsMessage>());
    }

    @Override
    public int send(Object message) {
        GameMessage gameMessage = paquetProcessor.processOut(message);
        sendUDP(gameMessage);
        return gameMessage.id;
    }

    @Override
    public void onMessageReceived(GameMessage message) {
        messageDispatcher.dispatchMessage(message, this);
    }

    @Override
    public void onConnection(Connection connection) {
    }

    @Override
    public void onDisconnection(Connection connection) {
    }

    public AckedFramesManager getAckedFramesManager() {
        return ackedFramesManager;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void addNewEntity(EntityMessage message) {
        synchronized (newEntitiesMsg) {
            newEntitiesMsg.add(message);
        }
    }

    public Set<EntityMessage> getNewEntities() {
        synchronized (newEntitiesMsg) {
            return new HashSet<EntityMessage>(newEntitiesMsg);
        }
    }

    public void clearNewEntities() {
        synchronized (newEntitiesMsg) {
            newEntitiesMsg.clear();
        }
    }

    public void addCommands(CommandsMessage commandsMessage) {
        if (commandsMessage.toTime <= lastReceivedCommandTime) {
            return;
        }
        synchronized (commandsMessage) {
            commandsMessages.add(commandsMessage);
            lastReceivedCommandTime = commandsMessage.toTime;
        }
    }

    public Set<CommandsMessage> getAndClearCommandsMessages() {
        synchronized (commandsMessages) {
            HashSet<CommandsMessage> cmds = new HashSet<CommandsMessage>(commandsMessages);
            commandsMessages.clear();
            return cmds;
        }
    }

    public int getLastValidatedEntityLocalId() {
        return lastValidatedEntityLocalId;
    }

    public void setLastValidatedEntityLocalId(int lastValidatedEntityLocalId) {
        this.lastValidatedEntityLocalId = lastValidatedEntityLocalId;
    }

    public long getLastReceivedCommandTime() {
        return lastReceivedCommandTime;
    }
}
