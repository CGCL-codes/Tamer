package net.sourceforge.ipp.analyze;

import net.sourceforge.ipp.*;
import net.sourceforge.ipp.client.PokerDataAccess;

/**
 * This class will calculate the raise amount to scare
 * out opponents with bad hands. The maths involved
 * are based on simple probabilistic equations using
 * the expected win/loss.
 * 
 * @author Roland Abt
 */
public class ScareEm {

    public static int calculate(PokerDataAccess data, String me) {
        int numPlayers = data.getPlayers().numPlayersNotFoldedOrBusted();
        CardVector hand = data.getHandCards(me);
        CardVector common = data.getCommonCards();
        int currentBet = data.getCurrentBet();
        int myBet = data.getPlayers().find(me).getCurrentBet();
        int owingAmount = currentBet - myBet;
        int potSize = data.getPotSize();
        return calculate(numPlayers, hand, common, owingAmount, potSize);
    }

    /**
	 * Returns the amount to raise to scare others out.
	 * @param numPlayers
	 * @param hand
	 * @param common
	 * @return
	 */
    public static int calculate(int numPlayers, CardVector hand, CardVector common, int owingAmount, int potSize) {
        MonteCarloEstimator.SimulationSize simSize = MonteCarloEstimator.SimulationSize.NORMAL;
        double myChances = MonteCarloEstimator.getInstance().estimateChances(numPlayers, hand, common, simSize);
        int myExpectancy = (int) (myChances * (potSize + owingAmount) - owingAmount);
        double opponentChances = (1.0 - myChances) / numPlayers;
        int scareRaise = (int) ((opponentChances * (potSize + owingAmount)) / (1.0 - opponentChances));
        int bet = owingAmount + scareRaise;
        double myScareRaiseExpectancy = (int) (bet - myChances * (potSize + bet));
        if (myExpectancy < 0) {
            if (owingAmount == 0) {
                return 0;
            } else {
                return -1;
            }
        }
        return scareRaise;
    }

    public static void main(String[] args) {
    }
}
