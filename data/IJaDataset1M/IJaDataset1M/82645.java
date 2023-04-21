package com.techjedi.dragonbot.chessclub;

import com.techjedi.dragonbot.Connection;
import com.techjedi.dragonbot.ManagerType;

/**
 * Provides player interaction via a connection to chessclub.com
 * 
 * @author Doug Bateman
 */
public class ChessclubConnection implements Connection {

    /**
	 * @see com.techjedi.dragonbot.Connection#tellPlayer(java.lang.String,
	 *      java.lang.String)
	 */
    public void tellPlayer(String player, String msg) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#tellPlayers(java.lang.String,
	 *      com.techjedi.dragonbot.ManagerType)
	 */
    public void tellPlayers(String msg, ManagerType lvl) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#alertPlayer(java.lang.String,
	 *      java.lang.String)
	 */
    public void alertPlayer(String player, String msg) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#addUser(java.lang.String)
	 */
    public void addUser(String player) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#removeUser(java.lang.String)
	 */
    public void removeUser(String player) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#addPowerUser(java.lang.String)
	 */
    public void addPowerUser(String player) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#removePowerUser(java.lang.String)
	 */
    public void removePowerUser(String player) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#sendServerCommand(java.lang.String)
	 */
    public void sendServerCommand(String cmd) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#setPaused(boolean)
	 */
    public void setPaused(boolean paused) {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#connect()
	 */
    public void connect() {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#disconnect()
	 */
    public void disconnect() {
    }

    /**
	 * @see com.techjedi.dragonbot.Connection#isInputAvailable()
	 */
    public boolean isInputAvailable() {
        return false;
    }
}
