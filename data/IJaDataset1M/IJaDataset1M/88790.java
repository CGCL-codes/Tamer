package de.ifgi.simcat.contextLogger;

/**
 * This class represents a concept that is part of a merge result performed by
 * the <code>RankingMerge</code> class. When merging two rankings r1 and r2
 * into one ArrayList L the concepts from both rankings will be copied to L in
 * such a way that all concepts from both rankings will be in L but L will not
 * hold any duplicate concepts.<br>
 * Every <code>MergeResultConcept</code> C stores two rank numbers. The first
 * one is its rank number according to r1, the second one is its rank number
 * according to r2. If C is not part of r1 but part of r2 its first rank number
 * is undefined, i.e. -1, and its second rank number holds the concept's rank
 * number according to r2 and vice versa.
 * 
 * @author Marius Austerschulte
 * 
 */
public class MergeResultConcept extends ResultConcept {

    private int secondRankNumber = -1;

    private double secondSimValue = 0.0;

    public MergeResultConcept(String conceptName, double simValue, int rankNumber, int secondRankNumber) {
        super(conceptName, simValue);
        this.setRankNumber(rankNumber);
        this.secondRankNumber = secondRankNumber;
    }

    public MergeResultConcept(ResultConcept c, int secondRankNumber) {
        super(c.getConceptName(), c.getSimilarityValue());
        this.setRankNumber(c.getRankNumber());
        this.secondRankNumber = secondRankNumber;
    }

    public MergeResultConcept(ResultConcept c) {
        super(c.getConceptName(), c.getSimilarityValue());
        this.setRankNumber(c.getRankNumber());
    }

    public int getSecondRankNumber() {
        return secondRankNumber;
    }

    public void setSecondRankNumber(int secondRankNumber) {
        this.secondRankNumber = secondRankNumber;
    }

    public void setSecondRankNumberUndefined() {
        this.secondRankNumber = -1;
    }

    public void setSecondSimValue(double simValue) {
        this.secondSimValue = simValue;
    }

    public double getSecondSimValue() {
        return this.secondSimValue;
    }
}
