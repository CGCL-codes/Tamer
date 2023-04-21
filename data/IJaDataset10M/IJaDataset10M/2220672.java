package structs;

import java.util.ArrayList;
import java.util.Stack;
import interfaces.ICard;
import interfaces.IPlayer;
import interfaces.Strategy;

public class Player implements IPlayer {

    private Stack<ICard> winStack = new Stack<ICard>();

    private ArrayList<ICard> currentHand = new ArrayList<ICard>();

    private Card playedCard;

    private Strategy strat = null;

    private Deck deck;

    private int name;

    public Player(int name, Deck deck) {
        this.deck = deck;
        this.name = name;
    }

    public Player(int name, Deck deck, Strategy st) {
        this.deck = deck;
        this.strat = st;
        this.name = name;
    }

    public String getName() {
        return "Jugador " + name;
    }

    public void setStrategy(Strategy strat) {
        this.strat = strat;
    }

    public void setPlayedCard(ICard card) {
        if (this.currentHand.indexOf(card) != -1) {
            this.currentHand.remove(this.currentHand.indexOf(card));
        }
        this.playedCard = (Card) this.deck.removeCard(card.getSuit(), card.getNumber());
    }

    public ICard getPlayedCard() {
        return this.playedCard;
    }

    public void addCard(ICard card) {
        if (this.currentHand.size() <= 3) {
            this.currentHand.add(this.deck.removeCard(card.getSuit(), card.getNumber()));
        }
    }

    public boolean hasCards() {
        if (this.currentHand.isEmpty()) {
            return false;
        }
        return true;
    }

    public void setCurrentHand(ArrayList<ICard> hand) {
        this.currentHand = hand;
    }

    public ArrayList<ICard> getCurrentHand() {
        return this.currentHand;
    }

    public ICard getNextMove() {
        if (this.strat != null) {
            ICard play = this.strat.nextMove();
            return play;
        }
        return null;
    }

    public Stack<ICard> getWinStack() {
        return this.winStack;
    }

    public void addToWinStack(ICard card) {
        this.winStack.push(card);
    }

    public int getWinStackPoints() {
        int totalPoints = 0;
        for (ICard card : this.winStack) {
            totalPoints += card.getPoints();
        }
        return totalPoints;
    }

    public String trySwapLife() {
        ICard life = this.deck.getLife();
        ICard swapCard2 = null;
        ICard swapCard7 = null;
        for (ICard card : this.currentHand) {
            if (card.getSuit().equalsIgnoreCase(life.getSuit()) && card.getNumber() == 2) {
                swapCard2 = card;
            } else if (card.getSuit().equalsIgnoreCase(life.getSuit()) && card.getNumber() == 7) {
                swapCard7 = card;
            }
        }
        if (swapCard2 != null) {
            return "Card 2";
        }
        if (swapCard7 != null && (life.getNumber() == 1 || life.getNumber() == 3 || life.getNumber() > 7)) {
            return "Card 7";
        }
        return "";
    }

    public boolean aiSwapLife() {
        ICard life = this.deck.getLife();
        ICard swapCard2 = null;
        ICard swapCard7 = null;
        for (ICard card : this.currentHand) {
            if (card.getSuit().equalsIgnoreCase(life.getSuit()) && card.getNumber() == 2) {
                swapCard2 = card;
            } else if (card.getSuit().equalsIgnoreCase(life.getSuit()) && card.getNumber() == 7) {
                swapCard7 = card;
            }
        }
        if (swapCard2 != null) {
            this.swapLife(swapCard2);
            return true;
        }
        if (swapCard7 != null && (life.getNumber() == 1 || life.getNumber() == 3 || life.getNumber() > 7)) {
            this.swapLife(swapCard7);
            return true;
        }
        return false;
    }

    public void swapLife(ICard card) {
        ICard life = this.deck.getLife();
        ICard swap = null;
        if (this.currentHand.indexOf(card) == -1) {
            swap = card;
        } else {
            swap = this.currentHand.remove(this.currentHand.indexOf(card));
        }
        if (this.strat != null) {
            this.deck.removeCard(life.getSuit(), life.getNumber());
        }
        this.deck.setLife(null);
        this.deck.addCard(swap);
        this.deck.setLife(swap);
        this.currentHand.add(life);
    }
}
