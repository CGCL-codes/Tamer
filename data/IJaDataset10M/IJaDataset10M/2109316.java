package at.ac.tuwien.big.easyholdem.poker;

/**
 * User: sam Date: Apr 2, 2005 Time: 4:03:51 PM
 */
public final class Card implements Comparable<Card> {

    private Rank rank;

    private Suit suit;

    public int hashCode() {
        return rank.hashCode() + suit.hashCode() * 43;
    }

    public boolean equals(Object o) {
        Card other = (Card) o;
        if (compareTo(other) == 0 && other.suit == suit) {
            return true;
        }
        return false;
    }

    public int compareTo(Card card) {
        return rank.compareTo(card.rank);
    }

    public enum Suit {

        CLUBS, DIAMONDS, HEARTS, SPADES;

        private final String[] suitStrings = { "c", "d", "h", "s" };

        public String toString() {
            return suitStrings[this.ordinal()];
        }

        public static Suit parse(String suit) {
            suit = suit.toLowerCase();
            switch(suit.toCharArray()[0]) {
                case 'c':
                    return CLUBS;
                case 'd':
                    return DIAMONDS;
                case 'h':
                    return HEARTS;
                case 's':
                    return SPADES;
                default:
                    throw new IllegalArgumentException("Invalid suit: " + suit);
            }
        }
    }

    public enum Rank {

        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

        private final String[] rankStrings = { "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A" };

        public String toString() {
            return rankStrings[this.ordinal()];
        }

        public static Rank parse(String rank) {
            rank = rank.toLowerCase();
            switch(rank.toCharArray()[0]) {
                case '2':
                    return TWO;
                case '3':
                    return THREE;
                case '4':
                    return FOUR;
                case '5':
                    return FIVE;
                case '6':
                    return SIX;
                case '7':
                    return SEVEN;
                case '8':
                    return EIGHT;
                case '9':
                    return NINE;
                case 't':
                    return TEN;
                case 'j':
                    return JACK;
                case 'q':
                    return QUEEN;
                case 'k':
                    return KING;
                case 'a':
                    return ACE;
                default:
                    throw new IllegalArgumentException("Invalid rank: " + rank);
            }
        }
    }

    /**
	 * Empty constructor.
	 * Just used for jax-ws compatibility.
	 */
    public Card() {
        this.rank = null;
        this.suit = null;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    /**
	 * @param rank the rank to set
	 */
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    /**
	 * @param suit the suit to set
	 */
    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String toString() {
        return "" + rank + suit;
    }

    public static Cards parse(String string) {
        int length = string.length();
        Cards cards = new Cards();
        for (int i = 0; i < length; i += 2) {
            cards.add(new Card(Rank.parse(string.substring(i, i + 1)), Suit.parse(string.substring(i + 1, i + 2))));
        }
        return cards;
    }
}
