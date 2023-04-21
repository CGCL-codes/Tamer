package com.shithead.game.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shithead.bo.Card;
import com.shithead.enums.CardFace;
import com.shithead.game.GameEngine;
import com.shithead.util.SHLogger;

public class SimpleAIPlayer extends AIPlayer {

    public SimpleAIPlayer(String name, GameEngine engine) {
        super(name, engine);
    }

    @Override
    protected void makeMove() {
        if (inHand.isEmpty() && faceUpCards.isEmpty()) {
            playHiddenCard();
            return;
        }
        List<Card> canPlay = inHand;
        if (canPlay.isEmpty()) {
            canPlay = faceUpCards;
        }
        CardFace pileTop = null;
        if (!pile.isEmpty()) {
            pileTop = validator.getPileTop(pile);
        }
        if (pileTop == null || pileTop == rules.getResetCard()) {
            calculateBestMove(CardFace.TWO, canPlay);
        } else {
            calculateBestMove(pileTop, canPlay);
        }
    }

    public void calculateBestMove(CardFace top, List<Card> cards) {
        Card toPlay = null;
        Map<CardFace, List<Card>> special = new HashMap<CardFace, List<Card>>();
        for (Card card : cards) {
            if (card.getFace() == rules.getBurnCard()) {
                special.put(rules.getBurnCard(), addToList(special.get(rules.getBurnCard()), card));
            } else if (card.getFace() == rules.getResetCard()) {
                special.put(rules.getResetCard(), addToList(special.get(rules.getResetCard()), card));
            } else if (card.getFace() == rules.getInvisibleCard()) {
                special.put(rules.getInvisibleCard(), addToList(special.get(rules.getInvisibleCard()), card));
            } else if (top != rules.getUnderCard() && card.getFace().compare(top) >= 0) {
                if (toPlay == null || card.getFace().compare(toPlay.getFace()) < 0) {
                    toPlay = card;
                }
            } else if (top == rules.getUnderCard() && card.getFace().compare(top) <= 0) {
                if (!rules.isAllowUnderOnUnder() && card.getFace() == top) {
                    continue;
                }
                if (toPlay == null || card.getFace().compare(toPlay.getFace()) < 0) {
                    toPlay = card;
                }
            }
        }
        List<Card> cardList = new ArrayList<Card>();
        if (toPlay == null) {
            if (special.containsKey(rules.getResetCard())) {
                cardList.addAll(special.get(rules.getResetCard()));
            } else if (special.containsKey(rules.getInvisibleCard())) {
                if (cards.size() == special.get(rules.getInvisibleCard()).size()) {
                    cardList.addAll(special.get(rules.getInvisibleCard()));
                } else {
                    cardList.add(special.get(rules.getInvisibleCard()).iterator().next());
                }
            } else if (special.containsKey(rules.getBurnCard())) {
                if (cards.size() == special.get(rules.getBurnCard()).size()) {
                    cardList.addAll(special.get(rules.getBurnCard()));
                } else {
                    cardList.add(special.get(rules.getBurnCard()).iterator().next());
                }
            }
        } else {
            cardList = validator.canPlayMulti(toPlay, inHand, faceUpCards);
        }
        if (cardList.isEmpty()) {
            SHLogger.Error("Cant find a card to play over " + top + " in " + cards);
        }
        if (validator.isValidMove(pile, cardList) != null) {
            SHLogger.Error("Producted invalid move " + cardList + " for pile " + pile);
        }
        sendMove(cardList);
    }
}
