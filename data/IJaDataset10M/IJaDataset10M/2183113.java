package br.furb.inf.tcc.tankcoders.message;

import br.furb.inf.tcc.server.OnlinePlayer;

/**
 * Message sent by game server.
 * @author Germano Fronza
 */
public class UserLogonRespose extends UserLogon {

    /**
	 * Players already online.
	 */
    private OnlinePlayer[] onlinePlayers;

    public OnlinePlayer[] getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(OnlinePlayer[] onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }
}
