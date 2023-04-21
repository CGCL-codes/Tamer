package net.sourceforge.ipp;

public abstract class HandType {

    public static final HandType worstHandPossible = new HandTypeHighCard(2, 2, 2, 2, 2);

    public static final HandType bestHandPossible = new HandTypeStraightFlush(1);

    /**
	 * Compares a HandType with another.
	 * 
	 * @param other
	 *            the other HandType.
	 * @return +1 if this HandType is more valuable; 0 for equal strength; -1 if
	 *         other HandType is more valuable.
	 */
    public abstract int compare(HandType other);

    public abstract boolean test(CardVector cards);

    @Override
    public abstract String toString();

    public static HandType fromString(String s) throws HandTypeFormatException {
        s = s.toUpperCase();
        if (s.startsWith("STRAIGHTFLUSH")) {
            return new HandTypeStraightFlush(s.substring(13).trim());
        } else if (s.startsWith("FOUROFAKIND")) {
            return new HandTypeFourOfAKind(s.substring(11).trim());
        } else if (s.startsWith("FLUSH")) {
            return new HandTypeFlush(s.substring(5).trim());
        } else if (s.startsWith("STRAIGHT")) {
            return new HandTypeStraight(s.substring(8).trim());
        } else if (s.startsWith("FULLHOUSE")) {
            return new HandTypeFullHouse(s.substring(9).trim());
        } else if (s.startsWith("THREEOFAKIND")) {
            return new HandTypeThreeOfAKind(s.substring(12).trim());
        } else if (s.startsWith("TWOPAIR")) {
            return new HandTypeTwoPair(s.substring(7).trim());
        } else if (s.startsWith("ONEPAIR")) {
            return new HandTypeOnePair(s.substring(7).trim());
        } else if (s.startsWith("HIGHCARD")) {
            return new HandTypeHighCard(s.substring(8).trim());
        } else {
            throw new HandTypeFormatException("Unknown HandType: " + s);
        }
    }

    public static HandType fromCards(CardVector cards) {
        HandType v;
        if ((v = HandTypeStraightFlush.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeFourOfAKind.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeFlush.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeStraight.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeFullHouse.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeThreeOfAKind.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeTwoPair.fromCards(cards)) != null) {
            return v;
        }
        if ((v = HandTypeOnePair.fromCards(cards)) != null) {
            return v;
        }
        return HandTypeHighCard.fromCards(cards);
    }
}
