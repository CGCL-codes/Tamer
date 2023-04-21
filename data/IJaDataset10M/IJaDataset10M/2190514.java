package org.jogre.ninetynine.common;

import nanoxml.XMLElement;
import org.jogre.common.comm.CommTableMessage;
import org.jogre.common.util.JogreUtils;
import org.jogre.ninetynine.std.Card;
import org.jogre.ninetynine.std.Hand;

/**
 * Communications object for sending a hand of cards to a player
 * 
 * @author Richard Walter
 * @version Alpha 0.1
 */
public class CommNinetyNineSendHand extends CommTableMessage {

    public static final String XML_NAME = "nn_hand";

    public static final String XML_ATT_SUITS = "suits";

    public static final String XML_ATT_VALUES = "values";

    public static final String XML_ATT_TRUMP_SUIT = "trump";

    private Hand theHand;

    private int trumpSuit;

    /**
	 * Constructor that takes a username, a hand, and a trump suit
	 * 
	 * @param username		Username
	 * @param theHand		The hand to be sent
	 * @param trumpSuit		The trump suit
	 */
    public CommNinetyNineSendHand(String username, Hand theHand, int trumpSuit) {
        super(username);
        this.theHand = theHand;
        this.trumpSuit = trumpSuit;
    }

    /**
	 * Constuctor that takes an xml element and creates an object from that xml
	 * element.
	 * 
	 * @param	message		XML element
	 */
    public CommNinetyNineSendHand(XMLElement message) {
        super(message);
        theHand = new Hand(JogreUtils.convertToIntArray(message.getStringAttribute(XML_ATT_SUITS)), JogreUtils.convertToIntArray(message.getStringAttribute(XML_ATT_VALUES)));
        theHand.sort();
        trumpSuit = message.getIntAttribute(XML_ATT_TRUMP_SUIT);
    }

    /**
	 * Get the hand
	 * 
	 * @return the hand
	 */
    public Hand getHand() {
        return this.theHand;
    }

    /**
	 * Get the trump suit
	 *
	 * @return the trump suit
	 */
    public int getTrumpSuit() {
        return this.trumpSuit;
    }

    /**
	 * Flattens this object into xml.
	 * 
	 * @see org.jogre.common.comm.ITransmittable#flatten()
	 */
    public XMLElement flatten() {
        XMLElement message = super.flatten(XML_NAME);
        int numCards = theHand.length();
        int[] values = new int[numCards];
        int[] suits = new int[numCards];
        for (int i = 0; i < numCards; i++) {
            Card c = theHand.getNthCard(i);
            values[i] = c.cardValue();
            suits[i] = c.cardSuit();
        }
        message.setAttribute(XML_ATT_SUITS, JogreUtils.valueOf(suits));
        message.setAttribute(XML_ATT_VALUES, JogreUtils.valueOf(values));
        message.setIntAttribute(XML_ATT_TRUMP_SUIT, trumpSuit);
        return message;
    }
}
