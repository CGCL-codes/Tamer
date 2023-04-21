package fr.corsica.games.belote.model;

import java.util.HashMap;
import java.util.Map;
import fr.corsica.games.belote.model.Card.CardColor;

/**
 * @author Dark-Water
 *
 */
public class PlayingCard {

    private static Map<Card.CardFace, Integer> atoutValues = new HashMap<Card.CardFace, Integer>();

    private static Map<Card.CardFace, Integer> nonAtoutValues = new HashMap<Card.CardFace, Integer>();

    private static CardColor atoutColor;

    static {
        atoutValues.put(Card.CardFace.SEVEN, 0);
        atoutValues.put(Card.CardFace.HEIGHT, 0);
        atoutValues.put(Card.CardFace.NINE, 14);
        atoutValues.put(Card.CardFace.TEN, 10);
        atoutValues.put(Card.CardFace.JACK, 20);
        atoutValues.put(Card.CardFace.QUEEN, 3);
        atoutValues.put(Card.CardFace.KING, 4);
        atoutValues.put(Card.CardFace.AS, 11);
        nonAtoutValues.put(Card.CardFace.SEVEN, 0);
        nonAtoutValues.put(Card.CardFace.HEIGHT, 0);
        nonAtoutValues.put(Card.CardFace.NINE, 0);
        nonAtoutValues.put(Card.CardFace.TEN, 10);
        nonAtoutValues.put(Card.CardFace.JACK, 2);
        nonAtoutValues.put(Card.CardFace.QUEEN, 3);
        nonAtoutValues.put(Card.CardFace.KING, 4);
        nonAtoutValues.put(Card.CardFace.AS, 11);
    }

    public static void setAtoutColor(CardColor atoutColor) {
        PlayingCard.atoutColor = atoutColor;
    }

    private PlayingCard() {
    }

    public static int getValue(Card card) {
        return card.getColor().equals(atoutColor) ? atoutValues.get(card) : nonAtoutValues.get(card);
    }

    public static int compareTo(Card card1, Card card2) {
        int value1 = -1;
        int value2 = -1;
        if (card1.getColor().equals(atoutColor)) {
            value1 = card1.getFace().ordinal() * 100;
        } else {
            value1 = card1.getFace().ordinal();
        }
        if (card2.getColor().equals(atoutColor)) {
            value1 = card2.getFace().ordinal() * 100;
        } else {
            value1 = card2.getFace().ordinal();
        }
        if (value1 < value2) {
            return -1;
        } else if (value1 == value2) {
            return 0;
        } else {
            return 1;
        }
    }
}
