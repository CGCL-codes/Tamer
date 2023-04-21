package fr.uha.ensisa.ir.walther.bluepoker.hands;

import java.util.Vector;

public class StraightFlush extends Hand {

    private final int handType = Hand.HAND_STRAIGHTFLUSH;

    private Vector cards;

    public StraightFlush() {
        this.cards = new Vector(0);
    }

    public StraightFlush(Vector cards) {
        this.cards = cards;
    }

    public boolean isBetterThan(Hand hand) {
        if (!isBasicallyBetterThan(hand)) {
        }
        return false;
    }

    public static boolean isConformTo(Vector cards) {
        return (HandUtils.countStraightFlush(cards) == 5);
    }

    public int getHandType() {
        return handType;
    }

    public Vector getCards() {
        return this.cards;
    }
}
