package org.DragonPokerServer.Classes;

import org.DragonPokerServer.Thread.ThreadUser;

/**
 *
 * @author alien
 */
public class Player {

    private ThreadUser threadUser;

    private float chips = 0;

    private boolean sitout = false;

    private boolean dealer = false;

    public Player() {
    }

    public Player(ThreadUser threadUser) {
        this.threadUser = threadUser;
    }

    public Player(ThreadUser threadUser, float chips) {
        this.threadUser = threadUser;
        this.chips = chips;
    }

    public float getChips() {
        return chips;
    }

    public void setChips(float chips) {
        this.chips = chips;
    }

    public ThreadUser getThreadUser() {
        return threadUser;
    }

    public void setThreadUser(ThreadUser threadUser) {
        this.threadUser = threadUser;
    }

    public boolean isDealer() {
        return dealer;
    }

    public void setDealer(boolean dealer) {
        this.dealer = dealer;
    }

    public boolean isSitout() {
        return sitout;
    }

    public void setSitout(boolean sitout) {
        this.sitout = sitout;
    }
}
