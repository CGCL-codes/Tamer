package cards;

public class Card {

    private final Rank rank;

    private final Suit suit;

    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    Rank getRank() {
        return rank;
    }

    Suit getSuit() {
        return suit;
    }

    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}
