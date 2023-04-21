package com.csft.poker;

public class Card {

    private int suit;

    private int value;

    private int index;

    private int cValue;

    private final String[] SUITS = new String[] { "Club", "Diamond", "Heart", "Spade" };

    private final String[] VALUES = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };

    /**
	 * each of the thirteen card ranks has its own prime number deuce = 2 trey =
	 * 3 four = 5 five = 7 ... king = 37 ace = 41
	 */
    int primes[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };

    /**
	 * Constructor
	 */
    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
        this.index = suit * 13 + value;
        int s = 0x8000 >> suit;
        int r = value;
        this.cValue = primes[r] | (r << 8) | s | (1 << (16 + r));
    }

    @Override
    public String toString() {
        return SUITS[suit].charAt(0) + VALUES[value];
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getcValue() {
        return cValue;
    }

    public int getIndex() {
        return index;
    }
}
