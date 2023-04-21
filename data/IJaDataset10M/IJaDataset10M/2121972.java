package net.sf.nodeInsecure.session;

import net.sf.nodeInsecure.dao.player.PlayerDAO;
import net.sf.nodeInsecure.player.Player;
import org.apache.log4j.Logger;
import java.util.Locale;

public class SessionImpl implements Session {

    private Player player;

    private PlayerDAO playerDAO;

    private Logger logger = Logger.getLogger(this.getClass());

    public SessionImpl(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    public Player getPlayer() {
        return player;
    }

    private void setPlayer(Player player) {
        this.player = player;
    }

    public Locale getLocale() {
        if (this.player != null) {
            return player.getLocale();
        } else {
            logger.info("No player is set... defaulting to US locale.");
            return Locale.US;
        }
    }

    private Player getPlayerById(String userId) {
        return playerDAO.byId(userId);
    }

    public boolean requestLogin(String userId, String password) {
        Player player = getPlayerById(userId);
        logger.debug("Player : " + player + " REQUESTING LOGIN WITH : " + password);
        if (player != null && player.validatePassword(password)) {
            this.setPlayer(player);
            return true;
        } else {
            return false;
        }
    }
}
