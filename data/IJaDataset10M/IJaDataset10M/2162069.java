package de.qnerd.rpgBot.renewed;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import de.qnerd.rpgBot.util.Config;

public class IRC_Bot extends PircBot {

    private GameController gameController;

    /**
	 * init Function
	 * setzt den Namen des Bots
	 *
	 */
    public IRC_Bot() {
        this.setName(Config.getConfig("botname"));
        this.gameController = new GameController(this);
    }

    /**
	 *
	 * @param sender
	 * @return
	 */
    public boolean checkOP(String sender) {
        boolean out = false;
        User[] uList = getUsers(Config.getConfig("channel"));
        User usender = null;
        for (int i = 0; i < uList.length; i++) {
            if (uList[i].getNick().equals(sender)) usender = uList[i];
        }
        if (usender != null && usender.isOp()) out = true;
        return out;
    }

    public void onNickChange(String oldNick, String login, String hostname, String newNick) {
        gameController.changePlayerName(oldNick, newNick);
    }

    public void onPart(String channel, String sender, String login, String hostname) {
        gameController.removePlayer(sender);
    }

    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        gameController.removePlayer(sourceNick);
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        gameController.newMsg(sender, login, hostname, message);
    }
}
