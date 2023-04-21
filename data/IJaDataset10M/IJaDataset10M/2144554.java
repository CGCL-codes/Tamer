package com.shoesobjects.bourre.decisionstrategies;

import com.shoesobjects.bourre.game.BourreHand;
import com.shoesobjects.bourre.game.Position;
import com.shoesobjects.deckofcards.Card;

public class BaseDecisionStrategy {

    protected int LOWER_TRUMP_VALUE = 20;

    protected int HIGHER_TRUMP_VALUE = 25;

    protected int HIGHEST_TRUMP_CARD_VALUE = 25;

    protected Position position;

    protected BourreHand hand;

    protected Card dealerTrumpCard;

    protected int calcDecision = -1;

    /**
     * Each Decision Strategy should override this method
     *
     * @return
     */
    protected boolean hasChooseToGoInHand() {
        return hand.hasHighestTrumpCard(this.dealerTrumpCard);
    }

    protected boolean hasMustGoInHand() {
        return hand.hasHighestTrumpCard(this.dealerTrumpCard);
    }

    protected boolean canCheck() {
        return position.canCheck();
    }

    protected int calculateDecision(BourreHand hand, Card dealerTrumpCard) {
        this.hand = hand;
        this.dealerTrumpCard = dealerTrumpCard;
        return calculateDecision();
    }

    protected int calculateDecision() {
        int decisionValue = 0;
        decisionValue += (hand.getNumberOfLowerTrumpCards(this.dealerTrumpCard) * LOWER_TRUMP_VALUE);
        decisionValue += (hand.getNumberOfHigherTrumpCards(this.dealerTrumpCard) * HIGHER_TRUMP_VALUE);
        if (hand.hasHighestTrumpCard(this.dealerTrumpCard)) {
            decisionValue += HIGHEST_TRUMP_CARD_VALUE;
        }
        return decisionValue;
    }

    public DecisionStrategy.Decision decide(BourreHand hand, Card dealerTrumpCard, Position position) {
        this.position = position;
        this.hand = hand;
        this.dealerTrumpCard = dealerTrumpCard;
        if (hasMustGoInHand()) {
            return DecisionStrategy.Decision.IN;
        }
        if (canCheck()) {
            return DecisionStrategy.Decision.CHECK;
        }
        if (hasChooseToGoInHand()) {
            return DecisionStrategy.Decision.IN;
        }
        return DecisionStrategy.Decision.OUT;
    }

    public String toString() {
        return "BaseDecisionStrategy";
    }
}
