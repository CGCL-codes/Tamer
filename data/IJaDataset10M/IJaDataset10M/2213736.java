package game;

import network.*;

public class PokerPlayer {

    private Connection connection;

    private int chips;

    private boolean dealer;

    private boolean smallBlind;

    private boolean bigBlind;

    public void requestAction(PokerAction[] pa) {
    }

    public PokerPlayer(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getChips() {
        return chips;
    }

    public void incrementChips(int amount) {
        chips += amount;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public void decrementChips(int amount) {
        chips -= amount;
    }

    public boolean isDealer() {
        return dealer;
    }

    public boolean isSmallBlind() {
        return smallBlind;
    }

    public boolean isBigBlind() {
        return bigBlind;
    }

    public void dealer() {
        dealer = true;
        smallBlind = bigBlind = false;
    }

    public void smallBlind() {
        smallBlind = true;
        dealer = bigBlind = false;
    }

    public void bigBlind() {
        bigBlind = true;
        smallBlind = dealer = false;
    }
}
