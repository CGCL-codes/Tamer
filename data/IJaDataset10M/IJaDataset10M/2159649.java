package Poker;

import java.util.Random;

public class Deck {

    public static final int NUM_DECKS = 1;

    public static final int CARDS_IN_DECK = Card.NUM_RANKS * Card.NUM_SUITS;

    private Card[] cards;

    private int nextCard;

    public Deck() {
        cards = new Card[NUM_DECKS * CARDS_IN_DECK];
        int i = 0;
        for (int d = 0; d < NUM_DECKS; d++) {
            for (int s = 0; s < Card.NUM_SUITS; s++) {
                for (int r = 0; r < Card.NUM_RANKS; r++) {
                    cards[i++] = new Card(s, r);
                }
            }
        }
        nextCard = 0;
    }

    public void shuffle(Random rand) {
        for (int i = cards.length - 1; i >= 0; i--) {
            int r = rand.nextInt(i + 1);
            Card t = cards[i];
            cards[i] = cards[r];
            cards[r] = t;
        }
        nextCard = 0;
    }

    public Card dealOne() {
        if (nextCard >= cards.length) return null;
        return cards[nextCard++];
    }

    public int countCardsRemaining() {
        return cards.length - nextCard;
    }

    /**
     * Play the exact same deck over again
     */
    public void reset() {
        nextCard = 0;
    }
}
