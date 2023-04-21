package de.rauchhaupt.games.poker.holdem.lib.comparators;

import java.util.Comparator;
import de.volkerraum.pokerbot.model.Card;
import de.volkerraum.pokerbot.model.Card.CARDVALUE;
import de.volkerraum.pokerbot.model.Card.COLOR;

public class CardComparator implements Comparator<Card> {

    public static final CardComparator instance = new CardComparator();

    public static final COLOR[] ColorArray = new COLOR[] { Card.COLOR.DIAMONDS, Card.COLOR.HEARTS, Card.COLOR.SPADES, Card.COLOR.CLUBS };

    public static final CARDVALUE[] ValueArray = new CARDVALUE[] { CARDVALUE.TWO, CARDVALUE.THREE, CARDVALUE.FOUR, CARDVALUE.FIVE, CARDVALUE.SIX, CARDVALUE.SEVEN, CARDVALUE.EIGHT, CARDVALUE.NINE, CARDVALUE.TEN, CARDVALUE.JACK, CARDVALUE.QUEEN, CARDVALUE.KING, CARDVALUE.ACE };

    /**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    @Override
    public int compare(Card o1, Card o2) {
        return computeIndexForCard(o2) - computeIndexForCard(o1);
    }

    public static int computeIndexForCard(Card aCard) {
        return (computeIndexForCardValue(aCard.getValue()) * 100) + computeIndexForCardColor(aCard.getColor());
    }

    public static int computeIndexForCardValue(Card.CARDVALUE aCardValue) {
        int returnValue = -1;
        if (aCardValue.equals(Card.CARDVALUE.TWO)) returnValue = 2; else if (aCardValue.equals(Card.CARDVALUE.THREE)) returnValue = 3; else if (aCardValue.equals(Card.CARDVALUE.FOUR)) returnValue = 4; else if (aCardValue.equals(Card.CARDVALUE.FIVE)) returnValue = 5; else if (aCardValue.equals(Card.CARDVALUE.SIX)) returnValue = 6; else if (aCardValue.equals(Card.CARDVALUE.SEVEN)) returnValue = 7; else if (aCardValue.equals(Card.CARDVALUE.EIGHT)) returnValue = 8; else if (aCardValue.equals(Card.CARDVALUE.NINE)) returnValue = 9; else if (aCardValue.equals(Card.CARDVALUE.TEN)) returnValue = 10; else if (aCardValue.equals(Card.CARDVALUE.JACK)) returnValue = 11; else if (aCardValue.equals(Card.CARDVALUE.QUEEN)) returnValue = 12; else if (aCardValue.equals(Card.CARDVALUE.KING)) returnValue = 13; else if (aCardValue.equals(Card.CARDVALUE.ACE)) returnValue = 14; else {
            System.out.println("Not known CardValue " + aCardValue);
            return -1;
        }
        return returnValue;
    }

    public static int computeIndexForCardColor(Card.COLOR aCardColor) {
        if (aCardColor.equals(Card.COLOR.DIAMONDS)) return 1; else if (aCardColor.equals(Card.COLOR.HEARTS)) return 2; else if (aCardColor.equals(Card.COLOR.SPADES)) return 3; else if (aCardColor.equals(Card.COLOR.CLUBS)) return 4; else {
            System.out.println("Not known CardColor " + aCardColor);
            return -1;
        }
    }
}
