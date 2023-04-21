package it.unibz.izock.networking;

import it.unibz.izock.exceptions.GameJoinFailedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The Class GameableImpl is a concrete implementation of a Gameable
 * class for one specific User.
 */
public class GameableImpl extends UnicastRemoteObject implements Gameable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 34716471899292318L;

    /** The _user. */
    private User _user;

    /** The _watten. */
    private NetworkWatten _watten;

    /**
	 * Instantiates a new gameable impl.
	 * 
	 * @param user the user
	 * @param watten the watten
	 * 
	 * @throws RemoteException the remote exception
	 */
    protected GameableImpl(User user, NetworkWatten watten) throws RemoteException {
        super();
        _user = user;
        _watten = watten;
    }

    public int getJoinedUsersCount() throws RemoteException {
        return _watten.getJoinedUsersCount();
    }

    public int getMaximumJoinedUserAmount() throws RemoteException {
        return _watten.getMaximumJoinedUserAmount();
    }

    public String getName() throws RemoteException {
        return _watten.getName();
    }

    public boolean isInProgress() throws RemoteException {
        return _watten.isInProgress();
    }

    public boolean isOpen() throws RemoteException {
        return _watten.isOpen();
    }

    public void join(GameClient client) throws RemoteException, GameJoinFailedException {
        _watten.join(_user, client);
    }

    public void giveUp() throws RemoteException {
        _watten.giveUp(_user);
    }
}
