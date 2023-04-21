package emil.poker.ai.opponentEvaluators;

import emil.poker.ai.NutsEvaluator;
import emil.poker.ai.NutsEvaluatorImpl;
import emil.poker.ai.util.ProbabilityHashMap;
import net.sourceforge.robotnik.poker.*;
import net.sourceforge.robotnik.poker.Hand.HandValue;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ChanceToGetHandValuesOnRiverEvaluatorImpl implements ChanceToGetHandValuesOnRiverEvaluator {

    private ProbabilityHashMap<HandValue, Double> strengthPerHandValue = new ProbabilityHashMap<HandValue, Double>();

    private ProbabilityHashMap<HandValue, Double> chanceToGetHandValuesOnRiver = new ProbabilityHashMap<HandValue, Double>();

    private List<Card> communityCards;

    private Hand hand;

    Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void evaluate() {
        Deck deck = new Deck();
        deck.init();
        deck.getCards().removeAll(hand.getCards());
        deck.getCards().removeAll(communityCards);
        if (communityCards.size() == 3 || communityCards.size() == 4) {
            ProbabilityHashMap<HandValue, Double> numOfTimesHandValueOccured = new ProbabilityHashMap<HandValue, Double>();
            for (Card riverCard : deck.getCards()) {
                List<Card> tempCards = new LinkedList<Card>();
                tempCards.addAll(communityCards);
                tempCards.add(riverCard);
                NutsEvaluator nutsEvaluator = new NutsEvaluatorImpl();
                nutsEvaluator.setCommunityCards(tempCards);
                nutsEvaluator.evaluate();
                HandValueEvaluator handEvaluator = new HandValueEvaluator(tempCards, Street.TURN, hand);
                handEvaluator.setNutsValue(nutsEvaluator.getNutsValue());
                handEvaluator.evaluate();
                HandValue handValue = handEvaluator.getHandValue();
                double chanceToGetCards = 1.0 / 46.0;
                chanceToGetHandValuesOnRiver.increase(handValue, chanceToGetCards);
                double strength = handEvaluator.getHandValueRelativeStrength();
                strengthPerHandValue.increase(handValue, strength);
                numOfTimesHandValueOccured.increase(handValue, 1.0);
            }
            for (Entry<HandValue, Double> entry : numOfTimesHandValueOccured.entrySet()) {
                strengthPerHandValue.multiply(entry.getKey(), 1 / entry.getValue());
            }
            if (communityCards.size() == 3) {
                NutsEvaluator nutsEvaluator = new NutsEvaluatorImpl();
                nutsEvaluator.setCommunityCards(communityCards);
                nutsEvaluator.evaluate();
                HandValueEvaluator handEvaluator = new HandValueEvaluator(communityCards, Street.FLOP, hand);
                handEvaluator.setNutsValue(nutsEvaluator.getNutsValue());
                handEvaluator.evaluate();
                HandValue currentHandValue = handEvaluator.getHandValue();
                double totalChanceForValuesExceptCurrent = 0.0;
                for (Entry<HandValue, Double> entry : ((HashMap<HandValue, Double>) chanceToGetHandValuesOnRiver).entrySet()) {
                    if (!entry.getKey().equals(currentHandValue)) {
                        chanceToGetHandValuesOnRiver.multiply(entry.getKey(), 2.0);
                        totalChanceForValuesExceptCurrent += chanceToGetHandValuesOnRiver.get(entry.getKey());
                    }
                }
                chanceToGetHandValuesOnRiver.put(currentHandValue, 1.0 - totalChanceForValuesExceptCurrent);
            }
        } else if (communityCards.size() == 5) {
            List<Card> tempCards = new LinkedList<Card>();
            tempCards.addAll(communityCards);
            NutsEvaluator nutsEvaluator = new NutsEvaluatorImpl();
            nutsEvaluator.setCommunityCards(tempCards);
            nutsEvaluator.evaluate();
            HandValueEvaluator handEvaluator = new HandValueEvaluator(tempCards, Street.RIVER, hand);
            handEvaluator.setNutsValue(nutsEvaluator.getNutsValue());
            handEvaluator.evaluate();
            HandValue handValue = handEvaluator.getHandValue();
            chanceToGetHandValuesOnRiver.increase(handValue, 1.0);
            double strength = handEvaluator.getHandValueRelativeStrength();
            strengthPerHandValue.increase(handValue, strength);
        } else {
            throw new RuntimeException(communityCards.size() + " community cards found. " + "Can only evaluate chance to win showdown on flop or turn.");
        }
    }

    public ProbabilityHashMap<HandValue, Double> getChanceToGetHandValuesOnRiver() {
        return chanceToGetHandValuesOnRiver;
    }

    public void setCommunityCards(List<Card> cards) {
        this.communityCards = cards;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setPlayer(Player player) {
        hand = player.getHand();
    }

    public void setPoker(Poker poker) {
        communityCards = poker.getTable().getCommunityCards();
    }

    public Map<HandValue, Double> getStrengthPerHandValue() {
        return strengthPerHandValue;
    }
}
