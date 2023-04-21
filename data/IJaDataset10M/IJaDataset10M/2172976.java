package dr.evolution.distance;

import dr.evolution.alignment.PatternList;

/**
 * compute jukes-cantor corrected distance matrix
 *
 * @version $Id: JukesCantorDistanceMatrix.java,v 1.4 2005/05/24 20:25:56 rambaut Exp $
 *
 * @author Andrew Rambaut
 * @author Korbinian Strimmer
 */
public class JukesCantorDistanceMatrix extends DistanceMatrix {

    /** constructor */
    public JukesCantorDistanceMatrix() {
        super();
    }

    /** constructor taking a pattern source */
    public JukesCantorDistanceMatrix(PatternList patterns) {
        super(patterns);
    }

    /**
	 * set the pattern source
	 */
    public void setPatterns(PatternList patterns) {
        super.setPatterns(patterns);
        final int stateCount = patterns.getStateCount();
        const1 = ((double) stateCount - 1) / stateCount;
        const2 = ((double) stateCount) / (stateCount - 1);
    }

    /**
	 * Calculate a pairwise distance
	 */
    protected double calculatePairwiseDistance(int i, int j) {
        final double obsDist = super.calculatePairwiseDistance(i, j);
        if (obsDist == 0.0) return 0.0;
        if (obsDist >= const1) {
            return MAX_DISTANCE;
        }
        final double expDist = -const1 * Math.log(1.0 - (const2 * obsDist));
        if (expDist < MAX_DISTANCE) {
            return expDist;
        } else {
            return MAX_DISTANCE;
        }
    }

    private double const1, const2;
}
