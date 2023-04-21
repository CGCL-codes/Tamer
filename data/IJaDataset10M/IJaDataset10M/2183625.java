package fr.uha.ensisa.ir.walther.bluepoker.hands;

import java.util.Vector;
import fr.uha.ensisa.ir.walther.bluepoker.core.Card;

public abstract class Hand {

    public static final int HAND_HIGHCARDS = 0;

    public static final int HAND_ONEPAIR = 1;

    public static final int HAND_TWOPAIR = 2;

    public static final int HAND_THREEOFAKIND = 3;

    public static final int HAND_STRAIGHT = 4;

    public static final int HAND_FLUSH = 5;

    public static final int HAND_FULLHOUSE = 6;

    public static final int HAND_FOUROFAKIND = 7;

    public static final int HAND_STRAIGHTFLUSH = 8;

    public abstract boolean isBetterThan(Hand hand);

    public abstract int getHandType();

    public abstract Vector getCards();

    protected boolean isBasicallyBetterThan(Hand hand) {
        return (getHandType() > hand.getHandType());
    }

    public String toString() {
        StringBuffer tmp = new StringBuffer();
        tmp.append("Hand type '").append(getHandType()).append("' is : (");
        for (int i = 0; i < getCards().size() - 1; ++i) tmp.append((Card) getCards().elementAt(i)).append(", ");
        if (getCards().size() > 0) tmp.append((Card) getCards().elementAt(getCards().size() - 1));
        tmp.append(")");
        return tmp.toString();
    }
}
