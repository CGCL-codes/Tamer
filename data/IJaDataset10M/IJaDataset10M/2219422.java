package at.ac.tuwien.ewa.g41.ue3.beans;

import at.ac.tuwien.big.easyholdem.player.Player;
import at.ac.tuwien.big.easyholdem.player.PlayerDAO;
import at.ac.tuwien.ewa.g41.ue3.wrapper.MutableBoolean;

/**
 * ManagedBean f�r den Login-Vorgang.
 * 
 * @author GregorLucny
 * @since 2009-05-06
 */
public class LoginBean {

    private PlayerDAO playerDao;

    private Player player;

    private MutableBoolean loggedIn;

    public String login() {
        Player loginPlayer = this.playerDao.getPlayerByUsername(this.player.getUserName());
        if (loginPlayer != null) {
            if (loginPlayer.getPassword().equals(this.player.getPassword())) {
                this.player.setId(loginPlayer.getId());
                this.player.setDateOfBirth(loginPlayer.getDateOfBirth());
                this.player.setFirstName(loginPlayer.getFirstName());
                this.player.setGender(loginPlayer.getGender());
                this.player.setLastName(loginPlayer.getLastName());
                this.player.setStack(loginPlayer.getStack());
                this.loggedIn.setValue(true);
                return "welcome";
            }
        }
        return "login";
    }

    public void setPlayerDao(PlayerDAO playerDao) {
        this.playerDao = playerDao;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLoggedIn(MutableBoolean bool) {
        this.loggedIn = bool;
    }
}
