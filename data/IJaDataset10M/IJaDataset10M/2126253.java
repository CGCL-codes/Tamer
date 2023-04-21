package com.evver.cardplatform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hand implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<Card> cards;

    /**
	 * Constructs an empy hand of cards
	 */
    public Hand() {
        this.cards = new ArrayList<Card>();
    }

    /**
	 * Constructs an hand of cards
	 * @param cards the cards in hand
	 */
    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    /**
	 * Adds the card to the hand
	 * @param card card to add
	 * @return success adding card
	 */
    public synchronized Boolean addCard(Card card) {
        if (card != null) {
            return this.cards.add(card);
        } else return Boolean.FALSE;
    }

    /**
	 * Adds the card to the hand in the desired position
	 * @param card the card to add
	 * @param index the index of the position
	 * @return successs adding card
	 */
    public synchronized Boolean addCard(Card card, Integer index) {
        if (card != null && index != null) {
            this.cards.add(index, card);
            return Boolean.TRUE;
        } else return Boolean.FALSE;
    }

    /**
	 * Draws a card from the top of the hand
	 * @return the drawn card
	 */
    public synchronized Card drawCard() {
        return drawCard(cards.size() - 1);
    }

    /**
	 * Draws a card from the desired position
	 * @param index the position in the hand
	 * @return the drawn card
	 */
    public synchronized Card drawCard(Integer index) {
        if (index != null && index > -1 && index < cards.size()) {
            Card card = (Card) cards.get(index);
            cards.remove(card);
            return card;
        } else return null;
    }

    /**
	 * Gets a list of held cards
	 * @return a list of held cards
	 */
    public List<Card> getHeldCards() {
        return cards;
    }
}
