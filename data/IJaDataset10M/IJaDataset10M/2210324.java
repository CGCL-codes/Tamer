package net.sourceforge.robotnik.poker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.robotnik.poker.util.Randomizer;

public class Deck {

    Randomizer randomizer;

    public Randomizer getRandomizer() {
        return randomizer;
    }

    public void setRandomizer(Randomizer randomizer) {
        this.randomizer = randomizer;
    }

    public static enum Suit {

        SPADES, HEARTS, CLUBS, DIAMONDS
    }

    public static Suit suitForShortString(String s) {
        for (Suit suit : Suit.values()) {
            if (suit.toString().substring(0, 1).equals(s.toUpperCase())) {
                return suit;
            }
        }
        return null;
    }

    public List<Hand> getAllRemainingHands() {
        LinkedList<Hand> hands = new LinkedList<Hand>();
        for (Card card : getCards()) {
            for (Card card2 : getCards()) {
                if (!card.equals(card2)) {
                    hands.add(new Hand(card, card2));
                }
            }
        }
        return hands;
    }

    LinkedList<Card> cards = new LinkedList<Card>();

    HashMap<Suit, LinkedList<Card>> cardsPerSuit;

    public void init() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (int i = 2; i <= 14; i++) {
                cards.add(new Card(i, suit));
            }
        }
        setCardsPerSuit();
    }

    private void setCardsPerSuit() {
        cardsPerSuit = new HashMap<Suit, LinkedList<Card>>();
        for (Suit suit2 : Suit.values()) {
            cardsPerSuit.put(suit2, new LinkedList<Card>());
        }
        for (Card card : cards) {
            LinkedList<Card> linkedList = cardsPerSuit.get(card.getSuit());
            linkedList.add(card);
        }
    }

    public LinkedList<Card> getCardsForSuit(Suit suit) {
        return cardsPerSuit.get(suit);
    }

    public Deck() {
        super();
        init();
    }

    private Hand getHand(String card1Value, String card2Value, Suit suit1, Suit suit2) throws HandNotCreatableException {
        Card card = new Card(card1Value, suit1);
        Card card2 = new Card(card2Value, suit2);
        if (existsInDeck(card) && existsInDeck(card2)) {
            removeFromDeck(card);
            removeFromDeck(card2);
            return new Hand(card, card2);
        } else {
            throw new HandNotCreatableException("Could not create hand '" + card1Value + card2Value + (suit1.equals(suit2) ? "s" : "") + "'.");
        }
    }

    private Hand getHand(String card1Value, String card2Value, boolean isSuited) throws HandNotCreatableException {
        Card card = null;
        Card card2 = null;
        Vector<Card> vector = new Vector<Card>();
        for (Suit suit : Suit.values()) {
            if (isSuited) {
                card = new Card(card1Value, suit);
                card2 = new Card(card2Value, suit);
                if (existsInDeck(card) && existsInDeck(card2)) {
                    removeFromDeck(card);
                    removeFromDeck(card2);
                    break;
                }
                card = null;
                card2 = null;
            } else {
                if (card == null && existsInDeck(new Card(card1Value, suit))) {
                    card = new Card(card1Value, suit);
                    removeFromDeck(card);
                    continue;
                }
                if (existsInDeck(new Card(card2Value, suit))) {
                    card2 = new Card(card2Value, suit);
                    removeFromDeck(card2);
                }
                if (card != null && card2 != null) {
                    break;
                }
            }
        }
        if (card == null || card2 == null || card.equals(card2)) {
            if (card != null) {
                cards.add(card);
            }
            if (card2 != null) {
                cards.add(card2);
            }
            throw new HandNotCreatableException("Could not create hand '" + card1Value + card2Value + (isSuited ? "s" : "") + "'.");
        }
        vector.add(card);
        vector.add(card2);
        return new Hand(vector);
    }

    /**
	 * Get Hand som motsvarar handDesc.
	 * Ta bort korten ur leken.
	 * 
	 * @param handDesc
	 */
    public Hand getHand(String handDesc) throws HandNotCreatableException {
        Pattern pattern = Pattern.compile("..[sS]");
        Matcher matcher = pattern.matcher(handDesc);
        String card1Value = String.valueOf(handDesc.charAt(0));
        String card2Value = String.valueOf(handDesc.charAt(1));
        Hand hand;
        try {
            hand = getHand(card1Value, card2Value, matcher.matches());
        } catch (HandNotCreatableException e) {
            hand = getHand(card2Value, card1Value, matcher.matches());
        }
        return hand;
    }

    public List<Hand> getHands(List<String> handDescs) throws HandNotCreatableException {
        LinkedList<Hand> hands = new LinkedList<Hand>();
        for (String string : handDescs) {
            List<Hand> handsForThisDesc = getAllHandsForDesc(string);
            hands.addAll(handsForThisDesc);
        }
        return hands;
    }

    /**
	 * Get Hand som motsvarar handDesc.
	 * Ta bort korten ur leken.
	 * 
	 * @param handDesc
	 */
    public Hand getHand(String handDesc, Suit suit1, Suit suit2) throws HandNotCreatableException {
        Pattern pattern = Pattern.compile("..[sS]");
        Matcher matcher = pattern.matcher(handDesc);
        String card1Value = String.valueOf(handDesc.charAt(0));
        String card2Value = String.valueOf(handDesc.charAt(1));
        Hand hand;
        try {
            hand = getHand(card1Value, card2Value, suit1, suit2);
        } catch (HandNotCreatableException e) {
            hand = getHand(card2Value, card1Value, suit2, suit1);
        }
        return hand;
    }

    /**
	 * 
	 * Get alla unika av hands som uppfyller desc
	 * Tex ska AA ge AcAd, AcAs, AcAh, AdAs...
	 * AK ger INTE both AcKh och KhAc, eftersom samma hand
	 * 
	 * @param desc	AA, AKs osv
	 * @return
	 */
    public List<Hand> getAllHandsForDesc(String desc) {
        Pattern pattern = Pattern.compile("..[sS]");
        Matcher matcher = pattern.matcher(desc);
        LinkedList<Hand> hands = new LinkedList<Hand>();
        Card tempCard1 = new Card(String.valueOf(desc.charAt(0)), Suit.CLUBS);
        Card tempCard2 = new Card(String.valueOf(desc.charAt(1)), Suit.CLUBS);
        List<Card> cardsForValue1 = getAllCardsOfValue(tempCard1.getValue());
        List<Card> cardsForValue2 = getAllCardsOfValue(tempCard2.getValue());
        for (Card card : cardsForValue1) {
            for (Card card2 : cardsForValue2) {
                if (card.equals(card2)) {
                    continue;
                }
                Hand hand = new Hand(card, card2);
                if (matcher.matches() && hands.indexOf(hand) < 0) {
                    if (card.getSuit().equals(card2.getSuit())) {
                        hands.add(new Hand(card, card2));
                    }
                } else if (hands.indexOf(hand) < 0) {
                    if (!card.getSuit().equals(card2.getSuit())) {
                        hands.add(new Hand(card, card2));
                    }
                }
            }
        }
        return hands;
    }

    public List<Hand> getAllHands() {
        return null;
    }

    public List<Card> getAllCardsOfValue(int value) {
        LinkedList<Card> copyCards = new LinkedList<Card>();
        copyCards.addAll(cards);
        LinkedList<Card> list = new LinkedList<Card>();
        for (Card card : copyCards) {
            if (card.getValue() == value) {
                list.add(card);
            }
        }
        return list;
    }

    public Card getRandomCard() {
        double exactCardPosition = (cards.size() - 1) * getRandomizer().random();
        Long cardPosition = new Long(Math.round(exactCardPosition));
        Card card = cards.get(cardPosition.intValue());
        cards.remove(card);
        return card;
    }

    public int size() {
        return cards.size();
    }

    public boolean existsInDeck(Card card) {
        return cards.indexOf(card) >= 0;
    }

    public void remove(List<Card> cards) {
        for (Card card : cards) {
            removeFromDeck(card);
        }
    }

    public void removeFromDeck(Card card) {
        cards.remove(card);
        LinkedList<Card> list = cardsPerSuit.get(card.getSuit());
        list.remove(card);
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    public void setCards(LinkedList<Card> cards) {
        this.cards = cards;
        setCardsPerSuit();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        if (cards != null ? !cards.equals(deck.cards) : deck.cards != null) return false;
        if (cardsPerSuit != null ? !cardsPerSuit.equals(deck.cardsPerSuit) : deck.cardsPerSuit != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = cards != null ? cards.hashCode() : 0;
        result = 31 * result + (cardsPerSuit != null ? cardsPerSuit.hashCode() : 0);
        return result;
    }
}
