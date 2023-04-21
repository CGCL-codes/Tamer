package de.rauchhaupt.games.poker.holdem.lib.handqualifier;

import de.rauchhaupt.games.poker.holdem.lib.Hand;

public class MoebiuseJrsChanceQualifier implements HandQualifier {

    public MoebiuseJrsChanceQualifier() {
    }

    public double computeHandQualifier(Hand hand) {
        HandStatistic tempStatistic = StatisticalResource.getInstance().giveRightStatistic(hand);
        double dAnalyzedHands = tempStatistic.getStatisticOnAnalyzedHands();
        WinLossStatistic tempWinLossStatistic = tempStatistic.getReferencialRatingWinLossStatisticForCards(hand);
        double dwins = tempWinLossStatistic.getWins();
        double dlosses = tempWinLossStatistic.getLosses();
        double tot = dwins + dlosses;
        if (tot == 0.0d) return -1.0d;
        double hitsTotal = 0.5 + (dwins / tot) - (dlosses / tot);
        return hitsTotal;
    }

    /**
    * @see de.rauchhaupt.games.poker.holdem.lib.handqualifier.HandQualifier#getName()
    */
    @Override
    public String getName() {
        return "MiobelloJr";
    }

    /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
    @Override
    public boolean equals(Object obj) {
        return getName().equals(obj.toString());
    }

    /**
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
        return getName();
    }
}
