package com.rlsoftwares.virtualdeck.network.messages;

import com.rlsoftwares.network.Message;
import com.rlsoftwares.virtualdeck.Game;

/**
 *
 * @author Rodrigo
 */
public class MessageCreateRoom extends Message {

    private boolean passwordProtected;

    private String password;

    private Game gameData;

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Game getGameData() {
        return gameData;
    }

    public void setGameData(Game gameData) {
        this.gameData = gameData;
    }
}
