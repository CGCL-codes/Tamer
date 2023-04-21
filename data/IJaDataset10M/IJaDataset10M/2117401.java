package de.rauchhaupt.games.poker.holdem.lib;

import de.rauchhaupt.games.poker.holdem.lib.comparators.CardComparator;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.HoldemPokerException;
import de.volkerraum.pokerbot.model.Card;

public class CoveredCard extends Card {

    private static final long serialVersionUID = 1L;

    public static final CoveredCard instance = new CoveredCard();

    boolean covered = true;

    public CoveredCard() {
        super(CARDVALUE.ACE, COLOR.CLUBS);
    }

    public void setCard(Card aCard) {
        covered = false;
        super.setColor(aCard.getColor());
        super.setValue(aCard.getValue());
    }

    /**
	 * Getter for the blind field.
	 * 
	 * @return Returns the blind.
	 */
    public boolean isCovered() {
        return covered;
    }

    public String toString() {
        if (isCovered()) return "COVERED"; else return super.toString();
    }

    public static Card[] fromCardString(String[] aString) throws HoldemPokerException {
        if (aString == null) return null;
        Card[] returnValue = new Card[aString.length];
        for (int i = 0; i < aString.length; i++) {
            returnValue[i] = fromCardString(aString[i]);
        }
        return returnValue;
    }

    public static Card fromCardString(String aString) throws HoldemPokerException {
        Card.CARDVALUE curValue = null;
        if (aString == null) return null;
        if ("".equals(aString)) return null;
        for (int i = 0; i < CardComparator.ValueArray.length; i++) {
            Card.CARDVALUE currentCardValue = CardComparator.ValueArray[i];
            if (aString.indexOf(currentCardValue.toString()) == 0) {
                curValue = currentCardValue;
            }
        }
        if (curValue == null) throw new HoldemPokerException("Cannot handle value of '" + aString + "'");
        Card.COLOR curColor = null;
        for (int i = 0; i < CardComparator.ColorArray.length; i++) {
            Card.COLOR currentCardColor = CardComparator.ColorArray[i];
            if (aString.indexOf(currentCardColor.toString()) > 0) {
                curColor = currentCardColor;
            }
        }
        if (curColor == null) throw new HoldemPokerException("Cannot handle color of '" + aString + "'");
        return new Card(curValue, curColor);
    }
}
