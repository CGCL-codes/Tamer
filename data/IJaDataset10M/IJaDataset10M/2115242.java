package net.sf.nebulacards.main;

import java.io.Serializable;

/**
 * A single playing card. Pip count is 1-based. (i.e. ace=1, 11=jack, 12=queen,
 * ...)
 * 
 * @author James Ranson
 */
public class PlayingCard implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6581071026141225813L;

    /**
	 * Card is a spade.
	 */
    public static final int SPADES = 0;

    /**
	 * Card is a diamond.
	 */
    public static final int DIAMONDS = 1;

    /**
	 * Card is a club.
	 */
    public static final int CLUBS = 2;

    /**
	 * Card is a heart.
	 */
    public static final int HEARTS = 3;

    private int suit;

    private int val;

    /**
	 * Make a new playing card with the given suit and value.
	 * 
	 * @param v
	 *            The value (i.e. pip count)
	 * @param s
	 *            The suit (use one of the suit constants)
	 */
    public PlayingCard(int v, int s) {
        suit = s;
        val = v;
    }

    /**
	 * Make a new playing card from the given string representation.
	 * 
	 * @param s
	 *            The string representation of the card.
	 * @see #fromString(String)
	 * @version 0.4
	 */
    public PlayingCard(String s) {
        PlayingCard c = fromString(s);
        suit = c.getSuit();
        val = c.getVal();
    }

    /**
	 * Provide a unique integer hash code for each card. This will be unique
	 * only in the standard 52-card deck. Subclasses should override this
	 * method.
	 * 
	 * @return A hash code.
	 */
    public int hashCode() {
        return getSuit() * 13 + getValue();
    }

    /**
	 * Determine if the card is the special NullPlayingCard.
	 * 
	 * @return true if this card is Null, or false if it is a regular card.
	 */
    public boolean isNull() {
        return false;
    }

    /**
	 * The suit of this card.
	 * 
	 * @return The suit.
	 */
    public synchronized int getSuit() {
        return suit;
    }

    /**
	 * The value of this card.
	 * 
	 * @return The value.
	 */
    public synchronized int getVal() {
        return val;
    }

    /**
	 * The value of this card. Same as getVal().
	 * 
	 * @see #getVal
	 * @return The value.
	 */
    public synchronized int getValue() {
        return val;
    }

    /**
	 * Is <b>this</b> card is exactly the same as the given card?
	 * 
	 * @param o
	 *            The card to be checked against this card.
	 * @return True if cards are the same, false otherwise.
	 */
    public synchronized boolean equals(Object o) {
        if (o instanceof PlayingCard) {
            PlayingCard c = (PlayingCard) o;
            if (c.isNull()) return false; else return (c.suit == this.suit && c.val == this.val);
        }
        return false;
    }

    /**
	 * Convert this card to a string. Consists of the value (as a number or
	 * capital letter), followed by the first letter of the suit.
	 * 
	 * @return The String representation of this card.
	 */
    public synchronized String toString() {
        String s;
        switch(suit) {
            case SPADES:
                s = "s";
                break;
            case CLUBS:
                s = "c";
                break;
            case HEARTS:
                s = "h";
                break;
            case DIAMONDS:
                s = "d";
                break;
            default:
                throw new IllegalArgumentException();
        }
        switch(val) {
            case 1:
                s += "a";
                break;
            case 11:
                s += "j";
                break;
            case 12:
                s += "q";
                break;
            case 13:
                s += "k";
                break;
            default:
                s += String.valueOf(val);
                break;
        }
        return s;
    }

    /**
	 * Make a card from a string representation. Reverses the process of
	 * toString(). Interprets "--" as NullPlayingCard.
	 * 
	 * @param s
	 *            The string representation of the card.
	 * @return A new card.
	 * @see #toString
	 */
    public static PlayingCard fromString(String s) {
        int val, suit;
        if (s.equals("--")) return NullPlayingCard.getInstance();
        switch(s.charAt(0)) {
            case 'a':
                val = 1;
                break;
            case '1':
                val = 10;
                break;
            case 'j':
                val = 11;
                break;
            case 'q':
                val = 12;
                break;
            case 'k':
                val = 13;
                break;
            default:
                val = (s.charAt(0) - '0');
        }
        switch(s.charAt(s.length() - 1)) {
            case 's':
                suit = SPADES;
                break;
            case 'd':
                suit = DIAMONDS;
                break;
            case 'c':
                suit = CLUBS;
                break;
            case 'h':
                suit = HEARTS;
                break;
            default:
                return null;
        }
        return new PlayingCard(val, suit);
    }
}
