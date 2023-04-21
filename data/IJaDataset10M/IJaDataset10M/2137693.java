package replayer.model;

import java.util.List;

public class HandState {

    private Player[] players;

    private List<Card> communityCards;

    private int currentPlayer;

    private int dealerPosition;

    private int potSize;

    public HandState(Player[] players, List<Card> communityCards, int currentPlayer, int dealerPosition, int potSize) {
        super();
        this.players = players;
        this.communityCards = communityCards;
        this.currentPlayer = currentPlayer;
        this.dealerPosition = dealerPosition;
        this.potSize = potSize;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getPotSize() {
        return potSize;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getDealerPosition() {
        return dealerPosition;
    }

    private String getCommunityCardsString() {
        String commString = "";
        for (Card card : communityCards) {
            commString += card.getRank() + card.getSuit();
        }
        return commString;
    }

    private String getPlayersString() {
        String playersString = "";
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                playersString += players[i];
            }
            playersString += "|";
        }
        return playersString.substring(0, playersString.length() - 1);
    }

    public String toString() {
        return "HANDSTATE:" + currentPlayer + ":" + dealerPosition + ":" + getPlayersString() + ":" + potSize + ":" + getCommunityCardsString();
    }
}
