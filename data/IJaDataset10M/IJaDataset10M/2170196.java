package de.hft_stuttgart.robofight.client.java;

import de.hft_stuttgart.botwar.common.interfaces.IRemotePlayer;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ralf.dauenhauer@gmx.de, michael.rieker@web.de
 */
public class JLobby {

    private JPlayer jPlayer;

    /**
     * creates a new lobby object.
     * @param jPlayer needs a JPlayer as param
     */
    public JLobby(final JPlayer jPlayer) {
        this.jPlayer = jPlayer;
    }

    /**
     * Do nothing.
     */
    public JLobby() {
    }

    /**
     * Player enters a Game and sets the gameId
     * @param gameId needs the current id of a game
     * @return boolean
     */
    public final boolean enterGame(final String gameId) {
        try {
            this.jPlayer.setGameID(Integer.parseInt(gameId));
            return this.jPlayer.getPlayer().enterGame(Integer.parseInt(gameId));
        } catch (RemoteException ex) {
            Logger.getLogger(JLobby.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Calls remote Objekt sends chat string.
     * @param message needs the message as param
     * @throws java.rmi.RemoteException throws a RemoteException
     */
    public final void sendChatMsg(final String message) throws RemoteException {
        jPlayer.getPlayer().sendChatMsg(IRemotePlayer.CHATTYPE.LOBBY, message, "");
    }
}
