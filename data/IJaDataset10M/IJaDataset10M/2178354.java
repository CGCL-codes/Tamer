package net.sourceforge.simplegamenet.framework.transport;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import net.sourceforge.simplegamenet.framework.model.ChatMessage;
import net.sourceforge.simplegamenet.framework.model.ClientEngineImpl;
import net.sourceforge.simplegamenet.specs.to.GameSettings;
import net.sourceforge.simplegamenet.specs.to.PlayerSettings;

public class ClientModem extends Modem implements PacketCodes {

    private ClientEngineImpl clientEngine;

    private ClientConnection clientConnection;

    private boolean active = true;

    public ClientModem(ClientEngineImpl clientEngine, Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        taskQueue = new TaskQueue(true);
        this.clientEngine = clientEngine;
        clientConnection = new ClientConnection(this, socket, objectInputStream, objectOutputStream);
        taskQueue.start();
        clientConnection.start();
    }

    public void processPacketInEventThread(Packet packet) {
        taskQueue.addTask(new ClientPacketTask(this, packet));
    }

    public void processPacket(Packet packet) {
        switch(packet.getPacketCode()) {
            case CON_SERVER_PONG:
                break;
            case CON_SERVER_PING:
                break;
            case E_CHAT_MESSAGE:
                break;
            case CHAT_GRANT_MESSAGE:
                if (!(packet.getData() instanceof ChatMessage)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.receiveChatMessage((ChatMessage) packet.getData());
                break;
            case E_GAME_CHANGE_GAME_SETTINGS_GAME_IN_PROGRESS:
                break;
            case E_GAME_CHANGE_GAME_SETTINGS_NOT_ACCEPTED:
                break;
            case GAME_UPDATE_GAME_SETTINGS:
                if (!(packet.getData() instanceof GameSettings)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.updateGameSettings((GameSettings) packet.getData());
                break;
            case CON_ADD_PLAYER:
                if (!(packet.getData() instanceof PlayerSettings)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.addPlayer((PlayerSettings) packet.getData());
                break;
            case E_GAME_CHANGE_PLAYER_SETTINGS_GAME_IN_PROGRESS:
                break;
            case E_GAME_CHANGE_PLAYER_SETTINGS_NOT_ACCEPTED:
                break;
            case GAME_UPDATE_PLAYER_SETTINGS:
                if (!(packet.getData() instanceof PlayerSettings)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.updatePlayerSettings((PlayerSettings) packet.getData());
                break;
            case CON_REMOVE_PLAYER_BY_LEFT:
                if (!(packet.getData() instanceof Integer)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.removePlayer((Integer) packet.getData(), RemovePlayerType.LEFT);
                break;
            case CON_KICK:
                quitJoinedGame(RemovePlayerType.KICKED);
                break;
            case CON_REMOVE_PLAYER_BY_KICK:
                if (!(packet.getData() instanceof Integer)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.removePlayer((Integer) packet.getData(), RemovePlayerType.KICKED);
                break;
            case CON_BAN:
                quitJoinedGame(RemovePlayerType.BANNED);
                break;
            case CON_REMOVE_PLAYER_BY_BAN:
                if (!(packet.getData() instanceof Integer)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.removePlayer((Integer) packet.getData(), RemovePlayerType.BANNED);
                break;
            case CON_REMOVE_PLAYER_BY_DISCONNECTION:
                if (!(packet.getData() instanceof Integer)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.removePlayer((Integer) packet.getData(), RemovePlayerType.DISCONNECTED);
                break;
            case CON_REMOVE_PLAYER_BY_CLIENT_VIOLATION:
                if (!(packet.getData() instanceof Integer)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.removePlayer((Integer) packet.getData(), RemovePlayerType.PROTOCOL_VIOLATION_KICKED);
                break;
            case CON_REMOVE_PLAYER_BY_SERVER_VIOLATION:
                if (!(packet.getData() instanceof Integer)) {
                    leaveProtocolViolatingServer();
                    return;
                }
                clientEngine.removePlayer((Integer) packet.getData(), RemovePlayerType.PROTOCOL_VIOLATION_LEFT);
                break;
            case CON_SERVER_CON_CLOSE:
                quitJoinedGame(RemovePlayerType.HOSTING_STOPPED);
                break;
            case E_GAME_CLIENT_START_GAME:
                break;
            case GAME_SERVER_GAME_STARTED:
                clientEngine.gameStarted();
                break;
            case GAME_SERVER_SEND_DATA:
                clientEngine.receiveData(packet.getData());
                break;
            case E_GAME_CLIENT_ABORT_GAME:
                break;
            case GAME_SERVER_GAME_STOPPING:
                clientEngine.gameStopping();
                clientConnection.sendPacket(new Packet(GAME_CLIENT_CONFIRM_GAME_STOPPING, null));
                break;
            case GAME_SERVER_GAME_ABORTING:
                clientEngine.gameAborting();
                clientConnection.sendPacket(new Packet(GAME_CLIENT_CONFIRM_GAME_ABORTING, null));
                break;
            case GAME_SERVER_GAME_ENDED:
                clientEngine.gameEnded();
                break;
            case E_CON_CLIENT_VIOLATION:
                quitJoinedGame(RemovePlayerType.HOSTING_STOPPED);
                break;
            default:
                leaveProtocolViolatingServer();
        }
    }

    public void sendChatMessage(ChatMessage chatMessage) {
        clientConnection.sendPacket(new Packet(CHAT_ASK_MESSAGE, chatMessage));
    }

    public void changeGameSettings(GameSettings gameSettings) {
        clientConnection.sendPacket(new Packet(GAME_CHANGE_GAME_SETTINGS, gameSettings));
    }

    public void changePlayerSettings(PlayerSettings playerSettings) {
        clientConnection.sendPacket(new Packet(GAME_CHANGE_PLAYER_SETTINGS, playerSettings));
    }

    public void startGame() {
        clientConnection.sendPacket(new Packet(GAME_CLIENT_START_GAME, null));
    }

    public void sendData(Serializable data) {
        clientConnection.sendPacket(new Packet(GAME_CLIENT_SEND_DATA, data));
    }

    public void abortGame() {
        clientConnection.sendPacket(new Packet(GAME_CLIENT_ABORT_GAME, null));
    }

    public void leaveServer() {
        clientConnection.sendPacket(new Packet(CON_CLIENT_CON_CLOSE, null));
        quitJoinedGame(RemovePlayerType.LEFT);
    }

    public void leaveProtocolViolatingServer() {
        clientConnection.sendPacket(new Packet(E_CON_SERVER_VIOLATION, null));
        quitJoinedGame(RemovePlayerType.PROTOCOL_VIOLATION_LEFT);
    }

    public void processDisconnectionInEventThread() {
        taskQueue.addTask(new ClientDisconnectionTask(this));
    }

    public void processDisconnection() {
        quitJoinedGame(RemovePlayerType.DISCONNECTED);
    }

    private void quitJoinedGame(int removePlayerType) {
        if (active) {
            taskQueue.close();
            clientConnection.close();
            clientEngine.joinedGameQuit(removePlayerType);
            active = false;
        }
    }
}
