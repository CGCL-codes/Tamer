package edu.sfsu.powerrangers.jeopardy;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ContestantClient extends GeneralClient, Remote {

    public void waitingForClueSelection() throws RemoteException;

    public void waitingForWager(String category) throws RemoteException;

    public void waitingForResponse(String category, String clue) throws RemoteException;

    public void waitingForBuzzIn(String category, String clue) throws RemoteException;

    public void waitingForNothing() throws RemoteException;
}
