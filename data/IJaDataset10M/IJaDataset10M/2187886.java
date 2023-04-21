package ca.genovese.briscola;

import java.util.LinkedList;
import java.util.List;
import ca.genovese.briscola.model.Card;
import ca.genovese.briscola.player.Player;

public class PlayerStateImpl implements PlayerState {

    Player player;

    List<PlayerState> playerList;

    List<Card> hand;

    String name;

    int points;

    public PlayerStateImpl() {
        hand = new LinkedList<Card>();
    }

    public void addCard(Card card) {
        hand.add(card);
        player.notifyCardDealt(card);
        assert (hand.size() <= 3);
    }

    public Card playCard() {
        return hand.remove(player.playCard(hand));
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public boolean hasCards() {
        return hand.size() > 0;
    }

    public void notifyCardPlayed(int playerId, Card card) {
        player.notifyCardPlayed(playerId, card);
    }

    public void notifyEndGame() {
        player.notifyEndGame();
    }

    public void notifyPlayerCardDealt(int playerId) {
        if (playerList.get(playerId) != this) player.notifyPlayerCardDealt(playerId);
    }

    public void notifyPlayerList(List<PlayerState> players) {
        player.notifyPlayerList(players);
        this.playerList = players;
    }

    public void notifyRoundWin(int playerId, int pointsWon) {
        player.notifyRoundWin(playerId, pointsWon);
    }

    public void notifyTrumpCard(Card trumpCard) {
        player.notifyTrumpCard(trumpCard);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
