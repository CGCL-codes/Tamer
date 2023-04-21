package net.sourceforge.ipp.analyze;

import net.sourceforge.ipp.*;
import net.sourceforge.ipp.analyze.MonteCarloEstimator.SimulationSize;
import net.sourceforge.ipp.client.PokerDataAccess;

/**
 * This class can estimate the chances of winning
 * by using a Monte Carlo approach.
 * 
 * Depending on the game phase, different simulation methods
 * are used to estimate the chances:
 * <ul>
 * <li>look-up tables
 * <li>fixed number of simulated games
 * <li>iterating through the whole sample space
 * </ul>
 * 
 * @author Roland Abt
 */
public class SmartMonteCarloEstimator {

    private static SmartMonteCarloEstimator instance = null;

    protected SmartMonteCarloEstimator() {
    }

    public static SmartMonteCarloEstimator getInstance() {
        if (instance == null) {
            instance = new SmartMonteCarloEstimator();
        }
        return instance;
    }

    public double estimateChances(PokerDataAccess data, String me) {
        int numPlayers = data.getPlayers().size();
        Player p = data.getPlayers().find(me);
        CardVector hand = p.getHand();
        CardVector common = data.getCommonCards();
        return estimateChances(numPlayers, hand, common);
    }

    public double estimateChances(int numPlayers, CardVector hand, CardVector common) {
        double chances = -1.0;
        switch(common.size()) {
            case 0:
                chances = StartingHands.lookupHandChances(hand, numPlayers);
                break;
            case 3:
            case 4:
                chances = MonteCarloEstimator.getInstance().estimateChances(numPlayers, hand, common, MonteCarloEstimator.SimulationSize.NORMAL);
                break;
            case 5:
                MonteCarloEstimator.SimulationSize simSize = MonteCarloEstimator.SimulationSize.ALL;
                chances = MonteCarloEstimator.getInstance().estimateChances(numPlayers, hand, common, simSize);
                break;
            default:
                System.err.println("Wrong number of community cards: " + common.size());
        }
        return chances;
    }

    public static void main(String[] args) {
    }
}
