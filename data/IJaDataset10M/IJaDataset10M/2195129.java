package net.peddn.typebattle.lib.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ClientService extends Remote {

    void setId(UUID id) throws RemoteException;

    UUID getId() throws RemoteException;

    void setClientVersion(String version) throws RemoteException;

    String getClientVersion() throws RemoteException;

    void setChatChannel(short chatChannel) throws RemoteException;

    short getChatChannel() throws RemoteException;

    void receiveChatMessage(ChatMessage message) throws RemoteException;

    void isReachable() throws RemoteException;
}
