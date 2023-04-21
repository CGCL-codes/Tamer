package ao.holdem.model.card.chance;

import ao.holdem.model.card.Card;
import ao.holdem.model.card.Community;
import ao.holdem.model.card.Hole;
import ao.util.data.Arrs;

/**
 *
 */
public class Deck {

    private final Card cards[];

    private int nextIndex = 0;

    public Deck() {
        cards = Card.VALUES.clone();
        Arrs.shuffle(cards);
    }

    private Deck(Card copyCards[], int copyNextIndex) {
        cards = copyCards;
        nextIndex = copyNextIndex;
    }

    public Hole nextHole() {
        return Hole.valueOf(nextCard(), nextCard());
    }

    public Community nextFlop() {
        return new Community(nextCard(), nextCard(), nextCard());
    }

    public Card nextCard() {
        assert nextIndex < cards.length;
        return cards[nextIndex++];
    }

    public void reset() {
        nextIndex = 0;
    }

    public int cardsDealt() {
        return nextIndex;
    }

    public Deck prototype() {
        return new Deck(cards, nextIndex);
    }
}
